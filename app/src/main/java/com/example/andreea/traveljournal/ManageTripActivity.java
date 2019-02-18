package com.example.andreea.traveljournal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.Calendar;

public class ManageTripActivity extends AppCompatActivity {

    private DatePickerDialog mDatePicker;
    private Button mBtnGallery;
    private Button mBtnCamera;
    private Button mBtnSave;
    private ImageView mPhoto;

    private EditText mTitle;
    private EditText mCountry;
    private RadioGroup mRadioGroupType;
    private SeekBar mSeekBarPrice;
    private Button mBtnStartDate;
    private Button mBtnEndDate;
    private RatingBar mRatingBar;
    private Uri uriImage;
    private Bitmap bitmapImage;
    private boolean isImageUri;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PHOTO = 0;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_trip);

        mTitle = findViewById(R.id.edit_text_title);
        mCountry = findViewById(R.id.edit_text_country);
        mRadioGroupType = findViewById(R.id.radio_group_type);
        mSeekBarPrice = findViewById(R.id.seekBar);
        mBtnStartDate = findViewById(R.id.buttonStartDate);
        mBtnEndDate = findViewById(R.id.buttonEndDate);
        mRatingBar = findViewById(R.id.ratingBar);

        mBtnGallery = findViewById(R.id.btnGallery);
        mPhoto = findViewById(R.id.image_view_camera);
        mBtnCamera = findViewById(R.id.btnPicture);
        mBtnSave = findViewById(R.id.button_save);

        db = FirebaseFirestore.getInstance();
    }

    public void btnDatePickerOnClick(final View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        final Button btn = findViewById(view.getId());
        mDatePicker = new DatePickerDialog(ManageTripActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker viewD, int year, int month, int day) {
                        btn.setText(viewD.getDayOfMonth() + "/" + (viewD.getMonth() + 1) + "/" + viewD.getYear());
                    }
                }, year, month, day);
        mDatePicker.show();
    }

    public void btnSelectGalleryPhoto(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                Uri selectedImageUri = data.getData();
                final String path = getPathFromURI(selectedImageUri);
                if (path != null) {
                    File f = new File(path);
                    selectedImageUri = Uri.fromFile(f);
                }
                // Set the image in ImageView
                uriImage = selectedImageUri;
                isImageUri = true;
                mPhoto.setImageURI(selectedImageUri);
            }
            if (requestCode == TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                bitmapImage = bitmap;
                isImageUri = false;
                mPhoto.setImageBitmap(bitmap);
            }
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public void btnCameraOnClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    public void btnSave(View view) {
        String title = mTitle.getText().toString();
        String country = mCountry.getText().toString();

        int radioButtonID = mRadioGroupType.getCheckedRadioButtonId();
        View radioButton = mRadioGroupType.findViewById(radioButtonID);
        int idx = mRadioGroupType.indexOfChild(radioButton);
        RadioButton r = (RadioButton) mRadioGroupType.getChildAt(idx);
        String typeTrip = r.getText().toString();

        double price = mSeekBarPrice.getProgress();
        String startDate =  mBtnStartDate.getText().toString();
        String endDate = mBtnEndDate.getText().toString();
        double rating = mRatingBar.getRating();

        String imageUrl;
        if (isImageUri == true) {
            imageUrl = uriImage.toString();
        } else {
            imageUrl = bitmapImage.toString();
        }

        CollectionReference dbTrips = db.collection("trips");
        Gallery gallery = new Gallery(imageUrl, title, country, price, rating, typeTrip, startDate, endDate);
        dbTrips.add(gallery)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getApplicationContext(), "Trip added", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
