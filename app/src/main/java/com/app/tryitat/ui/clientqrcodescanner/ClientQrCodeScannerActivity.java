package com.app.tryitat.ui.clientqrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ActivityClientQrCodeScannerBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.ui.locolbusinesssignup.LocolBusinessSignUpActivity;
import com.app.tryitat.ui.profile.model.UserDataModel;
import com.app.tryitat.ui.userqrcode.QrCodeActivity;
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
        database = FirebaseDatabase.getInstance();
        clientRef = database.getReference("Clients");
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        initClickListener();
        ActivityCompat.requestPermissions(ClientQrCodeScannerActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);

        getUserInfoData("3RAfDQsTRPRrY7JVxZcDLaCDOwn1");
    }

    private void initClickListener() {
        binding.backBtn.setOnClickListener(v->{
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

       // getUserInfoData(rawResult.getText());

        Log.d("MINAURL",">>>>>" + rawResult.getText());
    }

    private void getUserInfoData(String data_qr){
        database.getReference("User").child(Objects.requireNonNull(data_qr)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    rewardsImageList.add("3RAfDQsTRPRrY7JVxZcDLaCDOwn1");
                    Toast.makeText(ClientQrCodeScannerActivity.this, ""+ snapshot.child("name").getValue(String.class) +"\n"+
                            snapshot.child("email").getValue(String.class), Toast.LENGTH_SHORT).show();
                    updateUserData();
//                    int points = 0;
//                    String name = snapshot.child("name").getValue(String.class);
//                    String picture = snapshot.child("picture").getValue(String.class);
//                    String coverpicture = snapshot.child("coverpicture").getValue(String.class);
//                    String email = snapshot.child("email").getValue(String.class);
//                    String fcmToken = snapshot.child("fcmToken").getValue(String.class);
//                    String gender = snapshot.child("gender").getValue(String.class);
//                    String mobilePhone = snapshot.child("mobilePhone").getValue(String.class);
//                    if (snapshot.child("points").getValue(Integer.class)!=null)
//                        points = snapshot.child("points").getValue(Integer.class);
//                    String userId = snapshot.child("objectId").getValue(String.class);
//                    Constant.userData = new UserDataModel(userId, name, picture, coverpicture, email, fcmToken, gender, mobilePhone, points);
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
    private void updateUserData() {
        clientRef.child(Objects.requireNonNull("0voiJWGXdgYhHmAKlJSvofFtujt2")).child("qr_code").setValue(rewardsImageList);

    }
}