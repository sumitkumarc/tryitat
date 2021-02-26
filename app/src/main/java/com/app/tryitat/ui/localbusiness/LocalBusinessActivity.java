package com.app.tryitat.ui.localbusiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.tryitat.databinding.ActivityLocalBusinessBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.ui.clientsetting.ClientSettingActivity;
import com.app.tryitat.ui.localbusiness.adapter.PagerAdapter;
import com.app.tryitat.ui.setting.SettingActivity;

public class LocalBusinessActivity extends AppCompatActivity {
    private ActivityLocalBusinessBinding binding;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocalBusinessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new PagerAdapter(
                getSupportFragmentManager());
        binding.viewPager.setAdapter(adapter);

        // It is used to join TabLayout with ViewPager.
        binding.tab.setupWithViewPager(binding.viewPager);

        initClickListeners();
    }

    private void initClickListeners() {
        binding.backBtn.setOnClickListener(v->{
            finish();
        });

        binding.btnSetting.setOnClickListener(v->{
            if (Constant.currentUserType.equals("Client")){
                startActivity(new Intent(this, ClientSettingActivity.class));
            } else {
                startActivity(new Intent(this, SettingActivity.class));
            }
        });
    }
}