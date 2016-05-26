package a15.group.lab4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;


public class OwnerActivityEditRestaurantProfile extends AppCompatActivity {
    Restaurant restaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_edit_restaurant_profile);
        //toolbar
        //to add toolbar with back arrow
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = preferences.getString("restaurant", null);
        if(json != null) {
            restaurant = gson.fromJson(json, Restaurant.class);
            EditText editText = (EditText) findViewById(R.id.restaurantNameField);
            editText.setText(restaurant.getRestaurantName());
            editText = (EditText) findViewById(R.id.restaurantPhoneField);
            editText.setText(restaurant.getRestaurantPhone());
            editText = (EditText) findViewById(R.id.restaurantAddressField);
            editText.setText(restaurant.getRestaurantAddress());
            editText = (EditText) findViewById(R.id.restaurantEmailField);
            editText.setText(restaurant.getRestaurantEmail());
            editText = (EditText) findViewById(R.id.restaurantWebsiteField);
            editText.setText(restaurant.getRestaurantWebsite());
            editText = (EditText) findViewById(R.id.restaurantIVAField);
            editText.setText(restaurant.getRestaurantPiva());
        }
        else {
            restaurant = new Restaurant();
        }

    }
    public void saveRestaurantData(View view) {
        /*final EditText name = (EditText) findViewById(R.id.restaurantNameField);
        final EditText phone = (EditText) findViewById(R.id.restaurantPhoneField);
        final EditText address = (EditText) findViewById(R.id.restaurantAddressField);
        final EditText email = (EditText) findViewById(R.id.restaurantEmailField);
        final EditText website = (EditText) findViewById(R.id.restaurantWebsiteField);
        final EditText pIVA = (EditText) findViewById(R.id.restaurantIVAField);*/

        EditText txt = (EditText) findViewById(R.id.restaurantNameField);
        restaurant.setRestaurantName(txt.getText().toString());

        txt = (EditText) findViewById(R.id.restaurantPhoneField);
        restaurant.setRestaurantPhone(txt.getText().toString());

        txt = (EditText) findViewById(R.id.restaurantAddressField);
        restaurant.setRestaurantAddress(txt.getText().toString());

        txt = (EditText) findViewById(R.id.restaurantEmailField);
        restaurant.setRestaurantEmail(txt.getText().toString());

        txt = (EditText) findViewById(R.id.restaurantWebsiteField);
        restaurant.setRestaurantWebsite(txt.getText().toString());

        txt = (EditText) findViewById(R.id.restaurantIVAField);
        restaurant.setRestaurantPiva(txt.getText().toString());
        //save data to shared preferences

        Intent intent = new Intent(this, OwnerActivityRestaurantProfile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(restaurant);
        editor.putString("restaurant", json);
        editor.commit();
    }
}
