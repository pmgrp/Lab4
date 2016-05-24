package a15.group.lab4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
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
    View mRootView;
    String userId;
    Context context = this;
    ProgressDialog dialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Here check if user is logged
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            //user is logged in go to profile page
            goToMainActivity();
            finish();
        }

        //set layout file
        setContentView(R.layout.activity_main);
        //the sign in button
        mSignIn = (Button)findViewById(R.id.sign_in);
        //the root view
        mRootView = (View)findViewById(android.R.id.content);
    }

    //function related to signin button
    public void signIn(View view){
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setProviders(AuthUI.EMAIL_PROVIDER)
                        .setTheme(AuthUI.getDefaultTheme())
                        .build(),
                RC_SIGN_IN);
    }


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
            if(mAuth.getCurrentUser() != null) {
                userId = mAuth.getCurrentUser().getUid();
                //check if user is already in database
                mRef.child("users").child(userId).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //if user not present put data
                                if(!dataSnapshot.exists()){
                                    User user = new User();
                                    user.setType("Customer");
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    ref.child("users").child(userId).setValue(user);
                                    Log.d("TAG","User data has been added in database");
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

    @MainThread
    private void goToMainActivity(){
        //dialog.dismiss();
        Intent in = new Intent(this, UserActivityShowUserProfile.class);
        startActivity(in);
    }


    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}
