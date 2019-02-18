package com.example.andreea.traveljournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView mRecycleViewGallery;
    private List<Gallery> mGallery;
    private FloatingActionButton mButtonAdd;

    FirebaseFirestore db;
    CollectionReference dbTrips;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_recycler_view, container, false);
        db = FirebaseFirestore.getInstance();
        dbTrips = db.collection("trips");

        mRecycleViewGallery = rootView.findViewById(R.id.recyclerview_gallery);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecycleViewGallery.setLayoutManager(layoutManager);

        mGallery = getGallery();

        GalleryAdapter galleryAdapter = new GalleryAdapter(mGallery);

        mRecycleViewGallery.setAdapter(galleryAdapter);
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

    /*private void loadMenu() {
        FirebaseRecyclerAdapter<Gallery, GalleryViewHolder> adapter = new FirebaseRecyclerAdapter<Gallery, GalleryViewHolder>(Gallery.class, R.layout.gallery_item, GalleryViewHolder.class, dbTrips) {
            @Override
            protected void populateViewHolder(GalleryViewHolder viewHolder, Gallery model, int position) {

            }
        }
    }*/
}
