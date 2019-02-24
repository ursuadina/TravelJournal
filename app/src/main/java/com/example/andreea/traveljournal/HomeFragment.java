package com.example.andreea.traveljournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class HomeFragment extends Fragment implements GalleryAdapter.OnGallerySelectedListener {
    private RecyclerView mRecycleViewGallery;
    private List<Gallery> mGallery;
    private FloatingActionButton mButtonAdd;

    private FirebaseFirestore mFirestore;
    private Query mQuery;

    private View rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.activity_recycler_view, container, false);
        //db = FirebaseFirestore.getInstance();
        //dbTrips = db.collection("trips");
        initFirestore();

        mRecycleViewGallery = rootView.findViewById(R.id.recyclerview_gallery);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecycleViewGallery.setLayoutManager(layoutManager);

        //mGallery = getGallery();
        mGallery = new ArrayList<>();
        final GalleryAdapter galleryAdapter = new GalleryAdapter(mGallery);

        mRecycleViewGallery.setAdapter(galleryAdapter);
         mFirestore.collection("trips").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e!= null) {
                    Log.d(TAG, "error: " + e.getMessage());
                }
                for (DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()) {
                    if(doc.getType() == DocumentChange.Type.ADDED) {
                        Gallery galleries = doc.getDocument().toObject(Gallery.class);
                        mGallery.add(galleries);

                        galleryAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

       /* GalleryAdapter galleryAdapter = new GalleryAdapter(mQuery, this){
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mRecycleViewGallery.setVisibility(View.GONE);
                    //mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mRecycleViewGallery.setVisibility(View.VISIBLE);
                    //mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Toast.makeText(getContext(),
                        "Error: check logs for info.", Toast.LENGTH_LONG).show();
            }
        };*/
        mButtonAdd = rootView.findViewById(R.id.fab);
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManageTripActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
    public List<Gallery> getGallery() {
        //String mPhotoUrl, String mTitle, String mCountry, double mPrice, double mRating, String typeTrip, String startDate, String endDate
        List<Gallery> galleries = new ArrayList<>();
        galleries.add(new Gallery("islands", "Holiday 2017", "Islands", 130, 4, "City break", "13/04/2018", "15/04/2018"));
        galleries.add(new Gallery("rome", "Fall 2017","Rome", 250, 5,"City break", "13/04/2018", "15/04/2018"));
        galleries.add(new Gallery("london","Summer 2017","London",300,3,"City break", "13/04/2018", "15/04/2018"));
        galleries.add(new Gallery("paris","Winter 2017","Paris",100,2,"City break", "13/04/2018", "15/04/2018"));
        galleries.add(new Gallery("san_francisco","Spring 2018"," San Francisco",250.5,4,"City break", "13/04/2018", "15/04/2018"));
        galleries.add(new Gallery("greece","Summer 2018","Greece",234,3,"City break", "13/04/2018", "15/04/2018"));
        return galleries;
    }

    private void initFirestore() {

        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection("trips")
                .orderBy("startDate", Query.Direction.ASCENDING);
    }

    @Override
    public void onGallerySelected(DocumentSnapshot gallery) {
        Toast.makeText(getContext(), "Item selected", Toast.LENGTH_SHORT).show();
    }
    /*private void loadMenu() {
        FirebaseRecyclerAdapter<Gallery, GalleryViewHolder> adapter = new FirebaseRecyclerAdapter<Gallery, GalleryViewHolder>(Gallery.class, R.layout.gallery_item, GalleryViewHolder.class, dbTrips) {
            @Override
            protected void populateViewHolder(GalleryViewHolder viewHolder, Gallery model, int position) {

            }
        }
    }*/
}
