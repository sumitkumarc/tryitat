package com.app.tryitat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.app.tryitat.helper.Constant;
import com.app.tryitat.helper.SharedPref;
import com.app.tryitat.ui.clientcategory.ClientCategoryActivity;
import com.app.tryitat.ui.dashboard.catchoose.ProductCatChoseActivity;
import com.app.tryitat.ui.dashboard.user.UserDashboardActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivityFirst extends AppCompatActivity {
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_first);
        sharedPref = new SharedPref(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser()!=null){
                    if (sharedPref.getUserType().equalsIgnoreCase(Constant.userTypeClient)){
                        startActivity(new Intent(SplashActivityFirst.this, ClientCategoryActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivityFirst.this, ProductCatChoseActivity.class));
                    }
                } else {
                    startActivity(new Intent(SplashActivityFirst.this, SplashActivity.class));
                }
                finish();
            }
        }, 2000);
    }

//    private void hashKeyGeneration(){
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    getPackageName(),
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//                Toast.makeText(this, ""+Base64.encodeToString(md.digest(), Base64.DEFAULT), Toast.LENGTH_SHORT).show();
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
//    }
}