package a15.group.lab4;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
                                        notificationConfirmReservation();
                                    }
                                });
                                reservationRejected.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setReservationStatus(Reservation.REJECTED);
                                        notificationRejectReservation();
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
                                        notificationRejectReservation();
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
                                        notificationRejectReservation();
                                    }
                                });
                                break;
                            case Reservation.REJECTED:
                                reservationConfirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setReservationStatus(Reservation.CONFIRMED);
                                        notificationConfirmReservation();
                                    }
                                });
                                reservationRejected.setText("Delete");
                                reservationRejected.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        deleteReservation();
                                        notificationRejectReservation();
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

    private void notificationRejectReservation() {
        String contentText = "Reservation Rejected";
        createNotification(contentText, 2);
    }

    private void notificationConfirmReservation() {
        String contentText = "Reservation Confirmed";
        createNotification(contentText, 1);
    }

    private void createNotification(String text, int mId) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.logo_app_coyote)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(text);

// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, UserActivityShowOfferDetails.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(UserActivityShowOfferDetails.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
    }

    public void widenImage(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OwnerActivityReservationDetails.this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final ImageView image = new ImageView(this);
        Glide.with(context)
                .load(offer.getPhoto())
                .into(image);

        layout.addView(image);
        builder.setView(layout);

        builder.show();
    }

}
