package com.app.tryitat.ui.clientprofile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tryitat.R;
import com.app.tryitat.databinding.FragmentClientProfileBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.ui.clientfollowerlist.ClientFollowerListActivity;
import com.app.tryitat.ui.home.model.PostResponse;
import com.app.tryitat.ui.setting.SettingActivity;
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

        if (Constant.clientDataModel.getStoreName()!=null)
            binding.userName.setText(Constant.clientDataModel.getStoreName());
        if (Constant.clientDataModel.getPicture()!=null)
            Glide.with(getContext()).load(Constant.clientDataModel.getPicture()).placeholder(R.drawable.ic_person).into(binding.userImage);
        if (Constant.clientDataModel.getCoverpicture()!=null)
            Glide.with(getContext()).load(Constant.clientDataModel.getCoverpicture()).placeholder(R.drawable.ic_home_others).into(binding.coverImage);
        if (Constant.clientDataModel.getFollowers()!=null)
            binding.followerTv.setText(String.valueOf(Constant.clientDataModel.getFollowers().size()));
        if (Constant.clientDataModel.getFollowing()!=null)
            binding.followingTv.setText(String.valueOf(Constant.clientDataModel.getFollowing().size()));
        if (Constant.clientDataModel.getRewards()!=null){
            for (int i=0; i<=Constant.clientDataModel.getRewards().size(); i++){
                if (Constant.clientDataModel.getRewards().get("image"+(i+1))!=null){
                    rewardsImageList.add(Constant.clientDataModel.getRewards().get("image"+i+1));
                }

                if (Constant.clientDataModel.getRewards().get("price"+(i+1))!=null){
                    rewardsPriceList.add(Constant.clientDataModel.getRewards().get("price"+i+1));
                }
            }
        }


        binding.settingBtn.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), SettingActivity.class));
        });

        binding.followeView.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), ClientFollowerListActivity.class));
        });
    }
}