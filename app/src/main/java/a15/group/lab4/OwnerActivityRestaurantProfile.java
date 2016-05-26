package a15.group.lab4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;

public class OwnerActivityRestaurantProfile extends AppCompatActivity {

    Restaurant obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity_restaurant_profile);
        //toolbar
        //to add toolbar with back arrow
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = preferences.getString("restaurant", null);
        obj = gson.fromJson(json, Restaurant.class);
        if (obj != null) {
            TextView txt = (TextView) findViewById(R.id.restaurantName);
            txt.setText(obj.getRestaurantName());
            txt = (TextView) findViewById(R.id.restaurantPhone);
            txt.setText(obj.getRestaurantPhone());
            txt = (TextView) findViewById(R.id.restaurantAddress);
            txt.setText(obj.getRestaurantAddress());
            txt = (TextView) findViewById(R.id.restaurantEmail);
            txt.setText(obj.getRestaurantEmail());
            txt = (TextView) findViewById(R.id.restaurantWebsite);
            txt.setText(obj.getRestaurantWebsite());
            txt = (TextView) findViewById(R.id.restaurantIVA);
            txt.setText(obj.getRestaurantPiva());
        }
        else {
            obj = new Restaurant();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant_profile, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            Intent intent = new Intent(this, OwnerActivityEditRestaurantProfile.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(obj);
        editor.putString("restaurant", json);
        editor.commit();
    }
}