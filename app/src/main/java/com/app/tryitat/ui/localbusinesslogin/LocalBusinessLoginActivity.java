package com.app.tryitat.ui.localbusinesslogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.app.tryitat.databinding.ActivityLocalBusinessLoginBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.helper.SharedPref;
import com.app.tryitat.ui.clientcategory.ClientCategoryActivity;
import com.app.tryitat.ui.dashboard.catchoose.ProductCatChoseActivity;
import com.app.tryitat.ui.dashboard.client.ClientDashboardActivity;
import com.app.tryitat.ui.locolbusinesssignup.LocolBusinessSignUpActivity;
import com.app.tryitat.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

public class LocalBusinessLoginActivity extends AppCompatActivity {
    private ActivityLocalBusinessLoginBinding binding;
    private FirebaseAuth mAuth;
    private SharedPref sharedPref;
    private FirebaseDatabase database;
    private DatabaseReference clientRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocalBusinessLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        sharedPref = new SharedPref(this);
        database = FirebaseDatabase.getInstance();
        clientRef = database.getReference("Clients");

        if (!sharedPref.getUserEmail().equals("")){
            binding.emailEt.setText(sharedPref.getUserEmail());
        }

        if (!sharedPref.getUserPassword().equals("")){
            binding.passwordEt.setText(sharedPref.getUserPassword());
        }

        binding.enterBtn.setOnClickListener(v->{
            loginUserWithFirebase();
        });

        binding.signUpBtn.setOnClickListener(v->{
            startActivity(new Intent(this, LocolBusinessSignUpActivity.class));
        });
    }

    private void loginUserWithFirebase() {
        showProgress();
        String email = binding.emailEt.getText().toString();
        String password = binding.passwordEt.getText().toString();

        if (email.isEmpty()){
            binding.emailEt.setError("Enter email");
            dismissProgress();
            return;
        }

        if (password.isEmpty()){
            binding.passwordEt.setError("Enter Password");
            dismissProgress();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            dismissProgress();
                            checkAccountActivation(user.getUid(), email, password);
                        } else {
                            Toast.makeText(LocalBusinessLoginActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            dismissProgress();
                        }
                    }
                });

    }

    private void checkAccountActivation(String uid, String email, String password) {
        clientRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String approved = snapshot.child("approved").getValue(String.class);
                assert approved != null;
                if (approved.equalsIgnoreCase("yes")){
                    sharedPref.setUserType("client");
                    if (binding.rememberMeChecked.isChecked()){
                        sharedPref.setUserEmail(email);
                        sharedPref.setUserPassword(password);
                    }
                    if (sharedPref.getUserType().equalsIgnoreCase(Constant.userTypeClient)){
                        startActivity(new Intent(LocalBusinessLoginActivity.this, ClientDashboardActivity.class));
                    } else {
                        startActivity(new Intent(LocalBusinessLoginActivity.this, ProductCatChoseActivity.class));
                    }
                    finish();
                } else {
                    Toast.makeText(LocalBusinessLoginActivity.this, "Your account not approved yet.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    KProgressHUD progressHUD;
    private void showProgress(){
        progressHUD = KProgressHUD.create(LocalBusinessLoginActivity.this)
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