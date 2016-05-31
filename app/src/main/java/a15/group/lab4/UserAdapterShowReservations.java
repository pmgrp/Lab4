package a15.group.lab4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by eugeniosorbellini on 10/05/16.
 */
public class UserAdapterShowReservations extends RecyclerView.Adapter<UserAdapterShowReservations.ReservationViewHolder> {

    ArrayList<Reservation> reservations;

    UserAdapterShowReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView offerName;
        TextView restaurantName;
        TextView time;
        TextView status;
        ImageView image;

        ReservationViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.reservation_card);
            offerName = (TextView) itemView.findViewById(R.id.reservation_card_offer_name);
            restaurantName = (TextView) itemView.findViewById(R.id.reservation_card_restaurant);
            time = (TextView) itemView.findViewById(R.id.reservation_card_time);
            status = (TextView) itemView.findViewById(R.id.reservation_card_status);
            image = (ImageView) itemView.findViewById(R.id.reservation_card_image);
        }
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    @Override
    public ReservationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_reservation_card, viewGroup, false);
        ReservationViewHolder rvh = new ReservationViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(final ReservationViewHolder reservationViewHolder, int i) {

        reservationViewHolder.offerName.setText(reservations.get(i).getDailyOffer().getName());
        reservationViewHolder.restaurantName.setText(reservations.get(i).getDailyOffer().getRestaurantName());
        reservationViewHolder.time.setText(reservations.get(i).getDate() + " at " + reservations.get(i).getTime());
        reservationViewHolder.image.setImageURI(Uri.parse(reservations.get(i).getDailyOffer().getPhoto()));
        int status = reservations.get(i).getStatus();
        switch (status) {
            case Reservation.ARRIVED:
                reservationViewHolder.status.setText("To Be Approved");
                break;
            case Reservation.CONFIRMED:
                reservationViewHolder.status.setText("Approved");
                break;
            case Reservation.COMPLETED:
                reservationViewHolder.status.setText("Completed");
                break;
            case Reservation.REJECTED:
                reservationViewHolder.status.setText("Rejected");
                break;
        }
        reservationViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save current offer in shared preferences
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                Reservation reservation = reservations.get(reservationViewHolder.getAdapterPosition());
                String json = gson.toJson(reservation);
                editor.putString("reservation", json);
                editor.commit();
                //call activity to display details
                Intent i = new Intent(v.getContext(), UserActivityShowReservationDetails.class);
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}