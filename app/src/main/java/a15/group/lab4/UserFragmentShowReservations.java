package a15.group.lab4;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugeniosorbellini on 10/05/16.
 */
public class UserFragmentShowReservations extends Fragment {

    //adapter and recycler view for the card view
    UserAdapterShowReservations cardAdapter;
    RecyclerView rv;
    ArrayList<Reservation> reservations;
    FragmentListener mCallback;

    //implements an interface to retrieve data from activity
    public interface FragmentListener {
        //by now empty
    }

    public UserFragmentShowReservations() {
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
        //reservations = DataGen.makeReservations();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String dataString = preferences.getString("reservations", null);
        if (dataString != null) {
            Gson gson = new Gson();
            reservations = gson.fromJson(dataString, new TypeToken<List<Reservation>>() {
            }.getType());
        }
        else{
            reservations = new ArrayList<>();
        }


        //Log.d("TAG", reservations.get(1).getDailyOffer().getName());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_reservations, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = (RecyclerView) view.findViewById(R.id.reservations);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        cardAdapter = new UserAdapterShowReservations(reservations);
        rv.setAdapter(cardAdapter);
    }
}
