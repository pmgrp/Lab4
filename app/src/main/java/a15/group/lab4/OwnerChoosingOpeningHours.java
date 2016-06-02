package a15.group.lab4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_choosing_opening_hours);

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
        if (continuous.isChecked()) {

        }
    }

    public void updateHours(View view) {

    }

}
