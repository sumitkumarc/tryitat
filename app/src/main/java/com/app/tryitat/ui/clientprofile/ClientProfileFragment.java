package com.app.tryitat.ui.clientprofile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tryitat.R;
import com.app.tryitat.databinding.FragmentClientProfileBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.ui.clientdashboardfragment.RewaordsPointListActivity;
import com.app.tryitat.ui.clientdashboardfragment.RvReWordsCustomerListAdapter;
import com.app.tryitat.ui.clientfollowerlist.ClientFollowerListActivity;
import com.app.tryitat.ui.home.model.PostResponse;
import com.app.tryitat.ui.setting.SettingActivity;
import com.app.tryitat.utils.Common;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ClientProfileFragment extends Fragment {
    private FragmentClientProfileBinding binding;
    private List<PostResponse> postResponseList;
    private List<String> rewardsImageList = new ArrayList<>();
    private List<String> rewardsPriceList = new ArrayList<>();

    public ClientProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClientProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initClickListener();
    }

    private void initClickListener() {
        LinearLayoutManager rvFilterManager = new LinearLayoutManager(getContext());
        rvFilterManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rewardsRcv.setLayoutManager(rvFilterManager);
        binding.rewardsRcv.setItemAnimator(new DefaultItemAnimator());


        if (Constant.clientDataModel.getStoreName() != null)
            binding.userName.setText(Constant.clientDataModel.getStoreName());
        if (Constant.clientDataModel.getPicture() != null)
            Glide.with(getContext()).load(Constant.clientDataModel.getPicture()).placeholder(R.drawable.ic_person).into(binding.userImage);
        if (Constant.clientDataModel.getCoverpicture() != null)
            Glide.with(getContext()).load(Constant.clientDataModel.getCoverpicture()).placeholder(R.drawable.ic_home_others).into(binding.coverImage);
        if (Constant.clientDataModel.getFollowers() != null)
            binding.followerTv.setText(String.valueOf(Constant.clientDataModel.getFollowers().size()));
        if (Constant.clientDataModel.getFollowing() != null)
            binding.followingTv.setText(String.valueOf(Constant.clientDataModel.getFollowing().size()));
        if (Constant.clientDataModel.getRewards() != null) {
            for (int i = 0; i <= Constant.clientDataModel.getRewards().size(); i++) {
                if (Constant.clientDataModel.getRewards().get("image" + (i + 1)) != null) {
                    rewardsImageList.add(Constant.clientDataModel.getRewards().get("image" + i + 1));
                }

                if (Constant.clientDataModel.getRewards().get("price" + (i + 1)) != null) {
                    rewardsPriceList.add(Constant.clientDataModel.getRewards().get("price" + i + 1));
                }
            }
        }
        if (Constant.clientDataModel.getRewards() != null) {
            binding.rewardsRcv.setVisibility(View.VISIBLE);
            RvReWordsCustomerListAdapter rvReWordsCustomerListAdapter = new RvReWordsCustomerListAdapter(getContext(), Constant.clientDataModel.getRewards());
            binding.rewardsRcv.setAdapter(rvReWordsCustomerListAdapter);
        } else {
            binding.rewardsRcv.setVisibility(View.GONE);
        }

        binding.loyaltyEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.clientDataModel.getRewards() != null) {
                    startActivity(new Intent(getActivity(), RewaordsPointListActivity.class));
                }else {
                    SowDialogBox();
                }
            }
        });
        binding.settingBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SettingActivity.class));
        });

        binding.followeView.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ClientFollowerListActivity.class));
        });
    }

    public void SowDialogBox() {
        final Dialog dialogm = new Dialog(getContext());
        dialogm.setCancelable(true);
        dialogm.setContentView(R.layout.pop_add_amount);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogm.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialogm.getWindow().setAttributes(lp);
        dialogm.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        ImageView bt_close = dialogm.findViewById(R.id.bt_close);
//        final EditText ed_card_number = dialogm.findViewById(R.id.ed_card_number);
//        AppCompatButton bt_add_now = dialogm.findViewById(R.id.bt_add_now);
//        TextView txt_title = dialogm.findViewById(R.id.txt_title);

//        bt_add_now.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onClick(View view) {
//                if (!Common.edvalidateName(ed_card_number.getText().toString(), ed_card_number, getResources().getString(R.string.v_enter_card_no))) {
//                    return;
//                }
//                if (isConnected()) {
//                    showDialog();
//                    amountNew = Double.parseDouble(ed_card_number.getText().toString());
//                    initialiseSDK(Double.parseDouble(ed_card_number.getText().toString()), "");
//                    configureSDKMode();
//                }
//                // showDialogSelectPayment(ed_card_number.getText().toString());
//
//                dialogm.dismiss();
//            }
//        });
//        bt_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialogm.dismiss();
//            }
//        });
        dialogm.show();
    }
}