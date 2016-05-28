package a15.group.lab4;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;


public class UserFragmentShowOffers extends Fragment {

    UserAdapterShowOffers cardAdapter;
    RecyclerView rv;
    ArrayList<DailyOffer> offers;
    FragmentListener mCallback;
    Location mLastLocation;
    int spinner_position;

    public UserFragmentShowOffers() {
        // Required empty public constructor
    }

    //implements an interface to retrieve location from activity
    public interface FragmentListener {
        Location getLocation();
        int getSpinnerPosition();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make offers data
        offers = DataGen.makeOffers();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_offers, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /**
         * creates the card view with offers
         */
        rv = (RecyclerView) view.findViewById(R.id.offers);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        cardAdapter = new UserAdapterShowOffers(offers);
        rv.setAdapter(cardAdapter);
        updateDistance();
        spinner_position = mCallback.getSpinnerPosition();
        switch (spinner_position){
            case 0:
                sortByDistance();
                break;
            case 1:
                sortByPrice();
                break;
        }
    }

    public void updateDistance(){
        //get location from parent Activity
        mLastLocation = mCallback.getLocation();

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

}

