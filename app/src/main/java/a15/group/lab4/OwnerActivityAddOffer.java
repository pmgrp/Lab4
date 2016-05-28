package a15.group.lab4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class OwnerActivityAddOffer extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser fUser;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Restaurant restaurant;
    private String restaurantId;
    private DailyOffer offer;
    private NumberPicker pickerPrice;
    private NumberPicker pickerAvailability;
    private ImageView offerPhoto;
    private EditText offerName;
    private EditText offerDescription;
    private Bitmap currentPhoto = null;
    private static final int PICK_IMAGE_ID = 234;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                fUser = firebaseAuth.getCurrentUser();
                if (fUser == null) {
                    //go back to main activity if user is not logged in
                    Intent in = new Intent(OwnerActivityAddOffer.this, ActivityMain.class);
                    startActivity(in);
                } else {
                    checkRestaurant();
                }
            }
        };




    }

    private void populateView() {
        setContentView(R.layout.owner_activity_add_offer);


        //toolbar part
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set pickers ranges
        pickerPrice = (NumberPicker) findViewById(R.id.add_offer_price);
        pickerPrice.setMaxValue(1000);
        pickerPrice.setMinValue(0);
        pickerPrice.setWrapSelectorWheel(false);
        pickerPrice.setValue(0);

        pickerAvailability = (NumberPicker) findViewById(R.id.add_offer_availability);
        pickerAvailability.setMaxValue(1000);
        pickerAvailability.setMinValue(0);
        pickerAvailability.setWrapSelectorWheel(false);
        pickerAvailability.setValue(0);

        offerName = (EditText) findViewById(R.id.add_offer_name);
        offerDescription = (EditText) findViewById(R.id.add_offer_description);
        offerPhoto = (ImageView) findViewById(R.id.add_offer_photo);

        //storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://lab4-1318.appspot.com");

        //database reference
        mRef = database.getReference();


    }


    private void checkRestaurant(){
        //here check if user infos are in database
        mRef = database.getReference();
        String userId = fUser.getUid();
        mRef.child("restaurants").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //if user infos are not present display form to fill data
                        if (!dataSnapshot.exists()) {
                            Intent in = new Intent(OwnerActivityAddOffer.this, OwnerActivityMain.class);
                            startActivity(in);
                        }
                        else {
                            restaurantId = dataSnapshot.getKey();
                            restaurant = dataSnapshot.getValue(Restaurant.class);
                            populateView();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("TAG", "getUser:onCancelled", databaseError.toException());
                    }


                });

    }

    public void saveOfferData(View view) {

        offer = new DailyOffer();
        offer.setName(offerName.getText().toString());
        offer.setDescription(offerDescription.getText().toString());
        offer.setPrice(pickerPrice.getValue());
        offer.setAvailability(pickerPrice.getValue());
        offer.setRestaurantID(restaurantId);
        offer.setRestaurantName(restaurant.getRestaurantName());
        offer.setRestaurantLatitude(restaurant.getLatitude());
        offer.setRestaurantLongitude(restaurant.getLongitude());
        uploadOffer();


        Intent intent = new Intent(this, OwnerActivityShowOffers.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public void uploadOffer(){
        mRef.child("offers").push().setValue(offer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //hideProgressDialog();
                if(task.isSuccessful()){
                    Toast.makeText(OwnerActivityAddOffer.this, "Offer has been saved",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OwnerActivityAddOffer.this, OwnerActivityMain.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(OwnerActivityAddOffer.this, "An error occurred please try again",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void onImageViewClick(View view){
        Intent chooseImageIntent = imagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            //there is a foto
            case PICK_IMAGE_ID:
                currentPhoto = imagePicker.getImageFromResult(this, resultCode, data);
                if(currentPhoto != null) {
                    offerPhoto.setImageBitmap(currentPhoto);
                    offerPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
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