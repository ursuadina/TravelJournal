package com.example.andreea.traveljournal;

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

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class GalleryViewHolder extends RecyclerView.ViewHolder {
    public ImageView mImageViewPhoto;
    public TextView mTextViewTitle;
    public TextView mTextViewCountry;
    public TextView mTextViewPrice;
    public TextView mTextViewRating;
    public ImageButton mButtonFav;
    public CardView item_gallery;
    public GalleryViewHolder(@NonNull View itemView) {
        super(itemView);

        mImageViewPhoto = itemView.findViewById(R.id.imageview_photo);
        mTextViewTitle = itemView.findViewById(R.id.textview_title);
        mTextViewCountry = itemView.findViewById(R.id.textview_country);
        mTextViewPrice = itemView.findViewById(R.id.textview_price);
        mTextViewRating = itemView.findViewById(R.id.textview_rating);
        mButtonFav = itemView.findViewById(R.id.add_fav);
        item_gallery = (CardView) itemView.findViewById(R.id.item_gallery);
        mButtonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), mTextViewTitle.getText().toString() + " added to favourite", Toast.LENGTH_SHORT).show();
                mButtonFav.setImageResource(R.drawable.ic_bookmark_black_24dp);
            }
        });
    }
}
