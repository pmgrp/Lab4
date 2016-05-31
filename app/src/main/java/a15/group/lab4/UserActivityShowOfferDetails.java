package a15.group.lab4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UserActivityShowOfferDetails extends AppCompatActivity
        implements UserFragmentTimePicker.FragmentListener{

    private String ID;
    private Customer customer;
    private DailyOffer dailyOffer;
    private ArrayList<Restaurant> restaurants;
    private String restaurantID;
    private Restaurant restaurant;
    private String time;
    private int status;
    private Calendar myCalendar;
    static final int DIALOG_ID = 0;
    int xyear = -1;
    int xmonth = -1;
    int xday = -1;
    int xhour = -1;
    int xminute = -1;

    Reservation myReservation;
    ArrayList<Reservation> reservations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_show_offer_details);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();

        String json = preferences.getString("offer", null);

        if (json != null) {
            dailyOffer = gson.fromJson(json, DailyOffer.class);
            restaurants = DataGen.makeRestaurants();
            restaurantID = dailyOffer.getRestaurantID();
            for (Restaurant r : restaurants)
            {
                if (r.getID().contentEquals(restaurantID)) {
                    restaurant = r;
                }
            }

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            json = gson.toJson(restaurant);
            editor.putString("restaurant", json);
            editor.commit();

            ImageView imageView = (ImageView) findViewById(R.id.offer_details_image);
            imageView.setImageURI(Uri.parse(dailyOffer.getPhoto()));

            TextView textView = (TextView) findViewById(R.id.button_buy);
            textView.setText(String.format(Locale.getDefault(), "%d", dailyOffer.getPrice()) + " €");
            textView = (TextView) findViewById(R.id.offer_details_description);
            textView.setText(dailyOffer.getDescription());

            textView = (TextView) findViewById(R.id.offer_details_button_restaurant);
            textView.setText(restaurant.getRestaurantName());
            imageView = (ImageView) findViewById(R.id.offer_details_restaurant_image);
            imageView.setImageURI(Uri.parse(restaurant.getRestaurantPhoto()));

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_offer_details);
        toolbar.setTitle(dailyOffer.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //get reservations if there were already saved
        json = preferences.getString("reservations", null);
        if(json != null) {
            reservations = gson.fromJson(json, new TypeToken<List<Reservation>>() {
            }.getType());
        }
        else {
            reservations = new ArrayList<>();
        }

    }

    public void goToRestaurantDescription(View view) {
        startActivity(new Intent(this, UserActivityRestaurantProfile.class));
    }

    public void showDateTimePicker(View v) {
        DialogFragment newFragment = new UserFragmentTimePicker();
        newFragment.show(getSupportFragmentManager(), "timePicker");
        newFragment = new UserFragmentDatePicker();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onTimeFragmentOkListener(){
        Log.d("DAY", Integer.toString(xday));
        Log.d("MONTH", Integer.toString(xmonth));
        Log.d("YEAR", Integer.toString(xyear));
        Log.d("HOUR", Integer.toString(xhour));
        Log.d("MINUTE", Integer.toString(xminute));
        if(xhour != -1 && xminute != -1 && xday != -1 && xmonth != -1 && xyear != -1){
            //create dummy customer
            Customer dummyCustomer = new Customer();
            dummyCustomer.setName("Mario");
            dummyCustomer.setSurname("Rossi");
            dummyCustomer.setPhone("19247934");

            //create new reservation
            myReservation = new Reservation();
            myReservation.setCustomer(dummyCustomer);
            myReservation.setDate(Integer.toString(xday) + "/" + Integer.toString(xmonth) +"/" + Integer.toString(xyear));

            String tempMin = Integer.toString(xminute);
            String tempMin2;

            if(xminute < 10)
                tempMin2 = "0" + tempMin;
            else
                tempMin2 = tempMin;

            myReservation.setTime(Integer.toString(xhour) + ":" + tempMin2);
            myReservation.setDailyOffer(dailyOffer);
            myReservation.setStatus(Reservation.ARRIVED);

            //add the reservation
            reservations.add(myReservation);
            Toast t = Toast.makeText(this, "Reservation Created", Toast.LENGTH_LONG);
            t.show();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();
            JsonElement element = gson.toJsonTree(reservations, new TypeToken<List<Reservation>>(){}.getType());
            JsonArray jsonarray = element.getAsJsonArray();
            editor.putString("reservations", jsonarray.toString());
            editor.commit();

        }
        else{
            Toast t = Toast.makeText(this, "Reservation Not Completed", Toast.LENGTH_SHORT );
            t.show();
            xyear = -1;
            xmonth = -1;
            xday = -1;
            xhour = -1;
            xminute = -1;

        }
    }


    public void setTime(int hour, int minutes){
        this.xhour = hour;
        this.xminute = minutes;
    }

    /*

    private void createTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;

        mTimePicker = new TimePickerDialog(UserActivityShowOfferDetails.this, 4, new TimePickerDialog.OnTimeSetListener() {
            @Override
            // CLEMENT : si tu veux changer le design de l'horloge il faut que tu changes le nombre au-dessus
            // De 1 à 3, c'est des spinner et après c'est des horloges

            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                //eReminderTime.setText( selectedHour + ":" + selectedMinute);
                xhour = selectedHour;
                xminute = selectedMinute;

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

        Log.d("Hour: ", Integer.toString(xhour));
        Log.d("Minutes: ", Integer.toString(xminute));

        myReservation.setTime(Integer.toString(xhour) + ":" + Integer.toString(xminute));
        Customer dummyCustomer = new Customer();
        dummyCustomer.setName("The Big Don");
        dummyCustomer.setSurname("Donald Trump");
        dummyCustomer.setPhone("19247934");
        myReservation.setCustomer(dummyCustomer);

        reservations.add(myReservation);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(reservations, new TypeToken<List<Reservation>>(){}.getType());
        JsonArray jsonarray = element.getAsJsonArray();
        editor.putString("reservations", jsonarray.toString());
        editor.commit();
    }

    */

    public void setDate(int xyear, int xmonth, int xday){
        this.xyear = xyear;
        this.xmonth = xmonth;
        this.xday = xday;
    }




}






  /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
fab.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        //onClickPopupOffer();
        startActivity(new Intent(ActivityShowOffers.this, ActivityAddOffer.class));
        }
        });
*/


/*



   edittext.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            new DatePickerDialog(classname.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    });

      private void updateLabel() {

    String myFormat = "MM/dd/yy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    edittext.setText(sdf.format(myCalendar.getTime()));
    }
 */





/*    Button buy = (Button) findViewById(R.id.button_buy);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPopupOffer();
            }
        });

    }

    public void onClickPopupOffer() {
        final CharSequence[] items = { "Yes", "No" };
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivityShowOfferDetails.this);
        builder.setTitle("Do you want to add an offer_item ?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Yes")) {
                    startActivity(new Intent(UserActivityShowOfferDetails.this, ActivityShowOffers.class));
                } else if (items[item].equals("No")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }*/