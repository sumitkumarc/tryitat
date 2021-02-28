package com.app.tryitat.ui.clientqrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
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
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ClientQrCodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ActivityClientQrCodeScannerBinding binding;
    private ZXingScannerView mScannerView;
    private FirebaseDatabase database;
    private DatabaseReference clientRef;


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
                if (snapshot.exists()) {
                    ClientDataModel clientDataModel = snapshot.getValue(ClientDataModel.class);
                    rewardsImageList = clientDataModel.getFollowers();
                    if (rewardsImageList != null){
                        for (int i = 0; i < rewardsImageList.size(); i++){
                            if(!rewardsImageList.contains(data_qr)){
                                rewardsImageList.add(data_qr);
                            }
                        }
                    }else {
                        rewardsImageList = new ArrayList<>();
                        rewardsImageList.add(data_qr);
                    }

                    clientRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("customers").setValue(rewardsImageList);
                    Intent intent = new Intent(ClientQrCodeScannerActivity.this, ClientSettingActivity.class);
                    intent.putExtra("CLIENT_USER", 1);
                    intent.putExtra("CLIENT_USER_Followers", data_qr);
                    startActivity(intent);
                    finish();
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
}