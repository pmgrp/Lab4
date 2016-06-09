package a15.group.lab4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eugeniosorbellini on 10/05/16.
 */
public class UserActivityShowReservations extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private String userId;
    private ChildEventListener childEventListener;
    private UserAdapterShowReservations cardAdapter;
    private RecyclerView rv;
    private ArrayList<Reservation> reservations;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth == null){
            Intent in = new Intent(this, ActivityMain.class);
            startActivity(in);
        }
        else{
            userId = mAuth.getCurrentUser().getUid();
        }

        setContentView(R.layout.fragment_show_reservations);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    Reservation reservation = dataSnapshot.getValue(Reservation.class);
                    reservations.add(reservation);
                    cardAdapter.notifyItemInserted(cardAdapter.getItemCount() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Reservation reservation = dataSnapshot.getValue(Reservation.class);
                for(int i=0; i< reservations.size(); i++){
                    if(reservations.get(i).getReservationId().equals(dataSnapshot.getKey())){
                        reservations.remove(i);
                        reservations.add(reservation);
                        cardAdapter.notifyItemChanged(i);
                        break;
                    }
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for(int i=0; i< reservations.size(); i++) {
                    if (reservations.get(i).getReservationId().equals(dataSnapshot.getKey())) {
                        reservations.remove(i);
                        cardAdapter.notifyItemRemoved(i);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        reservations = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("user-reservations").child(userId);


        rv = (RecyclerView)findViewById(R.id.reservations);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        cardAdapter = new UserAdapterShowReservations(reservations);
        rv.setAdapter(cardAdapter);


        mRef.addChildEventListener(childEventListener);


    }

    @Override
    protected void onStop() {
        if(childEventListener != null){
            mRef.removeEventListener(childEventListener);
        }
        reservations.clear();
        super.onStop();
    }

}
