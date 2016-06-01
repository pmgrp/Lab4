package a15.group.lab4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.io.ByteArrayOutputStream;

/**
 * Created by eugeniosorbellini on 01/06/16.
 */
public class OwnerActivityModifyOffer extends BaseActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser fUser;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String offerId;
    private DailyOffer offer;
    private NumberPicker pickerPrice;
    private NumberPicker pickerAvailability;
    private ImageView offerPhoto;
    private EditText offerName;
    private EditText offerDescription;
    private Bitmap currentPhoto = null;
    private Bitmap currentThumb = null;
    private String photoUrl;
    private String thumbUrl;
    private static final int PICK_IMAGE_ID = 234;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        offerId = getIntent().getExtras().getString("offerID");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                fUser = firebaseAuth.getCurrentUser();
                if (fUser == null) {
                    //go back to main activity if user is not logged in
                    Intent in = new Intent(OwnerActivityModifyOffer.this, ActivityMain.class);
                    startActivity(in);
                }
            }
        };

        populateView();


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

        mRef.child("offers").child(offerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    offer = dataSnapshot.getValue(DailyOffer.class);
                    offerName.setText(offer.getName());
                    offerDescription.setText(offer.getDescription());
                    pickerAvailability.setValue(offer.getAvailability());
                    pickerPrice.setValue(offer.getPrice());
                    Glide.with(OwnerActivityModifyOffer.this)
                            .load(offer.getPhotoThumb())
                            .centerCrop()
                            .into(offerPhoto);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
            }
        });


    }

    public void saveOfferData(View view) {

        offer.setName(offerName.getText().toString());
        offer.setDescription(offerDescription.getText().toString());
        offer.setPrice(pickerPrice.getValue());
        offer.setAvailability(pickerAvailability.getValue());
        showProgressDialog();
        uploadOffer();




    }

    private void uploadOffer(){
        if(currentPhoto != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            currentPhoto.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            currentThumb = ThumbnailUtils.extractThumbnail(currentPhoto, currentPhoto.getWidth()/3, currentPhoto.getHeight()/3);
            byte[] dataPhoto = baos.toByteArray();
            StorageReference offerRef = storageRef.child(offerId).child("offer_photo.jpg");
            //upload photo
            UploadTask uploadTask = offerRef.putBytes(dataPhoto);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(OwnerActivityModifyOffer.this, "Error in uploading image, please try again",
                            Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    photoUrl = downloadUrl.toString();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    currentThumb.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] dataThumb = baos.toByteArray();
                    StorageReference thumbRef = storageRef.child(offerId).child("offer_thumb.jpg");
                    //upload Thumb
                    UploadTask thumbTask = thumbRef.putBytes(dataThumb);
                    thumbTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            thumbUrl = downloadUrl.toString();
                            offer.setPhoto(photoUrl.toString());
                            offer.setPhotoThumb(thumbUrl.toString());
                            mRef.child("restaurant-offers").child(fUser.getUid()).child(offerId).setValue(offer);
                            mRef.child("offers").child(offerId).setValue(offer).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        currentPhoto = null;
                                        //save thumbnail
                                        hideProgressDialog();
                                        Toast.makeText(OwnerActivityModifyOffer.this, "Offer has been saved",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(OwnerActivityModifyOffer.this, OwnerActivityShowOffers.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(OwnerActivityModifyOffer.this, "Some error occurred please try again",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OwnerActivityModifyOffer.this, "Error in uploading image, please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        //upload just text values
        else{
            mRef.child("restaurant-offers").child(fUser.getUid()).child(offerId).setValue(offer);
            mRef.child("offers").child(offerId).setValue(offer).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        currentPhoto = null;
                        //save thumbnail
                        hideProgressDialog();
                        Toast.makeText(OwnerActivityModifyOffer.this, "Offer has been saved",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OwnerActivityModifyOffer.this, OwnerActivityShowOffers.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(OwnerActivityModifyOffer.this, "Some error occurred please try again",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
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