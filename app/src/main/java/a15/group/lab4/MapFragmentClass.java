package a15.group.lab4;

import android.annotation.TargetApi;
import android.graphics.Camera;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by alicanturk on 08-Jun-16.
 */
public class MapFragmentClass extends Fragment {


    private GoogleMap googlemap;
    //private static final LatLng nor = new LatLng(63.422207, 10.418756);
    //private LatLng coordinates = new LatLng(36.4343435, 45.54354545);
    /*public void setCoords(LatLng coordinates)
    {
        this.coords = coordinates;
    }*/


    /*public void processMap(View v)
    {
        if(googlemap == null)
        {
            googlemap = ((MapFragment)getFragmentManager().findFragmentById(R.id.mapid)).getMap();
        }

        if(googlemap != null)
        {
            googlemap.addMarker(new MarkerOptions().position(nor).title("Marked area"));
            googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(nor,13));
        }
    }*/

    //problem at this part..



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View v = inflater.inflate(R.layout.map_fragment_layout, container, false);
        //processMap(v);
        return v;
    }
}
