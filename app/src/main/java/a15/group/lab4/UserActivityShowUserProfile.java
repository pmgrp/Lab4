package a15.group.lab4;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class UserActivityShowUserProfile extends BaseActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private ImageView userPhoto;
    private EditText userName;
    private EditText userSurname;
    private EditText userPhone;
    private EditText userBirthday;
    //private EditText userEmail;
    //private EditText userPassword;
    private DatabaseReference mRef;
    private ValueEventListener userFieldsListener;
    private String userId;
    private User user;

    private static final int PICK_IMAGE_ID = 234;
    private String imagePath=null;
    private Uri tempImageUri=null;


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

        showProgressDialog();

        userFieldsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                //user name
                userName = (EditText) findViewById(R.id.user_name);
                userSurname = (EditText) findViewById(R.id.user_surname);
                userPhone = (EditText) findViewById(R.id.user_phone);
                userBirthday = (EditText) findViewById(R.id.user_birthday);
                //userEmail = (EditText) findViewById(R.id.user_email);
                //userPassword = (EditText) findViewById(R.id.user_password);
                userPhoto = (ImageView) findViewById(R.id.user_photo);

                user = dataSnapshot.getValue(User.class);
                userName.setText(user.getName());
                userSurname.setText(user.getSurname());
                userPhone.setText(user.getPhone());
                userBirthday.setText(user.getBirthday());
                //userEmail.setText(user.getEmail());


                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo_app_coyote);
                userPhoto.setImageBitmap(getCircleBitmap(bitmap));
/*
                imagePath = user.getPhoto();
                if (imagePath != null) {
                    bitmap = imagePicker.loadImageFromStorage(imagePath);
                    userPhoto.setImageBitmap(getCircleBitmap(bitmap));
                }
                //if an image has been shot but not saved get it
                /*if(savedInstanceState != null){
                    tempImageUri = savedInstanceState.getParcelable("TempUri");
                }
                if(tempImageUri != null){
                    bitmap = imagePicker.getImageResized(this, tempImageUri);
                    if(userPhoto!=null)
                        userPhoto.setImageBitmap(bitmap);
                }*/
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        //set template
        setContentView(R.layout.user_activity_show_user_profile);
        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_offer_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void editProfileClicked(View view){
        String name = userName.getText().toString();
        String surname = userSurname.getText().toString();
        String phone = userPhone.getText().toString();
        String birthday = userBirthday.getText().toString();
        //String email = userEmail.getText().toString();
        //String password = userPassword.getText().toString();
        Bitmap bitmap = ((BitmapDrawable) userPhoto.getDrawable()).getBitmap();
        imagePath = imagePicker.saveToInternalStorage(bitmap, this);

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
        user.setBirthday(birthday);
        //user.setEmail(email);
        //user.setPassword(password);
        user.setPhoto(imagePath);
        showProgressDialog();
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
                hideProgressDialog();
            }
        });
    }


    public void modifyPhoto(View view){
        Intent chooseImageIntent = imagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                tempImageUri = imagePicker.getUriFromResult(this, resultCode, data);
                if(tempImageUri != null) {
                    Bitmap image = imagePicker.getImageResized(this, tempImageUri);
                    userPhoto.setImageBitmap(image);
                }
                else{
                    userPhoto.setImageResource(R.mipmap.logo_app_coyote);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    public void popupChangeEmail(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivityShowUserProfile.this);
        builder.setTitle("Modify Your Email Address");

        LinearLayout layout= new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final TextView currentEmail = new TextView(this);
        final EditText newEmail = new EditText(this);

        currentEmail.setText("current email address");
        currentEmail.setSingleLine();
        newEmail.setHint("New email address");
        newEmail.setSingleLine();
        newEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        layout.addView(currentEmail);
        layout.addView(newEmail);
        builder.setView(layout);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Email modified with success", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }


    public void popupChangePassword(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivityShowUserProfile.this);
        builder.setTitle("Modify Your Password");

        LinearLayout layout= new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText oldPassword = new EditText(this);
        final EditText newPassword = new EditText(this);
        final EditText confirmNewPassword = new EditText(this);

        oldPassword.setHint("Your old password");
        newPassword.setHint("Your new password");
        confirmNewPassword.setHint("Confirm your new password");
        oldPassword.setSingleLine();
        newPassword.setSingleLine();
        confirmNewPassword.setSingleLine();
        oldPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(oldPassword);
        layout.addView(newPassword);
        layout.addView(confirmNewPassword);
        builder.setView(layout);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (verifyOldPassword()) {
                    if ( newPassword.getText().toString().equals(confirmNewPassword.getText().toString()) ) {
                        Toast.makeText(getBaseContext(), "Password modified with success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "You didn't write the same new password", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getBaseContext(), "Your old password is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.show();
    }

    public boolean verifyOldPassword() {

        return true;
    }


    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    @Override
    public void onSaveInstanceState(Bundle onSave){
        super.onSaveInstanceState(onSave);
        onSave.putParcelable("TempUri", tempImageUri);
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


