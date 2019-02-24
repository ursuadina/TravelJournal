package com.example.andreea.traveljournal;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class GalleryViewHolder extends RecyclerView.ViewHolder {

    public ImageView mImageViewPhoto;
    public TextView mTextViewTitle;
    public TextView mTextViewCountry;
    public TextView mTextViewPrice;
    public TextView mTextViewRating;
    public ImageButton mButtonFav;
    public CardView item_gallery;

    private FirebaseFirestore mFirestore;

    public GalleryViewHolder(@NonNull View itemView) {
        super(itemView);

        mFirestore = FirebaseFirestore.getInstance();

        mImageViewPhoto = itemView.findViewById(R.id.imageview_photo);
        mTextViewTitle = itemView.findViewById(R.id.textview_title);
        mTextViewCountry = itemView.findViewById(R.id.textview_country);
        mTextViewPrice = itemView.findViewById(R.id.textview_price);
        mTextViewRating = itemView.findViewById(R.id.textview_rating);
        mButtonFav = itemView.findViewById(R.id.add_fav);
        //mButtonFav.setTag(R.drawable.ic_bookmark_border_black_24dp);
        item_gallery = (CardView) itemView.findViewById(R.id.item_gallery);
//        mButtonFav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if ((Integer) mButtonFav.getTag() == R.drawable.ic_bookmark_border_black_24dp) {
//                    Toast.makeText(v.getContext(), mTextViewTitle.getText().toString() + " added to favourite", Toast.LENGTH_SHORT).show();
//                    mButtonFav.setImageResource(R.drawable.ic_bookmark_black_24dp);
//                    mButtonFav.setTag(R.drawable.ic_bookmark_black_24dp);
//
//                    CollectionReference dbTrips = mFirestore.collection("trips");
//                    Gallery gallery = new Gallery(imageUrl, title, country, price, rating, typeTrip, startDate, endDate);
//                    dbTrips.add(gallery)
//                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    Toast.makeText(getApplicationContext(), "Trip added", Toast.LENGTH_SHORT).show();
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    if ((Integer) mButtonFav.getTag() == R.drawable.ic_bookmark_black_24dp) {
//                        Toast.makeText(v.getContext(), mTextViewTitle.getText().toString() + " removed from favourite", Toast.LENGTH_SHORT).show();
//                        mButtonFav.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
//                        mButtonFav.setTag(R.drawable.ic_bookmark_border_black_24dp);
//                    }
//                }
//            }
//        });

    }
    public void bind(final DocumentSnapshot snapshot,
                     final GalleryAdapter.OnGallerySelectedListener listener) {

        Gallery gallery = snapshot.toObject(Gallery.class);
        Resources resources = itemView.getResources();

        // Load image
        Glide.with(mImageViewPhoto.getContext())
                .load(gallery.getmPhotoUrl())
                .into(mImageViewPhoto);

        mTextViewTitle.setText(gallery.getmTitle());
        mTextViewPrice.setText("" + gallery.getmPrice());
        mTextViewCountry.setText(gallery.getmCountry());
        mTextViewRating.setText("" + gallery.getmRating());
        // Click listener
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onGallerySelected(snapshot);
                }
            }
        });
    }

}
