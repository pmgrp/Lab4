package a15.group.lab4;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class OwnerActivityShowOffers extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase databse;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private DatabaseReference mRef;
    private FirebaseUser fUser;
    private FirebaseRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        databse = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://lab4-1318.appspot.com");



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                fUser = firebaseAuth.getCurrentUser();
                if (fUser == null) {
                    //go back to main activity if user is not logged in
                    Intent in = new Intent(OwnerActivityShowOffers.this, ActivityMain.class);
                    startActivity(in);
                }
            }
        };

        populateView();



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

        mRef = databse.getReference().child("restaurant-offers").child(mAuth.getCurrentUser().getUid());
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.offers_grid);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        mAdapter = new FirebaseRecyclerAdapter<DailyOffer, OwnerDishHolder>(
                DailyOffer.class, R.layout.owner_offer_item, OwnerDishHolder.class, mRef) {
            @Override
            public void populateViewHolder(final OwnerDishHolder viewHolder, DailyOffer offer, final int position) {
                viewHolder.setName(offer.getName());
                viewHolder.setImage(offer.getPhotoThumb());
                viewHolder.getOfferContainer().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(v.getContext(), OwnerActivityModifyOffer.class);
                        in.putExtra("offerID", mAdapter.getRef(position).getKey());
                        startActivity(in);
                    }
                });
                viewHolder.getOfferContainer().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onClickPopupOptions(v, mAdapter.getRef(position).getKey(), position);
                        return true;
                    }
                });
            }
        };

        recyclerView.setAdapter(mAdapter);


    }

    public void onClickPopupOptions(View v, final String offerId, final int position) {
        final CharSequence[] items = { "Delete", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(OwnerActivityShowOffers.this);
        builder.setTitle("Options");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Delete")) {
                    //remove this offer from database
                    mRef.child(offerId).removeValue();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("offers").child(offerId);
                    ref.removeValue();
                    storageRef.child(offerId).child("offer_photo.jpg").delete();
                    storageRef.child(offerId).child("offer_thumb.jpg").delete();
                    mAdapter.notifyItemRemoved(position);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
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

}
