package a15.group.lab4;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by eugeniosorbellini on 21/05/16.
 */
public class ActivityMain extends AppCompatActivity {

    //request code for login
    private static final int RC_SIGN_IN = 100;
    Button mSignIn;
    View mRootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Here check if user is logged
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            //user is logged in go to profile page
            Intent in = new Intent(this, UserActivityMain.class);
            startActivity(in);
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

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent in = new Intent(this, UserActivityMain.class);
            startActivity(in);
            finish();
            return;
        }

        if (resultCode == RESULT_CANCELED) {
            showSnackbar(R.string.sign_in_cancelled);
            return;
        }

        showSnackbar(R.string.unknown_sign_in_response);
    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}
