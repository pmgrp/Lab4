package a15.group.lab4;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ISEN on 11/04/2016.
 */
public class AdapterShowOffers extends BaseAdapter {

    ArrayList<DailyOffer> dailyOffers = new ArrayList<>();

    public AdapterShowOffers(ArrayList<DailyOffer> dailyOffers) {
        this.dailyOffers = dailyOffers;
    }

    @Override
    public int getCount() {
        return dailyOffers.size();
    }

    @Override
    public Object getItem(final int position) {
        return dailyOffers.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_offer_item, parent, false);
        }

        TextView text = (TextView) view.findViewById(R.id.offer_text);
        ImageView image = (ImageView) view.findViewById(R.id.offer_image);


        text.setText(dailyOffers.get(position).getName());
        image.setImageURI(Uri.parse(dailyOffers.get(position).getPhoto()));

        return view;
    }
}