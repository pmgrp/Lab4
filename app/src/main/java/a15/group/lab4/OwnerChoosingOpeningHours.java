package a15.group.lab4;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class OwnerChoosingOpeningHours extends AppCompatActivity {

    private CheckBox selectAll;
    private CheckBox continuous;
    private CheckBox same;
    private CheckBox monday;
    private CheckBox tuesday;
    private CheckBox wednesday;
    private CheckBox thursday;
    private CheckBox friday;
    private CheckBox saturday;
    private CheckBox sunday;
    private CheckBox mondayLunch;
    private CheckBox tuesdayLunch;
    private CheckBox wednesdayLunch;
    private CheckBox thursdayLunch;
    private CheckBox fridayLunch;
    private CheckBox saturdayLunch;
    private CheckBox sundayLunch;
    private CheckBox mondayDiner;
    private CheckBox tuesdayDiner;
    private CheckBox wednesdayDiner;
    private CheckBox thursdayDiner;
    private CheckBox fridayDiner;
    private CheckBox saturdayDiner;
    private CheckBox sundayDiner;
    private EditText startLunchMonday;
    private EditText endLunchMonday;
    private EditText startDinerMonday;
    private EditText endDinerMonday;
    private EditText startLunchTuesday;
    private EditText endLunchTuesday;
    private EditText startDinerTuesday;
    private EditText endDinerTuesday;
    private EditText startLunchWednesday;
    private EditText endLunchWednesday;
    private EditText startDinerWednesday;
    private EditText endDinerWednesday;
    private EditText startLunchThursday;
    private EditText endLunchThursday;
    private EditText startDinerThursday;
    private EditText endDinerThursday;
    private EditText startLunchFriday;
    private EditText endLunchFriday;
    private EditText startDinerFriday;
    private EditText endDinerFriday;
    private EditText startLunchSaturday;
    private EditText endLunchSaturday;
    private EditText startDinerSaturday;
    private EditText endDinerSaturday;
    private EditText startLunchSunday;
    private EditText endLunchSunday;
    private EditText startDinerSunday;
    private EditText endDinerSunday;
    private EditText startHourMonday;
    private EditText endHourMonday;
    private EditText startHourTuesday;
    private EditText endHourTuesday;
    private EditText startHourWednesday;
    private EditText endHourWednesday;
    private EditText startHourThursday;
    private EditText endHourThursday;
    private EditText startHourFriday;
    private EditText endHourFriday;
    private EditText startHourSaturday;
    private EditText endHourSaturday;
    private EditText startHourSunday;
    private EditText endHourSunday;

    //firebase variables
    private FirebaseAuth mAuth;
    private DatabaseReference mRefOpenHour;
    private String restaurantId;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        //if not authenticated go to login page
        mAuth = FirebaseAuth.getInstance();
        if (mAuth == null) {
            startActivity(new Intent(context, ActivityMain.class));
        } else {
            restaurantId = mAuth.getCurrentUser().getUid();
        }

        //opening hours path of this restaurant in DB
        mRefOpenHour = FirebaseDatabase.getInstance().getReference().child("opening-hours").child(restaurantId);

        setContentView(R.layout.activity_owner_choosing_opening_hours);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initAllID();

        /**
         * Here firebase part to retrieve data from database
         */

        mRefOpenHour.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                OpeningDaysHours openingDaysHours = dataSnapshot.getValue(OpeningDaysHours.class);

                //if data is already present in databse
                if(openingDaysHours != null) {

                    continuous.setChecked(openingDaysHours.isContinuous());

                    monday.setChecked(openingDaysHours.isMonday());
                    tuesday.setChecked(openingDaysHours.isTuesday());
                    wednesday.setChecked(openingDaysHours.isWednesday());
                    thursday.setChecked(openingDaysHours.isThursday());
                    friday.setChecked(openingDaysHours.isFriday());
                    saturday.setChecked(openingDaysHours.isSaturday());
                    sunday.setChecked(openingDaysHours.isSunday());

                    mondayLunch.setChecked(openingDaysHours.isMondayMorning());
                    tuesdayLunch.setChecked(openingDaysHours.isTuesdayMorning());
                    wednesdayLunch.setChecked(openingDaysHours.isWednesdayMorning());
                    thursdayLunch.setChecked(openingDaysHours.isThursdayMorning());
                    fridayLunch.setChecked(openingDaysHours.isFridayMorning());
                    saturdayLunch.setChecked(openingDaysHours.isSaturdayMorning());
                    sundayLunch.setChecked(openingDaysHours.isSundayMorning());
                    mondayDiner.setChecked(openingDaysHours.isMondayEvening());
                    tuesdayDiner.setChecked(openingDaysHours.isTuesdayEvening());
                    wednesdayDiner.setChecked(openingDaysHours.isWednesdayEvening());
                    thursdayDiner.setChecked(openingDaysHours.isThursdayEvening());
                    fridayDiner.setChecked(openingDaysHours.isFridayEvening());
                    saturdayDiner.setChecked(openingDaysHours.isSaturdayEvening());
                    sundayDiner.setChecked(openingDaysHours.isSundayEvening());

                    startLunchMonday.setText(openingDaysHours.getMondayLunchStart());
                    endLunchMonday.setText(openingDaysHours.getMondayLunchEnd());
                    startLunchTuesday.setText(openingDaysHours.getTuesdayLunchStart());
                    endLunchTuesday.setText(openingDaysHours.getTuesdayLunchEnd());
                    startLunchWednesday.setText(openingDaysHours.getWednesdayLunchStart());
                    endLunchWednesday.setText(openingDaysHours.getWednesdayLunchEnd());
                    startLunchThursday.setText(openingDaysHours.getThursdayLunchStart());
                    endLunchThursday.setText(openingDaysHours.getThursdayLunchEnd());
                    startLunchFriday.setText(openingDaysHours.getFridayLunchStart());
                    endLunchFriday.setText(openingDaysHours.getFridayLunchEnd());
                    startLunchSaturday.setText(openingDaysHours.getSaturdayLunchStart());
                    endLunchSaturday.setText(openingDaysHours.getSaturdayLunchEnd());
                    startLunchSunday.setText(openingDaysHours.getSundayLunchStart());
                    endLunchSunday.setText(openingDaysHours.getSundayLunchEnd());

                    startDinerMonday.setText(openingDaysHours.getMondayDinerStart());
                    endDinerMonday.setText(openingDaysHours.getMondayDinerEnd());
                    startDinerTuesday.setText(openingDaysHours.getTuesdayDinerStart());
                    endDinerTuesday.setText(openingDaysHours.getTuesdayDinerEnd());
                    startDinerWednesday.setText(openingDaysHours.getWednesdayDinerStart());
                    endDinerWednesday.setText(openingDaysHours.getWednesdayDinerEnd());
                    startDinerThursday.setText(openingDaysHours.getThursdayDinerStart());
                    endDinerThursday.setText(openingDaysHours.getThursdayDinerEnd());
                    startDinerFriday.setText(openingDaysHours.getFridayDinerStart());
                    endDinerFriday.setText(openingDaysHours.getFridayDinerEnd());
                    startDinerSaturday.setText(openingDaysHours.getSaturdayDinerStart());
                    endDinerSaturday.setText(openingDaysHours.getSaturdayDinerEnd());
                    startDinerSunday.setText(openingDaysHours.getSundayDinerStart());
                    endDinerSunday.setText(openingDaysHours.getSundayDinerEnd());

                    startHourMonday.setText(openingDaysHours.getMondayAllDayStart());
                    endHourMonday.setText(openingDaysHours.getMondayAllDayEnd());
                    startHourTuesday.setText(openingDaysHours.getTuesdayAllDayStart());
                    endHourTuesday.setText(openingDaysHours.getTuesdayAllDayEnd());
                    startHourWednesday.setText(openingDaysHours.getWednesdayAllDayStart());
                    endHourWednesday.setText(openingDaysHours.getWednesdayAllDayEnd());
                    startHourThursday.setText(openingDaysHours.getThursdayAllDayStart());
                    endHourThursday.setText(openingDaysHours.getThursdayAllDayEnd());
                    startHourFriday.setText(openingDaysHours.getFridayAllDayStart());
                    endHourFriday.setText(openingDaysHours.getFridayAllDayEnd());
                    startHourSaturday.setText(openingDaysHours.getSaturdayAllDayStart());
                    endHourSaturday.setText(openingDaysHours.getSaturdayAllDayEnd());
                    startHourSunday.setText(openingDaysHours.getSundayAllDayStart());
                    endHourSunday.setText(openingDaysHours.getSundayAllDayEnd());

                    initAllCheckBox(openingDaysHours);
                }
                else{
                    //TODO manage here the case when opening hours are not still set
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void initAllID() {
        selectAll = (CheckBox) findViewById(R.id.select_all_radioButton);
        continuous = (CheckBox) findViewById(R.id.continuous_radioButton);
        same = (CheckBox) findViewById(R.id.same_radioButton);

        monday = (CheckBox) findViewById(R.id.monday_radioButton);
        tuesday = (CheckBox) findViewById(R.id.tuesday_radioButton);
        wednesday = (CheckBox) findViewById(R.id.wednesday_radioButton);
        thursday = (CheckBox) findViewById(R.id.thursday_radioButton);
        friday = (CheckBox) findViewById(R.id.friday_radioButton);
        saturday = (CheckBox) findViewById(R.id.saturday_radioButton);
        sunday = (CheckBox) findViewById(R.id.sunday_radioButton);

        mondayLunch = (CheckBox) findViewById(R.id.lunch_monday_radioButton);
        tuesdayLunch = (CheckBox) findViewById(R.id.lunch_tuesday_radioButton);
        wednesdayLunch = (CheckBox) findViewById(R.id.lunch_wednesday_radioButton);
        thursdayLunch = (CheckBox) findViewById(R.id.lunch_thursday_radioButton);
        fridayLunch = (CheckBox) findViewById(R.id.lunch_friday_radioButton);
        saturdayLunch = (CheckBox) findViewById(R.id.lunch_saturday_radioButton);
        sundayLunch = (CheckBox) findViewById(R.id.lunch_sunday_radioButton);
        mondayDiner = (CheckBox) findViewById(R.id.diner_monday_radioButton);
        tuesdayDiner = (CheckBox) findViewById(R.id.diner_tuesday_radioButton);
        wednesdayDiner = (CheckBox) findViewById(R.id.diner_wednesday_radioButton);
        thursdayDiner = (CheckBox) findViewById(R.id.diner_thursday_radioButton);
        fridayDiner = (CheckBox) findViewById(R.id.diner_friday_radioButton);
        saturdayDiner = (CheckBox) findViewById(R.id.diner_saturday_radioButton);
        sundayDiner = (CheckBox) findViewById(R.id.diner_sunday_radioButton);

        startLunchMonday = (EditText) findViewById(R.id.start_hour_lunch_monday);
        endLunchMonday = (EditText) findViewById(R.id.end_hour_lunch_monday);
        startDinerMonday = (EditText) findViewById(R.id.start_hour_diner_monday);
        endDinerMonday = (EditText) findViewById(R.id.end_hour_diner_monday);
        startLunchTuesday = (EditText) findViewById(R.id.start_hour_lunch_tuesday);
        endLunchTuesday = (EditText) findViewById(R.id.end_hour_lunch_tuesday);
        startDinerTuesday = (EditText) findViewById(R.id.start_hour_diner_tuesday);
        endDinerTuesday = (EditText) findViewById(R.id.end_hour_diner_tuesday);
        startLunchWednesday = (EditText) findViewById(R.id.start_hour_lunch_wednesday);
        endLunchWednesday = (EditText) findViewById(R.id.end_hour_lunch_wednesday);
        startDinerWednesday = (EditText) findViewById(R.id.start_hour_diner_wednesday);
        endDinerWednesday = (EditText) findViewById(R.id.end_hour_diner_wednesday);
        startLunchThursday = (EditText) findViewById(R.id.start_hour_lunch_thursday);
        endLunchThursday = (EditText) findViewById(R.id.end_hour_lunch_thursday);
        startDinerThursday = (EditText) findViewById(R.id.start_hour_diner_thursday);
        endDinerThursday = (EditText) findViewById(R.id.end_hour_diner_thursday);
        startLunchFriday = (EditText) findViewById(R.id.start_hour_lunch_friday);
        endLunchFriday = (EditText) findViewById(R.id.end_hour_lunch_friday);
        startDinerFriday = (EditText) findViewById(R.id.start_hour_diner_friday);
        endDinerFriday = (EditText) findViewById(R.id.end_hour_diner_friday);
        startLunchSaturday = (EditText) findViewById(R.id.start_hour_lunch_saturday);
        endLunchSaturday = (EditText) findViewById(R.id.end_hour_lunch_saturday);
        startDinerSaturday = (EditText) findViewById(R.id.start_hour_diner_saturday);
        endDinerSaturday = (EditText) findViewById(R.id.end_hour_diner_saturday);
        startLunchSunday = (EditText) findViewById(R.id.start_hour_lunch_sunday);
        endLunchSunday = (EditText) findViewById(R.id.end_hour_lunch_sunday);
        startDinerSunday = (EditText) findViewById(R.id.start_hour_diner_sunday);
        endDinerSunday = (EditText) findViewById(R.id.end_hour_diner_sunday);

        startHourMonday = (EditText) findViewById(R.id.start_hour_monday);
        endHourMonday = (EditText) findViewById(R.id.end_hour_monday);
        startHourTuesday = (EditText) findViewById(R.id.start_hour_tuesday);
        endHourTuesday = (EditText) findViewById(R.id.end_hour_tuesday);
        startHourWednesday = (EditText) findViewById(R.id.start_hour_wednesday);
        endHourWednesday = (EditText) findViewById(R.id.end_hour_wednesday);
        startHourThursday = (EditText) findViewById(R.id.start_hour_thursday);
        endHourThursday = (EditText) findViewById(R.id.end_hour_thursday);
        startHourFriday = (EditText) findViewById(R.id.start_hour_friday);
        endHourFriday = (EditText) findViewById(R.id.end_hour_friday);
        startHourSaturday = (EditText) findViewById(R.id.start_hour_saturday);
        endHourSaturday = (EditText) findViewById(R.id.end_hour_saturday);
        startHourSunday = (EditText) findViewById(R.id.start_hour_sunday);
        endHourSunday = (EditText) findViewById(R.id.end_hour_sunday);
    }

    private void initAllCheckBox(OpeningDaysHours openingDaysHours) {
        selectAll.setChecked(false);

        RelativeLayout rl;
        if (openingDaysHours.isContinuous()) {
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_monday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_monday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_tuesday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_tuesday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_wednesday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_wednesday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_thursday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_thursday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_friday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_friday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_saturday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_saturday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_sunday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_sunday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_monday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_tuesday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_wednesday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_thursday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_friday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_saturday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_sunday);
            rl.setVisibility(View.VISIBLE);
        } else {
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_monday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_monday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_tuesday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_tuesday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_wednesday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_wednesday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_thursday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_thursday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_friday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_friday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_saturday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_saturday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_sunday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_sunday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_monday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_tuesday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_wednesday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_thursday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_friday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_saturday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_sunday);
            rl.setVisibility(View.GONE);
        }

        if (openingDaysHours.isMonday()) {
            startLunchMonday.setEnabled(true);
            endLunchMonday.setEnabled(true);
            startDinerMonday.setEnabled(true);
            endDinerMonday.setEnabled(true);
            startHourMonday.setEnabled(true);
            endHourMonday.setEnabled(true);
        } else {
            startLunchMonday.setEnabled(false);
            endLunchMonday.setEnabled(false);
            startDinerMonday.setEnabled(false);
            endDinerMonday.setEnabled(false);
            startHourMonday.setEnabled(false);
            endHourMonday.setEnabled(false);
        }

        if (openingDaysHours.isMondayMorning()) {
            startLunchMonday.setEnabled(true);
            endLunchMonday.setEnabled(true);
        } else {
            startLunchMonday.setEnabled(false);
            endLunchMonday.setEnabled(false);
        }

        if (openingDaysHours.isMondayEvening()) {
            startDinerMonday.setEnabled(true);
            endDinerMonday.setEnabled(true);
        } else {
            startDinerMonday.setEnabled(false);
            endDinerMonday.setEnabled(false);
        }

        if (openingDaysHours.isTuesday()) {
            startLunchTuesday.setEnabled(true);
            endLunchTuesday.setEnabled(true);
            startDinerTuesday.setEnabled(true);
            endDinerTuesday.setEnabled(true);
            startHourTuesday.setEnabled(true);
            endHourTuesday.setEnabled(true);
        } else {
            startLunchTuesday.setEnabled(false);
            endLunchTuesday.setEnabled(false);
            startDinerTuesday.setEnabled(false);
            endDinerTuesday.setEnabled(false);
            startHourTuesday.setEnabled(false);
            endHourTuesday.setEnabled(false);
        }

        if (openingDaysHours.isTuesdayMorning()) {
            startLunchTuesday.setEnabled(true);
            endLunchTuesday.setEnabled(true);
        } else {
            startLunchTuesday.setEnabled(false);
            endLunchTuesday.setEnabled(false);
        }

        if (openingDaysHours.isTuesdayEvening()) {
            startDinerTuesday.setEnabled(true);
            endDinerTuesday.setEnabled(true);
        } else {
            startDinerTuesday.setEnabled(false);
            endDinerTuesday.setEnabled(false);
        }

        if (openingDaysHours.isWednesday()) {
            startLunchWednesday.setEnabled(true);
            endLunchWednesday.setEnabled(true);
            startDinerWednesday.setEnabled(true);
            endDinerWednesday.setEnabled(true);
            startHourWednesday.setEnabled(true);
            endHourWednesday.setEnabled(true);
        } else {
            startLunchWednesday.setEnabled(false);
            endLunchWednesday.setEnabled(false);
            startDinerWednesday.setEnabled(false);
            endDinerWednesday.setEnabled(false);
            startHourWednesday.setEnabled(false);
            endHourWednesday.setEnabled(false);
        }

        if (openingDaysHours.isWednesdayMorning()) {
            startLunchWednesday.setEnabled(true);
            endLunchWednesday.setEnabled(true);
        } else {
            startLunchWednesday.setEnabled(false);
            endLunchWednesday.setEnabled(false);
        }

        if (openingDaysHours.isWednesdayEvening()) {
            startDinerWednesday.setEnabled(true);
            endDinerWednesday.setEnabled(true);
        } else {
            startDinerWednesday.setEnabled(false);
            endDinerWednesday.setEnabled(false);
        }

        if (openingDaysHours.isThursday()) {
            startLunchThursday.setEnabled(true);
            endLunchThursday.setEnabled(true);
            startDinerThursday.setEnabled(true);
            endDinerThursday.setEnabled(true);
            startHourThursday.setEnabled(true);
            endHourThursday.setEnabled(true);
        } else {
            startLunchThursday.setEnabled(false);
            endLunchThursday.setEnabled(false);
            startDinerThursday.setEnabled(false);
            endDinerThursday.setEnabled(false);
            startHourThursday.setEnabled(false);
            endHourThursday.setEnabled(false);
        }

        if (openingDaysHours.isThursdayMorning()) {
            startLunchThursday.setEnabled(true);
            endLunchThursday.setEnabled(true);
        } else {
            startLunchThursday.setEnabled(false);
            endLunchThursday.setEnabled(false);
        }

        if (openingDaysHours.isThursdayEvening()) {
            startDinerThursday.setEnabled(true);
            endDinerThursday.setEnabled(true);
        } else {
            startDinerThursday.setEnabled(false);
            endDinerThursday.setEnabled(false);
        }

        if (openingDaysHours.isFriday()) {
            startLunchFriday.setEnabled(true);
            endLunchFriday.setEnabled(true);
            startDinerFriday.setEnabled(true);
            endDinerFriday.setEnabled(true);
            startHourFriday.setEnabled(true);
            endHourFriday.setEnabled(true);
        } else {
            fridayLunch.setChecked(false);
            fridayDiner.setChecked(false);
            startLunchFriday.setEnabled(false);
            endLunchFriday.setEnabled(false);
            startDinerFriday.setEnabled(false);
            endDinerFriday.setEnabled(false);
            startHourFriday.setEnabled(false);
            endHourFriday.setEnabled(false);
        }

        if (openingDaysHours.isFridayMorning()) {
            startLunchFriday.setEnabled(true);
            endLunchFriday.setEnabled(true);
        } else {
            startLunchFriday.setEnabled(false);
            endLunchFriday.setEnabled(false);
        }

        if (openingDaysHours.isFridayEvening()) {
            startDinerFriday.setEnabled(true);
            endDinerFriday.setEnabled(true);
        } else {
            startDinerFriday.setEnabled(false);
            endDinerFriday.setEnabled(false);
        }

        if (openingDaysHours.isSaturday()) {
            startLunchSaturday.setEnabled(true);
            endLunchSaturday.setEnabled(true);
            startDinerSaturday.setEnabled(true);
            endDinerSaturday.setEnabled(true);
            startHourSaturday.setEnabled(true);
            endHourSaturday.setEnabled(true);
        } else {
            startLunchSaturday.setEnabled(false);
            endLunchSaturday.setEnabled(false);
            startDinerSaturday.setEnabled(false);
            endDinerSaturday.setEnabled(false);
            startHourSaturday.setEnabled(false);
            endHourSaturday.setEnabled(false);
        }

        if (openingDaysHours.isSaturdayMorning()) {
            startLunchSaturday.setEnabled(true);
            endLunchSaturday.setEnabled(true);
        } else {
            startLunchSaturday.setEnabled(false);
            endLunchSaturday.setEnabled(false);
        }

        if (openingDaysHours.isSaturdayEvening()) {
            startDinerSaturday.setEnabled(true);
            endDinerSaturday.setEnabled(true);
        } else {
            startDinerSaturday.setEnabled(false);
            endDinerSaturday.setEnabled(false);
        }

        if (openingDaysHours.isSunday()) {
            startLunchSunday.setEnabled(true);
            endLunchSunday.setEnabled(true);
            startDinerSunday.setEnabled(true);
            endDinerSunday.setEnabled(true);
            startHourSunday.setEnabled(true);
            endHourSunday.setEnabled(true);
        } else {
            startLunchSunday.setEnabled(false);
            endLunchSunday.setEnabled(false);
            startDinerSunday.setEnabled(false);
            endDinerSunday.setEnabled(false);
            startHourSunday.setEnabled(false);
            endHourSunday.setEnabled(false);
        }

        if (openingDaysHours.isSundayMorning()) {
            startLunchSunday.setEnabled(true);
            endLunchSunday.setEnabled(true);
        } else {
            startLunchSunday.setEnabled(false);
            endLunchSunday.setEnabled(false);
        }

        if (openingDaysHours.isSundayEvening()) {
            startDinerSunday.setEnabled(true);
            endDinerSunday.setEnabled(true);
        } else {
            startDinerSunday.setEnabled(false);
            endDinerSunday.setEnabled(false);
        }
    }

    public void selectAllDays(View view) {
        if (selectAll.isChecked()) {
            monday.setChecked(true);
            tuesday.setChecked(true);
            wednesday.setChecked(true);
            thursday.setChecked(true);
            friday.setChecked(true);
            saturday.setChecked(true);
            sunday.setChecked(true);
            mondayLunch.setChecked(true);
            tuesdayLunch.setChecked(true);
            wednesdayLunch.setChecked(true);
            thursdayLunch.setChecked(true);
            fridayLunch.setChecked(true);
            saturdayLunch.setChecked(true);
            sundayLunch.setChecked(true);
            mondayDiner.setChecked(true);
            tuesdayDiner.setChecked(true);
            wednesdayDiner.setChecked(true);
            thursdayDiner.setChecked(true);
            fridayDiner.setChecked(true);
            saturdayDiner.setChecked(true);
            sundayDiner.setChecked(true);
            startLunchMonday.setEnabled(true);
            endLunchMonday.setEnabled(true);
            startDinerMonday.setEnabled(true);
            endDinerMonday.setEnabled(true);
            startLunchTuesday.setEnabled(true);
            endLunchTuesday.setEnabled(true);
            startDinerTuesday.setEnabled(true);
            endDinerTuesday.setEnabled(true);
            startLunchWednesday.setEnabled(true);
            endLunchWednesday.setEnabled(true);
            startDinerWednesday.setEnabled(true);
            endDinerWednesday.setEnabled(true);
            startLunchThursday.setEnabled(true);
            endLunchThursday.setEnabled(true);
            startDinerThursday.setEnabled(true);
            endDinerThursday.setEnabled(true);
            startLunchFriday.setEnabled(true);
            endLunchFriday.setEnabled(true);
            startDinerFriday.setEnabled(true);
            endDinerFriday.setEnabled(true);
            startLunchSaturday.setEnabled(true);
            endLunchSaturday.setEnabled(true);
            startDinerSaturday.setEnabled(true);
            endDinerSaturday.setEnabled(true);
            startLunchSunday.setEnabled(true);
            endLunchSunday.setEnabled(true);
            startDinerSunday.setEnabled(true);
            endDinerSunday.setEnabled(true);
        } else {
            monday.setChecked(false);
            tuesday.setChecked(false);
            wednesday.setChecked(false);
            thursday.setChecked(false);
            friday.setChecked(false);
            saturday.setChecked(false);
            sunday.setChecked(false);
            mondayLunch.setChecked(false);
            tuesdayLunch.setChecked(false);
            wednesdayLunch.setChecked(false);
            thursdayLunch.setChecked(false);
            fridayLunch.setChecked(false);
            saturdayLunch.setChecked(false);
            sundayLunch.setChecked(false);
            mondayDiner.setChecked(false);
            tuesdayDiner.setChecked(false);
            wednesdayDiner.setChecked(false);
            thursdayDiner.setChecked(false);
            fridayDiner.setChecked(false);
            saturdayDiner.setChecked(false);
            sundayDiner.setChecked(false);
            startLunchMonday.setEnabled(false);
            endLunchMonday.setEnabled(false);
            startDinerMonday.setEnabled(false);
            endDinerMonday.setEnabled(false);
            startLunchTuesday.setEnabled(false);
            endLunchTuesday.setEnabled(false);
            startDinerTuesday.setEnabled(false);
            endDinerTuesday.setEnabled(false);
            startLunchWednesday.setEnabled(false);
            endLunchWednesday.setEnabled(false);
            startDinerWednesday.setEnabled(false);
            endDinerWednesday.setEnabled(false);
            startLunchThursday.setEnabled(false);
            endLunchThursday.setEnabled(false);
            startDinerThursday.setEnabled(false);
            endDinerThursday.setEnabled(false);
            startLunchFriday.setEnabled(false);
            endLunchFriday.setEnabled(false);
            startDinerFriday.setEnabled(false);
            endDinerFriday.setEnabled(false);
            startLunchSaturday.setEnabled(false);
            endLunchSaturday.setEnabled(false);
            startDinerSaturday.setEnabled(false);
            endDinerSaturday.setEnabled(false);
            startLunchSunday.setEnabled(false);
            endLunchSunday.setEnabled(false);
            startDinerSunday.setEnabled(false);
            endDinerSunday.setEnabled(false);
        }
    }

    public void continuous(View view) {
        RelativeLayout rl;
        if (continuous.isChecked()) {
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_monday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_monday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_tuesday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_tuesday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_wednesday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_wednesday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_thursday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_thursday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_friday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_friday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_saturday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_saturday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_sunday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_sunday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_monday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_tuesday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_wednesday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_thursday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_friday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_saturday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_sunday);
            rl.setVisibility(View.VISIBLE);
        } else {
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_monday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_monday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_tuesday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_tuesday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_wednesday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_wednesday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_thursday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_thursday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_friday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_friday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_saturday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_saturday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_lunch_sunday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_diner_sunday);
            rl.setVisibility(View.VISIBLE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_monday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_tuesday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_wednesday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_thursday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_friday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_saturday);
            rl.setVisibility(View.GONE);
            rl = (RelativeLayout) findViewById(R.id.RL_continuous_sunday);
            rl.setVisibility(View.GONE);
        }
    }

    public void sameHours(View view) {
        if (same.isChecked()) {
            String lunchMondayStart = startLunchMonday.getText().toString();
            String lunchMondayEnd = endLunchMonday.getText().toString();
            String dinerMondayStart = startDinerMonday.getText().toString();
            String dinerMondayEnd = endDinerMonday.getText().toString();
            String hourMondayStart = startHourMonday.getText().toString();
            String hourMondayEnd = endHourMonday.getText().toString();

            startLunchTuesday.setText(lunchMondayStart);
            endLunchTuesday.setText(lunchMondayEnd);
            startDinerTuesday.setText(dinerMondayStart);
            endDinerTuesday.setText(dinerMondayEnd);
            startLunchWednesday.setText(lunchMondayStart);
            endLunchWednesday.setText(lunchMondayEnd);
            startDinerWednesday.setText(dinerMondayStart);
            endDinerWednesday.setText(dinerMondayEnd);
            startLunchThursday.setText(lunchMondayStart);
            endLunchThursday.setText(lunchMondayEnd);
            startDinerThursday.setText(dinerMondayStart);
            endDinerThursday.setText(dinerMondayEnd);
            startLunchFriday.setText(lunchMondayStart);
            endLunchFriday.setText(lunchMondayEnd);
            startDinerFriday.setText(dinerMondayStart);
            endDinerFriday.setText(dinerMondayEnd);
            startLunchSaturday.setText(lunchMondayStart);
            endLunchSaturday.setText(lunchMondayEnd);
            startDinerSaturday.setText(dinerMondayStart);
            endDinerSaturday.setText(dinerMondayEnd);
            startLunchSunday.setText(lunchMondayStart);
            endLunchSunday.setText(lunchMondayEnd);
            startDinerSunday.setText(dinerMondayStart);
            endDinerSunday.setText(dinerMondayEnd);

            startHourTuesday.setText(hourMondayStart);
            endHourTuesday.setText(hourMondayEnd);
            startHourWednesday.setText(hourMondayStart);
            endHourWednesday.setText(hourMondayEnd);
            startHourThursday.setText(hourMondayStart);
            endHourThursday.setText(hourMondayEnd);
            startHourFriday.setText(hourMondayStart);
            endHourFriday.setText(hourMondayEnd);
            startHourSaturday.setText(hourMondayStart);
            endHourSaturday.setText(hourMondayEnd);
            startHourSunday.setText(hourMondayStart);
            endHourSunday.setText(hourMondayEnd);
        }
    }

    public void monday(View view) {
        if (monday.isChecked()) {
            mondayLunch.setChecked(true);
            mondayDiner.setChecked(true);
            startLunchMonday.setEnabled(true);
            endLunchMonday.setEnabled(true);
            startDinerMonday.setEnabled(true);
            endDinerMonday.setEnabled(true);
            startHourMonday.setEnabled(true);
            endHourMonday.setEnabled(true);
        } else {
            mondayLunch.setChecked(false);
            mondayDiner.setChecked(false);
            startLunchMonday.setEnabled(false);
            endLunchMonday.setEnabled(false);
            startDinerMonday.setEnabled(false);
            endDinerMonday.setEnabled(false);
            startHourMonday.setEnabled(false);
            endHourMonday.setEnabled(false);
        }
    }

    public void mondayLunch(View view) {
        if (mondayLunch.isChecked()) {
            startLunchMonday.setEnabled(true);
            endLunchMonday.setEnabled(true);
        } else {
            startLunchMonday.setEnabled(false);
            endLunchMonday.setEnabled(false);
        }
    }

    public void mondayDiner(View view) {
        if (mondayDiner.isChecked()) {
            startDinerMonday.setEnabled(true);
            endDinerMonday.setEnabled(true);
        } else {
            startDinerMonday.setEnabled(false);
            endDinerMonday.setEnabled(false);
        }
    }

    public void tuesday(View view) {
        if (tuesday.isChecked()) {
            tuesdayLunch.setChecked(true);
            tuesdayDiner.setChecked(true);
            startLunchTuesday.setEnabled(true);
            endLunchTuesday.setEnabled(true);
            startDinerTuesday.setEnabled(true);
            endDinerTuesday.setEnabled(true);
            startHourTuesday.setEnabled(true);
            endHourTuesday.setEnabled(true);
        } else {
            tuesdayLunch.setChecked(false);
            tuesdayDiner.setChecked(false);
            startLunchTuesday.setEnabled(false);
            endLunchTuesday.setEnabled(false);
            startDinerTuesday.setEnabled(false);
            endDinerTuesday.setEnabled(false);
            startHourTuesday.setEnabled(false);
            endHourTuesday.setEnabled(false);
        }
    }

    public void tuesdayLunch(View view) {
        if (tuesdayLunch.isChecked()) {
            startLunchTuesday.setEnabled(true);
            endLunchTuesday.setEnabled(true);
        } else {
            startLunchTuesday.setEnabled(false);
            endLunchTuesday.setEnabled(false);
        }
    }

    public void tuesdayDiner(View view) {
        if (tuesdayDiner.isChecked()) {
            startDinerTuesday.setEnabled(true);
            endDinerTuesday.setEnabled(true);
        } else {
            startDinerTuesday.setEnabled(false);
            endDinerTuesday.setEnabled(false);
        }
    }

    public void wednesday(View view) {
        if (wednesday.isChecked()) {
            wednesdayLunch.setChecked(true);
            wednesdayDiner.setChecked(true);
            startLunchWednesday.setEnabled(true);
            endLunchWednesday.setEnabled(true);
            startDinerWednesday.setEnabled(true);
            endDinerWednesday.setEnabled(true);
            startHourWednesday.setEnabled(true);
            endHourWednesday.setEnabled(true);
        } else {
            wednesdayLunch.setChecked(false);
            wednesdayDiner.setChecked(false);
            startLunchWednesday.setEnabled(false);
            endLunchWednesday.setEnabled(false);
            startDinerWednesday.setEnabled(false);
            endDinerWednesday.setEnabled(false);
            startHourWednesday.setEnabled(false);
            endHourWednesday.setEnabled(false);
        }
    }

    public void wednesdayLunch(View view) {
        if (wednesdayLunch.isChecked()) {
            startLunchWednesday.setEnabled(true);
            endLunchWednesday.setEnabled(true);
        } else {
            startLunchWednesday.setEnabled(false);
            endLunchWednesday.setEnabled(false);
        }
    }

    public void wednesdayDiner(View view) {
        if (wednesdayDiner.isChecked()) {
            startDinerWednesday.setEnabled(true);
            endDinerWednesday.setEnabled(true);
        } else {
            startDinerWednesday.setEnabled(false);
            endDinerWednesday.setEnabled(false);
        }
    }

    public void thursday(View view) {
        if (thursday.isChecked()) {
            thursdayLunch.setChecked(true);
            thursdayDiner.setChecked(true);
            startLunchThursday.setEnabled(true);
            endLunchThursday.setEnabled(true);
            startDinerThursday.setEnabled(true);
            endDinerThursday.setEnabled(true);
            startHourThursday.setEnabled(true);
            endHourThursday.setEnabled(true);
        } else {
            thursdayLunch.setChecked(false);
            thursdayDiner.setChecked(false);
            startLunchThursday.setEnabled(false);
            endLunchThursday.setEnabled(false);
            startDinerThursday.setEnabled(false);
            endDinerThursday.setEnabled(false);
            startHourThursday.setEnabled(false);
            endHourThursday.setEnabled(false);
        }
    }

    public void thursdayLunch(View view) {
        if (thursdayLunch.isChecked()) {
            startLunchThursday.setEnabled(true);
            endLunchThursday.setEnabled(true);
        } else {
            startLunchThursday.setEnabled(false);
            endLunchThursday.setEnabled(false);
        }
    }

    public void thursdayDiner(View view) {
        if (thursdayDiner.isChecked()) {
            startDinerThursday.setEnabled(true);
            endDinerThursday.setEnabled(true);
        } else {
            startDinerThursday.setEnabled(false);
            endDinerThursday.setEnabled(false);
        }
    }

    public void friday(View view) {
        if (friday.isChecked()) {
            fridayLunch.setChecked(true);
            fridayDiner.setChecked(true);
            startLunchFriday.setEnabled(true);
            endLunchFriday.setEnabled(true);
            startDinerFriday.setEnabled(true);
            endDinerFriday.setEnabled(true);
            startHourFriday.setEnabled(true);
            endHourFriday.setEnabled(true);
        } else {
            fridayLunch.setChecked(false);
            fridayDiner.setChecked(false);
            startLunchFriday.setEnabled(false);
            endLunchFriday.setEnabled(false);
            startDinerFriday.setEnabled(false);
            endDinerFriday.setEnabled(false);
            startHourFriday.setEnabled(false);
            endHourFriday.setEnabled(false);
        }
    }

    public void fridayLunch(View view) {
        if (fridayLunch.isChecked()) {
            startLunchFriday.setEnabled(true);
            endLunchFriday.setEnabled(true);
        } else {
            startLunchFriday.setEnabled(false);
            endLunchFriday.setEnabled(false);
        }
    }

    public void fridayDiner(View view) {
        if (fridayDiner.isChecked()) {
            startDinerFriday.setEnabled(true);
            endDinerFriday.setEnabled(true);
        } else {
            startDinerFriday.setEnabled(false);
            endDinerFriday.setEnabled(false);
        }
    }

    public void saturday(View view) {
        if (saturday.isChecked()) {
            saturdayLunch.setChecked(true);
            saturdayDiner.setChecked(true);
            startLunchSaturday.setEnabled(true);
            endLunchSaturday.setEnabled(true);
            startDinerSaturday.setEnabled(true);
            endDinerSaturday.setEnabled(true);
            startHourSaturday.setEnabled(true);
            endHourSaturday.setEnabled(true);
        } else {
            saturdayLunch.setChecked(false);
            saturdayDiner.setChecked(false);
            startLunchSaturday.setEnabled(false);
            endLunchSaturday.setEnabled(false);
            startDinerSaturday.setEnabled(false);
            endDinerSaturday.setEnabled(false);
            startHourSaturday.setEnabled(false);
            endHourSaturday.setEnabled(false);
        }
    }

    public void saturdayLunch(View view) {
        if (saturdayLunch.isChecked()) {
            startLunchSaturday.setEnabled(true);
            endLunchSaturday.setEnabled(true);
        } else {
            startLunchSaturday.setEnabled(false);
            endLunchSaturday.setEnabled(false);
        }
    }

    public void saturdayDiner(View view) {
        if (saturdayDiner.isChecked()) {
            startDinerSaturday.setEnabled(true);
            endDinerSaturday.setEnabled(true);
        } else {
            startDinerSaturday.setEnabled(false);
            endDinerSaturday.setEnabled(false);
        }
    }

    public void sunday(View view) {
        if (sunday.isChecked()) {
            sundayLunch.setChecked(true);
            sundayDiner.setChecked(true);
            startLunchSunday.setEnabled(true);
            endLunchSunday.setEnabled(true);
            startDinerSunday.setEnabled(true);
            endDinerSunday.setEnabled(true);
            startHourSunday.setEnabled(true);
            endHourSunday.setEnabled(true);
        } else {
            sundayLunch.setChecked(false);
            sundayDiner.setChecked(false);
            startLunchSunday.setEnabled(false);
            endLunchSunday.setEnabled(false);
            startDinerSunday.setEnabled(false);
            endDinerSunday.setEnabled(false);
            startHourSunday.setEnabled(false);
            endHourSunday.setEnabled(false);
        }
    }

    public void sundayLunch(View view) {
        if (sundayLunch.isChecked()) {
            startLunchSunday.setEnabled(true);
            endLunchSunday.setEnabled(true);
        } else {
            startLunchSunday.setEnabled(false);
            endLunchSunday.setEnabled(false);
        }
    }

    public void sundayDiner(View view) {
        if (sundayDiner.isChecked()) {
            startDinerSunday.setEnabled(true);
            endDinerSunday.setEnabled(true);
        } else {
            startDinerSunday.setEnabled(false);
            endDinerSunday.setEnabled(false);
        }
    }

    public void setHour(final View view) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(OwnerChoosingOpeningHours.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                ((EditText) view).setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void updateHours(View view) {
        OpeningDaysHours openingDaysHours = new OpeningDaysHours();

        openingDaysHours.setContinuous(continuous.isChecked());

        openingDaysHours.setMonday(monday.isChecked());
        openingDaysHours.setTuesday(tuesday.isChecked());
        openingDaysHours.setWednesday(wednesday.isChecked());
        openingDaysHours.setThursday(thursday.isChecked());
        openingDaysHours.setFriday(friday.isChecked());
        openingDaysHours.setSaturday(saturday.isChecked());
        openingDaysHours.setSunday(sunday.isChecked());

        openingDaysHours.setMondayMorning(mondayLunch.isChecked());
        openingDaysHours.setTuesdayMorning(tuesdayLunch.isChecked());
        openingDaysHours.setWednesdayMorning(wednesdayLunch.isChecked());
        openingDaysHours.setThursdayMorning(thursdayLunch.isChecked());
        openingDaysHours.setFridayMorning(fridayLunch.isChecked());
        openingDaysHours.setSaturdayMorning(saturdayLunch.isChecked());
        openingDaysHours.setSundayMorning(sundayLunch.isChecked());
        openingDaysHours.setMondayEvening(mondayDiner.isChecked());
        openingDaysHours.setTuesdayEvening(tuesdayDiner.isChecked());
        openingDaysHours.setWednesdayEvening(wednesdayDiner.isChecked());
        openingDaysHours.setThursdayEvening(thursdayDiner.isChecked());
        openingDaysHours.setFridayEvening(fridayDiner.isChecked());
        openingDaysHours.setSaturdayEvening(saturdayDiner.isChecked());
        openingDaysHours.setSundayEvening(sundayDiner.isChecked());

        openingDaysHours.setMondayLunchStart(startLunchMonday.getText().toString());
        openingDaysHours.setMondayLunchEnd(endLunchMonday.getText().toString());
        openingDaysHours.setTuesdayLunchStart(startLunchTuesday.getText().toString());
        openingDaysHours.setTuesdayLunchEnd(endLunchTuesday.getText().toString());
        openingDaysHours.setWednesdayLunchStart(startLunchWednesday.getText().toString());
        openingDaysHours.setWednesdayLunchEnd(endLunchWednesday.getText().toString());
        openingDaysHours.setThursdayLunchStart(startLunchThursday.getText().toString());
        openingDaysHours.setThursdayLunchEnd(endLunchThursday.getText().toString());
        openingDaysHours.setFridayLunchStart(startLunchFriday.getText().toString());
        openingDaysHours.setFridayLunchEnd(endLunchFriday.getText().toString());
        openingDaysHours.setSaturdayLunchStart(startLunchSaturday.getText().toString());
        openingDaysHours.setSaturdayLunchEnd(endLunchSaturday.getText().toString());
        openingDaysHours.setSundayLunchStart(startLunchSunday.getText().toString());
        openingDaysHours.setSundayLunchEnd(endLunchSunday.getText().toString());

        openingDaysHours.setMondayDinerStart(startDinerMonday.getText().toString());
        openingDaysHours.setMondayDinerEnd(endDinerMonday.getText().toString());
        openingDaysHours.setTuesdayDinerStart(startDinerTuesday.getText().toString());
        openingDaysHours.setTuesdayDinerEnd(endDinerTuesday.getText().toString());
        openingDaysHours.setWednesdayDinerStart(startDinerWednesday.getText().toString());
        openingDaysHours.setWednesdayDinerEnd(endDinerWednesday.getText().toString());
        openingDaysHours.setThursdayDinerStart(startDinerThursday.getText().toString());
        openingDaysHours.setThursdayDinerEnd(endDinerThursday.getText().toString());
        openingDaysHours.setFridayDinerStart(startDinerFriday.getText().toString());
        openingDaysHours.setFridayDinerEnd(endDinerFriday.getText().toString());
        openingDaysHours.setSaturdayDinerStart(startDinerSaturday.getText().toString());
        openingDaysHours.setSaturdayDinerEnd(endDinerSaturday.getText().toString());
        openingDaysHours.setSundayDinerStart(startDinerSunday.getText().toString());
        openingDaysHours.setSundayDinerEnd(endDinerSunday.getText().toString());

        openingDaysHours.setMondayAllDayStart(startHourMonday.getText().toString());
        openingDaysHours.setMondayAllDayEnd(endHourMonday.getText().toString());
        openingDaysHours.setTuesdayAllDayStart(startHourTuesday.getText().toString());
        openingDaysHours.setTuesdayAllDayEnd(endHourTuesday.getText().toString());
        openingDaysHours.setWednesdayAllDayStart(startHourWednesday.getText().toString());
        openingDaysHours.setWednesdayAllDayEnd(endHourWednesday.getText().toString());
        openingDaysHours.setThursdayAllDayStart(startHourThursday.getText().toString());
        openingDaysHours.setThursdayAllDayEnd(endHourThursday.getText().toString());
        openingDaysHours.setFridayAllDayStart(startHourFriday.getText().toString());
        openingDaysHours.setFridayAllDayEnd(endHourFriday.getText().toString());
        openingDaysHours.setSaturdayAllDayStart(startHourSaturday.getText().toString());
        openingDaysHours.setSaturdayAllDayEnd(endHourSaturday.getText().toString());
        openingDaysHours.setSundayAllDayStart(startHourSunday.getText().toString());
        openingDaysHours.setSundayAllDayEnd(endHourSunday.getText().toString());

        /* here firebase part */
        mRefOpenHour.setValue(openingDaysHours).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Changes Saved",
                            Toast.LENGTH_SHORT).show();
                    //if data has been saved successfully go to main page
                    Intent intent = new Intent(context, OwnerActivityMain.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Can't save changes, try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}