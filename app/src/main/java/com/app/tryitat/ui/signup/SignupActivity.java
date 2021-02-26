package com.app.tryitat.ui.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.app.tryitat.databinding.ActivitySignupBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.helper.SharedPref;
import com.app.tryitat.ui.clientcategory.ClientCategoryActivity;
import com.app.tryitat.ui.dashboard.catchoose.ProductCatChoseActivity;
import com.app.tryitat.ui.dashboard.user.UserDashboardActivity;
import com.app.tryitat.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private FirebaseAuth mAuth;
    private SharedPref sharedPref;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("User");
        sharedPref = new SharedPref(this);

        binding.registrationBtn.setOnClickListener(v->{
            signUpWithFirebase();
        });

        binding.backBtn.setOnClickListener(v->{
            finish();
        });

    }

    private void signUpWithFirebase(){
        showProgress();
        String userName = binding.userNameEt.getText().toString();
        String email = binding.emailEt.getText().toString();
        String phone = binding.phoneEt.getText().toString();
        String password = binding.passwordEt.getText().toString();
        String reTypePassword = binding.retypePasswordEt.getText().toString();

        if (userName.isEmpty()){
            binding.userNameEt.setError("Username is required!");
            dismissProgress();
            return;
        }

        if (email.isEmpty()){
            binding.emailEt.setError("Email is required!");
            dismissProgress();
            return;
        }

        if (phone.isEmpty()){
            binding.phoneEt.setError("Phone is required!");
            dismissProgress();
            return;
        }

        if (password.isEmpty()){
            binding.passwordEt.setError("Password is required!");
            dismissProgress();
            return;
        }

        if (reTypePassword.isEmpty()){
            binding.retypePasswordEt.setError("Retype your password.");
            dismissProgress();
            return;
        }

        if (!reTypePassword.equals(password)){
            binding.retypePasswordEt.setError("Password not matched");
            binding.passwordEt.setError("Password not matched");
            dismissProgress();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            updateUserData(user, userName, email, phone);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignupActivity.this, "Register failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            dismissProgress();
                        }
                    }
                });

    }

    private void updateUserData(FirebaseUser user, String userName, String email, String phone) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("email", email);
        hashMap.put("name", userName);
        hashMap.put("mobilePhone", phone);
        hashMap.put("objectId", user.getUid());
        hashMap.put("loginMethod", "Email");
        myRef.child(user.getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dismissProgress();
                if (sharedPref.getUserType().equalsIgnoreCase(Constant.userTypeClient)){
                    startActivity(new Intent(SignupActivity.this, ClientCategoryActivity.class));
                } else {
                    startActivity(new Intent(SignupActivity.this, ProductCatChoseActivity.class));
                }
                finish();
            }
        });
    }

    KProgressHUD progressHUD;
    private void showProgress(){
        progressHUD = KProgressHUD.create(SignupActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.3f)
                .show();
    }

    private void dismissProgress(){
        if (progressHUD!=null){
            progressHUD.dismiss();
        }
    }

}