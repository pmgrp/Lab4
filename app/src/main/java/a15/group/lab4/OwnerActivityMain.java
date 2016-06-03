package a15.group.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OwnerActivityMain extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser fUser;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private String userId;
    private boolean isRestaurantSet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                fUser = firebaseAuth.getCurrentUser();
                if(fUser == null){
                    //go back to main activity if user is not logged in
                    Intent in = new Intent(OwnerActivityMain.this, ActivityMain.class);
                    startActivity(in);
                }
                else{
                    checkRestaurant();
                }
            }
        };
    }

    private void checkRestaurant(){
        //here check if user infos are in database
        mRef = database.getReference();
        userId = fUser.getUid();
        mRef.child("restaurants").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //if user infos are not present display form to fill data
                        if (!dataSnapshot.exists()) {
                            isRestaurantSet = false;
                        }
                        else if(dataSnapshot.exists()){
                            isRestaurantSet = true;
                        }
                        populateView();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("TAG", "getUser:onCancelled", databaseError.toException());
                    }


                });

    }


    private void populateView(){
        setContentView(R.layout.owner_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }




    public void onShowReservationsClicked(View view) {

        Intent in = new Intent(this, OwnerActivityShowReservations.class);
        startActivity(in);
    }

    public void onUserProfileClicked(View view){
        Intent in = new Intent(this, UserActivityShowUserProfile.class);
        startActivity(in);
    }

    public void onDailyOffersClicked(View view) {
        Intent in = new Intent(this, OwnerActivityShowOffers.class);
        startActivity(in);
    }


    public void onRestaurantProfileClicked(View view) {

        Intent in = new Intent(this, OwnerActivityRestaurantProfile.class);
        startActivity(in);
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
