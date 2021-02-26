package com.app.tryitat.ui.clientcategory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.tryitat.databinding.ActivityClientCategoryBottomNavBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.helper.SharedPref;
import com.app.tryitat.ui.clientsetting.ClientSettingActivity;
import com.app.tryitat.ui.dashboard.user.UserDashboardActivity;
import com.app.tryitat.ui.setting.SettingActivity;

public class ClientCategoryBottomNavActivity extends AppCompatActivity {
    private ActivityClientCategoryBottomNavBinding binding;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientCategoryBottomNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPref = new SharedPref(this);
        initClickListeners();
    }

    private void initClickListeners() {
        binding.homeOtherCat.setOnClickListener(v->{
            Constant.postCategory = "Food & Drinks";
            finish();
        });

        binding.fashionCat.setOnClickListener(v->{
            Constant.postCategory = "Fashion";
            finish();
        });

        binding.foodDrinkCat.setOnClickListener(v->{
            Constant.postCategory = "Home & Others";
            finish();
        });

        binding.allItemsCat.setOnClickListener(v->{
            Constant.postCategory = "All Items";
            finish();
        });

        binding.successCat.setOnClickListener(v->{
            Constant.postCategory = "Success";
            finish();
        });

        binding.settingBtn.setOnClickListener(v->{
            if (sharedPref.getUserType().equalsIgnoreCase(Constant.userTypeClient)){
                startActivity(new Intent(this, ClientSettingActivity.class));
            } else {
                startActivity(new Intent(this, SettingActivity.class));
            }
        });
    }

}