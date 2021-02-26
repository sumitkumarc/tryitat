package com.app.tryitat.ui.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ActivitySettingBinding;
import com.app.tryitat.helper.SharedPref;
import com.app.tryitat.ui.localbusinesslogin.LocalBusinessLoginActivity;
import com.app.tryitat.ui.login.LoginActivity;
import com.app.tryitat.ui.profile.model.UserDataModel;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends AppCompatActivity {
    private ActivitySettingBinding binding;
    private FirebaseDatabase database;
    private UserDataModel dataModel;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        sharedPref = new SharedPref(this);
        setSpinner();

        initClickListeners();
        setUserInfoData();
    }

    private void initClickListeners(){
        binding.backBtn.setOnClickListener(v->{
            finish();
        });

        binding.saveBtn.setOnClickListener(v->{

        });

        binding.btnSwitchOff.setOnClickListener(v->{
            FirebaseAuth.getInstance().signOut();
            sharedPref.setUserType("");
            sharedPref.setUserEmail("");
            sharedPref.setUserPassword("");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void setUserInfoData(){
        database.getReference("User").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int points =0;
                    String name = snapshot.child("name").getValue(String.class);
                    String picture = snapshot.child("picture").getValue(String.class);
                    String coverpicture = snapshot.child("coverpicture").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String fcmToken = snapshot.child("fcmToken").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);
                    String mobilePhone = snapshot.child("mobilePhone").getValue(String.class);
                    if (snapshot.child("points").getValue(Integer.class)!=null)
                        points = snapshot.child("points").getValue(Integer.class);
                    String userId = snapshot.child("objectId").getValue(String.class);

                    dataModel = new UserDataModel(userId, name, picture, coverpicture, email, fcmToken, gender, mobilePhone, points);

                    binding.emailEt.setText(email);
                    binding.firstNameEt.setText(name.split(" ")[0]);
                    binding.lastNameEt.setText(name.split(" ").length>1?name.split(" ")[1]:"");
                    Glide.with(getApplicationContext()).load(picture).placeholder(R.drawable.ic_person).into(binding.userImg);
                    Glide.with(getApplicationContext()).load(coverpicture).placeholder(R.drawable.ic_home_others).into(binding.coverImg);

                    if (gender!=null)
                        binding.genderSp.setSelection(genderAdapter.getPosition(gender));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    ArrayAdapter<CharSequence> genderAdapter;
    private void setSpinner(){
        genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.genderSp.setAdapter(genderAdapter);
    }

}