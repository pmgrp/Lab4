package a15.group.lab4;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.internal.StreetViewLifecycleDelegate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UserActivityShowOfferDetails extends AppCompatActivity
        implements UserFragmentTimePicker.FragmentListener {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference mRefOffer;
    private DatabaseReference mRefRestaurant;
    private DatabaseReference mRefUserReservation;
    private DatabaseReference mRefOwnerReservation;
    private DatabaseReference mRefUser;
    private DatabaseReference mRefOpenHours;
    private ValueEventListener openingHoursListener;

    private String offerId;
    private String restaurantId;
    private String userId;
    private String reservationId;
    private DailyOffer offer;
    private Restaurant restaurant;
    private Reservation reservation;
    private User user;
    private Context context;

    private Toolbar toolbar;
    int xyear = -1;
    int xmonth = -1;
    int xday = -1;
    int xhour = -1;
    int xminute = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        mAuth = FirebaseAuth.getInstance();
        if (mAuth == null) {
            Intent in = new Intent(context, ActivityMain.class);
            startActivity(in);
        } else {
            userId = mAuth.getCurrentUser().getUid();
        }

        setContentView(R.layout.user_activity_show_offer_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar_offer_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        offerId = getIntent().getStringExtra("offerID");
        restaurantId = getIntent().getStringExtra("restaurantID");

        database = FirebaseDatabase.getInstance();
        mRefOffer = database.getReference().child("offers").child(offerId);
        mRefRestaurant = database.getReference().child("restaurants").child(restaurantId);
        mRefUserReservation = database.getReference().child("user-reservations").child(userId);
        mRefOwnerReservation = database.getReference().child("owner-reservations").child(restaurantId);
        mRefUser = database.getReference().child("users").child(userId);
        mRefOpenHours = FirebaseDatabase.getInstance().getReference().child("opening-hours").child(restaurantId);

        mRefRestaurant.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    restaurant = dataSnapshot.getValue(Restaurant.class);
                    getUser(mRefUser);
                    getOffer(mRefOffer);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
            }
        });

        //listener for opening hours
        openingHoursListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if owner has set the opening hours
                if (dataSnapshot.exists()) {
                    OpeningDaysHours openingDaysHours = dataSnapshot.getValue(OpeningDaysHours.class);
                    //TODO display opening hours in view
                } else {
                    //here if opening hours are not set by owner
                    //TODO if there aren't opnening hours display other things
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //here set the listener
        mRefOpenHours.addValueEventListener(openingHoursListener);
    }

    private void getUser(DatabaseReference mRefUser) {
        mRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void getOffer(DatabaseReference mRefOffer) {
        mRefOffer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    offer = dataSnapshot.getValue(DailyOffer.class);
                    toolbar.setTitle(offer.getName());
                    ImageView offerDetailsImage = (ImageView) findViewById(R.id.offer_details_image);
                    Glide.with(context)
                            .load(offer.getPhoto())
                            .into(offerDetailsImage);
                    TextView textView = (TextView) findViewById(R.id.button_buy);
                    textView.setText(String.format(Locale.getDefault(), "%d", offer.getPrice()) + " €");
                    textView = (TextView) findViewById(R.id.offer_details_description);
                    textView.setText(offer.getDescription());
                    textView = (TextView) findViewById(R.id.offer_details_button_restaurant);
                    textView.setText(offer.getRestaurantName());
                    ImageView restaurantDetailsImage = (ImageView) findViewById(R.id.offer_details_restaurant_image);
                    Glide.with(context)
                            .load(restaurant.getRestaurantPhoto())
                            .centerCrop()
                            .into(restaurantDetailsImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void goToRestaurantDescription(View view) {
        Intent in = new Intent(this, UserActivityRestaurantProfile.class);
        in.putExtra("restaurantID", offer.getRestaurantID());
        startActivity(in);
    }

    public void showDateTimePicker(View v) {
        DialogFragment newFragment = new UserFragmentTimePicker();
        newFragment.show(getSupportFragmentManager(), "timePicker");
        newFragment = new UserFragmentDatePicker();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void newNotif(View view) {
        String contentText = "New reservation Made";
        createNotification(contentText, 3);
    }

    private void createNotification(String text, int mId) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.logo_app_coyote)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(text);

// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, UserActivityShowOfferDetails.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(UserActivityShowOfferDetails.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
    }

    public void onTimeFragmentOkListener() {
        Log.d("DAY", Integer.toString(xday));
        Log.d("MONTH", Integer.toString(xmonth));
        Log.d("YEAR", Integer.toString(xyear));
        Log.d("HOUR", Integer.toString(xhour));
        Log.d("MINUTE", Integer.toString(xminute));
        if (xhour != -1 && xminute != -1 && xday != -1 && xmonth != -1 && xyear != -1) {

            bookReservation();

        } else {
            Toast t = Toast.makeText(this, "Reservation Not Completed", Toast.LENGTH_SHORT);
            t.show();
            xyear = -1;
            xmonth = -1;
            xday = -1;
            xhour = -1;
            xminute = -1;

        }
    }

    private void bookReservation() {
        String tempMin = Integer.toString(xminute);
        String tempMin2;
        if (xminute < 10)
            tempMin2 = "0" + tempMin;
        else
            tempMin2 = tempMin;

        String date = Integer.toString(xday) + "/" + Integer.toString(xmonth) + "/" + Integer.toString(xyear);
        String time = Integer.toString(xhour) + ":" + tempMin2;

        reservationId = mRefUserReservation.push().getKey();
        reservation = new Reservation(userId, offerId, restaurantId, reservationId,
                user.getName(), user.getSurname(), user.getPhone(), user.getEmail(), offer.getPrice(),
                offer.getName(), restaurant.getRestaurantName(), restaurant.getRestaurantPhoto(), date, time, "", Reservation.ARRIVED);
        mRefUserReservation.child(reservationId).setValue(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mRefOwnerReservation.child(reservationId).setValue(reservation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Reservation Booked",
                                        Toast.LENGTH_LONG).show();
                                Intent in = new Intent(context, UserActivityShowReservations.class);
                                startActivity(in);
                            } else {
                                Toast.makeText(context, "Connection Error",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(context, "Connection Error",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setTime(int hour, int minutes) {
        this.xhour = hour;
        this.xminute = minutes;
    }

    public void setDate(int xyear, int xmonth, int xday) {
        this.xyear = xyear;
        this.xmonth = xmonth;
        this.xday = xday;
    }

    public void backToMenu(View view) {
        Intent in = new Intent(this, ActivityMain.class);
        startActivity(in);
    }

    public void widenImage(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivityShowOfferDetails.this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final ImageView image = new ImageView(this);
        Glide.with(context)
                .load(offer.getPhoto())
                .into(image);

        layout.addView(image);
        builder.setView(layout);

        builder.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (openingHoursListener != null) {
            mRefOpenHours.removeEventListener(openingHoursListener);
        }
    }


}