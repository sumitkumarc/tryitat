package com.app.tryitat.ui.clientcustomerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.tryitat.databinding.ActivityCustomerListBinding;
import com.app.tryitat.ui.clientprofile.model.ClientDataModel;
import com.app.tryitat.ui.clientqrcodescanner.ClientQrCodeScannerActivity;
import com.app.tryitat.ui.signup.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomerListActivity extends AppCompatActivity {
    private ActivityCustomerListBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference clientRef;
    private List<String> customerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        clientRef = database.getReference("Clients");

        initClickListener();

        LinearLayoutManager rvFilterManager = new LinearLayoutManager(this);
        rvFilterManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(rvFilterManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        getUserInfoData();
    }

    private void initClickListener() {
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    KProgressHUD progressHUD;

    private void showProgress() {
        progressHUD = KProgressHUD.create(CustomerListActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.3f)
                .show();
    }

    private void dismissProgress() {
        if (progressHUD != null) {
            progressHUD.dismiss();
        }
    }

    private void getUserInfoData() {
        showProgress();
        clientRef.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()) {
                        ClientDataModel clientDataModel = snapshot.getValue(ClientDataModel.class);
                        customerList = clientDataModel.getCustomers();
                        if(customerList != null){
                            if (customerList.size() != 0) {
                                binding.txtNoData.setVisibility(View.GONE);
                                binding.recyclerView.setVisibility(View.VISIBLE);
                                RvCustomerListAdapter adapter = new RvCustomerListAdapter(CustomerListActivity.this, customerList);
                                binding.recyclerView.setAdapter(adapter);
                            } else {
                                binding.txtNoData.setVisibility(View.VISIBLE);
                                binding.recyclerView.setVisibility(View.GONE);
                            }
                            dismissProgress();
                        }else {
                            dismissProgress();
                            binding.recyclerView.setVisibility(View.GONE);
                            binding.txtNoData.setVisibility(View.VISIBLE);
                        }

                    }
                }catch (Exception e){
                    dismissProgress();
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.txtNoData.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}