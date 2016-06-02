package a15.group.lab4;

import android.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;


public class UserActivityShowOffers extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private ChildEventListener childEventListener;

    private Spinner spinner;

    private UserAdapterShowOffers cardAdapter;
    private RecyclerView rv;
    private ArrayList<DailyOffer> offers;
    //int spinner_position;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make offers data
        //offers = DataGen.makeOffers();

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

        setContentView(R.layout.fragment_show_offers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_spinner);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /**
         * Spinner (the switch on the toolbar)
         *
         * */
        spinner = (Spinner) findViewById(R.id.spinner);
        //spinner.setVerticalScrollbarPosition(spinner_position);

        final ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.spinner_options, R.layout.user_spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.user_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        //spinner.setSelection(spinner_position);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //the fragment that contains the offers
                    switch (position) {
                        case 0:
                            //offers ordered by distance
                            sortByDistance();
                            break;
                        case 1:
                            //offers ordered by price
                            sortByPrice();
                            break;
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    sortByDistance();
                }
            });
        }

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    DailyOffer offer = dataSnapshot.getValue(DailyOffer.class);
                    updateDistance(offer);
                    offers.add(offer);
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

        offers = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();

        rv = (RecyclerView)findViewById(R.id.offers);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        cardAdapter = new UserAdapterShowOffers(offers);
        rv.setAdapter(cardAdapter);
    }



    public void updateDistance(){
        if (mLastLocation != null) {
            //compute distance in meters in distance[0]
            for (int i = 0; i < offers.size(); i++) {
                float dist[] = {0,0,0};
                Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                        offers.get(i).getRestaurantLatitude(), offers.get(i).getRestaurantLongitude(), dist);
                        offers.get(i).distance = dist[0];
            }
        }

    }

    public void updateDistance(DailyOffer offer){
        if (mLastLocation != null) {
            //compute distance in meters in distance[0]
                float dist[] = {0,0,0};
                Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                        offer.getRestaurantLatitude(), offer.getRestaurantLongitude(), dist);
                offer.distance = dist[0];
        }

    }


    /**
     * Those methods are called also by parent activity according to spinner position
     */

    public void sortByDistance(){
        if(offers != null)
            Collections.sort(offers, new OfferDistanceComparator());
        if(cardAdapter != null)
            cardAdapter.notifyDataSetChanged();
    }

    public void sortByPrice(){
        if(offers != null)
            Collections.sort(offers, new OfferPriceComparator());
        if(cardAdapter != null)
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

        mRef.child("offers").addChildEventListener(childEventListener);

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
            mRef.child("offers").removeEventListener(childEventListener);
        }
        super.onStop();
    }

}

