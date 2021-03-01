package com.app.tryitat.ui.dashboard.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;

import com.app.tryitat.helper.Constant;
import com.app.tryitat.ui.addpost.AddNewPostActivity;
import com.app.tryitat.ui.dashboard.catchoose.ProductCatChoseActivity;
import com.app.tryitat.ui.dashboard.interfaces.PickerOptionListener;
import com.app.tryitat.ui.profile.model.UserDataModel;
import com.app.tryitat.utils.Common;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.app.tryitat.R;
import com.app.tryitat.databinding.ActivityUserDashboardBinding;
import com.app.tryitat.ui.camera.CameraFragment;
import com.app.tryitat.ui.categories.CategoriesFragment;
import com.app.tryitat.ui.home.HomeFragment;
import com.app.tryitat.ui.notification.NotificationFragment;
import com.app.tryitat.ui.profile.ProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static androidx.core.content.FileProvider.getUriForFile;

public class UserDashboardActivity extends AppCompatActivity implements PickerOptionListener {
    private ActivityUserDashboardBinding binding;
    private FirebaseDatabase database;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        fragment = new HomeFragment();
        checkAndRequestPermissions(UserDashboardActivity.this);
        initClickListeners();
        binding.menuHome.performClick();
        getUserInfoData();
    }

    private void initClickListeners() {
        binding.menuHome.setOnClickListener(v->{
            fragment = new HomeFragment();
            loadFragment(fragment);
            binding.menuHome.setBackgroundColor(getResources().getColor(R.color.tab_select));
            binding.menuCategories.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuCamera.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuProfile.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuNotification.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.imageM1.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM2.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM3.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM4.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM5.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.menuM1Txt.setTextColor(getResources().getColor(R.color.white));
            binding.menuM2Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM3Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM4Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM5Txt.setTextColor(getResources().getColor(R.color.ash_500));
        });

        binding.menuCategories.setOnClickListener(v->{
//            fragment = new CategoriesFragment();
//            loadFragment(fragment);
            startActivity(new Intent(this, ProductCatChoseActivity.class));
//            binding.menuHome.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
//            binding.menuCategories.setBackgroundColor(getResources().getColor(R.color.tab_select));
//            binding.menuCamera.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
//            binding.menuProfile.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
//            binding.menuNotification.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
//            binding.imageM1.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
//            binding.imageM2.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
//            binding.imageM3.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
//            binding.imageM4.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
//            binding.imageM5.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
//            binding.menuM1Txt.setTextColor(getResources().getColor(R.color.ash_500));
//            binding.menuM2Txt.setTextColor(getResources().getColor(R.color.white));
//            binding.menuM3Txt.setTextColor(getResources().getColor(R.color.ash_500));
//            binding.menuM4Txt.setTextColor(getResources().getColor(R.color.ash_500));
//            binding.menuM5Txt.setTextColor(getResources().getColor(R.color.ash_500));
        });

        binding.menuCamera.setOnClickListener(v->{
            showImagePickerOptions(this, this);
//            fragment = new CameraFragment();
//            loadFragment(fragment);
//            binding.menuHome.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
//            binding.menuCategories.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
//            binding.menuCamera.setBackgroundColor(getResources().getColor(R.color.tab_select));
//            binding.menuProfile.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
//            binding.menuNotification.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
//            binding.imageM1.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
//            binding.imageM2.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
//            binding.imageM3.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
//            binding.imageM4.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
//            binding.imageM5.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
//            binding.menuM1Txt.setTextColor(getResources().getColor(R.color.ash_500));
//            binding.menuM2Txt.setTextColor(getResources().getColor(R.color.ash_500));
//            binding.menuM3Txt.setTextColor(getResources().getColor(R.color.white));
//            binding.menuM4Txt.setTextColor(getResources().getColor(R.color.ash_500));
//            binding.menuM5Txt.setTextColor(getResources().getColor(R.color.ash_500));
        });

        binding.menuProfile.setOnClickListener(v->{
            fragment = new ProfileFragment();
            loadFragment(fragment);
            binding.menuHome.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuCategories.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuCamera.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuProfile.setBackgroundColor(getResources().getColor(R.color.tab_select));
            binding.menuNotification.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.imageM1.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM2.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM3.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM4.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM5.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.menuM1Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM2Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM3Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM4Txt.setTextColor(getResources().getColor(R.color.white));
            binding.menuM5Txt.setTextColor(getResources().getColor(R.color.ash_500));
        });

        binding.menuNotification.setOnClickListener(v->{
            fragment = new NotificationFragment();
            loadFragment(fragment);
            binding.menuHome.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuCategories.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuCamera.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuProfile.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuNotification.setBackgroundColor(getResources().getColor(R.color.tab_select));
            binding.imageM1.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM2.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM3.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM4.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM5.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.menuM1Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM2Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM3Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM4Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM5Txt.setTextColor(getResources().getColor(R.color.white));
        });
    }

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {


//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment fragment;
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    fragment = new HomeFragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_categories:
//                    fragment = new CategoriesFragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_camera:
//                    fragment = new CameraFragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_profile:
//                    fragment = new ProfileFragment();
//                    loadFragment(fragment);
//                    return true;
//
//                case R.id.navigation_notification:
//                    fragment = new NotificationFragment();
//                    loadFragment(fragment);
//                    return true;
//            }
//
//            return false;
//        }
//    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getUserInfoData(){
        database.getReference("User").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int points = 0;
                    String name = snapshot.child("name").getValue(String.class);
                    String picture = snapshot.child("picture").getValue(String.class);
                    String coverpicture = snapshot.child("coverpicture").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String fcmToken = snapshot.child("fcmToken").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);
                    String mobilePhone = snapshot.child("mobilePhone").getValue(String.class);
                    if (snapshot.child("points").getValue(Integer.class)!=null)
                        points = snapshot.child("points").getValue(Integer.class);
                    String userId = snapshot.child("objectId").getValue(String.class);
                    Constant.userData = new UserDataModel(userId, name, picture, coverpicture, email, fcmToken, gender, mobilePhone, points);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_GALLERY_IMAGE = 1;
    public static String fileName;
    public static void showImagePickerOptions(Context context, PickerOptionListener listener) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose any one: ");

        // add a list
        String[] animals = {context.getString(R.string.lbl_take_camera_picture), context.getString(R.string.lbl_choose_from_gallery)};
        builder.setItems(animals, (dialog, which) -> {
            switch (which) {
                case 0:
                    listener.onTakeCameraSelected();
                    break;
                case 1:
                    listener.onChooseGallerySelected();
                    break;
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void takeCameraImage() {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            fileName = System.currentTimeMillis() + ".jpg";
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName));
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void chooseImageFromGallery() {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Log.e("SMD", "onActivityResult: " + getCacheImagePath(fileName) );
                    Intent intent= new Intent(UserDashboardActivity.this, AddNewPostActivity.class);
                    intent.putExtra("img_url", getCacheImagePath(fileName));
                    startActivity(intent);
                } else {
                    setResultCancelled();
                }
                break;
            case REQUEST_GALLERY_IMAGE:
                if (resultCode == RESULT_OK) {
//                    Uri imageUri = data.getData();
                    Uri selectedImageUri = data.getData();
                    String u_profile = getPath(selectedImageUri);
                    Log.d(">>>>>", "EROOR" + u_profile.toString());
//                    File N_file = null;
//                    try {
//                        N_file = Common.getCompressed(UserDashboardActivity.this, u_profile);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    u_profile =N_file.getPath();
                    Intent intent= new Intent(UserDashboardActivity.this, AddNewPostActivity.class);
                    intent.putExtra("img_url",u_profile);
                    startActivity(intent);
                    Log.e("SMD", "onActivityResult: "+u_profile );
                } else {
                    setResultCancelled();
                }
                break;
            default:
                setResultCancelled();
        }
    }
    String res;
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
    private void setResultCancelled() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    private Uri getCacheImagePath(String fileName) {
        File path = new File(getExternalCacheDir(), "camera");
        if (!path.exists()) path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(UserDashboardActivity.this, getPackageName() + ".provider", image);
    }

    @Override
    public void onTakeCameraSelected() {
        takeCameraImage();
    }

    @Override
    public void onChooseGallerySelected() {
        chooseImageFromGallery();
    }

    public static boolean checkAndRequestPermissions(Context context) {

        int locationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 2);
            return false;
        }
        return true;
    }
}