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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by eugeniosorbellini on 29/04/16.
 */
public class UserAdapterShowRestaurants extends RecyclerView.Adapter<UserAdapterShowRestaurants.RestaurantViewHolder> {

    ArrayList<Restaurant> restaurants;

    UserAdapterShowRestaurants(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public static class RestaurantViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView restaurantImage;
        TextView restaurantName;
        TextView restaurantPhone;
        TextView restaurantAddress;
        TextView restaurantDistance;

        RestaurantViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.restaurant_card);
            restaurantImage = (ImageView) itemView.findViewById(R.id.restaurant_card_image);
            restaurantName = (TextView) itemView.findViewById(R.id.restaurant_card_restaurant);
            restaurantPhone = (TextView) itemView.findViewById(R.id.restaurant_card_phone);
            restaurantAddress = (TextView) itemView.findViewById(R.id.restaurant_card_address);
            restaurantDistance = (TextView) itemView.findViewById(R.id.restaurant_card_distance);
        }
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_restaurant_card, viewGroup, false);
        RestaurantViewHolder rvh = new RestaurantViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(final RestaurantViewHolder restaurantViewHolder, int i) {

        //restaurantViewHolder.restaurantImage.setImageURI(Uri.parse(restaurants.get(i).getRestaurantPhoto()));
        Glide.with(restaurantViewHolder.cv.getContext())
                .load(restaurants.get(i).getRestaurantPhoto())
                .centerCrop()
                .into(restaurantViewHolder.restaurantImage);
        restaurantViewHolder.restaurantName.setText(restaurants.get(i).getRestaurantName());
        restaurantViewHolder.restaurantPhone.setText(restaurants.get(i).getRestaurantPhone());
        restaurantViewHolder.restaurantAddress.setText(restaurants.get(i).getRestaurantAddress());
        float km = restaurants.get(i).distance / 1000;
        restaurantViewHolder.restaurantDistance.setText(String.format(Locale.getDefault(), "%.1f", km));
        restaurantViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save current restaurant in shared preferences
                Restaurant restaurant = restaurants.get(restaurantViewHolder.getAdapterPosition());
                Intent i = new Intent(v.getContext(), UserActivityRestaurantProfile.class);
                i.putExtra("restaurantID", restaurant.getID());
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}