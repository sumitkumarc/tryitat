package com.app.tryitat.ui.clientqrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.tryitat.databinding.ActivityClientQrCodeScannerBinding;

public class ClientQrCodeScannerActivity extends AppCompatActivity {
    private ActivityClientQrCodeScannerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientQrCodeScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initClickListener();

    }

    private void initClickListener() {
        binding.backBtn.setOnClickListener(v->{
            finish();
        });
    }
}