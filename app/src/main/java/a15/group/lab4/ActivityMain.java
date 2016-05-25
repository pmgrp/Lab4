package a15.group.lab4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by eugeniosorbellini on 21/05/16.
 */
public class ActivityMain extends AppCompatActivity {

    //request code for login
    private static final int RC_SIGN_IN = 100;
    Button mSignIn;
    EditText mEmail;
    EditText mPassword;
    RadioButton mCustomer;
    RadioButton mOwner;
    View mRootView;
    String userId;
    Context context = this;
    ProgressDialog dialog;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                    addUserInfo();
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        /*
        //Here check if user is logged
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            //user is logged in go to profile page
            goToMainActivity();
            finish();
        }
        */
        //set layout file
        setContentView(R.layout.activity_main);
        //the email
        mEmail = (EditText) findViewById(R.id.user_email);
        //the password
        mPassword = (EditText) findViewById(R.id.user_password);
        //Customer radio
        mCustomer = (RadioButton) findViewById(R.id.radio_customer);
        //Owner radio
        mOwner = (RadioButton) findViewById(R.id.radio_owner);
        //the sign in button
        mSignIn = (Button) findViewById(R.id.sign_in);
        //the root view
        mRootView = (View) findViewById(android.R.id.content);

    }

    //function related to signin button
    public void signIn(View view) {
        final String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();

        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                //Toast.makeText(ActivityMain.this, "Authentication failed.",
                                  //      Toast.LENGTH_SHORT).show();
                                tryLogIn(email,password);

                            }
                            else{
                                Toast.makeText(ActivityMain.this, "New User Created.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }

    }

    public void tryLogIn(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("TAG", "user logged in succesfully" + task.isSuccessful());
                if(!task.isSuccessful()){
                    Toast.makeText(ActivityMain.this, "Authentication failed. Maybe you are already registered with a different password",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ActivityMain.this, "User logged in.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /*
    //manage result of login
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            return;
        }

        showSnackbar(R.string.unknown_response);
    }


    private void handleSignInResponse(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //show a dialog while doing async operations
            dialog = ProgressDialog.show(context, "Loading", "Please wait...", true);
            //here add user data in user database if not present
            mAuth = FirebaseAuth.getInstance();
            mRef = FirebaseDatabase.getInstance().getReference();
            if (mAuth.getCurrentUser() != null) {
                userId = mAuth.getCurrentUser().getUid();
                //check if user is already in database
                mRef.child("users").child(userId).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //if user not present put data
                                if (!dataSnapshot.exists()) {
                                    User user = new User();
                                    user.setType("Customer");
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    ref.child("users").child(userId).setValue(user);
                                    Log.d("TAG", "User data has been added in database");
                                }
                                goToMainActivity();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("TAG", "getUser:onCancelled", databaseError.toException());
                            }


                        });
            }
            return;
        }

        if (resultCode == RESULT_CANCELED) {
            showSnackbar(R.string.sign_in_cancelled);
            return;
        }

        showSnackbar(R.string.unknown_sign_in_response);
    }
    */

    private void addUserInfo() {
        //dialog.dismiss();
        Intent in = new Intent(this, ActivityAddUserInfo.class);
        startActivity(in);
    }


    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
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
