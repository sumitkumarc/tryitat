package com.app.tryitat.ui.dashboard.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ActivityClientDashboardBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.ui.camera.CameraFragment;
import com.app.tryitat.ui.categories.CategoriesFragment;
import com.app.tryitat.ui.clientcategory.ClientCategoryActivity;
import com.app.tryitat.ui.clientcategory.ClientCategoryBottomNavActivity;
import com.app.tryitat.ui.clientdashboardfragment.ClientDashboardFragment;
import com.app.tryitat.ui.clientprofile.ClientProfileFragment;
import com.app.tryitat.ui.clientprofile.model.ClientDataModel;
import com.app.tryitat.ui.home.HomeFragment;
import com.app.tryitat.ui.home.model.PostResponse;
import com.app.tryitat.ui.notification.NotificationFragment;
import com.app.tryitat.ui.profile.ProfileFragment;
import com.app.tryitat.ui.profile.model.UserDataModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ClientDashboardActivity extends AppCompatActivity {
    private ActivityClientDashboardBinding binding;
    private Fragment fragment;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();

        initClickListeners();
        binding.menuHome.performClick();
        getClientDetails();
    }

    private void initClickListeners() {
        binding.menuHome.setOnClickListener(v->{
            fragment = new HomeFragment();
            loadFragment(fragment);
            binding.menuHome.setBackgroundColor(getResources().getColor(R.color.tab_select));
            binding.menuCategories.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuDashboard.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuProfile.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuNotification.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.imageM1.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM2.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM3.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM4.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM5.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.menuM1Txt.setTextColor(getResources().getColor(R.color.white));
            binding.menuM2Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM3Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM4Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM5Txt.setTextColor(getResources().getColor(R.color.ash_500));
        });

        binding.menuCategories.setOnClickListener(v->{
            startActivity(new Intent(this, ClientCategoryBottomNavActivity.class));
//            fragment = new CategoriesFragment();
//            loadFragment(fragment);
//            binding.menuHome.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
//            binding.menuCategories.setBackgroundColor(getResources().getColor(R.color.tab_select));
//            binding.menuDashboard.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
//            binding.menuProfile.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
//            binding.menuNotification.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
//            binding.imageM1.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
//            binding.imageM2.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
//            binding.imageM3.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
//            binding.imageM4.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
//            binding.imageM5.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
//            binding.menuM1Txt.setTextColor(getResources().getColor(R.color.ash_500));
//            binding.menuM2Txt.setTextColor(getResources().getColor(R.color.white));
//            binding.menuM3Txt.setTextColor(getResources().getColor(R.color.ash_500));
//            binding.menuM4Txt.setTextColor(getResources().getColor(R.color.ash_500));
//            binding.menuM5Txt.setTextColor(getResources().getColor(R.color.ash_500));
        });

        binding.menuDashboard.setOnClickListener(v->{
            fragment = new ClientDashboardFragment();
            loadFragment(fragment);
            binding.menuHome.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuCategories.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuDashboard.setBackgroundColor(getResources().getColor(R.color.tab_select));
            binding.menuProfile.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuNotification.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.imageM1.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM2.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM3.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM4.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM5.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.menuM1Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM2Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM3Txt.setTextColor(getResources().getColor(R.color.white));
            binding.menuM4Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM5Txt.setTextColor(getResources().getColor(R.color.ash_500));
        });

        binding.menuProfile.setOnClickListener(v->{
            fragment = new ClientProfileFragment();
            loadFragment(fragment);
            binding.menuHome.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuCategories.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuDashboard.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuProfile.setBackgroundColor(getResources().getColor(R.color.tab_select));
            binding.menuNotification.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.imageM1.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM2.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM3.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM4.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM5.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.menuM1Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM2Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM3Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM4Txt.setTextColor(getResources().getColor(R.color.white));
            binding.menuM5Txt.setTextColor(getResources().getColor(R.color.ash_500));
        });

        binding.menuNotification.setOnClickListener(v->{
            fragment = new NotificationFragment();
            loadFragment(fragment);
            binding.menuHome.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuCategories.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuDashboard.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuProfile.setBackgroundColor(getResources().getColor(R.color.tab_not_select));
            binding.menuNotification.setBackgroundColor(getResources().getColor(R.color.tab_select));
            binding.imageM1.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM2.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM3.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM4.setColorFilter(ContextCompat.getColor(this, R.color.ash_500), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.imageM5.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            binding.menuM1Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM2Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM3Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM4Txt.setTextColor(getResources().getColor(R.color.ash_500));
            binding.menuM5Txt.setTextColor(getResources().getColor(R.color.white));
        });
    }
//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment fragment;
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    fragment = new HomeFragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_categories:
//                    fragment = new CategoriesFragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_dashboard:
//                    fragment = new ClientDashboardFragment();
//                    loadFragment(fragment);
//                    return true;
//                case R.id.navigation_profile:
//                    fragment = new ProfileFragment();
//                    loadFragment(fragment);
//                    return true;
//
//                case R.id.navigation_notification:
//                    fragment = new NotificationFragment();
//                    loadFragment(fragment);
//                    return true;
//            }
//
//            return false;
//        }
//    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getClientDetails(){
        database.getReference("Clients").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ClientDataModel clientDataModel = snapshot.getValue(ClientDataModel.class);
                    Constant.clientDataModel = clientDataModel;
                    if (Constant.clientDataModel.getRewards() != null){
//                        for (int i = 0; i < rewardsImageList.size(); i++){
//                            if(!rewardsImageList.contains(data_qr)){
//                                rewardsImageList.add(data_qr);
//                            }
//                        }
                    }else {
//                        rewardsImageList = new ArrayList<>();
//                        rewardsImageList.add(data_qr);
                    }
//                    String address;
//                    String approved;
//                    String email;
//                    String fcmToken;
//                    String lat;
//                    String lng;
//                    String objectId;
//                    String phone;
//                    String picture;
//                    String storeName;
//                    String storeSubName;
//                    String userType;
//                    address = snapshot.child("address").getValue(String.class);
//                    approved = snapshot.child("approved").getValue(String.class);
//                    email = snapshot.child("email").getValue(String.class);
//                    fcmToken = snapshot.child("fcmToken").getValue(String.class);
//                    lat = snapshot.child("lat").getValue(String.class);
//                    lng = snapshot.child("lng").getValue(String.class);
//                    objectId = snapshot.child("objectId").getValue(String.class);
//                    phone = snapshot.child("phone").getValue(String.class);
//                    picture = snapshot.child("picture").getValue(String.class);
//                    storeName = snapshot.child("storeName").getValue(String.class);
//                    storeSubName = snapshot.child("storeSubName").getValue(String.class);
//                    userType = snapshot.child("userType").getValue(String.class);
//                    Constant.clientDataModel = new ClientDataModel(userId, name, picture, coverpicture, email, fcmToken, gender, mobilePhone, points);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

}