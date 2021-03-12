package com.app.tryitat.ui.pointlist;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ActivityNotificationListBinding;
import com.app.tryitat.ui.UserCropActivity;
import com.app.tryitat.ui.clientnotificationsend.NotificationListActivity;
import com.app.tryitat.ui.clientnotificationsend.RvNotCustomerListAdapter;
import com.app.tryitat.ui.clientprofile.model.ClientDataModel;
import com.app.tryitat.ui.dashboard.interfaces.PickerOptionListener;
import com.app.tryitat.ui.dashboard.user.UserDashboardActivity;
import com.app.tryitat.ui.home.model.PostResponse;
import com.app.tryitat.utils.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.app.tryitat.utils.Common.getCompressed;

public class PointListActivity extends AppCompatActivity implements PickerOptionListener {

    private ActivityNotificationListBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference clientRef;
    private List<PointModel> UserPoints = new ArrayList<>();
    RvPointsCustomerListAdapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        clientRef = database.getReference("UserPoints");

        LinearLayoutManager rvFilterManager = new LinearLayoutManager(this);
        rvFilterManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(rvFilterManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        binding.title.setText("Your Points");

        initClickListener();
        getUserInfoData();
    }

    private void initClickListener() {
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    KProgressHUD progressHUD;

    private void showProgress() {
        progressHUD = KProgressHUD.create(PointListActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.3f)
                .show();
    }

    private void dismissProgress() {
        if (progressHUD != null) {
            progressHUD.dismiss();
        }
    }

    private void getUserInfoData() {
        showProgress();
        clientRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        PointModel post = dataSnapshot.getValue(PointModel.class);
                        assert post != null;
                        if (post.getUserID().equals("9paY3iPA7thRkvz1GUO1PVfVVN82")) {
                            UserPoints.add(post);
                        }
                    }
                    if (UserPoints != null) {
                        if (UserPoints.size() > 0) {
                            binding.txtNoData.setVisibility(View.GONE);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                            madapter = new RvPointsCustomerListAdapter(PointListActivity.this, UserPoints);
                            binding.recyclerView.setAdapter(madapter);
                        } else {
                            binding.recyclerView.setVisibility(View.GONE);
                            binding.txtNoData.setVisibility(View.VISIBLE);
                        }
                        dismissProgress();
                    } else {
                        dismissProgress();
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.txtNoData.setVisibility(View.VISIBLE);
                    }
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
                            Intent pictureIntent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            if(pictureIntent.resolveActivity(getPackageManager()) != null){
                                //Create a file to store the image
                                File photoFile = null;
                                try {
                                    photoFile = createImageFile();
                                } catch (IOException ex) {
                                }
                                if (photoFile != null) {
                                    Uri fileUri = FileProvider.getUriForFile(PointListActivity.this, getPackageName()+".provider", photoFile);
                                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                            fileUri);
                                    startActivityForResult(pictureIntent,
                                            REQUEST_IMAGE_CAPTURE);
                                }
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
    String imageFilePath;
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File cacheDir = getExternalCacheDir();
        if (cacheDir == null)
            cacheDir = getCacheDir();
        String rootDir = cacheDir.getAbsolutePath() + "/ImageCompressor";
        File root = new File(rootDir);
        if (!root.exists())
            root.mkdirs();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                root      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    String u_profile =null;
//                    Log.d(">>>>>", "EROOR" + u_profile.toString());
                    File N_file = null;
                    try {
                        N_file = getCompressed(this, imageFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    u_profile =N_file.getPath();
                    Intent intent= new Intent(PointListActivity.this, UserCropActivity.class);
                    intent.putExtra("img_url", u_profile);
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
//                    Log.d(">>>>>", "EROOR" + u_profile.toString());
                    File N_file = null;
                    try {
                        N_file = Common.getCompressed(PointListActivity.this, u_profile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    u_profile =N_file.getPath();
                    Intent intent= new Intent(PointListActivity.this, UserCropActivity.class);
                    intent.putExtra("img_url", u_profile);
                    startActivity(intent);
//                    Intent intent= new Intent(UserDashboardActivity.this, AddNewPostActivity.class);
//                    intent.putExtra("img_url",u_profile);
//                    startActivity(intent);
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
    @Override
    public void onTakeCameraSelected() {
        takeCameraImage();
    }

    @Override
    public void onChooseGallerySelected() {
        chooseImageFromGallery();
    }

}
