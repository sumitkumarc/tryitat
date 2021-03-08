package com.app.tryitat.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tryitat.R;
import com.app.tryitat.databinding.FragmentHomeBinding;
import com.app.tryitat.databinding.FragmentProfileBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.helper.UIHelper;
import com.app.tryitat.ui.comment.CommentActivity;
import com.app.tryitat.ui.home.adapter.DashboardPostAdapter;
import com.app.tryitat.ui.home.interfaces.HomeAdapterListener;
import com.app.tryitat.ui.home.model.PostResponse;
import com.app.tryitat.ui.pointlist.PointListActivity;
import com.app.tryitat.ui.profile.model.UserDataModel;
import com.app.tryitat.ui.setting.SettingActivity;
import com.app.tryitat.ui.userqrcode.QrCodeActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.omega_r.libs.omegaintentbuilder.OmegaIntentBuilder;
import com.omega_r.libs.omegaintentbuilder.downloader.DownloadCallback;
import com.omega_r.libs.omegaintentbuilder.handlers.ContextIntentHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileFragment extends Fragment implements HomeAdapterListener {
    private FragmentProfileBinding binding;
    private List<PostResponse> postResponseList;
    private DashboardPostAdapter dashboardPostAdapter;
    private FirebaseDatabase database;
    private DatabaseReference postRef;
    private UserDataModel dataModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance();
        postResponseList = new ArrayList<>();
        initClickListener();
        setUserInfoData();
        initUserPostRcv();
        getUsersPost();
    }

    private void initClickListener(){
        binding.settingBtn.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), SettingActivity.class));
        });
        binding.pointView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PointListActivity.class));
            }
        });

        binding.qrBtn.setOnClickListener(v->{
            Intent i = new Intent(getActivity(), QrCodeActivity.class);
            i.putExtra("uid", dataModel.getUserId());
            startActivity(new Intent(i));
        });

    }

    private void setUserInfoData(){
        database.getReference("User").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int points=0;
                    String name = snapshot.child("name").getValue(String.class);
                    String picture = snapshot.child("picture").getValue(String.class);
                    String coverpicture = snapshot.child("coverpicture").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String fcmToken = snapshot.child("fcmToken").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);
                    String mobilePhone = snapshot.child("mobilePhone").getValue(String.class);
                    if (snapshot.child("points").getValue(Integer.class)!=null){
                        points = snapshot.child("points").getValue(Integer.class);
                        binding.pointTv.setText(String.valueOf(points));
                    }
                    String userId = snapshot.child("objectId").getValue(String.class);



                    dataModel = new UserDataModel(userId, name, picture, coverpicture, email, fcmToken, gender, mobilePhone, points);
                    binding.userName.setText(name);
                    Glide.with(getContext()).load(picture).placeholder(R.drawable.ic_person).into(binding.userImage);
                    Glide.with(getContext()).load(coverpicture).placeholder(R.drawable.ic_home_others).into(binding.coverImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initUserPostRcv(){
        dashboardPostAdapter = new DashboardPostAdapter(getContext(), postResponseList, this);
        binding.postProfileRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.postProfileRcv.setAdapter(dashboardPostAdapter);
    }

    private void getUsersPost(){
        showProgress();
        postRef = database.getReference("Post");
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    PostResponse post = dataSnapshot.getValue(PostResponse.class);
                    assert post != null;
                    if (post.getUserID().equals(FirebaseAuth.getInstance().getUid()))
                        postResponseList.add(post);
                }
                dashboardPostAdapter.notifyDataSetChanged();
                dismissProgress();
                if (postResponseList.size()>0){
                    binding.noPostTv.setVisibility(View.GONE);
                }
                binding.postTv.setText(String.valueOf(postResponseList.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    KProgressHUD progressHUD;
    private void showProgress(){
        progressHUD = KProgressHUD.create(getActivity())
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

    @Override
    public void onLikedBtnClick(int position, PostResponse postRes) {
        postRef = database.getReference("Post").child(postRes.getObjectId());
        HashMap<String, Object> totalLikedObject = new HashMap<>();
        totalLikedObject.put("Likes", (postRes.getLikes()+1));
        postResponseList.get(position).setLikes((postRes.getLikes()+1));
        HashMap<String, String> lkdUsers = new HashMap<>();
        if (postRes.getLikedUser()!=null)
            lkdUsers.putAll(postRes.getLikedUser());
        lkdUsers.put(Constant.userData.getName(), Constant.userData.getUserId());
        postRes.setLikedUser(lkdUsers);
        totalLikedObject.put("LikedUser", lkdUsers);
        postRef.updateChildren(totalLikedObject);
        dashboardPostAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCommentBtnClick(PostResponse postResponse) {
        Constant.postResponse = postResponse;
        startActivity(new Intent(getActivity(), CommentActivity.class));
    }


    @Override
    public void onTriedBtnClick(int position, PostResponse postResponse) {
        new OmegaIntentBuilder(getContext())
                .share()
                .filesUrls(postResponse.getPhoto())
                .download(new DownloadCallback() {
                    @Override
                    public void onDownloaded(boolean success, @NotNull ContextIntentHandler contextIntentHandler) {
                        contextIntentHandler.startActivity();
                    }
                });
    }

    @Override
    public void onLocationBtnClick(int position, String postId) {

    }

    @Override
    public void onFollowBtnClick() {

    }
}