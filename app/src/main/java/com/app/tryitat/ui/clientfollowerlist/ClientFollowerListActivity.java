package com.app.tryitat.ui.clientfollowerlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.tryitat.databinding.ActivityClientFollowerListBinding;

public class ClientFollowerListActivity extends AppCompatActivity {
    private ActivityClientFollowerListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientFollowerListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}