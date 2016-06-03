package a15.group.lab4;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class OwnerActivityShowReservations extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase databse;
    private DatabaseReference mRef;
    private FirebaseUser fUser;
    private FirebaseRecyclerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        databse = FirebaseDatabase.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                fUser = firebaseAuth.getCurrentUser();
                if (fUser == null) {
                    //go back to main activity if user is not logged in
                    Intent in = new Intent(OwnerActivityShowReservations.this, ActivityMain.class);
                    startActivity(in);
                }
            }
        };




        setContentView(R.layout.owner_activity_show_reservations);
        //to add toolbar with back arrow
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //

        mRef = databse.getReference().child("owner-reservations").child(mAuth.getCurrentUser().getUid());
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.reservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new FirebaseRecyclerAdapter<Reservation, OwnerReservationHolder>(
                Reservation.class, R.layout.owner_reservation_card, OwnerReservationHolder.class, mRef) {
            @Override
            public void populateViewHolder(final OwnerReservationHolder viewHolder, Reservation reservation, final int position) {
                viewHolder.setName(reservation.getUserName(), reservation.getUserSurname());
                viewHolder.setPhone(reservation.getUserPhone());
                viewHolder.setComment(reservation.getComment());
                viewHolder.setStatus(reservation.getStatus());
                viewHolder.setTime(reservation.getDate(), reservation.getTime());
                viewHolder.setPrice(reservation.getOfferPrice());
                viewHolder.displayButton(reservation.getStatus());
                viewHolder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickPopupOptions(v, mAdapter.getRef(position).getKey(), position);
                    }
                });
            }
        };

        recyclerView.setAdapter(mAdapter);


    }

    public void onClickPopupOptions(View v, final String reservationId, final int position) {
        final CharSequence[] items = { "Delete", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(OwnerActivityShowReservations.this);
        builder.setTitle("Options");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Delete")) {
                    //remove this offer from database
                    mRef.child(reservationId).removeValue();
                    mAdapter.notifyItemRemoved(position);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
