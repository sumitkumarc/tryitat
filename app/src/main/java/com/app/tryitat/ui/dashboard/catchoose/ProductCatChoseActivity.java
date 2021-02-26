package com.app.tryitat.ui.dashboard.catchoose;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.tryitat.databinding.ActivityProductCatChoseBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.helper.SharedPref;
import com.app.tryitat.ui.clientsetting.ClientSettingActivity;
import com.app.tryitat.ui.dashboard.user.UserDashboardActivity;
import com.app.tryitat.ui.setting.SettingActivity;

public class ProductCatChoseActivity extends AppCompatActivity {
    private ActivityProductCatChoseBinding binding;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductCatChoseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPref = new SharedPref(this);

        initClickListeners();
    }

    private void initClickListeners() {
        binding.homeOtherCat.setOnClickListener(v->{
            Constant.postCategory = "Food & Drinks";
            startActivity(new Intent(this, UserDashboardActivity.class));
            finish();
        });

        binding.fashionCat.setOnClickListener(v->{
            Constant.postCategory = "Fashion";
            startActivity(new Intent(this, UserDashboardActivity.class));
            finish();
        });

        binding.foodDrinkCat.setOnClickListener(v->{
            Constant.postCategory = "Home & Others";
            startActivity(new Intent(this, UserDashboardActivity.class));
            finish();
        });

        binding.allItemsCat.setOnClickListener(v->{
            Constant.postCategory = "All Items";
            startActivity(new Intent(this, UserDashboardActivity.class));
            finish();
        });

        binding.successCat.setOnClickListener(v->{
            Constant.postCategory = "Success";
            startActivity(new Intent(this, UserDashboardActivity.class));
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