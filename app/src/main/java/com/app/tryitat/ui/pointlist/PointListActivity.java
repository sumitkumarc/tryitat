package com.app.tryitat.ui.pointlist;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.tryitat.databinding.ActivityNotificationListBinding;
import com.app.tryitat.ui.clientnotificationsend.NotificationListActivity;
import com.app.tryitat.ui.clientnotificationsend.RvNotCustomerListAdapter;
import com.app.tryitat.ui.clientprofile.model.ClientDataModel;
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

public class PointListActivity extends AppCompatActivity {

    private ActivityNotificationListBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference clientRef;
    private List<PointModel> UserPoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        clientRef = database.getReference("UserPoints");

        LinearLayoutManager rvFilterManager = new LinearLayoutManager(this);
        rvFilterManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(rvFilterManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        binding.title.setText("Your Points");

//        initClickListener();/
        getUserInfoData();
    }
    KProgressHUD progressHUD;

    private void showProgress() {
        progressHUD = KProgressHUD.create(PointListActivity.this)
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
        clientRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
//
//                    int points = 0;
//                    String name = snapshot.child("clientID").getValue(String.class);
//                    String picture = snapshot.child("clientName").getValue(String.class);
//                    String coverpicture = snapshot.child("clientPic").getValue(String.class);
//                    String email = snapshot.child("objectId").getValue(String.class);
//                    String fcmToken = snapshot.child("showCamera").getValue(String.class);
//                    String gender = snapshot.child("totalPoints").getValue(String.class);
//                    String mobilePhone = snapshot.child("userID").getValue(String.class);
//                    String visits = snapshot.child("visits").getValue(String.class);


                    //PointModel clientDataModel = new PointModel(name,picture,coverpicture,email,fcmToken,gender,mobilePhone,visits);
//                    UserPoints = clientDataModel.getCustomers();
                    if(UserPoints != null) {
//                        for (int i = 0 ;i<UserPoints.size() ;i++){
//                            if((FirebaseAuth.getInstance().getUid().equals(snapshot.child("name").getValue(String.class)))){
////                                PointModel  pointModel= new PointModel();
////                                pointModel.setClientID();
////                                pointModel.setClientName();
////                                pointModel.setObjectId();
////                                pointModel.setTotalPoints();
////                                pointModel.setUserID();
////                                UserPoints.add(pointModel);
//                            }
//
//                        }

                        if (UserPoints.size() != 0) {
                            binding.txtNoData.setVisibility(View.GONE);
                            binding.recyclerView.setVisibility(View.VISIBLE);
//                            RvNotCustomerListAdapter adapter = new RvNotCustomerListAdapter(NotificationListActivity.this, customerList);
//                            binding.recyclerView.setAdapter(adapter);
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

}
