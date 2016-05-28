package a15.group.lab4;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class OwnerActivityShowOffers extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser fUser;

    ArrayList<DailyOffer> dailyOffers;
    AdapterShowOffers adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                fUser = firebaseAuth.getCurrentUser();
                if (fUser == null) {
                    //go back to main activity if user is not logged in
                    Intent in = new Intent(OwnerActivityShowOffers.this, ActivityMain.class);
                    startActivity(in);
                } else {
                    populateView();
                }
            }
        };
    }

    private void populateView(){
        setContentView(R.layout.owner_activity_show_offers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //button to add offer
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onClickPopupOffer();
                startActivity(new Intent(OwnerActivityShowOffers.this, OwnerActivityAddOffer.class));
            }
        });


        //make grid view
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        //adapter = new AdapterShowOffers(dailyOffers);
        //gridView.setAdapter(adapter);
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


        //set listeners on elements of grid view
        /*
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(OwnerActivityShowOffers.this, ActivityDisplayOffer.class);
                intent.putExtra("index", position);
                startActivity(intent);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                onClickPopupOptions(parent, v, position);
                return true;
            }
        });

    }

    public void onClickPopupOptions(final AdapterView<?> parent, View v, final int position) {
        final CharSequence[] items = { "Delete", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityShowOffers.this);
        builder.setTitle("Options");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Delete")) {
                    dailyOffers.remove(position);
                    adapter.notifyDataSetChanged();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /*
    public void onClickPopupOffer() {
        final CharSequence[] items = { "Yes", "No" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityShowOffers.this);
        builder.setTitle("Do you want to add an offer_item ?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Yes")) {
                    startActivity(new Intent(ActivityShowOffers.this, ActivityAddOffer.class));
                } else if (items[item].equals("No")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    */

        //save alla data when switch activity


}
