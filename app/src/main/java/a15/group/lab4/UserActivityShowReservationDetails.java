package a15.group.lab4;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

public class UserActivityShowReservationDetails extends AppCompatActivity {

    private Reservation reservation;
    private DailyOffer dailyOffer;
    private String dailyOfferID;

    private Customer customer;
    private Restaurant restaurant;
    private String restaurantID;
    private ArrayList<Restaurant> restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_show_reservation_details);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();

        String json = preferences.getString("reservation", null);
        if(json != null) {
            reservation = gson.fromJson(json, Reservation.class);

            dailyOffer = reservation.getDailyOffer();
            customer = reservation.getCustomer();
            restaurantID = dailyOffer.getRestaurantID();
            restaurants = DataGen.makeRestaurants();

            for (Restaurant r : restaurants) {
                if (r.getID().contentEquals(restaurantID)) {
                    restaurant = r;
                }
            }

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            json = gson.toJson(restaurant);
            editor.putString("restaurant", json);
            editor.commit();

            json = gson.toJson(dailyOffer);
            editor.putString("offer", json);
            editor.commit();

            ImageView imageView = (ImageView) findViewById(R.id.reservation_details_image);
            imageView.setImageURI(Uri.parse(dailyOffer.getPhoto()));

            TextView textView = (TextView) findViewById(R.id.reservation_details_price);
            textView.setText(String.format(Locale.getDefault(), "%d", dailyOffer.getPrice()) + " €");
            textView = (TextView) findViewById(R.id.reservation_details_date_time);
            textView.setText(reservation.getDate() + " at " + reservation.getTime());
            textView = (TextView) findViewById(R.id.reservation_details_customer_name);
            textView.setText(customer.getName() + " " + customer.getSurname());

            textView = (TextView) findViewById(R.id.reservation_details_button_restaurant);
            textView.setText(restaurant.getRestaurantName());
            imageView = (ImageView) findViewById(R.id.reservation_details_restaurant_image);
            imageView.setImageURI(Uri.parse(restaurant.getRestaurantPhoto()));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_reservation_details);
        toolbar.setTitle(dailyOffer.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    public void goToOfferDescription(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivityShowReservationDetails.this);

        builder.setTitle(dailyOffer.getName()+" : ");
        LinearLayout layout= new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView description = new TextView(this);
        description.setText(dailyOffer.getDescription());
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
        //startActivity(new Intent(this, UserActivityShowOfferDetails.class));
    }




    public void goToRestaurantDescription(View view) {
        startActivity(new Intent(this, UserActivityRestaurantProfile.class));
    }


}
