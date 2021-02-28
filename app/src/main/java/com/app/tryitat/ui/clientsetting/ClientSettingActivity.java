package com.app.tryitat.ui.clientsetting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ActivityClientSettingBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.helper.SharedPref;
import com.app.tryitat.ui.clientprofile.model.ClientDataModel;
import com.app.tryitat.ui.clientqrcodescanner.ClientQrCodeScannerActivity;
import com.app.tryitat.ui.localbusinesslogin.LocalBusinessLoginActivity;
import com.app.tryitat.ui.profile.model.UserDataModel;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ClientSettingActivity extends AppCompatActivity {
    private ActivityClientSettingBinding binding;
    private FirebaseDatabase database;
    private ClientDataModel clientData;
    private SharedPref sharedPref;
    int CLIENT_USER = 1;
    String CLIENT_USER_Followers = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        sharedPref = new SharedPref(this);
        CLIENT_USER = getIntent().getIntExtra("CLIENT_USER", 0);
        if (CLIENT_USER == 1) {
            CLIENT_USER_Followers = getIntent().getStringExtra("CLIENT_USER_Followers");
            getClientCLIENTDetails(CLIENT_USER_Followers);
            binding.updateBtn.setBackground(getResources().getDrawable(R.drawable.yellow_square_bg));
        } else {
            binding.updateBtn.setBackground(getResources().getDrawable(R.drawable.violet_square_bg));
            getClientDetails();
        }


        initClickListener();
    }

    private void initClickListener() {
        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnSwitchOff.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            sharedPref.setUserType("");
            sharedPref.setUserEmail("");
            sharedPref.setUserPassword("");
            Intent intent = new Intent(this, LocalBusinessLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void getClientCLIENTDetails(String CLIENT_USER_Followers) {
        database.getReference("User").child(Objects.requireNonNull(CLIENT_USER_Followers)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        UserDataModel userDataModel = snapshot.getValue(UserDataModel.class);
                        if (userDataModel.getName() != null)
                            binding.storeNameEt.setText(userDataModel.getName());
                        if (userDataModel.getEmail() != null)
                            binding.emailEt.setText(userDataModel.getEmail());
                        if (userDataModel.getMobilePhone() != null)
                            binding.phoneEt.setText(userDataModel.getMobilePhone());
                        if (userDataModel.getPicture() != null)
                            Glide.with(getApplicationContext()).load(userDataModel.getPicture()).placeholder(R.drawable.ic_person).into(binding.userImg);
                        if (userDataModel.getCoverpicture() != null)
                            Glide.with(getApplicationContext()).load(userDataModel.getCoverpicture()).placeholder(R.drawable.ic_home_others).into(binding.coverImg);
                        if (userDataModel.getAddress() != null)
                            binding.locationEt.setText(userDataModel.getAddress());
                    } catch (Exception e) {

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.getReference("Clients").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()) {
                        ClientDataModel clientDataModel = snapshot.getValue(ClientDataModel.class);
                        clientData = clientDataModel;

                        if (clientData.getStoreName() != null)
                            binding.storeNameEt.setText(clientDataModel.getStoreName());

                        if (clientData.getEmail() != null)
                            binding.emailEt.setText(clientDataModel.getEmail());
                        if (clientData.getPhone() != null)
                            binding.phoneEt.setText(clientDataModel.getPhone());
                        if (clientData.getPicture() != null)
                            Glide.with(getApplicationContext()).load(clientData.getPicture()).placeholder(R.drawable.ic_person).into(binding.userImg);
                        if (clientData.getCoverpicture() != null)
                            Glide.with(getApplicationContext()).load(clientData.getCoverpicture()).placeholder(R.drawable.ic_home_others).into(binding.coverImg);
                        if (clientData.getAddress() != null)
                            binding.locationEt.setText(clientDataModel.getAddress());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getClientDetails() {
        database.getReference("Clients").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ClientDataModel clientDataModel = snapshot.getValue(ClientDataModel.class);
                    clientData = clientDataModel;

                    if (clientData.getStoreName() != null)
                        binding.storeNameEt.setText(clientDataModel.getStoreName());
                    if (clientData.getAddress() != null)
                        binding.locationEt.setText(clientDataModel.getAddress());
                    if (clientData.getEmail() != null)
                        binding.emailEt.setText(clientDataModel.getEmail());
                    if (clientData.getPhone() != null)
                        binding.phoneEt.setText(clientDataModel.getPhone());

                    if (clientData.getPicture() != null)
                        Glide.with(getApplicationContext()).load(clientData.getPicture()).placeholder(R.drawable.ic_person).into(binding.userImg);
                    if (clientData.getCoverpicture() != null)
                        Glide.with(getApplicationContext()).load(clientData.getCoverpicture()).placeholder(R.drawable.ic_home_others).into(binding.coverImg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}