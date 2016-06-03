package a15.group.lab4;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by eugeniosorbellini on 03/06/16.
 */
public class OwnerReservationHolder extends RecyclerView.ViewHolder {
    View mView;

    public OwnerReservationHolder(View view){
        super(view);
        mView = view;
    }

    public void setName(String name, String surname){
        TextView userName = (TextView)mView.findViewById(R.id.customer_name);
        TextView userSurname = (TextView)mView.findViewById(R.id.customer_surname);
        userName.setText(name);
        userSurname.setText(surname);
    }

    public void setPhone(String phone){
        TextView phoneNumber = (TextView)mView.findViewById(R.id.customer_phone);
        phoneNumber.setText(phone);
    }

    public void setComment(String comment){
        TextView customerComment = (TextView)mView.findViewById(R.id.customer_comment);
        customerComment.setText(comment);
    }

    public void setTime(String date, String time){
        TextView lunchTime = (TextView)mView.findViewById(R.id.lunch_time);
        lunchTime.setText(date + " at " + time);
    }

    public void setPrice(int price){
        TextView dishPrice = (TextView)mView.findViewById(R.id.reservation_tot_price);
        dishPrice.setText(Integer.toString(price));
    }

    public void setStatus(int status){
        TextView reservationStatus = (TextView)mView.findViewById(R.id.reservation_status);
        switch(status){
            case Reservation.ARRIVED:
                reservationStatus.setText(R.string.reservations_arrived);
                break;
            case Reservation.CONFIRMED:
                reservationStatus.setText(R.string.reservations_confirmed);
                break;
            case Reservation.COMPLETED:
                reservationStatus.setText(R.string.reservations_completed);
                break;
            case Reservation.REJECTED:
                reservationStatus.setText(R.string.reservations_rejected);
        }
    }

    public CardView getCard(){
        CardView cv = (CardView)mView.findViewById(R.id.reservation_card);
        return cv;
    }

    public Button getDeleteButton(){
        Button deleteButton = (Button)mView.findViewById(R.id.reservation_delete);
        return deleteButton;
    }


    public void displayButton(int status){
        Button deleteButton = (Button)mView.findViewById(R.id.reservation_delete);
        TextView lunchTimeTitle = (TextView)mView.findViewById(R.id.lunch_time_title);
        TextView lunchTime = (TextView)mView.findViewById(R.id.lunch_time);
        if(status == Reservation.COMPLETED) {
            deleteButton.setVisibility(View.VISIBLE);
            lunchTime.setVisibility(View.GONE);
            lunchTimeTitle.setVisibility(View.GONE);
        }else{
            deleteButton.setVisibility(View.GONE);
            lunchTime.setVisibility(View.VISIBLE);
            lunchTimeTitle.setVisibility(View.VISIBLE);
        }

    }

}
