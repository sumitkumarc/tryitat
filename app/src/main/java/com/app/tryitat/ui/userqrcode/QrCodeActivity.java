package com.app.tryitat.ui.userqrcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ActivityQrCodeBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.helper.SharedPref;
import com.app.tryitat.ui.clientsetting.ClientSettingActivity;
import com.app.tryitat.ui.setting.SettingActivity;
import com.google.zxing.Result;

import net.glxn.qrgen.android.QRCode;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeActivity extends AppCompatActivity  {
    private ActivityQrCodeBinding binding;
    private String userId;
    private SharedPref sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userId = getIntent().getStringExtra("uid");
        binding.name.setText(Constant.userData.getName());
        sharedPref = new SharedPref(this);

        initClickListener();
        generateQrCode();
    }

    private void initClickListener(){
        binding.backBtn.setOnClickListener(v->{
            finish();
        });

        binding.btnSetting.setOnClickListener(v->{
            if (sharedPref.getUserType().equalsIgnoreCase(Constant.userTypeClient)){
                startActivity(new Intent(this, ClientSettingActivity.class));
            } else {
                startActivity(new Intent(this, SettingActivity.class));
            }
        });

        binding.okBtn.setOnClickListener(v->{
            finish();
        });
    }

    private void generateQrCode(){
        Bitmap myBitmap = QRCode.from(userId).withSize(140, 140).bitmap();
        binding.qrImgView.setImageBitmap(myBitmap);
    }
}