package com.example.andreea.traveljournal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.List;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

    private List<Gallery> mGallery;
    private ImageButton btn;
    private Gallery currentGallery;

    private String imageUrl;
    private String title;
    private String country;
    private String typeTrip;
    private double price;
    private String startDate;
    private String endDate;
    private double rating;

    private FirebaseFirestore mFirestore;

    public GalleryAdapter(List<Gallery> mGallery) {
        this.mGallery = mGallery;
    }

    public interface OnGallerySelectedListener {

        void onGallerySelected(DocumentSnapshot restaurant);

    }

    private OnGallerySelectedListener mListener;

   /* public GalleryAdapter(Query query, OnGallerySelectedListener listener) {
        super(query);
        mListener = listener;
    }*/
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
        //galleryViewHolder.bind(getSnapshot(i), mListener);
        mFirestore = FirebaseFirestore.getInstance();

        currentGallery = mGallery.get(i);
        galleryViewHolder.mTextViewTitle.setText(currentGallery.getmTitle());
        galleryViewHolder.mTextViewCountry.setText(currentGallery.getmCountry());
        galleryViewHolder.mTextViewPrice.setText("" + currentGallery.getmPrice());
        galleryViewHolder.mTextViewRating.setText("" + currentGallery.getmRating());
        /*Context context = galleryViewHolder.mImageViewPhoto.getContext();
        int id = context.getResources().getIdentifier(currentGallery.getmPhotoUrl(), "drawable", context.getPackageName());
        galleryViewHolder.mImageViewPhoto.setImageResource(id);*/
        /*Glide.with(galleryViewHolder.mImageViewPhoto.getContext())
                .load(currentGallery.getmPhotoUrl())
                .into(galleryViewHolder.mImageViewPhoto);*/
        Picasso.get().load(currentGallery.getmPhotoUrl()).into(galleryViewHolder.mImageViewPhoto);

        imageUrl = currentGallery.getmPhotoUrl();
        title = currentGallery.getmTitle();
        country = currentGallery.getmCountry();
        typeTrip = currentGallery.getTypeTrip();
        price = currentGallery.getmPrice();
        startDate = currentGallery.getStartDate();
        endDate = currentGallery.getEndDate();
        rating = currentGallery.getmRating();

        btn = galleryViewHolder.mButtonFav;
        btn.setTag(R.drawable.ic_bookmark_border_black_24dp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Integer) btn.getTag() == R.drawable.ic_bookmark_border_black_24dp) {
                    Toast.makeText(v.getContext(), currentGallery.getmTitle() + " added to favourite", Toast.LENGTH_SHORT).show();
                    btn.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    btn.setTag(R.drawable.ic_bookmark_black_24dp);

                    CollectionReference dbTrips = mFirestore.collection("favourites");
                    Gallery gallery = new Gallery(imageUrl, title, country, price, rating, typeTrip, startDate, endDate);
                    dbTrips.add(gallery)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    //Toast.makeText(t.getContext(), "Trip added", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Toast.makeText(v.getContext(), "Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    if ((Integer) btn.getTag() == R.drawable.ic_bookmark_black_24dp) {
                        //Toast.makeText(v.getContext(), mTextViewTitle.getText().toString() + " removed from favourite", Toast.LENGTH_SHORT).show();
                        btn.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                        btn.setTag(R.drawable.ic_bookmark_border_black_24dp);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGallery.size();
    }
}

