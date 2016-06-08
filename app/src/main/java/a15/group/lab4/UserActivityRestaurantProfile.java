package a15.group.lab4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
public class UserActivityRestaurantProfile extends AppCompatActivity implements OnMapReadyCallback {

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

    private boolean continuous;

    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private boolean sunday;

    private boolean mondayLunch;
    private boolean mondayDiner;
    private boolean tuesdayLunch;
    private boolean tuesdayDiner;
    private boolean wednesdayLunch;
    private boolean wednesdayDiner;
    private boolean thursdayLunch;
    private boolean thursdayDiner;
    private boolean fridayLunch;
    private boolean fridayDiner;
    private boolean saturdayLunch;
    private boolean saturdayDiner;
    private boolean sundayLunch;
    private boolean sundayDiner;

    private String mondayLunchStart;
    private String mondayLunchEnd;
    private String mondayDinerStart;
    private String mondayDinerEnd;
    private String tuesdayLunchStart;
    private String tuesdayLunchEnd;
    private String tuesdayDinerStart;
    private String tuesdayDinerEnd;
    private String wednesdayLunchStart;
    private String wednesdayLunchEnd;
    private String wednesdayDinerStart;
    private String wednesdayDinerEnd;
    private String thursdayLunchStart;
    private String thursdayLunchEnd;
    private String thursdayDinerStart;
    private String thursdayDinerEnd;
    private String fridayLunchStart;
    private String fridayLunchEnd;
    private String fridayDinerStart;
    private String fridayDinerEnd;
    private String saturdayLunchStart;
    private String saturdayLunchEnd;
    private String saturdayDinerStart;
    private String saturdayDinerEnd;
    private String sundayLunchStart;
    private String sundayLunchEnd;
    private String sundayDinerStart;
    private String sundayDinerEnd;

    private String mondayAllDayStart;
    private String mondayAllDayEnd;
    private String tuesdayAllDayStart;
    private String tuesdayAllDayEnd;
    private String wednesdayAllDayStart;
    private String wednesdayAllDayEnd;
    private String thursdayAllDayStart;
    private String thursdayAllDayEnd;
    private String fridayAllDayStart;
    private String fridayAllDayEnd;
    private String saturdayAllDayStart;
    private String saturdayAllDayEnd;
    private String sundayAllDayStart;
    private String sundayAllDayEnd;

    @Override
    public void onMapReady(GoogleMap map) {

        LatLng currentLocation = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());

