package com.example.andreea.traveljournal;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    private FirebaseFirestore mFirestore;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri file;
    private Uri downloadUri;
    private UploadTask uploadTask;


    private boolean enterFirstOnSucces;
    private boolean enterSecondOnSuccess;
    private String downloadUri2;
    private String mCurrentPhotoPath;

    private String imageUrl;
    private String title;
    private String country;
    private String typeTrip;
    private double price;
    private String startDate;
    private String endDate;
    private double rating;

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

        mFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            mBtnCamera.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
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
                /*uriImage = file;
                mPhoto.setImageURI(file);*/
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                mPhoto.setImageBitmap(bitmap);
                /*Uri imageUri = Uri.parse(mCurrentPhotoPath);
                File file1 = new File(imageUri.getPath());
                mPhoto.setImageUri(imageUri);*/
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

    private File getOutputMediaFile(){
        File mediaStorageDir = new File(getFilesDir(), "poze app");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }

    public void btnCameraOnClick(View view) {

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            mBtnCamera.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        } else {*/

       /* StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());*/

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            /*file = Uri.fromFile(getOutputMediaFile());

            intent.putExtra(MediaStore.EXTRA_OUTPUT, file);*/

            startActivityForResult(intent, TAKE_PHOTO);

       // }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                mBtnCamera.setEnabled(true);
            }
        }
    }

    public void btnSave(View view) {
        title = mTitle.getText().toString();
        country = mCountry.getText().toString();

        int radioButtonID = mRadioGroupType.getCheckedRadioButtonId();
        View radioButton = mRadioGroupType.findViewById(radioButtonID);
        int idx = mRadioGroupType.indexOfChild(radioButton);
        RadioButton r = (RadioButton) mRadioGroupType.getChildAt(idx);
        typeTrip = r.getText().toString();

        price = mSeekBarPrice.getProgress();
        startDate =  mBtnStartDate.getText().toString();
        endDate = mBtnEndDate.getText().toString();
        rating = mRatingBar.getRating();

//        if (isImageUri == true) {
//            imageUrl = uriImage.toString();
//        } else {
//            imageUrl = bitmapImage.toString();
//        }
        storageRef = storage.getReference();
        final String imageName = title + country +  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
        //StorageReference imageRef = storageRef.child(imageName);
        final StorageReference imageRef = storageRef.child("images/" + imageName );

        imageRef.putFile(uriImage)
              .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      enterFirstOnSucces = true;
                      imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                          @Override
                          public void onSuccess(Uri uri) {
                              //enterSecondOnSuccess = true;
                              downloadUri = uri;
                              imageUrl = downloadUri.toString();
                              CollectionReference dbTrips = mFirestore.collection("trips");
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
                              /*final String downloadUrl = uri.toString();
                              downloadUri2 = downloadUrl;*/
                          }
                      });
                  }
              });
// Register observers to listen for when the download is done or if it fails
        /*uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "Imaginea nu s-a incarcat in storage", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final StorageReference ref = storageRef.child("images/" + uriImage.getLastPathSegment());
                uploadTask = ref.putFile(file);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadUri = task.getResult();
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });

            }
        });*/
        /*imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                enterFirstOnSucces = true;
                downloadUri = uri;
            }
        });*/
        //imageUrl = imageRef.getDownloadUrl().toString();
//        imageUrl = getDownloadUri(imageName, imageRef);//downloadUri.toString();
//        CollectionReference dbTrips = mFirestore.collection("trips");
//        //imageUrl = imageRef.getDownloadUrl().toString();
//        Gallery gallery = new Gallery(imageUrl, title, country, price, rating, typeTrip, startDate, endDate);
//        dbTrips.add(gallery)
//            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                @Override
//                public void onSuccess(DocumentReference documentReference) {
//                    Toast.makeText(getApplicationContext(), "Trip added", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    private String getDownloadUri(String imageName, StorageReference imageRef) {
        final String[] downUri = new String[1];
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //enterSecondOnSuccess = true;
                downUri[0] = uri.toString();
                              /*final String downloadUrl = uri.toString();
                              downloadUri2 = downloadUrl;*/
            }
        });
        return downUri[0];
    }
}
