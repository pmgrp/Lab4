package a15.group.lab4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import android.support.v4.app.Fragment;
public class UserActivityRestaurantProfile extends AppCompatActivity {

    private Restaurant restaurant;
    private ImageView imageView;
    private TextView textView;
    private ImageView likeButton;
    private FirebaseAuth mAuth;
    private String userId;
    private DatabaseReference mRefLike;
    private Context context;
    private Toolbar toolbar;
    private ValueEventListener likeListener;
    private ValueEventListener restaurantListener;
    private ValueEventListener openingHoursListener;
    private boolean liked;
    private int likeCount = 0;
    private DatabaseReference mRefRestaurant;
    private DatabaseReference mRefOpenHours;
    //lock for like count
    private final Object lock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;

        mAuth = FirebaseAuth.getInstance();
        if(mAuth == null){
            startActivity(new Intent(context, ActivityMain.class));
        }
        else{
            userId = mAuth.getCurrentUser().getUid();
        }

        //set template
        setContentView(R.layout.user_activity_restaurant_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar_restaurant_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String restaurantId = getIntent().getStringExtra("restaurantID");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRefRestaurant = database.getReference().child("restaurants").child(restaurantId);
        mRefLike = FirebaseDatabase.getInstance().getReference().child("restaurants-likes").child(restaurantId).child(userId);
        mRefOpenHours = FirebaseDatabase.getInstance().getReference().child("opening-hours").child(restaurantId);


        //listener for likes
        likeListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //like is already set
                if(dataSnapshot.exists()){
                    likeButton.setImageResource(R.mipmap.like_blue_thumb);
                    liked = true;
                }
                else if(!dataSnapshot.exists()){
                    likeButton.setImageResource(R.mipmap.like_button_thumb);
                    liked = false;
                }

                likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference mRef = FirebaseDatabase.getInstance()
                                .getReference().child("restaurants-likes").child(restaurantId);
                        int nLikes = getLikeCount();
                        if(!liked){
                            mRefRestaurant.child("likeCount").setValue(nLikes+1);
                            mRef.child(userId).setValue(userId);
                        }
                        else{
                            mRefRestaurant.child("likeCount").setValue(nLikes-1);
                            mRef.child(userId).removeValue();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
            }
        };

        //listener for restaurant
        restaurantListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    restaurant = dataSnapshot.getValue(Restaurant.class);
                    //get number of likes
                    setLikeCount(restaurant.getLikeCount());
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

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
            }

        };

        //listener for opening hours
        openingHoursListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if owner has set the opening hours
                if(dataSnapshot.exists()){
                    OpeningDaysHours openingDaysHours = dataSnapshot.getValue(OpeningDaysHours.class);
                    //TODO display opening hours in view
                }
                else{
                    //here if opening hours are not set by owner
                    //TODO if there aren't opnening hours display other things
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //set listeners
        mRefRestaurant.addValueEventListener(restaurantListener);
        mRefLike.addValueEventListener(likeListener);
        mRefOpenHours.addValueEventListener(openingHoursListener);

        FragmentManager FM = getFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        MapFragmentClass MF = new MapFragmentClass();
        FT.add(R.id.mapLayout, MF);
        //LatLng latlo = new LatLng(9.2442422, 53.35353);
        //MF.setCoords(latlo);
        FT.commit();


    }

    //that methods should be thread safe
    public void setLikeCount(int value){
        synchronized (lock) {
            this.likeCount = value;
        }
    }

    public int getLikeCount(){
        synchronized (lock){
            return this.likeCount;
        }
    }


    @Override
    public void onStop(){
        super.onStop();
        if(likeListener != null){
            mRefLike.removeEventListener(likeListener);
        }
        if(restaurantListener != null){
            mRefRestaurant.removeEventListener(restaurantListener);
        }
        if(openingHoursListener != null){
            mRefOpenHours.removeEventListener(openingHoursListener);
        }
    }

    public void backToMenu(View view){
        Intent in = new Intent(this, ActivityMain.class);
        startActivity(in);
    }

}
