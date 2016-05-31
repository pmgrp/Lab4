package a15.group.lab4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;


public class OwnerActivityEditRestaurantProfile extends BaseActivity {

    //values for bundle
    private final String RESTAURANT_NAME = "restaurant_name";
    private final String RESTAURANT_PHONE = "restaurant_phone";
    private final String RESTAURANT_ADDRESS = "restaurant_address";
    private final String RESTAURANT_EMAIL = "restaurant_email";
    private final String RESTAURANT_WEBSITE = "restaurant_website";
    private final String RESTAURANT_PIVA = "restaurant_piva";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser fUser;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Restaurant restaurant;
    private EditText restaurantName;
    private EditText restaurantPhone;
    private EditText restaurantAddress;
    private EditText restaurantEmail;
    private EditText restaurantWebsite;
    private EditText restaurantPiva;
    private ImageView restaurantPhoto;
    private Bitmap currentPhoto = null;
    private static final int PICK_IMAGE_ID = 234;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                fUser = firebaseAuth.getCurrentUser();
                if (fUser == null) {
                    //go back to main activity if user is not logged in
                    Intent in = new Intent(OwnerActivityEditRestaurantProfile.this, ActivityMain.class);
                    startActivity(in);
                }

            }
        };

        //storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://lab4-1318.appspot.com");

        //here fetch data from database
        mRef = database.getReference();
        mRef.child("restaurants").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    //set the foto
                    if (!restaurant.getRestaurantPhoto().isEmpty() && currentPhoto == null) {
                        Glide.with(OwnerActivityEditRestaurantProfile.this)
                                .load(restaurant.getRestaurantPhoto())
                                .centerCrop()
                                .into(restaurantPhoto);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
            }
        });


        populateView(savedInstanceState);

    }

    private void populateView(Bundle savedInstanceState){
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
        restaurantPhoto = (ImageView) findViewById(R.id.restaurant_photo);
        if(savedInstanceState != null) {
            restaurantName.setText(savedInstanceState.getString(RESTAURANT_NAME));
            restaurantPhone.setText(savedInstanceState.getString(RESTAURANT_PHONE));
            restaurantAddress.setText(savedInstanceState.getString(RESTAURANT_ADDRESS));
            restaurantEmail.setText(savedInstanceState.getString(RESTAURANT_EMAIL));
            restaurantWebsite.setText(savedInstanceState.getString(RESTAURANT_WEBSITE));
            restaurantPiva.setText(savedInstanceState.getString(RESTAURANT_PIVA));
            if (currentPhoto != null) {
                restaurantPhoto.setImageBitmap(currentPhoto);
                restaurantPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }
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

        //save image
        if(currentPhoto != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            currentPhoto.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] data = baos.toByteArray();
            StorageReference restaurantRef = storageRef.child(fUser.getUid()).child("restaurant_photo.jpg");
            UploadTask uploadTask = restaurantRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(OwnerActivityEditRestaurantProfile.this, "Error in uploading image, please try again",
                            Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    restaurant.setRestaurantPhoto(downloadUrl.toString());
                    currentPhoto = null;
                    uploadRestaurantProfile();
                }
            });
        }
        else{
            uploadRestaurantProfile();
        }

    }

    public void uploadRestaurantProfile(){
        mRef.child("restaurants").child(fUser.getUid()).setValue(restaurant).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    restaurantPhoto.setImageBitmap(currentPhoto);
                    restaurantPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString(RESTAURANT_NAME, restaurantName.getText().toString());
        savedInstanceState.putString(RESTAURANT_PHONE, restaurantPhone.getText().toString());
        savedInstanceState.putString(RESTAURANT_ADDRESS, restaurantAddress.getText().toString());
        savedInstanceState.putString(RESTAURANT_EMAIL, restaurantEmail.getText().toString());
        savedInstanceState.putString(RESTAURANT_WEBSITE, restaurantWebsite.getText().toString());
        savedInstanceState.putString(RESTAURANT_PIVA, restaurantPiva.getText().toString());

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
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

