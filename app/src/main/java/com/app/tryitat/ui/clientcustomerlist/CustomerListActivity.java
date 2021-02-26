package com.app.tryitat.ui.clientcustomerlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.tryitat.databinding.ActivityCustomerListBinding;

public class CustomerListActivity extends AppCompatActivity {
    private ActivityCustomerListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initClickListener();
    }

    private void initClickListener() {
        binding.backBtn.setOnClickListener(v->{
            finish();
        });
    }
}