        map.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title(restaurant.getRestaurantName()));
        moveToCurrentLocation(map, currentLocation);
    }

    private void moveToCurrentLocation(GoogleMap map, LatLng currentLocation)
    {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;

        mAuth = FirebaseAuth.getInstance();
        if (mAuth == null) {
            startActivity(new Intent(context, ActivityMain.class));
        } else {
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
                if (dataSnapshot.exists()) {
                    likeButton.setImageResource(R.mipmap.like_blue_thumb);
                    liked = true;
                } else if (!dataSnapshot.exists()) {
                    likeButton.setImageResource(R.mipmap.like_button_thumb);
                    liked = false;
                }

                likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference mRef = FirebaseDatabase.getInstance()
                                .getReference().child("restaurants-likes").child(restaurantId);
                        int nLikes = getLikeCount();
                        if (!liked) {
                            mRefRestaurant.child("likeCount").setValue(nLikes + 1);
                            mRef.child(userId).setValue(userId);
                        } else {
                            mRefRestaurant.child("likeCount").setValue(nLikes - 1);
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
                if (dataSnapshot.exists()) {
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

                    SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
                    FragmentTransaction fragmentTransaction =
                            getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.map, mMapFragment);
                    fragmentTransaction.commit();
                    mMapFragment.getMapAsync(UserActivityRestaurantProfile.this);

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
                if (dataSnapshot.exists()) {
                    OpeningDaysHours openingDaysHours = dataSnapshot.getValue(OpeningDaysHours.class);

                    continuous = openingDaysHours.isContinuous();

                    monday = openingDaysHours.isMonday();
                    tuesday = openingDaysHours.isTuesday();
                    wednesday = openingDaysHours.isWednesday();
                    thursday = openingDaysHours.isThursday();
                    friday = openingDaysHours.isFriday();
                    saturday = openingDaysHours.isSaturday();
                    sunday = openingDaysHours.isSunday();

                    mondayLunch = openingDaysHours.isMondayMorning();
                    mondayDiner = openingDaysHours.isMondayEvening();
                    tuesdayLunch = openingDaysHours.isTuesdayMorning();
                    tuesdayDiner = openingDaysHours.isTuesdayEvening();
                    wednesdayLunch = openingDaysHours.isWednesdayMorning();
                    wednesdayDiner = openingDaysHours.isWednesdayEvening();
                    thursdayLunch = openingDaysHours.isThursdayMorning();
                    thursdayDiner = openingDaysHours.isThursdayEvening();
                    fridayLunch = openingDaysHours.isFridayMorning();
                    fridayDiner = openingDaysHours.isFridayEvening();
                    saturdayLunch = openingDaysHours.isSaturdayMorning();
                    saturdayDiner = openingDaysHours.isSaturdayEvening();
                    sundayLunch = openingDaysHours.isSundayMorning();
                    sundayDiner = openingDaysHours.isSundayEvening();


                    mondayLunchStart = openingDaysHours.getMondayLunchStart();
                    mondayLunchEnd = openingDaysHours.getMondayLunchEnd();
                    mondayDinerStart = openingDaysHours.getMondayDinerStart();
                    mondayDinerEnd = openingDaysHours.getMondayDinerEnd();
                    tuesdayLunchStart = openingDaysHours.getTuesdayLunchStart();
                    tuesdayLunchEnd = openingDaysHours.getTuesdayLunchEnd();
                    tuesdayDinerStart = openingDaysHours.getTuesdayDinerStart();
                    tuesdayDinerEnd = openingDaysHours.getTuesdayDinerEnd();
                    wednesdayLunchStart = openingDaysHours.getWednesdayLunchStart();
                    wednesdayLunchEnd = openingDaysHours.getWednesdayLunchEnd();
                    wednesdayDinerStart = openingDaysHours.getWednesdayDinerStart();
                    wednesdayDinerEnd = openingDaysHours.getWednesdayDinerEnd();
                    thursdayLunchStart = openingDaysHours.getThursdayLunchStart();
                    thursdayLunchEnd = openingDaysHours.getThursdayLunchEnd();
                    thursdayDinerStart = openingDaysHours.getThursdayDinerStart();
                    thursdayDinerEnd = openingDaysHours.getThursdayDinerEnd();
                    fridayLunchStart = openingDaysHours.getFridayLunchStart();
                    fridayLunchEnd = openingDaysHours.getFridayLunchEnd();
                    fridayDinerStart = openingDaysHours.getFridayDinerStart();
                    fridayDinerEnd = openingDaysHours.getFridayDinerEnd();
                    saturdayLunchStart = openingDaysHours.getSaturdayLunchStart();
                    saturdayLunchEnd = openingDaysHours.getSaturdayLunchEnd();
                    saturdayDinerStart = openingDaysHours.getSaturdayDinerStart();
                    saturdayDinerEnd = openingDaysHours.getSaturdayDinerEnd();
                    sundayLunchStart = openingDaysHours.getSundayLunchStart();
                    sundayLunchEnd = openingDaysHours.getSundayLunchEnd();
                    sundayDinerStart = openingDaysHours.getSundayDinerStart();
                    sundayDinerEnd = openingDaysHours.getSundayDinerEnd();

                    mondayAllDayStart = openingDaysHours.getMondayAllDayStart();
                    mondayAllDayEnd = openingDaysHours.getMondayAllDayEnd();
                    tuesdayAllDayStart = openingDaysHours.getTuesdayAllDayStart();
                    tuesdayAllDayEnd = openingDaysHours.getTuesdayAllDayEnd();
                    wednesdayAllDayStart = openingDaysHours.getWednesdayAllDayStart();
                    wednesdayAllDayEnd = openingDaysHours.getWednesdayAllDayEnd();
                    thursdayAllDayStart = openingDaysHours.getThursdayAllDayStart();
                    thursdayAllDayEnd = openingDaysHours.getThursdayAllDayEnd();
                    fridayAllDayStart = openingDaysHours.getFridayAllDayStart();
                    fridayAllDayEnd = openingDaysHours.getFridayAllDayEnd();
                    saturdayAllDayStart = openingDaysHours.getSaturdayAllDayStart();
                    saturdayAllDayEnd = openingDaysHours.getSaturdayAllDayEnd();
                    sundayAllDayStart = openingDaysHours.getSundayAllDayStart();
                    sundayAllDayEnd = openingDaysHours.getSundayAllDayEnd();
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

    }

    //that methods should be thread safe
    public void setLikeCount(int value) {
        synchronized (lock) {
            this.likeCount = value;
        }
    }

    public int getLikeCount() {
        synchronized (lock) {
            return this.likeCount;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (likeListener != null) {
            mRefLike.removeEventListener(likeListener);
        }
        if (restaurantListener != null) {
            mRefRestaurant.removeEventListener(restaurantListener);
        }
        if (openingHoursListener != null) {
            mRefOpenHours.removeEventListener(openingHoursListener);
        }
    }

    public void backToMenu(View view) {
        Intent in = new Intent(this, ActivityMain.class);
        startActivity(in);
    }

    public void showOpeningHours(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.user_popup_opening_hours, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivityRestaurantProfile.this);
        builder.setTitle("Opening Hours");
        builder.setView(dialogLayout);

        final TextView startLunchMonday = (TextView) dialogLayout.findViewById(R.id.start_lunch_monday);
        final TextView startLunchTuesday = (TextView) dialogLayout.findViewById(R.id.start_lunch_tuesday);
        final TextView startLunchWednesday = (TextView) dialogLayout.findViewById(R.id.start_lunch_wednesday);
        final TextView startLunchThursday = (TextView) dialogLayout.findViewById(R.id.start_lunch_thursday);
        final TextView startLunchFriday = (TextView) dialogLayout.findViewById(R.id.start_lunch_friday);
        final TextView startLunchSaturday = (TextView) dialogLayout.findViewById(R.id.start_lunch_saturday);
        final TextView startLunchSunday = (TextView) dialogLayout.findViewById(R.id.start_lunch_sunday);

        final TextView endLunchMonday = (TextView) dialogLayout.findViewById(R.id.end_lunch_monday);
        final TextView endLunchTuesday = (TextView) dialogLayout.findViewById(R.id.end_lunch_tuesday);
        final TextView endLunchWednesday = (TextView) dialogLayout.findViewById(R.id.end_lunch_wednesday);
        final TextView endLunchThursday = (TextView) dialogLayout.findViewById(R.id.end_lunch_thursday);
        final TextView endLunchFriday = (TextView) dialogLayout.findViewById(R.id.end_lunch_friday);
        final TextView endLunchSaturday = (TextView) dialogLayout.findViewById(R.id.end_lunch_saturday);
        final TextView endLunchSunday = (TextView) dialogLayout.findViewById(R.id.end_lunch_sunday);

        final TextView startDinerMonday = (TextView) dialogLayout.findViewById(R.id.start_diner_monday);
        final TextView startDinerTuesday = (TextView) dialogLayout.findViewById(R.id.start_diner_tuesday);
        final TextView startDinerWednesday = (TextView) dialogLayout.findViewById(R.id.start_diner_wednesday);
        final TextView startDinerThursday = (TextView) dialogLayout.findViewById(R.id.start_diner_thursday);
        final TextView startDinerFriday = (TextView) dialogLayout.findViewById(R.id.start_diner_friday);
        final TextView startDinerSaturday = (TextView) dialogLayout.findViewById(R.id.start_diner_saturday);
        final TextView startDinerSunday = (TextView) dialogLayout.findViewById(R.id.start_diner_sunday);

        final TextView endDinerMonday = (TextView) dialogLayout.findViewById(R.id.end_diner_monday);
        final TextView endDinerTuesday = (TextView) dialogLayout.findViewById(R.id.end_diner_tuesday);
        final TextView endDinerWednesday = (TextView) dialogLayout.findViewById(R.id.end_diner_wednesday);
        final TextView endDinerThursday = (TextView) dialogLayout.findViewById(R.id.end_diner_thursday);
        final TextView endDinerFriday = (TextView) dialogLayout.findViewById(R.id.end_diner_friday);
        final TextView endDinerSaturday = (TextView) dialogLayout.findViewById(R.id.end_diner_saturday);
        final TextView endDinerSunday = (TextView) dialogLayout.findViewById(R.id.end_diner_sunday);

        startLunchMonday.setText(mondayLunchStart);
        endLunchMonday.setText(mondayLunchEnd);
        startDinerMonday.setText(mondayDinerStart);
        endDinerMonday.setText(mondayDinerEnd);
        startLunchTuesday.setText(tuesdayLunchStart);
        endLunchTuesday.setText(tuesdayLunchEnd);
        startDinerTuesday.setText(tuesdayDinerStart);
        endDinerTuesday.setText(tuesdayDinerEnd);
        startLunchWednesday.setText(wednesdayLunchStart);
        endLunchWednesday.setText(wednesdayLunchEnd);
        startDinerWednesday.setText(wednesdayDinerStart);
        endDinerWednesday.setText(wednesdayDinerEnd);
        startLunchThursday.setText(thursdayLunchStart);
        endLunchThursday.setText(thursdayLunchEnd);
        startDinerThursday.setText(thursdayDinerStart);
        endDinerThursday.setText(thursdayDinerEnd);
        startLunchFriday.setText(fridayLunchStart);
        endLunchFriday.setText(fridayLunchEnd);
        startDinerFriday.setText(fridayDinerStart);
        endDinerFriday.setText(fridayDinerEnd);
        startLunchSaturday.setText(saturdayLunchStart);
        endLunchSaturday.setText(saturdayLunchEnd);
        startDinerSaturday.setText(saturdayDinerStart);
        endDinerSaturday.setText(saturdayDinerEnd);
        startLunchSunday.setText(sundayLunchStart);
        endLunchSunday.setText(sundayLunchEnd);
        startDinerSunday.setText(sundayDinerStart);
        endDinerSunday.setText(sundayDinerEnd);

        LinearLayout ll;
        TextView tv;

        if (!monday) {
            ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_monday);
            ll.setVisibility(View.INVISIBLE);
            tv = (TextView) dialogLayout.findViewById(R.id.monday_title);
            tv.setTextColor(getResources().getColor(R.color.lightgrey));
        } else {
            if (!mondayLunch && !continuous) {
                ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_monday_lunch);
                ll.setVisibility(View.INVISIBLE);
            }
            if (!mondayDiner && !continuous) {
                ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_monday_diner);
                ll.setVisibility(View.INVISIBLE);
            }
        }

        if (!tuesday) {
            ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_tuesday);
            ll.setVisibility(View.INVISIBLE);
            tv = (TextView) dialogLayout.findViewById(R.id.tuesday_title);
            tv.setTextColor(getResources().getColor(R.color.lightgrey));
        } else {
            if (!tuesdayLunch && !continuous) {
                ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_tuesday_lunch);
                ll.setVisibility(View.INVISIBLE);
            }
            if (!tuesdayDiner && !continuous) {
                ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_tuesday_diner);
                ll.setVisibility(View.INVISIBLE);
            }
        }

        if (!wednesday) {
            ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_wednesday);
            ll.setVisibility(View.INVISIBLE);
            tv = (TextView) dialogLayout.findViewById(R.id.wednesday_title);
            tv.setTextColor(getResources().getColor(R.color.lightgrey));
        } else {
            if (!wednesdayLunch && !continuous) {
                ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_wednesday_lunch);
                ll.setVisibility(View.INVISIBLE);
            }
            if (!wednesdayDiner && !continuous) {
                ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_wednesday_diner);
                ll.setVisibility(View.INVISIBLE);
            }
        }

        if (!thursday) {
            ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_thursday);
            ll.setVisibility(View.INVISIBLE);
            tv = (TextView) dialogLayout.findViewById(R.id.thursday_title);
            tv.setTextColor(getResources().getColor(R.color.lightgrey));
        } else {
            if (!thursdayLunch && !continuous) {
                ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_thursday_lunch);
                ll.setVisibility(View.INVISIBLE);
            }
            if (!thursdayDiner && !continuous) {
                ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_thursday_diner);
                ll.setVisibility(View.INVISIBLE);
            }
        }

        if (!friday) {
            ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_friday);
            ll.setVisibility(View.INVISIBLE);
            tv = (TextView) dialogLayout.findViewById(R.id.friday_title);
            tv.setTextColor(getResources().getColor(R.color.lightgrey));
        } else {
            if (!fridayLunch && !continuous) {
                ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_friday_lunch);
                ll.setVisibility(View.INVISIBLE);
            }
            if (!fridayDiner && !continuous) {
                ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_friday_diner);
                ll.setVisibility(View.INVISIBLE);
            }
        }

        if (!saturday) {
            ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_saturday);
            ll.setVisibility(View.INVISIBLE);
            tv = (TextView) dialogLayout.findViewById(R.id.saturday_title);
            tv.setTextColor(getResources().getColor(R.color.lightgrey));
        } else {
            if (!saturdayLunch && !continuous) {
                ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_saturday_lunch);
                ll.setVisibility(View.INVISIBLE);
            }
            if (!saturdayDiner && !continuous) {
                ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_saturday_diner);
                ll.setVisibility(View.INVISIBLE);
            }
        }

        if (!sunday) {
            ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_sunday);
            ll.setVisibility(View.INVISIBLE);
            tv = (TextView) dialogLayout.findViewById(R.id.sunday_title);
            tv.setTextColor(getResources().getColor(R.color.lightgrey));
        } else {
            if (!sundayLunch && !continuous) {
                ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_sunday_lunch);
                ll.setVisibility(View.INVISIBLE);
            }
            if (!sundayDiner && !continuous) {
                ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_sunday_diner);
                ll.setVisibility(View.INVISIBLE);
            }
        }

        if (continuous) {
            ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_monday_diner);
            ll.setVisibility(View.INVISIBLE);

            ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_tuesday_diner);
            ll.setVisibility(View.INVISIBLE);

            ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_wednesday_diner);
            ll.setVisibility(View.INVISIBLE);

            ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_thursday_diner);
            ll.setVisibility(View.INVISIBLE);

            ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_friday_diner);
            ll.setVisibility(View.INVISIBLE);

            ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_saturday_diner);
            ll.setVisibility(View.INVISIBLE);

            ll = (LinearLayout) dialogLayout.findViewById(R.id.linear_sunday_diner);
            ll.setVisibility(View.INVISIBLE);

            startLunchMonday.setText(mondayAllDayStart);
            endLunchMonday.setText(mondayAllDayEnd);
            startLunchTuesday.setText(tuesdayAllDayStart);
            endLunchTuesday.setText(tuesdayAllDayEnd);
            startLunchWednesday.setText(wednesdayAllDayStart);
            endLunchWednesday.setText(wednesdayAllDayEnd);
            startLunchThursday.setText(thursdayAllDayStart);
            endLunchThursday.setText(thursdayAllDayEnd);
            startLunchFriday.setText(fridayAllDayStart);
            endLunchFriday.setText(fridayAllDayEnd);
            startLunchSaturday.setText(saturdayAllDayStart);
            endLunchSaturday.setText(saturdayAllDayEnd);
            startLunchSunday.setText(sundayAllDayStart);
            endLunchSunday.setText(sundayAllDayEnd);

        }

        builder.setView(dialogLayout);

        builder.show();
    }

}
