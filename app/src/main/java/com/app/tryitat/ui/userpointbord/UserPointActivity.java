package com.app.tryitat.ui.userpointbord;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ActivityNotificationListBinding;
import com.app.tryitat.databinding.ActivityPointBordBinding;
import com.app.tryitat.ui.pointlist.PointListActivity;
import com.app.tryitat.utils.Common;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;

public class UserPointActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference clientRef;
    ActivityPointBordBinding binding;
    String ClientName;
    String ClientPic;
    String ObjectId;
    int TotalPoints;
    int Visits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPointBordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        clientRef = database.getReference("UserPoints");

        LordData();


    }

    private void LordData() {
        ClientName = getIntent().getStringExtra("ClientName");
        TotalPoints = getIntent().getIntExtra("TotalPoints", 0);
        ClientPic = getIntent().getStringExtra("ClientPic");
        ObjectId = getIntent().getStringExtra("ObjectId");
        Visits = getIntent().getIntExtra("Visits", 0);

        binding.title.setText(ClientName);
        binding.txtTotalPoint.setText(String.valueOf(TotalPoints));
        binding.txtVisits.setText(String.valueOf(Visits));
        binding.txtPointsOfVisits.setText(String.valueOf(Visits * 5));

        int post_count = TotalPoints - Visits * 5 / 5;
        int post_point_count = post_count * 5;
        binding.txtPost.setText(Common.isStrempty(String.valueOf(post_count)));
        binding.txtPostCount.setText(Common.isStrempty(String.valueOf(post_point_count)));

        Glide.with(this).load(Common.isStrempty(ClientPic)).placeholder(getResources().getDrawable(R.drawable.default_image_100)).error(getResources().getDrawable(R.drawable.default_image_100)).into(binding.ivImg);

        binding.txtSelectPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SowDialogBox();
            }
        });

        binding.txtRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("UserPoints").child(String.valueOf(ObjectId)).child("totalPoints").setValue(0);
                FirebaseDatabase.getInstance().getReference("UserPoints").child(String.valueOf(ObjectId)).child("Visits").setValue(0);
                FirebaseDatabase.getInstance().getReference("UserPoints").child(String.valueOf(ObjectId)).child("showCamera").setValue("");
            }
        });
    }

    public void SowDialogBox() {
        final Dialog dialogm = new Dialog(this);
        dialogm.setCancelable(true);
        dialogm.setContentView(R.layout.pop_point_list);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogm.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialogm.getWindow().setAttributes(lp);
        dialogm.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogm.getWindow().setAttributes(lp);
        dialogm.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView txt_50 = dialogm.findViewById(R.id.txt_50);
        TextView txt_100 = dialogm.findViewById(R.id.txt_100);
        TextView txt_150 = dialogm.findViewById(R.id.txt_150);
        TextView txt_200 = dialogm.findViewById(R.id.txt_200);
        TextView txt_250 = dialogm.findViewById(R.id.txt_250);
        TextView txt_300 = dialogm.findViewById(R.id.txt_300);
        txt_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtSelectPoint.setText("50");
                dialogm.dismiss();
            }
        });
        txt_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtSelectPoint.setText("100");
                dialogm.dismiss();
            }
        });
        txt_150.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtSelectPoint.setText("150");
                dialogm.dismiss();
            }
        });
        txt_200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtSelectPoint.setText("200");
                dialogm.dismiss();
            }
        });
        txt_250.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtSelectPoint.setText("250");
                dialogm.dismiss();
            }
        });
        txt_300.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtSelectPoint.setText("300");
                dialogm.dismiss();
            }
        });
        dialogm.findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogm.dismiss();
            }
        });
        dialogm.show();
    }

    KProgressHUD progressHUD;

    private void showProgress() {
        progressHUD = KProgressHUD.create(UserPointActivity.this)
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
}
