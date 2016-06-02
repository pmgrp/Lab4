package a15.group.lab4;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

public class UserActivityShowReservationDetails extends AppCompatActivity {


    private Context context;
    private Toolbar toolbar;
    private String userId;
    private String offerId;
    private String restaurantId;
    private DailyOffer offer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth == null){
            Intent in = new Intent(context, ActivityMain.class);
            startActivity(in);
        }
        else{
            userId = mAuth.getCurrentUser().getUid();
        }

        setContentView(R.layout.user_activity_show_reservation_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar_reservation_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        offerId = getIntent().getStringExtra("offerID");
        restaurantId = getIntent().getStringExtra("restaurantID");
        String reservationId = getIntent().getStringExtra("reservationID");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRefOffer = database.getReference().child("offers").child(offerId);
        DatabaseReference mRefRestaurant = database.getReference().child("restaurants").child(restaurantId);
        DatabaseReference mRefUserReservation = database.getReference().child("user-reservations").child(userId).child(reservationId);
        DatabaseReference mRefUser = database.getReference().child("users").child(userId);



        getReservation(mRefUserReservation);
        getOffer(mRefOffer);
        getRestaurant(mRefRestaurant);
        getUser(mRefUser);











    }

    private void getOffer(DatabaseReference mRefOffer) {
        mRefOffer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    offer = dataSnapshot.getValue(DailyOffer.class);
                    toolbar.setTitle(offer.getName());
                    TextView textView = (TextView) findViewById(R.id.reservation_details_price);
                    textView.setText(String.format(Locale.getDefault(), "%d", offer.getPrice()) + " €");
                    ImageView imageView = (ImageView) findViewById(R.id.reservation_details_image);
                    Glide.with(context)
                            .load(offer.getPhoto())
                            .centerCrop()
                            .into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void getRestaurant(DatabaseReference mRefRestaurant){
        mRefRestaurant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                    TextView textView = (TextView) findViewById(R.id.reservation_details_button_restaurant);
                    textView.setText(restaurant.getRestaurantName());
                    ImageView imageView = (ImageView) findViewById(R.id.reservation_details_restaurant_image);
                    Glide.with(context)
                            .load(restaurant.getRestaurantPhoto())
                            .centerCrop()
                            .into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

    private void getUser(DatabaseReference mRefUser){
        mRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    User user = dataSnapshot.getValue(User.class);
                    TextView textView = (TextView) findViewById(R.id.reservation_details_customer_name);
                    textView.setText(user.getName() + " " + user.getSurname());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
            }
        });



    }

    private void getReservation(DatabaseReference mRefUserReservation){
        mRefUserReservation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Reservation reservation = dataSnapshot.getValue(Reservation.class);
                    TextView textView = (TextView) findViewById(R.id.reservation_details_date_time);
                    textView.setText(reservation.getDate() + " at " + reservation.getTime());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }


    public void goToOfferDescription(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivityShowReservationDetails.this);

        builder.setTitle(offer.getName()+" : ");
        LinearLayout layout= new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView description = new TextView(this);
        description.setText(offer.getDescription());
        description.setTextColor(Color.GRAY);
        description.setGravity(Gravity.CENTER);
        description.setTextSize(18);
        description.setPadding(0,15,0,0);
        layout.addView(description);
        builder.setView(layout);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        /*
        Intent in = new Intent(this, UserActivityShowOfferDetails.class);
        in.putExtra("offerID", offerId);
        in.putExtra("restaurantID", restaurantId);
        startActivity(in);
        */
    }



    public void goToRestaurantDescription(View view) {
        Intent in = new Intent(this, UserActivityRestaurantProfile.class);
        in.putExtra("restaurantID", restaurantId);
        startActivity(in);
    }


}
