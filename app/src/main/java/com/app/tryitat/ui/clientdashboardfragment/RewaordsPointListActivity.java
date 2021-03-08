package com.app.tryitat.ui.clientdashboardfragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ActivityNotificationListBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.ui.addpost.AddNewPostActivity;
import com.app.tryitat.ui.clientnotificationsend.NotificationListActivity;
import com.app.tryitat.ui.clientnotificationsend.RvNotCustomerListAdapter;
import com.app.tryitat.ui.clientprofile.model.ClientDataModel;
import com.app.tryitat.ui.dashboard.interfaces.PickerOptionListener;
import com.app.tryitat.ui.dashboard.user.UserDashboardActivity;
import com.app.tryitat.utils.Common;
import com.app.tryitat.utils.rv_interface;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.content.FileProvider.getUriForFile;
import static com.app.tryitat.utils.Common.getCompressed;

public class RewaordsPointListActivity extends AppCompatActivity implements PickerOptionListener, rv_interface {
    private ActivityNotificationListBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference clientRef;
    private HashMap<String, String> customerList = new HashMap<>();

    private List<String> rewardsImageList = new ArrayList<>();
    private List<String> rewardsPriceList = new ArrayList<>();
    HashMap<String, String> mStrings = new HashMap<String, String>();
    private String[] mKeys;
    rv_interface anInterface;
    String oldstr = "";
    ImageView iv_img;
    Uri photoURI;
    private FirebaseDatabase databaseReference;
    StorageReference storageReference;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        clientRef = database.getReference("Clients");
        anInterface = RewaordsPointListActivity.this;
        LinearLayoutManager rvFilterManager = new LinearLayoutManager(this);
        rvFilterManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(rvFilterManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        databaseReference = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        binding.title.setText("Edit Reward");
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
        progressHUD = KProgressHUD.create(RewaordsPointListActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.3f)
                .show();
    }

    public Object getItem(int position) {
        return mStrings.get(mKeys[position]);
    }

    private void dismissProgress() {
        if (progressHUD != null) {
            progressHUD.dismiss();
        }
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

    private void getUserInfoData() {
        showProgress();
//FirebaseAuth.getInstance().getUid()
        clientRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ClientDataModel clientDataModel = snapshot.getValue(ClientDataModel.class);
                    customerList= new HashMap<>();
                    customerList = clientDataModel.getRewards();

                    if (clientDataModel.getRewards() != null) {

                        mStrings = clientDataModel.getRewards();
                        mKeys = mStrings.keySet().toArray(new String[mStrings.size()]);
                        rewardsImageList= new ArrayList<>();
                        rewardsPriceList= new ArrayList<>();
                        for (int i = 0; i < mStrings.size(); i++) {
                            if (mKeys[i].toString().contains("image")) {

                                rewardsImageList.add(Common.isStrempty(getItem(i).toString()));
                            }
                            if (mKeys[i].toString().contains("price")) {

                                rewardsPriceList.add(Common.isStrempty(getItem(i).toString()));
                            }
                        }


                    }
                    try {
                        if (customerList.size() != 0) {
                            binding.txtNoData.setVisibility(View.GONE);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                            RvReWordsCustomerListAdapter adapter = new RvReWordsCustomerListAdapter(RewaordsPointListActivity.this, rewardsImageList, rewardsPriceList, true);
                            binding.recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(RewaordsPointListActivity.this::OnItemClick);
                        } else {
                            binding.recyclerView.setVisibility(View.GONE);
                            binding.txtNoData.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.txtNoData.setVisibility(View.VISIBLE);
                    }
                    dismissProgress();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
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
                                    Uri fileUri = FileProvider.getUriForFile(RewaordsPointListActivity.this, getPackageName()+".provider", photoFile);
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
//                    Log.e("SMD", "onActivityResult: " + getCacheImagePath(fileName));
                    String u_profile = null;
                    Log.d(">>>>>", "EROOR" + u_profile.toString());
                    File N_file = null;
                    try {
                        N_file = getCompressed(this, imageFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                     photoURI = FileProvider.getUriForFile(this, getPackageName()+".provider", N_file);

                    u_profile = N_file.getPath();
                    Glide.with(this).load(u_profile).placeholder(getResources().getDrawable(R.drawable.ic_profile)).into(iv_img);
                } else {
                    setResultCancelled();
                }
                break;
            case REQUEST_GALLERY_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    Uri selectedImageUri = data.getData();
                    String u_profile = getPath(selectedImageUri);
//                    Log.d(">>>>>", "EROOR" + u_profile.toString());
                    File N_file = null;
                    try {
                        N_file = getCompressed(RewaordsPointListActivity.this, u_profile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    u_profile = N_file.getPath();
                    File f = new File(u_profile);
                     photoURI = FileProvider.getUriForFile(this, getPackageName() + ".provider", f);
                    Glide.with(this).load(u_profile).placeholder(getResources().getDrawable(R.drawable.ic_profile)).into(iv_img);
                    Log.e("SMD", "onActivityResult: " + imageUri);
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
        return getUriForFile(RewaordsPointListActivity.this, getPackageName() + ".provider", image);
    }

    @Override
    public void onTakeCameraSelected() {
        takeCameraImage();
    }

    @Override
    public void onChooseGallerySelected() {
        chooseImageFromGallery();
    }

    @Override
    public void OnItemClick(String id, String item_id,int i) {
        oldstr = id;
        SowDialogBox(id, item_id,i);
    }

    private void SowDialogBox(String s, String s1,int i) {
        final Dialog dialogm = new Dialog(this);
        dialogm.setCancelable(true);
        dialogm.setContentView(R.layout.pop_add_amount);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogm.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialogm.getWindow().setAttributes(lp);
        dialogm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText ed_title = dialogm.findViewById(R.id.ed_title);
        CardView cv_img = dialogm.findViewById(R.id.cv_img);
        ed_title.setText(s);
        iv_img = dialogm.findViewById(R.id.iv_img);
        Glide.with(this).load(Common.isStrempty(s1)).placeholder(getResources().getDrawable(R.drawable.ic_profile)).into(iv_img);
        cv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerOptions(RewaordsPointListActivity.this, RewaordsPointListActivity.this);
            }
        });
        dialogm.findViewById(R.id.update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.edvalidateName(ed_title.getText().toString(), ed_title, "Enter Reward name")) {
                    return;
                }
                customerList.put("price"+ (i +1),(ed_title.getText().toString()));
                if (photoURI != null) {
                    customerList.put("image"+(i +1), String.valueOf(photoURI));
                    sendPhotoToTheServer();
                }else {
                    customerList.put("image"+(i +1), String.valueOf(s1));
                }
                new AsyncCaller().execute();
                dialogm.dismiss();
            }
        });

        dialogm.findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogm.dismiss();
            }
        });
        dialogm.show();
    }
    private void sendPhotoToTheServer() {


        if (photoURI != null) {

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
                                            photoURI = uri;


                                            // Got the download URL for 'path/to/aFile'
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                            photoURI = null;
                                        }
                                    });
                                    progressDialog.dismiss();
                                    Toast.makeText(RewaordsPointListActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Log.e("MAIN_URL", ">>>>>>>" + e.getMessage());
                            Toast.makeText(RewaordsPointListActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private class AsyncCaller extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
//
            runOnUiThread(new Runnable() {
                public void run() {
                    clientRef.child(FirebaseAuth.getInstance().getUid()).child("rewards").setValue(customerList);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

    }
}