package com.example.andreea.traveljournal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

    private List<Gallery> mGallery;

    public GalleryAdapter(List<Gallery> mGallery) {
        this.mGallery = mGallery;
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_item, viewGroup, false);
        final GalleryViewHolder viewHolder = new GalleryViewHolder(itemView);
        viewHolder.item_gallery.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), "Edit", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder galleryViewHolder, int i) {
        Gallery currentGallery = mGallery.get(i);
        galleryViewHolder.mTextViewTitle.setText(currentGallery.getmTitle());
        galleryViewHolder.mTextViewCountry.setText(currentGallery.getmCountry());
        galleryViewHolder.mTextViewPrice.setText("" + currentGallery.getmPrice());
        galleryViewHolder.mTextViewRating.setText("" + currentGallery.getmRating());
        Context context = galleryViewHolder.mImageViewPhoto.getContext();
        int id = context.getResources().getIdentifier(currentGallery.getmPhotoUrl(), "drawable", context.getPackageName());
        galleryViewHolder.mImageViewPhoto.setImageResource(id);
    }

    @Override
    public int getItemCount() {
        return mGallery.size();
    }
}

