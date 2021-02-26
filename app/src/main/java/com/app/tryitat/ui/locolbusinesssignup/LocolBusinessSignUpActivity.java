
package com.app.tryitat.ui.locolbusinesssignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ActivityLocolBusinessSignUpBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

public class LocolBusinessSignUpActivity extends AppCompatActivity {
    private ActivityLocolBusinessSignUpBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference clientRef;
    private String lat="";
    private String lang ="";
    private FusedLocationProviderClient fusedLocationClient;
    private final CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocolBusinessSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        clientRef = database.getReference("Clients");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        binding.registrationBtn.setOnClickListener(v->{
            signUpWithFirebase();
        });

        binding.backBtn.setOnClickListener(v->{
            finish();
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestCurrentLocation();

            }
        }, 1500);
    }

    private void signUpWithFirebase(){
        showProgress();
        String storeName = binding.userNameEt.getText().toString();
        String email = binding.emailEt.getText().toString();
        String phone = binding.phoneEt.getText().toString();
        String password = binding.passwordEt.getText().toString();
        String reTypePassword = binding.retypePasswordEt.getText().toString();

        if (storeName.isEmpty()){
            binding.userNameEt.setError("Username is required!");
            dismissProgress();
            return;
        }

//        if (address.isEmpty()){
//            binding.locationEt.setError("Location is required!");
//            dismissProgress();
//            return;
//        }

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
                            updateUserData(user, storeName, email, phone);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LocolBusinessSignUpActivity.this, "Register failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            dismissProgress();
                        }
                    }
                });
    }

    private void updateUserData(FirebaseUser user, String storeName, String email, String phone) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("email", email);
        hashMap.put("storeName", storeName);
        hashMap.put("storeSubName", "");
        hashMap.put("phone", phone);
        hashMap.put("approved", "No");
        hashMap.put("objectId", user.getUid());
        hashMap.put("userType", "CLIENT");
        hashMap.put("lat", lat);
        hashMap.put("lng", lang);
        clientRef.child(user.getUid()).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dismissProgress();
                Dialog dialog = new Dialog(LocolBusinessSignUpActivity.this);
                dialog.setContentView(R.layout.dialog_account_create_success);
//                Window window = dialog.getWindow();
//                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                Button okBtn = dialog.findViewById(R.id.ok_btn);
                okBtn.setOnClickListener(v->{
                    dialog.dismiss();
                    finish();
                });
                dialog.show();
            }
        });
        dismissProgress();
    }

    KProgressHUD progressHUD;
    private void showProgress(){
        progressHUD = KProgressHUD.create(LocolBusinessSignUpActivity.this)
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


    private void requestCurrentLocation() {
        // Request permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Main code
            Task<Location> currentLocationTask = fusedLocationClient.getCurrentLocation(
                    PRIORITY_HIGH_ACCURACY,
                    cancellationTokenSource.getToken()
            );

            currentLocationTask.addOnCompleteListener((new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {

                    if (task.isSuccessful()) {
                        // Task completed successfully
                        Location location = task.getResult();
                        lat = String.valueOf(location.getLatitude());
                        lang = String.valueOf(location.getLongitude());
                    } else {
                        // Task failed with an exception
                        Exception exception = task.getException();
                    }
                }
            }));
        } else {
            Dexter.withContext(this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                        @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                            requestCurrentLocation();
                        }
                        @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        }
                        @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        }
                    }).check();
        }
    }

}