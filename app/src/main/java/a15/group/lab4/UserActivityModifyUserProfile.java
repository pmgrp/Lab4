package a15.group.lab4;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class UserActivityModifyUserProfile extends AppCompatActivity {

    private Customer profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_customer_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_offer_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();

        String json = preferences.getString("profile", null);
        if(json != null) {
            profile = gson.fromJson(json, Customer.class);
        }
        else {
            profile = new Customer();
            profile.setSurname("Dupont");
            profile.setName("Henry");
            profile.setEmail("henry_dupont@email.it");
            profile.setPhone("392 456 5874");
            profile.setPassword("*****");
        }

        ImageView imageView = (ImageView) findViewById(R.id.customer_profile_photo);
        //imageView.setImageURI(Uri.parse(profile.getPhoto()));

        TextView textView = (TextView) findViewById(R.id.customer_profile_surname);
        textView.setText(profile.getSurname());
        textView = (TextView) findViewById(R.id.customer_profile_name);
        textView.setText(profile.getName());
        textView = (TextView) findViewById(R.id.customer_profile_email);
        textView.setText(profile.getEmail());
        textView = (TextView) findViewById(R.id.customer_profile_phone);
        textView.setText(profile.getPhone());


    }


    public void onClickPopupOffer() {
        final CharSequence[] items = { "Cancel", "Save" };
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivityModifyUserProfile.this);
        builder.setTitle("Change of Password");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Save")) {
                    //save new password
                    EditText editText = (EditText) findViewById(R.id.modify_customer_profile_password);
                    String password = editText.getText().toString();

                    dialog.dismiss();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public void saveData() {

    }

}
