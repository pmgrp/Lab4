package a15.group.lab4;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by eugeniosorbellini on 29/05/16.
 */
public class OwnerDishHolder extends RecyclerView.ViewHolder {
    View mView;
    RelativeLayout mOfferContainer;

    public OwnerDishHolder(View view){
        super(view);
        mView = view;
    }

    public void setName(String name){
        TextView offerName = (TextView)mView.findViewById(R.id.offer_text);
        offerName.setText(name);
    }

    public void setImage(String image){
        ImageView offerPhoto = (ImageView)mView.findViewById(R.id.offer_image);
        Glide.with(mView.getContext())
                .load(image)
                .centerCrop()
                .into(offerPhoto);

    }

    public RelativeLayout getOfferContainer(){
        mOfferContainer = (RelativeLayout)mView.findViewById(R.id.offer_container);
        return mOfferContainer;
    }

}
