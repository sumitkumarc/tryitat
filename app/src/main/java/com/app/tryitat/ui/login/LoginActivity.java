package com.app.tryitat.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.tryitat.databinding.ActivityLoginBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.helper.SharedPref;
import com.app.tryitat.ui.clientcategory.ClientCategoryActivity;
import com.app.tryitat.ui.dashboard.catchoose.ProductCatChoseActivity;
import com.app.tryitat.ui.dashboard.user.UserDashboardActivity;
import com.app.tryitat.ui.signup.SignupActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        sharedPref = new SharedPref(this);

        if (!sharedPref.getUserEmail().equals("")){
            binding.emailEt.setText(sharedPref.getUserEmail());
        }

        if (!sharedPref.getUserPassword().equals("")){
            binding.passwordEt.setText(sharedPref.getUserPassword());
        }


        binding.enterBtn.setOnClickListener(v->{
            loginUserWithFirebase();
        });

        binding.facebookLoginBtn.setOnClickListener(v->{
            facebookLogin();
            binding.loginButton.performClick();
        });

        binding.signUpWithEmail.setOnClickListener(v->{
            startActivity(new Intent(this, SignupActivity.class));
            finish();
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
                            if (binding.rememberMeChecked.isChecked()){
                                sharedPref.setUserEmail(email);
                                sharedPref.setUserPassword(password);
                            }
                            sharedPref.setUserType("normal");
                            dismissProgress();
                            if (sharedPref.getUserType().equalsIgnoreCase(Constant.userTypeClient)){
                                startActivity(new Intent(LoginActivity.this, ClientCategoryActivity.class));
                            } else {
                                startActivity(new Intent(LoginActivity.this, ProductCatChoseActivity.class));
                            }
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            dismissProgress();
                        }
                    }
                });

    }

    CallbackManager callbackManager;
    private void facebookLogin(){
        callbackManager = CallbackManager.Factory.create();

        String EMAIL = "email";
        binding.loginButton.setReadPermissions("email", "public_profile");
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        binding.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("SMD", "facebook:onSuccess:" + loginResult);
                Log.d("SMD", "facebook:onSuccess:" + loginResult.toString());
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("SMD", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SMD", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            dismissProgress();

                            if (sharedPref.getUserType().equalsIgnoreCase(Constant.userTypeClient)){
                                startActivity(new Intent(LoginActivity.this, ClientCategoryActivity.class));
                            } else {
                                startActivity(new Intent(LoginActivity.this, ProductCatChoseActivity.class));
                            }
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SMD", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    KProgressHUD progressHUD;
    private void showProgress(){
        progressHUD = KProgressHUD.create(LoginActivity.this)
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