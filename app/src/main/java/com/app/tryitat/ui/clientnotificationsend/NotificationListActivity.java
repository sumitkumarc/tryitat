package com.app.tryitat.ui.clientnotificationsend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.app.tryitat.databinding.ActivityNotificationListBinding;
import com.app.tryitat.ui.clientprofile.model.ClientDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class NotificationListActivity extends AppCompatActivity {
    private ActivityNotificationListBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference clientRef;
    private List<String> customerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        clientRef = database.getReference("Clients");

        LinearLayoutManager rvFilterManager = new LinearLayoutManager(this);
        rvFilterManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(rvFilterManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        initClickListener();
        getUserInfoData();
    }

    private void initClickListener() {
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });
    }
    KProgressHUD progressHUD;

    private void showProgress() {
        progressHUD = KProgressHUD.create(NotificationListActivity.this)
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
                if (snapshot.exists()) {
                    ClientDataModel clientDataModel = snapshot.getValue(ClientDataModel.class);
                    customerList = clientDataModel.getCustomers();
                    if(customerList != null) {
                        if (customerList.size() != 0) {
                            binding.txtNoData.setVisibility(View.GONE);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                            RvNotCustomerListAdapter adapter = new RvNotCustomerListAdapter(NotificationListActivity.this, customerList);
                            binding.recyclerView.setAdapter(adapter);
                        } else {
                            binding.recyclerView.setVisibility(View.GONE);
                            binding.txtNoData.setVisibility(View.VISIBLE);
                        }
                        dismissProgress();
                    }else {
                        dismissProgress();
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.txtNoData.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
}