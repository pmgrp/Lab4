package a15.group.lab4;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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

public class ActivityAddUserInfo extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser fUser;
    private DatabaseReference mRef;
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

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                fUser = firebaseAuth.getCurrentUser();
                if (fUser != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + fUser.getUid());
                    //here check if user infos are in database
                    mRef = FirebaseDatabase.getInstance().getReference();
                    userId = fUser.getUid();
                    mRef.child("users").child(userId).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //if user infos are not present display form to fill data
                                    if (!dataSnapshot.exists()) {
                                        Log.d("TAG", "populate view");
                                        populateActivity();
                                    }
                                    else {
                                        User user = dataSnapshot.getValue(User.class);
                                        goToMainActivity(user.getType());
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.d("TAG", "getUser:onCancelled", databaseError.toException());
                                }


                            });

                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                    goToMainActivity(NOUSER);
                }
                // ...
            }
        };


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
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
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


    @UiThread
    private void populateActivity(){
        setContentView(R.layout.activity_add_user_info);
        customer = (RadioButton)findViewById(R.id.radio_customer);
        owner = (RadioButton)findViewById(R.id.radio_owner);
        name = (EditText)findViewById(R.id.user_name);
        surname = (EditText)findViewById(R.id.user_surname);

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
            //TODO manage Owner case
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
