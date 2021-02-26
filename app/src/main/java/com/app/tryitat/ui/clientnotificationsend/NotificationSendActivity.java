package com.app.tryitat.ui.clientnotificationsend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.tryitat.databinding.ActivityNotificationSendBinding;

public class NotificationSendActivity extends AppCompatActivity {
    private ActivityNotificationSendBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationSendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initClickListener();

    }

    private void initClickListener() {
        binding.backBtn.setOnClickListener(v->{
            finish();
        });
    }
}