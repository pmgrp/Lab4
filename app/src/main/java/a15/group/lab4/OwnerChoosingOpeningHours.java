package a15.group.lab4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import java.sql.Time;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_choosing_opening_hours);

        initAllID();

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
        } else {
            monday.setChecked(false);
            tuesday.setChecked(false);
            wednesday.setChecked(false);
            thursday.setChecked(false);
            friday.setChecked(false);
            saturday.setChecked(false);
            sunday.setChecked(false);
        }


    }

    public void continuous(View view) {
        RelativeLayout rl = null;
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
        }
    }

    public void monday(View view) {
        if (monday.isChecked()) {
            mondayLunch.setChecked(true);
            mondayDiner.setChecked(true);
            startLunchMonday.setFocusable(true);
            endLunchMonday.setFocusable(true);
            startDinerMonday.setFocusable(true);
            endDinerMonday.setFocusable(true);
            startLunchMonday.setFocusableInTouchMode(true);
            endLunchMonday.setFocusableInTouchMode(true);
            startDinerMonday.setFocusableInTouchMode(true);
            endDinerMonday.setFocusableInTouchMode(true);
        } else {
            mondayLunch.setChecked(false);
            mondayDiner.setChecked(false);
            startLunchMonday.setFocusable(false);
            endLunchMonday.setFocusable(false);
            startDinerMonday.setFocusable(false);
            endDinerMonday.setFocusable(false);
            startLunchMonday.setFocusableInTouchMode(false);
            endLunchMonday.setFocusableInTouchMode(false);
            startDinerMonday.setFocusableInTouchMode(false);
            endDinerMonday.setFocusableInTouchMode(false);
        }
    }

    public void mondayLunch(View view) {
        if (mondayLunch.isChecked()) {
            startLunchMonday.setFocusable(true);
            endLunchMonday.setFocusable(true);
            startDinerMonday.setFocusable(true);
            endDinerMonday.setFocusable(true);
            startLunchMonday.setFocusableInTouchMode(true);
            endLunchMonday.setFocusableInTouchMode(true);
            startDinerMonday.setFocusableInTouchMode(true);
            endDinerMonday.setFocusableInTouchMode(true);
        } else {
            startLunchMonday.setFocusable(false);
            endLunchMonday.setFocusable(false);
            startDinerMonday.setFocusable(false);
            endDinerMonday.setFocusable(false);
            startLunchMonday.setFocusableInTouchMode(false);
            endLunchMonday.setFocusableInTouchMode(false);
            startDinerMonday.setFocusableInTouchMode(false);
            endDinerMonday.setFocusableInTouchMode(false);
        }
    }

    public void mondayDiner(View view) {
        if (mondayDiner.isChecked()) {
            startLunchMonday.setFocusable(true);
            endLunchMonday.setFocusable(true);
            startDinerMonday.setFocusable(true);
            endDinerMonday.setFocusable(true);
            startLunchMonday.setFocusableInTouchMode(true);
            endLunchMonday.setFocusableInTouchMode(true);
            startDinerMonday.setFocusableInTouchMode(true);
            endDinerMonday.setFocusableInTouchMode(true);
        } else {
            startLunchMonday.setFocusable(false);
            endLunchMonday.setFocusable(false);
            startDinerMonday.setFocusable(false);
            endDinerMonday.setFocusable(false);
            startLunchMonday.setFocusableInTouchMode(false);
            endLunchMonday.setFocusableInTouchMode(false);
            startDinerMonday.setFocusableInTouchMode(false);
            endDinerMonday.setFocusableInTouchMode(false);
        }
    }

    public void tuesday(View view) {
        if (tuesday.isChecked()) {
            tuesdayLunch.setChecked(true);
            tuesdayDiner.setChecked(true);
            startLunchTuesday.setFocusable(true);
            endLunchTuesday.setFocusable(true);
            startDinerTuesday.setFocusable(true);
            endDinerTuesday.setFocusable(true);
            startLunchTuesday.setFocusableInTouchMode(true);
            endLunchTuesday.setFocusableInTouchMode(true);
            startDinerTuesday.setFocusableInTouchMode(true);
            endDinerTuesday.setFocusableInTouchMode(true);
        } else {
            tuesdayLunch.setChecked(false);
            tuesdayDiner.setChecked(false);
            startLunchTuesday.setFocusable(false);
            endLunchTuesday.setFocusable(false);
            startDinerTuesday.setFocusable(false);
            endDinerTuesday.setFocusable(false);
            startLunchTuesday.setFocusableInTouchMode(false);
            endLunchTuesday.setFocusableInTouchMode(false);
            startDinerTuesday.setFocusableInTouchMode(false);
            endDinerTuesday.setFocusableInTouchMode(false);
        }
    }

    public void tuesdayLunch(View view) {
        if (tuesdayLunch.isChecked()) {
            startLunchTuesday.setFocusable(true);
            endLunchTuesday.setFocusable(true);
            startDinerTuesday.setFocusable(true);
            endDinerTuesday.setFocusable(true);
            startLunchTuesday.setFocusableInTouchMode(true);
            endLunchTuesday.setFocusableInTouchMode(true);
            startDinerTuesday.setFocusableInTouchMode(true);
            endDinerTuesday.setFocusableInTouchMode(true);
        } else {
            startLunchTuesday.setFocusable(false);
            endLunchTuesday.setFocusable(false);
            startDinerTuesday.setFocusable(false);
            endDinerTuesday.setFocusable(false);
            startLunchTuesday.setFocusableInTouchMode(false);
            endLunchTuesday.setFocusableInTouchMode(false);
            startDinerTuesday.setFocusableInTouchMode(false);
            endDinerTuesday.setFocusableInTouchMode(false);
        }
    }

    public void tuesdayDiner(View view) {
        if (tuesdayDiner.isChecked()) {
            startLunchTuesday.setFocusable(true);
            endLunchTuesday.setFocusable(true);
            startDinerTuesday.setFocusable(true);
            endDinerTuesday.setFocusable(true);
            startLunchTuesday.setFocusableInTouchMode(true);
            endLunchTuesday.setFocusableInTouchMode(true);
            startDinerTuesday.setFocusableInTouchMode(true);
            endDinerTuesday.setFocusableInTouchMode(true);
        } else {
            startLunchTuesday.setFocusable(false);
            endLunchTuesday.setFocusable(false);
            startDinerTuesday.setFocusable(false);
            endDinerTuesday.setFocusable(false);
            startLunchTuesday.setFocusableInTouchMode(false);
            endLunchTuesday.setFocusableInTouchMode(false);
            startDinerTuesday.setFocusableInTouchMode(false);
            endDinerTuesday.setFocusableInTouchMode(false);
        }
    }

    public void wednesday(View view) {
        if (wednesday.isChecked()) {
            wednesdayLunch.setChecked(true);
            wednesdayDiner.setChecked(true);
            startLunchWednesday.setFocusable(true);
            endLunchWednesday.setFocusable(true);
            startDinerWednesday.setFocusable(true);
            endDinerWednesday.setFocusable(true);
            startLunchWednesday.setFocusableInTouchMode(true);
            endLunchWednesday.setFocusableInTouchMode(true);
            startDinerWednesday.setFocusableInTouchMode(true);
            endDinerWednesday.setFocusableInTouchMode(true);
        } else {
            wednesdayLunch.setChecked(false);
            wednesdayDiner.setChecked(false);
            startLunchWednesday.setFocusable(false);
            endLunchWednesday.setFocusable(false);
            startDinerWednesday.setFocusable(false);
            endDinerWednesday.setFocusable(false);
            startLunchWednesday.setFocusableInTouchMode(false);
            endLunchWednesday.setFocusableInTouchMode(false);
            startDinerWednesday.setFocusableInTouchMode(false);
            endDinerWednesday.setFocusableInTouchMode(false);
        }
    }

    public void wednesdayLunch(View view) {
        if (wednesdayLunch.isChecked()) {
            startLunchWednesday.setFocusable(true);
            endLunchWednesday.setFocusable(true);
            startDinerWednesday.setFocusable(true);
            endDinerWednesday.setFocusable(true);
            startLunchWednesday.setFocusableInTouchMode(true);
            endLunchWednesday.setFocusableInTouchMode(true);
            startDinerWednesday.setFocusableInTouchMode(true);
            endDinerWednesday.setFocusableInTouchMode(true);
        } else {
            startLunchWednesday.setFocusable(false);
            endLunchWednesday.setFocusable(false);
            startDinerWednesday.setFocusable(false);
            endDinerWednesday.setFocusable(false);
            startLunchWednesday.setFocusableInTouchMode(false);
            endLunchWednesday.setFocusableInTouchMode(false);
            startDinerWednesday.setFocusableInTouchMode(false);
            endDinerWednesday.setFocusableInTouchMode(false);
        }
    }

    public void wednesdayDiner(View view) {
        if (wednesdayDiner.isChecked()) {
            startLunchWednesday.setFocusable(true);
            endLunchWednesday.setFocusable(true);
            startDinerWednesday.setFocusable(true);
            endDinerWednesday.setFocusable(true);
            startLunchWednesday.setFocusableInTouchMode(true);
            endLunchWednesday.setFocusableInTouchMode(true);
            startDinerWednesday.setFocusableInTouchMode(true);
            endDinerWednesday.setFocusableInTouchMode(true);
        } else {
            startLunchWednesday.setFocusable(false);
            endLunchWednesday.setFocusable(false);
            startDinerWednesday.setFocusable(false);
            endDinerWednesday.setFocusable(false);
            startLunchWednesday.setFocusableInTouchMode(false);
            endLunchWednesday.setFocusableInTouchMode(false);
            startDinerWednesday.setFocusableInTouchMode(false);
            endDinerWednesday.setFocusableInTouchMode(false);
        }
    }

    public void thursday(View view) {
        if (thursday.isChecked()) {
            thursdayLunch.setChecked(true);
            thursdayDiner.setChecked(true);
            startLunchThursday.setFocusable(true);
            endLunchThursday.setFocusable(true);
            startDinerThursday.setFocusable(true);
            endDinerThursday.setFocusable(true);
            startLunchThursday.setFocusableInTouchMode(true);
            endLunchThursday.setFocusableInTouchMode(true);
            startDinerThursday.setFocusableInTouchMode(true);
            endDinerThursday.setFocusableInTouchMode(true);
        } else {
            thursdayLunch.setChecked(false);
            thursdayDiner.setChecked(false);
            startLunchThursday.setFocusable(false);
            endLunchThursday.setFocusable(false);
            startDinerThursday.setFocusable(false);
            endDinerThursday.setFocusable(false);
            startLunchThursday.setFocusableInTouchMode(false);
            endLunchThursday.setFocusableInTouchMode(false);
            startDinerThursday.setFocusableInTouchMode(false);
            endDinerThursday.setFocusableInTouchMode(false);
        }
    }

    public void thursdayLunch(View view) {
        if (thursdayLunch.isChecked()) {
            startLunchThursday.setFocusable(true);
            endLunchThursday.setFocusable(true);
            startDinerThursday.setFocusable(true);
            endDinerThursday.setFocusable(true);
            startLunchThursday.setFocusableInTouchMode(true);
            endLunchThursday.setFocusableInTouchMode(true);
            startDinerThursday.setFocusableInTouchMode(true);
            endDinerThursday.setFocusableInTouchMode(true);
        } else {
            startLunchThursday.setFocusable(false);
            endLunchThursday.setFocusable(false);
            startDinerThursday.setFocusable(false);
            endDinerThursday.setFocusable(false);
            startLunchThursday.setFocusableInTouchMode(false);
            endLunchThursday.setFocusableInTouchMode(false);
            startDinerThursday.setFocusableInTouchMode(false);
            endDinerThursday.setFocusableInTouchMode(false);
        }
    }

    public void thursdayDiner(View view) {
        if (thursdayDiner.isChecked()) {
            startLunchThursday.setFocusable(true);
            endLunchThursday.setFocusable(true);
            startDinerThursday.setFocusable(true);
            endDinerThursday.setFocusable(true);
            startLunchThursday.setFocusableInTouchMode(true);
            endLunchThursday.setFocusableInTouchMode(true);
            startDinerThursday.setFocusableInTouchMode(true);
            endDinerThursday.setFocusableInTouchMode(true);
        } else {
            startLunchThursday.setFocusable(false);
            endLunchThursday.setFocusable(false);
            startDinerThursday.setFocusable(false);
            endDinerThursday.setFocusable(false);
            startLunchThursday.setFocusableInTouchMode(false);
            endLunchThursday.setFocusableInTouchMode(false);
            startDinerThursday.setFocusableInTouchMode(false);
            endDinerThursday.setFocusableInTouchMode(false);
        }
    }

    public void friday(View view) {
        if (friday.isChecked()) {
            fridayLunch.setChecked(true);
            fridayDiner.setChecked(true);
            startLunchFriday.setFocusable(true);
            endLunchFriday.setFocusable(true);
            startDinerFriday.setFocusable(true);
            endDinerFriday.setFocusable(true);
            startLunchFriday.setFocusableInTouchMode(true);
            endLunchFriday.setFocusableInTouchMode(true);
            startDinerFriday.setFocusableInTouchMode(true);
            endDinerFriday.setFocusableInTouchMode(true);
        } else {
            fridayLunch.setChecked(false);
            fridayDiner.setChecked(false);
            startLunchFriday.setFocusable(false);
            endLunchFriday.setFocusable(false);
            startDinerFriday.setFocusable(false);
            endDinerFriday.setFocusable(false);
            startLunchFriday.setFocusableInTouchMode(false);
            endLunchFriday.setFocusableInTouchMode(false);
            startDinerFriday.setFocusableInTouchMode(false);
            endDinerFriday.setFocusableInTouchMode(false);
        }
    }

    public void fridayLunch(View view) {
        if (fridayLunch.isChecked()) {
            startLunchFriday.setFocusable(true);
            endLunchFriday.setFocusable(true);
            startDinerFriday.setFocusable(true);
            endDinerFriday.setFocusable(true);
            startLunchFriday.setFocusableInTouchMode(true);
            endLunchFriday.setFocusableInTouchMode(true);
            startDinerFriday.setFocusableInTouchMode(true);
            endDinerFriday.setFocusableInTouchMode(true);
        } else {
            startLunchFriday.setFocusable(false);
            endLunchFriday.setFocusable(false);
            startDinerFriday.setFocusable(false);
            endDinerFriday.setFocusable(false);
            startLunchFriday.setFocusableInTouchMode(false);
            endLunchFriday.setFocusableInTouchMode(false);
            startDinerFriday.setFocusableInTouchMode(false);
            endDinerFriday.setFocusableInTouchMode(false);
        }
    }

    public void fridayDiner(View view) {
        if (fridayDiner.isChecked()) {
            startLunchFriday.setFocusable(true);
            endLunchFriday.setFocusable(true);
            startDinerFriday.setFocusable(true);
            endDinerFriday.setFocusable(true);
            startLunchFriday.setFocusableInTouchMode(true);
            endLunchFriday.setFocusableInTouchMode(true);
            startDinerFriday.setFocusableInTouchMode(true);
            endDinerFriday.setFocusableInTouchMode(true);
        } else {
            startLunchFriday.setFocusable(false);
            endLunchFriday.setFocusable(false);
            startDinerFriday.setFocusable(false);
            endDinerFriday.setFocusable(false);
            startLunchFriday.setFocusableInTouchMode(false);
            endLunchFriday.setFocusableInTouchMode(false);
            startDinerFriday.setFocusableInTouchMode(false);
            endDinerFriday.setFocusableInTouchMode(false);
        }
    }

    public void saturday(View view) {
        if (saturday.isChecked()) {
            saturdayLunch.setChecked(true);
            saturdayDiner.setChecked(true);
            startLunchSaturday.setFocusable(true);
            endLunchSaturday.setFocusable(true);
            startDinerSaturday.setFocusable(true);
            endDinerSaturday.setFocusable(true);
            startLunchSaturday.setFocusableInTouchMode(true);
            endLunchSaturday.setFocusableInTouchMode(true);
            startDinerSaturday.setFocusableInTouchMode(true);
            endDinerSaturday.setFocusableInTouchMode(true);
        } else {
            saturdayLunch.setChecked(false);
            saturdayDiner.setChecked(false);
            startLunchSaturday.setFocusable(false);
            endLunchSaturday.setFocusable(false);
            startDinerSaturday.setFocusable(false);
            endDinerSaturday.setFocusable(false);
            startLunchSaturday.setFocusableInTouchMode(false);
            endLunchSaturday.setFocusableInTouchMode(false);
            startDinerSaturday.setFocusableInTouchMode(false);
            endDinerSaturday.setFocusableInTouchMode(false);
        }
    }

    public void saturdayLunch(View view) {
        if (saturdayLunch.isChecked()) {
            startLunchSaturday.setFocusable(true);
            endLunchSaturday.setFocusable(true);
            startDinerSaturday.setFocusable(true);
            endDinerSaturday.setFocusable(true);
            startLunchSaturday.setFocusableInTouchMode(true);
            endLunchSaturday.setFocusableInTouchMode(true);
            startDinerSaturday.setFocusableInTouchMode(true);
            endDinerSaturday.setFocusableInTouchMode(true);
        } else {
            startLunchSaturday.setFocusable(false);
            endLunchSaturday.setFocusable(false);
            startDinerSaturday.setFocusable(false);
            endDinerSaturday.setFocusable(false);
            startLunchSaturday.setFocusableInTouchMode(false);
            endLunchSaturday.setFocusableInTouchMode(false);
            startDinerSaturday.setFocusableInTouchMode(false);
            endDinerSaturday.setFocusableInTouchMode(false);
        }
    }

    public void saturdayDiner(View view) {
        if (saturdayDiner.isChecked()) {
            startLunchSaturday.setFocusable(true);
            endLunchSaturday.setFocusable(true);
            startDinerSaturday.setFocusable(true);
            endDinerSaturday.setFocusable(true);
            startLunchSaturday.setFocusableInTouchMode(true);
            endLunchSaturday.setFocusableInTouchMode(true);
            startDinerSaturday.setFocusableInTouchMode(true);
            endDinerSaturday.setFocusableInTouchMode(true);
        } else {
            startLunchSaturday.setFocusable(false);
            endLunchSaturday.setFocusable(false);
            startDinerSaturday.setFocusable(false);
            endDinerSaturday.setFocusable(false);
            startLunchSaturday.setFocusableInTouchMode(false);
            endLunchSaturday.setFocusableInTouchMode(false);
            startDinerSaturday.setFocusableInTouchMode(false);
            endDinerSaturday.setFocusableInTouchMode(false);
        }
    }

    public void sunday(View view) {
        if (sunday.isChecked()) {
            sundayLunch.setChecked(true);
            sundayDiner.setChecked(true);
            startLunchSunday.setFocusable(true);
            endLunchSunday.setFocusable(true);
            startDinerSunday.setFocusable(true);
            endDinerSunday.setFocusable(true);
            startLunchSunday.setFocusableInTouchMode(true);
            endLunchSunday.setFocusableInTouchMode(true);
            startDinerSunday.setFocusableInTouchMode(true);
            endDinerSunday.setFocusableInTouchMode(true);
        } else {
            sundayLunch.setChecked(false);
            sundayDiner.setChecked(false);
            startLunchSunday.setFocusable(false);
            endLunchSunday.setFocusable(false);
            startDinerSunday.setFocusable(false);
            endDinerSunday.setFocusable(false);
            startLunchSunday.setFocusableInTouchMode(false);
            endLunchSunday.setFocusableInTouchMode(false);
            startDinerSunday.setFocusableInTouchMode(false);
            endDinerSunday.setFocusableInTouchMode(false);
        }
    }

    public void sundayLunch(View view) {
        if (sundayLunch.isChecked()) {
            startLunchSunday.setFocusable(true);
            endLunchSunday.setFocusable(true);
            startDinerSunday.setFocusable(true);
            endDinerSunday.setFocusable(true);
            startLunchSunday.setFocusableInTouchMode(true);
            endLunchSunday.setFocusableInTouchMode(true);
            startDinerSunday.setFocusableInTouchMode(true);
            endDinerSunday.setFocusableInTouchMode(true);
        } else {
            startLunchSunday.setFocusable(false);
            endLunchSunday.setFocusable(false);
            startDinerSunday.setFocusable(false);
            endDinerSunday.setFocusable(false);
            startLunchSunday.setFocusableInTouchMode(false);
            endLunchSunday.setFocusableInTouchMode(false);
            startDinerSunday.setFocusableInTouchMode(false);
            endDinerSunday.setFocusableInTouchMode(false);
        }
    }

    public void sundayDiner(View view) {
        if (sundayDiner.isChecked()) {
            startLunchSunday.setFocusable(true);
            endLunchSunday.setFocusable(true);
            startDinerSunday.setFocusable(true);
            endDinerSunday.setFocusable(true);
            startLunchSunday.setFocusableInTouchMode(true);
            endLunchSunday.setFocusableInTouchMode(true);
            startDinerSunday.setFocusableInTouchMode(true);
            endDinerSunday.setFocusableInTouchMode(true);
        } else {
            startLunchSunday.setFocusable(false);
            endLunchSunday.setFocusable(false);
            startDinerSunday.setFocusable(false);
            endDinerSunday.setFocusable(false);
            startLunchSunday.setFocusableInTouchMode(false);
            endLunchSunday.setFocusableInTouchMode(false);
            startDinerSunday.setFocusableInTouchMode(false);
            endDinerSunday.setFocusableInTouchMode(false);
        }
    }

    public void updateHours(View view) {
        OpeningDaysHours openingDaysHours = new OpeningDaysHours();

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
    }
}
