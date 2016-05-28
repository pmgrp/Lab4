package a15.group.lab4;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityAddUserInfo extends BaseActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private FirebaseUser fUser;
    private String userId;
    private RadioButton customer;
    private RadioButton owner;
    private EditText name;
    private EditText surname;
    private final static String CUSTOMER = "Customer";
    private final static String OWNER = "Owner";
    private final static String NOUSER = "NoUser";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                fUser = firebaseAuth.getCurrentUser();
                if (fUser != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in_activity_add_user_info:" + fUser.getUid());
                    populateView();
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                    goToMainActivity(NOUSER);
                }
                // ...
            }
        };


    }


    private void populateView(){
        //show a wait dialog while fetching data
        showProgressDialog();
        //here check if user infos are in database
        mRef = database.getReference();
        userId = fUser.getUid();
        mRef.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //if user infos are not present display form to fill data
                        if (!dataSnapshot.exists()) {
                            Log.d("TAG", "populate view");
                            //display form data if user has no data
                            //remove wait dialog
                            hideProgressDialog();
                            setContentView(R.layout.activity_add_user_info);
                            customer = (RadioButton)findViewById(R.id.radio_customer);
                            owner = (RadioButton)findViewById(R.id.radio_owner);
                            name = (EditText)findViewById(R.id.user_name);
                            surname = (EditText)findViewById(R.id.user_surname);
                        }
                        else if(dataSnapshot.exists()){
                            hideProgressDialog();
                            User user = dataSnapshot.getValue(User.class);
                            goToMainActivity(user.getType());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("TAG", "getUser:onCancelled", databaseError.toException());
                    }


                });
    }

    public void saveUserData(View view){
        final User user;
        user = new User();
        String uName = name.getText().toString();
        String uSurname = surname.getText().toString();
        if(customer.isChecked()){
            user.setType(CUSTOMER);
        }
        if(owner.isChecked()) {
            user.setType(OWNER);
        }
        if(uName.isEmpty() || uSurname.isEmpty()){
            Toast.makeText(ActivityAddUserInfo.this, "Please Fill All Fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        user.setName(uName);
        user.setSurname(uSurname);
            mRef = database.getReference();
            mRef.child("users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ActivityAddUserInfo.this, "User Created",
                                Toast.LENGTH_SHORT).show();
                        //start activity
                        goToMainActivity(user.getType());
                    }
                    else{
                        Toast.makeText(ActivityAddUserInfo.this, "An error occurred please try again",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }


    private void goToMainActivity(String userType) {
        Intent in;
        switch (userType){
            case CUSTOMER:
                in = new Intent(this, UserActivityMain.class);
                startActivity(in);
                break;
            case NOUSER:
                in = new Intent(this, ActivityMain.class);
                startActivity(in);
                break;
            case OWNER:
                in = new Intent(this, OwnerActivityMain.class);
                startActivity(in);
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
