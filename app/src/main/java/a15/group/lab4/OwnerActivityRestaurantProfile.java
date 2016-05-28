package a15.group.lab4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class OwnerActivityRestaurantProfile extends BaseActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Restaurant restaurant;
    private TextView restaurantName;
    private TextView restaurantPhone;
    private TextView restaurantAddress;
    private TextView restaurantEmail;
    private TextView restaurantWebsite;
    private TextView restaurantPiva;
    private ImageView restaurantPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fUser = firebaseAuth.getCurrentUser();
                if(fUser == null){
                    //go back to main activity if user is not logged in
                    Intent in = new Intent(OwnerActivityRestaurantProfile.this, ActivityMain.class);
                    startActivity(in);
                }
            }
        };

        ValueEventListener restaurantFieldsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get restaurant object and use the values to update the UI
                if(dataSnapshot.exists()) {
                    restaurant = dataSnapshot.getValue(Restaurant.class);
                    restaurantName.setText(restaurant.getRestaurantName());
                    restaurantPhone.setText(restaurant.getRestaurantPhone());
                    restaurantAddress.setText(restaurant.getRestaurantAddress());
                    restaurantEmail.setText(restaurant.getRestaurantEmail());
                    restaurantWebsite.setText(restaurant.getRestaurantWebsite());
                    restaurantPiva.setText(restaurant.getRestaurantPiva());
                    //set the foto
                    if(!restaurant.getRestaurantPhoto().isEmpty()) {
                        Glide.with(OwnerActivityRestaurantProfile.this)
                                .load(restaurant.getRestaurantPhoto())
                                .centerCrop()
                                .into(restaurantPhoto);
                    }
                    // ...
                }
                //there is no restaurant set for this owner
                else{
                    //hideProgressDialog();
                    Toast.makeText(OwnerActivityRestaurantProfile.this, "Please Fill in Restaurant Infos",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        setContentView(R.layout.owner_activity_restaurant_profile);
        //toolbar
        //to add toolbar with back arrow
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        restaurantName = (TextView)findViewById(R.id.restaurantName);
        restaurantPhone = (TextView)findViewById(R.id.restaurantPhone);
        restaurantAddress = (TextView) findViewById(R.id.restaurantAddress);
        restaurantEmail = (TextView) findViewById(R.id.restaurantEmail);
        restaurantWebsite = (TextView) findViewById(R.id.restaurantWebsite);
        restaurantPiva = (TextView) findViewById(R.id.restaurantIVA);
        restaurantPhoto = (ImageView) findViewById(R.id.restaurant_photo);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

        //here fetch data from database
        //showProgressDialog();
        mRef.child("restaurants").child(mAuth.getCurrentUser().getUid()).addValueEventListener(restaurantFieldsListener);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant_profile, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            Intent intent = new Intent(this, OwnerActivityEditRestaurantProfile.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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