package a15.group.lab4;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by eugeniosorbellini on 10/06/16.
 */
public class UserActivityRestaurantMap extends AppCompatActivity implements OnMapReadyCallback {


    Double latitude=null;
    Double longitude=null;
    String restaurantName;

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng currentLocation = new LatLng(latitude, longitude);

        map.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title(restaurantName));
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

        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);
        restaurantName = getIntent().getStringExtra("restaurantName");

        setContentView(R.layout.user_activity_restaurant_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_frame, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);

    }


}
