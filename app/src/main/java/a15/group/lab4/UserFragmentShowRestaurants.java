package a15.group.lab4;

import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.Collections;

public class UserFragmentShowRestaurants extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    //adapter and recycler view for the card view
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    UserAdapterShowRestaurants cardAdapter;
    RecyclerView rv;
    ArrayList<Restaurant> restaurants;
    ChildEventListener childEventListener;

    private FirebaseDatabase database;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Set Api for location
         */

        if (mGoogleApiClient == null) {
            // Building the GoogleApi client
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                    updateDistance(restaurant);
                    restaurants.add(restaurant);
                    sortByDistance();
                    cardAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //generate array list to contain restaurants
        restaurants = new ArrayList<>();

        setContentView(R.layout.fragment_show_restaurants);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        rv = (RecyclerView)findViewById(R.id.restaurants);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        cardAdapter = new UserAdapterShowRestaurants(restaurants);
        rv.setAdapter(cardAdapter);





    }


    public void updateDistance(){
        if (mLastLocation != null) {
            //compute distance in meters in distance
            for (int i = 0; i < restaurants.size(); i++) {
                float[] distance = {0,0,0,};
                Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                        restaurants.get(i).getLatitude(), restaurants.get(i).getLongitude(), distance);
                restaurants.get(i).distance = distance[0];
            }
        }
    }

    public void updateDistance(Restaurant restaurant){
        if (mLastLocation != null) {
            //compute distance in meters in distance
            float[] distance = {0,0,0,};
            Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                    restaurant.getLatitude(), restaurant.getLongitude(), distance);
            restaurant.distance = distance[0];
        }
    }

    public void sortByDistance() {
        if (restaurants != null)
            Collections.sort(restaurants, new RestaurantDistanceComparator());
        if (cardAdapter != null)
            cardAdapter.notifyDataSetChanged();
    }



    /**
     * here location methods part
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        mRef.child("restaurants").addChildEventListener(childEventListener);

    }


    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.d("TAG", "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
        Toast.makeText(getApplicationContext(),
                "This device is not supported.", Toast.LENGTH_LONG)
                .show();
        finish();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        if(childEventListener != null){
            mRef.child("restaurants").removeEventListener(childEventListener);
        }
        super.onStop();
    }
}
