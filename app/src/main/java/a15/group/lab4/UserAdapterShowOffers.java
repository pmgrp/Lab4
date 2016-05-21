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
import java.util.Locale;

/**
 * Created by eugeniosorbellini on 26/04/16.
 */
public class UserAdapterShowOffers extends RecyclerView.Adapter<UserAdapterShowOffers.OfferViewHolder> {


    ArrayList<DailyOffer> offers;

    UserAdapterShowOffers(ArrayList<DailyOffer> offers){
        this.offers = offers;
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView offerImage;
        TextView offerRestaurantName;
        TextView offerName;
        TextView offerPrice;
        TextView offerDistance;

        OfferViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.offer_card);
            offerImage = (ImageView)itemView.findViewById(R.id.offer_card_image);
            offerRestaurantName = (TextView)itemView.findViewById(R.id.offer_card_restaurant);
            offerName = (TextView)itemView.findViewById(R.id.offer_card_offer);
            offerPrice = (TextView)itemView.findViewById(R.id.offer_card_price);
            offerDistance = (TextView)itemView.findViewById(R.id.offer_card_distance);
        }
    }

    @Override
    public int getItemCount(){
        return offers.size();
    }

    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.offer_card, viewGroup, false);
        OfferViewHolder ovh = new OfferViewHolder(v);
        return ovh;
    }

    @Override
    public void onBindViewHolder(final OfferViewHolder offerViewHolder, int i) {

        offerViewHolder.offerImage.setImageURI(Uri.parse(offers.get(i).getPhoto()));
        offerViewHolder.offerRestaurantName.setText(offers.get(i).getRestaurantName());
        offerViewHolder.offerName.setText(offers.get(i).getName());
        offerViewHolder.offerPrice.setText(String.format(Locale.getDefault(),"%d", offers.get(i).getPrice()));
        float km = offers.get(i).distance[0]/1000;
        offerViewHolder.offerDistance.setText(String.format(Locale.getDefault(),"%.1f", km));
        offerViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save current offer in shared preferences
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();
                DailyOffer offer = offers.get(offerViewHolder.getAdapterPosition());
                String json = gson.toJson(offer);
                editor.putString("offer", json);
                editor.commit();
                //call activity to display details
                Intent i = new Intent(v.getContext(), UserActivityShowOfferDetails.class);
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
