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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by eugeniosorbellini on 21/05/16.
 */
public class ActivityMain extends AppCompatActivity {

    //request code for login
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser fUser;
    private EditText mEmail;
    private EditText mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                fUser = firebaseAuth.getCurrentUser();
                if (fUser != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + fUser.getUid());
                    addUserInfo();
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                    populateView();
                }


                // ...
            }
        };

    }

    private void populateView(){
        //set layout file
        setContentView(R.layout.activity_main);
        //the email
        mEmail = (EditText) findViewById(R.id.user_email);
        //the password
        mPassword = (EditText) findViewById(R.id.user_password);
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
                                //user may already exist try login
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


    private void addUserInfo() {
        Intent in = new Intent(this, ActivityAddUserInfo.class);
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
