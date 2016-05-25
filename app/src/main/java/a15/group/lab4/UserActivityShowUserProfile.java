package a15.group.lab4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class UserActivityShowUserProfile extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private Button signOutButton;
    private EditText userName;
    private EditText userSurname;
    private EditText userPhone;
    private DatabaseReference mRef;
    private ValueEventListener userFieldsListener;
    private String userId;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    userId = user.getUid();
                    //get database reference
                    mRef = FirebaseDatabase.getInstance().getReference();
                    mRef.child("users").child(userId).addValueEventListener(userFieldsListener);
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Intent in = new Intent(UserActivityShowUserProfile.this, ActivityMain.class);
                    startActivity(in);
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        userFieldsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                user = dataSnapshot.getValue(User.class);
                userName.setText(user.getName());
                userSurname.setText(user.getSurname());
                userPhone.setText(user.getPhone());
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };




        //set template
        setContentView(R.layout.activity_show_user_profile);
        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_offer_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //sign out button
        signOutButton = (Button)findViewById(R.id.sign_out);
        //user name
        userName = (EditText)findViewById(R.id.user_name);
        userSurname = (EditText)findViewById(R.id.user_surname);
        userPhone = (EditText)findViewById(R.id.user_phone);

    }

    public void editProfileClicked(View view){
        String name = userName.getText().toString();
        String surname = userSurname.getText().toString();
        String phone = userPhone.getText().toString();
        if(name.isEmpty() || surname.isEmpty()){
            Toast.makeText(UserActivityShowUserProfile.this, "Fill at least Name and Surname Fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(user == null || userId == null || mRef == null){
            return;
        }
        user.setName(name);
        user.setSurname(surname);
        user.setPhone(phone);
        mRef.child("users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UserActivityShowUserProfile.this, "User info have been changed",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(UserActivityShowUserProfile.this, "An error occurred please try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void signOutClicked(View view){
        mAuth.signOut();
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


