package com.app.tryitat.ui.clientprofile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tryitat.R;
import com.app.tryitat.databinding.FragmentClientProfileBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.ui.addpost.AddNewPostActivity;
import com.app.tryitat.ui.clientdashboardfragment.RewaordsPointListActivity;
import com.app.tryitat.ui.clientdashboardfragment.RvReWordsCustomerListAdapter;
import com.app.tryitat.ui.clientfollowerlist.ClientFollowerListActivity;
import com.app.tryitat.ui.dashboard.interfaces.PickerOptionListener;
import com.app.tryitat.ui.dashboard.user.UserDashboardActivity;
import com.app.tryitat.ui.home.model.PostResponse;
import com.app.tryitat.ui.setting.SettingActivity;
import com.app.tryitat.utils.Common;
import com.bumptech.glide.Glide;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ClientProfileFragment extends Fragment implements PickerOptionListener {
    private FragmentClientProfileBinding binding;
    private List<PostResponse> postResponseList;
    private List<String> rewardsImageList = new ArrayList<>();
    private List<String> rewardsPriceList = new ArrayList<>();
    Map<String, String> mStrings = new TreeMap<>();
    private String[] mKeys;
    //    private List<String> rewardsPriceList = new ArrayList<>();
    public static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int REQUEST_GALLERY_IMAGE = 1;
    String imageFilePath;
    CircleImageView iv_img;

    public ClientProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClientProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initClickListener();
    }
    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible) {
            initClickListener();
        }
    }
    public Object getItem(int position) {
        return mStrings.get(mKeys[position]);
    }

    private void initClickListener() {
        LinearLayoutManager rvFilterManager = new LinearLayoutManager(getContext());
        rvFilterManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rewardsRcv.setLayoutManager(rvFilterManager);
        binding.rewardsRcv.setItemAnimator(new DefaultItemAnimator());


        if (Constant.clientDataModel.getStoreName() != null)
            binding.userName.setText(Constant.clientDataModel.getStoreName());
        if (Constant.clientDataModel.getPicture() != null)
            Glide.with(getContext()).load(Constant.clientDataModel.getPicture()).placeholder(R.drawable.ic_person).into(binding.userImage);
        if (Constant.clientDataModel.getCoverpicture() != null)
            Glide.with(getContext()).load(Constant.clientDataModel.getCoverpicture()).placeholder(R.drawable.ic_home_others).into(binding.coverImage);
        if (Constant.clientDataModel.getFollowers() != null)
            binding.followerTv.setText(String.valueOf(Constant.clientDataModel.getFollowers().size()));
        if (Constant.clientDataModel.getFollowing() != null)
            binding.followingTv.setText(String.valueOf(Constant.clientDataModel.getFollowing().size()));
        if (Constant.clientDataModel.getRewards() != null) {

            mStrings = Constant.clientDataModel.getRewards();
            mKeys = mStrings.keySet().toArray(new String[mStrings.size()]);

            for (int i = 0; i < mStrings.size(); i++) {
                if (mKeys[i].toString().contains("image")) {
                    rewardsImageList.add(Common.isStrempty(getItem(i).toString()));
                }
                if (mKeys[i].toString().contains("price")) {
                    rewardsPriceList.add(Common.isStrempty(getItem(i).toString()));
                }
            }
        }else {
            mStrings = new TreeMap<>();
            mStrings.put("image1", "image1");
            mStrings.put("image2", "image2");
            mStrings.put("image3", "image3");
            mStrings.put("image4", "image4");
            mStrings.put("image5", "image5");
            mStrings.put("image6", "image6");
            mStrings.put("price1", "5$");
            mStrings.put("price2", "10$");
            mStrings.put("price3", "15$");
            mStrings.put("price4", "20$");
            mStrings.put("price5", "25$");
            mStrings.put("price6", "30$");
            mKeys = mStrings.keySet().toArray(new String[mStrings.size()]);
            rewardsImageList = new ArrayList<>();
            rewardsPriceList = new ArrayList<>();
            for (int i = 0; i < mStrings.size(); i++) {
                if (mKeys[i].toString().contains("image")) {
                    rewardsImageList.add(Common.isStrempty(getItem(i).toString()));
                }
                if (mKeys[i].toString().contains("price")) {

                    rewardsPriceList.add(Common.isStrempty(getItem(i).toString()));
                }
            }
        }
        if (mStrings.size() != 0) {
            binding.rewardsRcv.setVisibility(View.VISIBLE);
            RvReWordsCustomerListAdapter rvReWordsCustomerListAdapter = new RvReWordsCustomerListAdapter(getContext(), rewardsImageList, rewardsPriceList, false);
            binding.rewardsRcv.setAdapter(rvReWordsCustomerListAdapter);
        } else {
            binding.rewardsRcv.setVisibility(View.GONE);
        }

        binding.loyaltyEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (Constant.clientDataModel.getRewards() != null) {
                    startActivity(new Intent(getActivity(), RewaordsPointListActivity.class));
//                } else {
//                    SowDialogBox();
//                }
            }
        });
        binding.settingBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SettingActivity.class));
        });

        binding.followeView.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ClientFollowerListActivity.class));
        });
    }
    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File cacheDir = getActivity().getExternalCacheDir();
        if (cacheDir == null)
            cacheDir = getActivity().getCacheDir();
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
    private void takeCameraImage() {
        Dexter.withContext(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent pictureIntent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            if(pictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
                                //Create a file to store the image
                                File photoFile = null;
                                try {
                                    photoFile = createImageFile();
                                } catch (IOException ex) {
                                }
                                if (photoFile != null) {
                                    Uri fileUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName()+".provider", photoFile);
                                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                            fileUri);
                                    startActivityForResult(pictureIntent,
                                            REQUEST_IMAGE_CAPTURE);
                                }
                            }


//                            fileName = System.currentTimeMillis() + ".jpg";
//                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName));
//                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void chooseImageFromGallery() {
        Dexter.withContext(getActivity())
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
    public void SowDialogBox() {
        final Dialog dialogm = new Dialog(getContext());
        dialogm.setCancelable(true);
        dialogm.setContentView(R.layout.pop_add_amount);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogm.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialogm.getWindow().setAttributes(lp);
        dialogm.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogm.getWindow().setAttributes(lp);
        dialogm.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        EditText ed_title = dialogm.findViewById(R.id.ed_title);
        iv_img = dialogm.findViewById(R.id.iv_img);
        iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  showImagePickerOptions(getContext(), (PickerOptionListener) getContext());
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Log.e("SMD", "onActivityResult: " + imageFilePath);
                    Glide.with(this).load(imageFilePath).placeholder(getResources().getDrawable(R.drawable.ic_profile)).into(iv_img);
                } else {
                    setResultCancelled();
                }
                break;
            case REQUEST_GALLERY_IMAGE:
                if (resultCode == RESULT_OK) {
                   Uri selectedImageUri = data.getData();
                    String u_profile = getPath(selectedImageUri);
                    File N_file = null;
                    try {
                        N_file = Common.getCompressed(getContext(), u_profile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    u_profile = N_file.getPath();
                    File f = new File(u_profile);
                    Uri photoURI = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", f);
                    Glide.with(this).load(u_profile).placeholder(getResources().getDrawable(R.drawable.ic_profile)).into(iv_img);
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
        Cursor cursor =getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


    private void setResultCancelled() {
        Intent intent = new Intent();
        getActivity().setResult(Activity.RESULT_CANCELED, intent);
        getActivity().finish();
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