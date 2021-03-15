package com.app.tryitat.clientqrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ActivityClientQrCodeScannerBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.ui.clientprofile.model.ClientDataModel;
import com.app.tryitat.ui.clientsetting.ClientSettingActivity;
import com.app.tryitat.ui.locolbusinesssignup.LocolBusinessSignUpActivity;
import com.app.tryitat.ui.profile.model.UserDataModel;
import com.app.tryitat.ui.userqrcode.QrCodeActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ClientQrCodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ActivityClientQrCodeScannerBinding binding;
    private ZXingScannerView mScannerView;
    private FirebaseDatabase database;
    private DatabaseReference clientRef;
    List<UserPoints> universityList = new ArrayList<>();
    ClientDataModel clientDataModel;
    private List<String> rewardsImageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientQrCodeScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        clientRef = database.getReference("Clients");

        initClickListener();
        ActivityCompat.requestPermissions(ClientQrCodeScannerActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
    }

    private void initClickListener() {
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(final Result rawResult) {
        mScannerView.stopCamera();
        getUserInfoData(rawResult.getText());

        Log.d("MINAURL", ">>>>>" + rawResult.getText());
    }


    private void getUserInfoData(String data_qr) {
        database.getReference("Clients").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showProgress();
                if (snapshot.exists()) {
                    clientDataModel = snapshot.getValue(ClientDataModel.class);
                    rewardsImageList = clientDataModel.getCustomers();
                    if (rewardsImageList != null) {
                        for (int i = 0; i < rewardsImageList.size(); i++) {
                            if (!rewardsImageList.contains(data_qr)) {
                                rewardsImageList.add(data_qr);
                            }
                        }
                    } else {
                        rewardsImageList = new ArrayList<>();
                        rewardsImageList.add(data_qr);
                    }

                    clientRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("customers").setValue(rewardsImageList);
                    addUserPointsData(data_qr);

                    runOnUiThread(new Runnable() {
                        public void run() {

                            finish();
                            Toast.makeText(ClientQrCodeScannerActivity.this, "Qr code scan successfully..", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(ClientQrCodeScannerActivity.this, "Permission denied to camera", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    KProgressHUD progressHUD;

    private void showProgress() {
        progressHUD = KProgressHUD.create(getApplicationContext())
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

    public void addUserPointsData(String data_qr) {
        database.getReference("UserPoints").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    universityList.clear();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        UserPoints university = postSnapshot.getValue(UserPoints.class);
                        universityList.add(university);
                        // here you can access to name property like university.name

                    }
                    for (int i = 0; i < universityList.size(); i++) {
                        if (universityList.get(i).userID.equals(data_qr)) {
                            if (universityList.get(i).clientID.equals(FirebaseAuth.getInstance().getUid())) {
                                int total = 0;
                                int yourpoints = 0;
                                try {
                                    total = universityList.get(i).visits + 1;
                                    yourpoints = universityList.get(i).totalPoints + 5;
                                } catch (Exception e) {
                                    total = 5;
                                    yourpoints = 5;
                                }
                                UserPoints userPoints = new UserPoints();
                                userPoints.setClientID(universityList.get(i).clientID);
                                userPoints.setClientName(universityList.get(i).clientName);
                                userPoints.setClientPic(universityList.get(i).clientPic);
                                userPoints.setObjectId(universityList.get(i).objectId);
                                userPoints.setShowCamera("yes");
                                userPoints.setTotalPoints(yourpoints);
                                userPoints.setVisits(total);
                                userPoints.setUserID(data_qr);
                                universityList.set(i, userPoints);
                                FirebaseDatabase.getInstance().getReference("UserPoints").child(universityList.get(i).objectId).setValue(universityList);
                                dismissProgress();
                                onBackPressed();
                            } else {
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("clientID", clientDataModel.getObjectId());
                                hashMap.put("clientName", clientDataModel.getStoreName());
                                hashMap.put("clientPic", clientDataModel.getPicture());
                                hashMap.put("totalPoints", 5);
                                hashMap.put("visits", 1);
                                hashMap.put("userID", data_qr);
                                hashMap.put("showCamera", "yes");
                                sendPostDataToFirebase(hashMap);
                            }
                        } else {
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("clientID", clientDataModel.getObjectId());
                            hashMap.put("clientName", clientDataModel.getStoreName());
                            hashMap.put("clientPic", clientDataModel.getPicture());
                            hashMap.put("totalPoints", 5);
                            hashMap.put("visits", 1);
                            hashMap.put("userID", data_qr);
                            hashMap.put("showCamera", "yes");
                            sendPostDataToFirebase(hashMap);
                        }
                    }
                    Log.d(">>>>>", ">>>>>" + universityList.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendPostDataToFirebase(HashMap<String, Object> hashMap) {
        DatabaseReference postRef = database.getReference("UserPoints").push();
        hashMap.put("objectId", postRef.getKey());
        postRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dismissProgress();
                Log.d(">>>>>>>>", ">>>Done" + task.toString());
                // postRef.updateChildren(map);

            }
        });
        onBackPressed();

    }
}