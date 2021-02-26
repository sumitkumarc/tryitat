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
import com.app.tryitat.ui.localbusinesslogin.LocalBusinessLoginActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        sharedPref = new SharedPref(this);

        getClientDetails();

        initClickListener();
    }

    private void initClickListener() {
        binding.backBtn.setOnClickListener(v->{
            onBackPressed();
        });

        binding.btnSwitchOff.setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            sharedPref.setUserType("");
            sharedPref.setUserEmail("");
            sharedPref.setUserPassword("");
            Intent intent = new Intent(this, LocalBusinessLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void getClientDetails(){
        database.getReference("Clients").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ClientDataModel clientDataModel = snapshot.getValue(ClientDataModel.class);
                    clientData = clientDataModel;

                    if (clientData.getStoreName()!=null)
                        binding.storeNameEt.setText(clientDataModel.getStoreName());
                    if (clientData.getAddress()!=null)
                        binding.locationEt.setText(clientDataModel.getAddress());
                    if (clientData.getEmail()!=null)
                        binding.emailEt.setText(clientDataModel.getEmail());
                    if (clientData.getPhone()!=null)
                        binding.phoneEt.setText(clientDataModel.getPhone());

                    if (clientData.getPicture()!=null)
                        Glide.with(getApplicationContext()).load(clientData.getPicture()).placeholder(R.drawable.ic_person).into(binding.userImg);
                    if (clientData.getCoverpicture()!=null)
                        Glide.with(getApplicationContext()).load(clientData.getCoverpicture()).placeholder(R.drawable.ic_home_others).into(binding.coverImg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}