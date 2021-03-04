package com.app.tryitat.ui.addpost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ActivityAddNewPostBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.ui.clientnotificationsend.NotificationSendActivity;
import com.bumptech.glide.Glide;
import com.app.tryitat.utils.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AddNewPostActivity extends AppCompatActivity {
    private ActivityAddNewPostBinding binding;
    private String imageUrl;
    private FirebaseDatabase databaseReference;
    Uri photoURI;
    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNewPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imageUrl = getIntent().getStringExtra("img_url");
        File f = new File(imageUrl);
        photoURI = FileProvider.getUriForFile(this, getPackageName() + ".provider", f);
        Glide.with(getApplicationContext()).load(imageUrl).into(binding.pImage);
        databaseReference = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
//        uploadMethod();
        getUserPostData();

        initClickListener();
    }

    private void initClickListener() {
        binding.shareBtn.setOnClickListener(v -> {
            getUserPostData();
        });
    }

    private float rating1 = 0.0f;
    private float rating2 = 0.0f;
    private String category = "";
    private String photoUrl = "";
    String reviewDesc;
    String location = "";
    String subTitle = "";
    String title = "";

    private void getUserPostData() {
        title = binding.pTitle.getText().toString();
        subTitle = binding.pCompany.getText().toString();
        reviewDesc = binding.pReviewDetails.getText().toString();
        location = "";

        binding.ratingBar1.setOnClickListener(v -> {
            rating1 = binding.ratingBar1.getRating();
        });

        binding.ratingBar2.setOnClickListener(v -> {
            rating2 = binding.ratingBar2.getRating();
        });

        binding.fashionCat.setOnClickListener(v -> {
            binding.fashionCat.setBackground(getResources().getDrawable(R.drawable.yellow_circle_bg));
            binding.fashionCat.setTextColor(getResources().getColor(R.color.white));
            binding.foodDrinkCat.setBackground(null);
            binding.foodDrinkCat.setTextColor(getResources().getColor(R.color.black));
            binding.homeOtherCat.setBackground(null);
            binding.homeOtherCat.setTextColor(getResources().getColor(R.color.black));
            category = binding.fashionCat.getText().toString();

        });

        binding.foodDrinkCat.setOnClickListener(v -> {
            binding.foodDrinkCat.setBackground(getDrawable(R.drawable.yellow_circle_bg));
            binding.foodDrinkCat.setTextColor(getResources().getColor(R.color.white));
            binding.fashionCat.setBackground(null);
            binding.fashionCat.setTextColor(getResources().getColor(R.color.black));
            binding.homeOtherCat.setBackground(null);
            binding.homeOtherCat.setTextColor(getResources().getColor(R.color.black));
            category = binding.foodDrinkCat.getText().toString();
        });

        binding.homeOtherCat.setOnClickListener(v -> {
            binding.homeOtherCat.setBackground(getDrawable(R.drawable.yellow_circle_bg));
            binding.homeOtherCat.setTextColor(getResources().getColor(R.color.white));
            binding.fashionCat.setBackground(null);
            binding.fashionCat.setTextColor(getResources().getColor(R.color.black));
            binding.foodDrinkCat.setBackground(null);
            binding.foodDrinkCat.setTextColor(getResources().getColor(R.color.black));
            category = binding.homeOtherCat.getText().toString();
        });

        float ratingOverall = (rating1 + rating2) / 2;

        if (title.isEmpty()) {
            binding.pTitle.setError("Required");
            return;
        }

        if (subTitle.isEmpty()) {
            binding.pCompany.setError("Required");
            return;
        }

//        if (ratingOverall<=0){
//            Toast.makeText(this, "Please rate it", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (category.equals("")) {
            Toast.makeText(this, "Select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        sendPhotoToTheServer();

    }

    String finalimageurl = "";

    // UploadImage method
    private String sendPhotoToTheServer() {


        if (imageUrl != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();


            // Defining the child of storageReference
            StorageReference ref = storageReference.child(Constant.userData.getUserId() + "/post/");

            // adding listeners on upload
            // or failure of image
            ref.putFile(photoURI)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            Log.d(">>>>>>>>", ">>>URI" + uri);
                                            finalimageurl = uri.toString();

                                            new AsyncCaller().execute();
                                            // Got the download URL for 'path/to/aFile'
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                            finalimageurl = "";
                                        }
                                    });
                                    progressDialog.dismiss();
                                    Toast.makeText(AddNewPostActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            finalimageurl = "";
                            Log.e("MAIN_URL", ">>>>>>>" + e.getMessage());
                            Toast.makeText(AddNewPostActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        return finalimageurl;
    }

    private void sendPostDataToFirebase(HashMap<String, Object> hashMap) {
        int total = 0;
        try {
            total = Constant.userData.getPoints() + 5;
        } catch (Exception e) {
            total = 5;
        }
        databaseReference.getReference("User").child(Constant.userData.getUserId()).child("points").setValue(total);


        DatabaseReference postRef = databaseReference.getReference("Post").push();
        hashMap.put("objectId", postRef.getKey());
        postRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Map map = new HashMap();
                map.put("createdAt", ServerValue.TIMESTAMP);
                map.put("updatedAt", ServerValue.TIMESTAMP);
                Log.d(">>>>>>>>", ">>>Done" + task.toString());
                postRef.updateChildren(map);

            }
        });
        onBackPressed();

    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("Address", "");
            hashMap.put("Comments", 0);
            hashMap.put("Inyourmind", Common.isStrempty(reviewDesc));
            hashMap.put("Likes", 0);
            hashMap.put("Latitude", 0);
            hashMap.put("Longitude", 0);
            hashMap.put("Name", Common.isStrempty(title));
            hashMap.put("Photo", Common.isStrempty(finalimageurl));
            hashMap.put("Place", "");
            hashMap.put("UserID", Constant.userData.getUserId());
            hashMap.put("UserName", Constant.userData.getName());
            hashMap.put("category", Common.isStrempty(category));
            hashMap.put("rating", "1");
            runOnUiThread(new Runnable() {
                public void run() {
                    sendPostDataToFirebase(hashMap);
                }
            });

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

    }

}