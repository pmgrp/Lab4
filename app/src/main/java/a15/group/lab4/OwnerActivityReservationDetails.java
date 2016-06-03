package a15.group.lab4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OwnerActivityReservationDetails extends AppCompatActivity {


    private Context context;
    private FirebaseDatabase database;
    private DailyOffer offer;
    private String offerId;
    private String restaurantId;
    private String userId;
    private String reservationId;
    private Reservation reservation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth == null){
            Intent in = new Intent(context, ActivityMain.class);
            startActivity(in);
        }
        else{
            userId = mAuth.getCurrentUser().getUid();
        }

        reservationId = getIntent().getStringExtra("reservationID");
        offerId = getIntent().getStringExtra("offerID");

        database = FirebaseDatabase.getInstance();
        DatabaseReference mRefOffer = database.getReference().child("offers").child(offerId);
        DatabaseReference mRefOwnerReservation = database.getReference().child("owner-reservations").child(userId).child(reservationId);

        setContentView(R.layout.owner_activity_reservation_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getReservation(mRefOwnerReservation);
        getOffer(mRefOffer);


    }


    private void getOffer(DatabaseReference mRefOffer) {
        mRefOffer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    offer = dataSnapshot.getValue(DailyOffer.class);
                    TextView textView = (TextView) findViewById(R.id.offer_price);
                    textView.setText(String.format(Locale.getDefault(), "%d", offer.getPrice()) + " €");
                    ImageView imageView = (ImageView) findViewById(R.id.offer_photo);
                    Glide.with(context)
                            .load(offer.getPhoto())
                            .centerCrop()
                            .into(imageView);
                    textView = (TextView) findViewById(R.id.offer_availability);
                    textView.setText(Integer.toString(offer.getAvailability()));
                    textView = (TextView) findViewById(R.id.offer_name);
                    textView.setText(offer.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void getReservation(DatabaseReference mRefOwnerReservation){
        mRefOwnerReservation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    reservation = dataSnapshot.getValue(Reservation.class);
                    TextView textView = (TextView) findViewById(R.id.customer_name);
                    textView.setText(reservation.getUserName());
                    textView = (TextView) findViewById(R.id.customer_surname);
                    textView.setText(reservation.getUserSurname());
                    textView = (TextView) findViewById(R.id.customer_phone);
                    textView.setText(reservation.getUserPhone());
                    textView = (TextView) findViewById(R.id.customer_email);
                    textView.setText(reservation.getUserEmail());
                    textView = (TextView) findViewById(R.id.customer_comment);
                    textView.setText(reservation.getComment());
                    //buttons
                    Button reservationConfirm = (Button) findViewById(R.id.reservation_confirm);
                    Button reservationRejected = (Button) findViewById(R.id.reservation_reject);
                    if (reservationConfirm != null && reservationRejected != null) {
                        switch (reservation.getStatus()) {
                            case Reservation.ARRIVED:
                                reservationConfirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setReservationStatus(Reservation.CONFIRMED);
                                    }
                                });
                                reservationRejected.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setReservationStatus(Reservation.REJECTED);

                                    }
                                });
                                break;

                            case Reservation.CONFIRMED:
                                reservationConfirm.setVisibility(View.INVISIBLE);
                                //set button for completion
                                reservationRejected.setText("Completed");
                                reservationRejected.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setReservationStatus(Reservation.COMPLETED);
                                    }
                                });

                                break;
                            case Reservation.COMPLETED:
                                reservationConfirm.setVisibility(View.INVISIBLE);
                                reservationRejected.setText("Delete");
                                reservationRejected.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteReservation();
                                    }
                                });
                                break;
                            case Reservation.REJECTED:
                                reservationConfirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setReservationStatus(Reservation.CONFIRMED);
                                    }
                                });
                                reservationRejected.setText("Delete");
                                reservationRejected.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteReservation();
                                    }
                                });
                                break;

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("ERR", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }


    private void setReservationStatus(int status){

        DatabaseReference mRef2 = database.getReference().child("user-reservations").child(reservation.getCustomerId()).child(reservationId);
        mRef2.child("status").setValue(status);
        DatabaseReference mRef = database.getReference().child("owner-reservations").child(userId).child(reservationId);
        mRef.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent in = new Intent(context, OwnerActivityShowReservations.class);
                    startActivity(in);
                }
            }
        });

    }

    private void deleteReservation(){
        DatabaseReference mRef = database.getReference().child("owner-reservations").child(userId).child(reservationId);
        mRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent in = new Intent(context, OwnerActivityShowReservations.class);
                startActivity(in);
            }
        });
    }

}
