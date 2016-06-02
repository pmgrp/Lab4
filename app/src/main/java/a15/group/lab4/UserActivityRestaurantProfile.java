package a15.group.lab4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class UserActivityRestaurantProfile extends AppCompatActivity {

    Restaurant restaurant;
    ImageView imageView;
    TextView textView;
    ImageView likeButton;
    private Context context;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        String restaurantId = getIntent().getStringExtra("restaurantID");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth == null){
            Intent in = new Intent(context, ActivityMain.class);
            startActivity(in);
        }

        setContentView(R.layout.user_activity_restaurant_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar_restaurant_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRefRestaurant = database.getReference().child("restaurants").child(restaurantId);
        getRestaurant(mRefRestaurant);
    }

    private void getRestaurant(DatabaseReference mRefRestaurant){
        mRefRestaurant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    restaurant = dataSnapshot.getValue(Restaurant.class);
                    toolbar.setTitle(restaurant.getRestaurantName());
                    imageView = (ImageView) findViewById(R.id.restaurant_profile_image);
                    Glide.with(context)
                            .load(restaurant.getRestaurantPhoto())
                            .centerCrop()
                            .into(imageView);

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

                    /*textView = (TextView) findViewById(R.id.restaurant_profile_restaurant_iva);
                    textView.setText(restaurant.getRestaurantPiva());*/

                    textView = (TextView) findViewById(R.id.likeStatText);
                    textView.setText("+ " + Integer.toString(restaurant.getLikeCount()));

                    likeButton = (ImageView) findViewById(R.id.likeButton);
                    if (restaurant.getLiked()) {
                        likeButton.setImageResource(R.mipmap.like_blue_thumb);
                    } else {
                        likeButton.setImageResource(R.mipmap.like_button_thumb);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

    public void likeTheRestaurant(View view) {
        if (restaurant.getLiked()) {
            likeButton.setImageResource(R.mipmap.like_button_thumb);
            restaurant.setLiked(false);
            //saveInGson();
        } else {
            likeButton.setImageResource(R.mipmap.like_blue_thumb);
            restaurant.setLiked(true);
            //saveInGson();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
    }

}
