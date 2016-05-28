package a15.group.lab4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;


public class OwnerActivityEditRestaurantProfile extends BaseActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRef;
    private Restaurant restaurant;
    private EditText restaurantName;
    private EditText restaurantPhone;
    private EditText restaurantAddress;
    private EditText restaurantEmail;
    private EditText restaurantWebsite;
    private EditText restaurantPiva;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //manage login part
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fUser = firebaseAuth.getCurrentUser();
                if (fUser == null) {
                    //go back to main activity if user is not logged in
                    Intent in = new Intent(OwnerActivityEditRestaurantProfile.this, ActivityMain.class);
                    startActivity(in);
                }
            }
        };

        //This listener Fill in fields when data changes
        ValueEventListener restaurantFieldsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get restaurant object and use the values to update the UI
                if (dataSnapshot.exists()) {
                    restaurant = dataSnapshot.getValue(Restaurant.class);
                    restaurantName.setText(restaurant.getRestaurantName());
                    restaurantPhone.setText(restaurant.getRestaurantPhone());
                    restaurantAddress.setText(restaurant.getRestaurantAddress());
                    restaurantEmail.setText(restaurant.getRestaurantEmail());
                    restaurantWebsite.setText(restaurant.getRestaurantWebsite());
                    restaurantPiva.setText(restaurant.getRestaurantPiva());
                    // ...
                }
                //there is no restaurant set for this owner
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };


        setContentView(R.layout.owner_activity_edit_restaurant_profile);
        //toolbar
        //to add toolbar with back arrow
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        restaurantName = (EditText) findViewById(R.id.restaurantNameField);
        restaurantPhone = (EditText) findViewById(R.id.restaurantPhoneField);
        restaurantAddress = (EditText) findViewById(R.id.restaurantAddressField);
        restaurantEmail = (EditText) findViewById(R.id.restaurantEmailField);
        restaurantWebsite = (EditText) findViewById(R.id.restaurantWebsiteField);
        restaurantPiva = (EditText) findViewById(R.id.restaurantIVAField);

        mRef = FirebaseDatabase.getInstance().getReference();

        //here fetch data from database
        showProgressDialog();
        mRef.child("restaurants").child(mAuth.getCurrentUser().getUid()).addValueEventListener(restaurantFieldsListener);

    }

    public void saveRestaurantData(View view) {

        if(restaurant == null)
            restaurant = new Restaurant();
        restaurant.setRestaurantName(restaurantName.getText().toString());
        restaurant.setRestaurantPhone(restaurantPhone.getText().toString());
        restaurant.setRestaurantAddress(restaurantAddress.getText().toString());
        restaurant.setRestaurantEmail(restaurantEmail.getText().toString());
        restaurant.setRestaurantWebsite(restaurantWebsite.getText().toString());
        restaurant.setRestaurantPiva(restaurantPiva.getText().toString());
        restaurant.setID("");
        Geocoder geocoder = new Geocoder(this, Locale.ITALY);
        List<Address> addresses = null;
        try{
            addresses = geocoder.getFromLocationName(restaurantAddress.getText().toString(), 1);
        }
        catch (IOException e){
            Toast.makeText(OwnerActivityEditRestaurantProfile.this, "Can't get coordinates",
                    Toast.LENGTH_SHORT).show();
        }
        if(addresses != null &&  addresses.size() > 0) {
            Toast.makeText(OwnerActivityEditRestaurantProfile.this, "Restaurant Address found on Gmaps!",
                    Toast.LENGTH_SHORT).show();
            double latitude= addresses.get(0).getLatitude();
            double longitude= addresses.get(0).getLongitude();
            restaurant.setLatitude(latitude);
            restaurant.setLongitude(longitude);
        }
        else {
            restaurant.setLatitude(0);
            restaurant.setLongitude(0);
            Toast.makeText(OwnerActivityEditRestaurantProfile.this, "Couldn't find restaurant address",
                    Toast.LENGTH_SHORT).show();
        }
        restaurant.setRestaurantPhoto("");
        restaurant.setLikeCount(0);
        //showProgressDialog();
        Log.d("TAG", "here save data");
        mRef.child("restaurants").child(mAuth.getCurrentUser().getUid()).setValue(restaurant).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //hideProgressDialog();
                if(task.isSuccessful()){
                    Toast.makeText(OwnerActivityEditRestaurantProfile.this, "Restaurant info have been changed",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OwnerActivityEditRestaurantProfile.this, OwnerActivityRestaurantProfile.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(OwnerActivityEditRestaurantProfile.this, "An error occurred please try again",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

