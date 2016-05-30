package a15.group.lab4;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class UserActivityRestaurantProfile extends AppCompatActivity {

    Restaurant restaurant;
    ImageView imageView;
    TextView textView;
    ImageView likeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_restaurant_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_restaurant_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();

        String json = preferences.getString("restaurant", null);
        if (json != null) {
            restaurant = gson.fromJson(json, Restaurant.class);

            imageView = (ImageView) findViewById(R.id.restaurant_profile_image);
            imageView.setImageURI(Uri.parse(restaurant.getRestaurantPhoto()));

            textView = (TextView) findViewById(R.id.restaurant_profile_restaurant);
            textView.setText(restaurant.getRestaurantName());

            textView = (TextView) findViewById(R.id.restaurant_profile_restaurant_email);
            textView.setText(restaurant.getRestaurantEmail());

            textView = (TextView) findViewById(R.id.restaurant_profile_restaurant_address);
            textView.setText(restaurant.getRestaurantAddress());

            textView = (TextView) findViewById(R.id.restaurant_profile_restaurant_phone);
            textView.setText(restaurant.getRestaurantPhone());

            textView = (TextView) findViewById(R.id.restaurant_profile_restaurant_website);
            textView.setText(restaurant.getRestaurantWebsite());

            textView = (TextView) findViewById(R.id.restaurant_profile_restaurant_iva);
            textView.setText(restaurant.getRestaurantPiva());

            textView = (TextView) findViewById(R.id.likeStatText);
            textView.setText("+ " + Integer.toString(restaurant.getLikeCount()));

            likeButton = (ImageView) findViewById(R.id.likeButton);
            if (restaurant.getLiked()) {
                likeButton.setBackgroundColor(getResources().getColor(R.color.button_color));
            } else {
                likeButton.setBackgroundColor(Color.TRANSPARENT);
            }
        }

    }

    public void likeTheRestaurant(View view) {
        if (restaurant.getLiked()) {
            likeButton.setBackgroundColor(Color.TRANSPARENT);
            restaurant.setLiked(false);
            saveInGson();
        } else {
            likeButton.setBackgroundColor(getResources().getColor(R.color.button_color));
            restaurant.setLiked(true);
            saveInGson();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        saveInGson();
    }

    private void saveInGson() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(restaurant);
        editor.putString("restaurant", json);
        editor.commit();
    }

}
