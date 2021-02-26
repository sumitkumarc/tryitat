package com.app.tryitat.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.tryitat.databinding.FragmentHomeBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.helper.SharedPref;
import com.app.tryitat.helper.UIHelper;
import com.app.tryitat.ui.clientsetting.ClientSettingActivity;
import com.app.tryitat.ui.comment.CommentActivity;
import com.app.tryitat.ui.home.adapter.DashboardPostAdapter;
import com.app.tryitat.ui.home.interfaces.HomeAdapterListener;
import com.app.tryitat.ui.home.model.PostResponse;
import com.app.tryitat.ui.localbusiness.LocalBusinessActivity;
import com.app.tryitat.ui.setting.SettingActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeFragment extends Fragment implements HomeAdapterListener {
    private FragmentHomeBinding binding;
    private DashboardPostAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference postRef;
    private List<PostResponse> postResponseList = new ArrayList<>();
    private SharedPref sharedPref;

    public HomeFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        sharedPref = new SharedPref(getContext());
        initClickListener();
        initPostRcv();
        showProgress();
        getAllPost();
    }

    private void initClickListener(){
        binding.settingBtn.setOnClickListener(v->{
            if (sharedPref.getUserType().equalsIgnoreCase(Constant.userTypeClient)){
                startActivity(new Intent(getActivity(), ClientSettingActivity.class));
            } else {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });

        binding.btnLocalBusiness.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), LocalBusinessActivity.class));
        });
    }

    private void initPostRcv() {
        adapter = new DashboardPostAdapter(getContext(), postResponseList, this);
        binding.mainPostRcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.mainPostRcv.setAdapter(adapter);
    }

    private void getAllPost(){
        postRef = database.getReference("Post");
        postRef.orderByChild("createdAt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    PostResponse post = dataSnapshot.getValue(PostResponse.class);
                    postResponseList.add(post);
                }
                Collections.reverse(postResponseList);
                adapter.notifyDataSetChanged();
                dismissProgress();
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
        adapter.notifyDataSetChanged();
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
        Toast.makeText(getContext(), "Follow success", Toast.LENGTH_SHORT).show();
    }
}