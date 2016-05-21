package a15.group.lab4;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;

public class UserFragmentShowRestaurants extends Fragment {

    //adapter and recycler view for the card view
    UserAdapterShowRestaurants cardAdapter;
    RecyclerView rv;
    ArrayList<Restaurant> restaurants;
    FragmentListener mCallback;
    Location mLastLocation;

    //implements an interface to retrieve location from activity
    public interface FragmentListener {
        Location getLocation();
    }

    public UserFragmentShowRestaurants() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (FragmentListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement FragmentListener");
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurants = DataGen.makeRestaurants();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_restaurants, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = (RecyclerView) view.findViewById(R.id.restaurants);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        cardAdapter = new UserAdapterShowRestaurants(restaurants);
        rv.setAdapter(cardAdapter);
        updateDistance();
        sortByDistance();

    }

    public void updateDistance(){
        mLastLocation = mCallback.getLocation();
        if (mLastLocation != null) {
            //compute distance in meters in distance[0]
            for (int i = 0; i < restaurants.size(); i++) {
                Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(),
                        restaurants.get(i).getLatitude(), restaurants.get(i).getLongitude(), restaurants.get(i).distance);
            }
        }
    }

    public void sortByDistance() {
        if (restaurants != null)
            Collections.sort(restaurants, new RestaurantDistanceComparator());
        if (cardAdapter != null)
            cardAdapter.notifyDataSetChanged();
    }
}
