package a15.group.lab4;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class UserActivityShowUserProfile extends AppCompatActivity {

    private Customer profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

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

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(profile);
        editor.putString("profile", json);
        editor.commit();
    }

}


