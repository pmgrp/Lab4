package a15.group.lab4;

import android.content.SharedPreferences;
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
            textView.setText(Integer.toString(restaurant.getLikeCount()) + " people like this restaurant.");
        }

        final Button likeButton = (Button) findViewById(R.id.likeButton);
        final TextView likeStatText = (TextView) findViewById(R.id.likeStatText);

        final String textBeforeLike = likeStatText.getText().toString();


        likeButton.setTag(1);
        likeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final int status = (Integer) v.getTag();
                if (status == 1) {
                    likeStatText.setText("You and " + Integer.toString(restaurant.getLikeCount()) + " people like this restaurant.");
                    likeButton.setText("Unlike this Restaurant");
                    v.setTag(0); //unlike
                } else {
                    likeButton.setText("Like this Restaurant");
                    likeStatText.setText(textBeforeLike);
                    v.setTag(1); //like
                }
            }
        });
    }
}