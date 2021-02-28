package com.app.tryitat.ui.clientdashboardfragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.tryitat.R;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.ui.clientnotificationsend.NotificationListActivity;
import com.app.tryitat.ui.clientnotificationsend.RvNotCustomerListAdapter;
import com.app.tryitat.ui.clientprofile.model.ClientDataModel;
import com.app.tryitat.ui.dashboard.interfaces.PickerOptionListener;
import com.app.tryitat.ui.dashboard.user.UserDashboardActivity;
import com.app.tryitat.utils.Common;
import com.app.tryitat.utils.rv_interface;
import com.bumptech.glide.Glide;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.content.FileProvider.getUriForFile;

public class RewaordsPointListActivity extends AppCompatActivity implements PickerOptionListener, rv_interface {
    private com.app.tryitat.databinding.ActivityNotificationListBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference clientRef;
    private HashMap<String, String> customerList;

    private List<String> rewardsImageList = new ArrayList<>();
    private List<String> rewardsPriceList = new ArrayList<>();
    HashMap<String, String> mStrings = new HashMap<String, String>();
    private String[] mKeys;
    rv_interface anInterface;
    String oldstr = "";
    CircleImageView iv_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.app.tryitat.databinding.ActivityNotificationListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        clientRef = database.getReference("Clients");
        anInterface = RewaordsPointListActivity.this;
        LinearLayoutManager rvFilterManager = new LinearLayoutManager(this);
        rvFilterManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(rvFilterManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

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
//
        clientRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ClientDataModel clientDataModel = snapshot.getValue(ClientDataModel.class);
                    customerList = clientDataModel.getRewards();

                    if (clientDataModel.getRewards() != null) {

                        mStrings = clientDataModel.getRewards();
                        mKeys = mStrings.keySet().toArray(new String[mStrings.size()]);

                        for (int i = 0; i < mStrings.size(); i++) {
                            if(mKeys[i ].toString().contains("image")){
                                rewardsImageList.add(Common.isStrempty(getItem(i ).toString()));
                            }
                            if(mKeys[i].toString().contains("price")){
                                rewardsPriceList.add(Common.isStrempty(getItem(i).toString()));
                            }
                        }


                    }
                    if (customerList.size() != 0) {
                        binding.txtNoData.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        RvReWordsCustomerListAdapter adapter = new RvReWordsCustomerListAdapter(RewaordsPointListActivity.this, rewardsImageList,rewardsPriceList,true);
                        binding.recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(RewaordsPointListActivity.this::OnItemClick);
                    } else {
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
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                    Glide.with(this).load(getCacheImagePath(fileName)).placeholder(getResources().getDrawable(R.drawable.ic_profile)).into(iv_img);
                } else {
                    setResultCancelled();
                }
                break;
            case REQUEST_GALLERY_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    Glide.with(this).load(imageUri).placeholder(getResources().getDrawable(R.drawable.ic_profile)).into(iv_img);
                    Log.e("SMD", "onActivityResult: "+imageUri );
                } else {
                    setResultCancelled();
                }
                break;
            default:
                setResultCancelled();
        }
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
    public void OnItemClick(String id, String item_id) {
        oldstr = id;
        SowDialogBox(id,item_id);
    }
    private void SowDialogBox(String s, String s1) {
        final Dialog dialogm = new Dialog(this);
        dialogm.setCancelable(true);
        dialogm.setContentView(R.layout.pop_add_amount);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogm.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialogm.getWindow().setAttributes(lp);
        dialogm.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        EditText ed_title = dialogm.findViewById(R.id.ed_title);
        ed_title.setText(s);
        iv_img = dialogm.findViewById(R.id.iv_img);
        Glide.with(this).load(Common.isStrempty(s1)).placeholder(getResources().getDrawable(R.drawable.ic_profile)).into(iv_img);
        iv_img.setOnClickListener(new View.OnClickListener() {
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
}