package com.app.tryitat.ui.clientcategory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.tryitat.databinding.ActivityClientCategoryBinding;
import com.app.tryitat.databinding.ActivityClientSettingBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.helper.SharedPref;
import com.app.tryitat.ui.clientcustomerlist.CustomerListActivity;
import com.app.tryitat.ui.clientnotificationsend.NotificationSendActivity;
import com.app.tryitat.ui.clientqrcodescanner.ClientQrCodeScannerActivity;
import com.app.tryitat.ui.clientsetting.ClientSettingActivity;
import com.app.tryitat.ui.dashboard.client.ClientDashboardActivity;
import com.app.tryitat.ui.dashboard.user.UserDashboardActivity;
import com.app.tryitat.ui.setting.SettingActivity;

public class ClientCategoryActivity extends AppCompatActivity {
    private ActivityClientCategoryBinding binding;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPref = new SharedPref(this);

        initClickListeners();
    }

    private void initClickListeners() {
        binding.loyaltyCat.setOnClickListener(v->{
            startActivity(new Intent(this, ClientDashboardActivity.class));
        });

        binding.customerListCat.setOnClickListener(v->{
            startActivity(new Intent(this, CustomerListActivity.class));
        });

        binding.notificationCat.setOnClickListener(v->{
            startActivity(new Intent(this, NotificationSendActivity.class));
        });

        binding.qrCodeCat.setOnClickListener(v->{
            startActivity(new Intent(this, ClientQrCodeScannerActivity.class));
        });

        binding.accountSettingCat.setOnClickListener(v->{
            startActivity(new Intent(this, ClientSettingActivity.class));
        });

        binding.settingBtn.setOnClickListener(v->{
            startActivity(new Intent(this, ClientSettingActivity.class));
        });
    }
}