package com.app.tryitat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.tryitat.databinding.ActivitySplashBinding;
import com.app.tryitat.ui.localbusinesslogin.LocalBusinessLoginActivity;
import com.app.tryitat.ui.login.LoginActivity;
import com.app.tryitat.ui.signup.SignupActivity;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        });

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, SignupActivity.class));
            }
        });

        binding.businessLoginBtn.setOnClickListener(v->{
            startActivity(new Intent(SplashActivity.this, LocalBusinessLoginActivity.class));
        });

    }


}