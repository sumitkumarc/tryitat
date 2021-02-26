package com.app.tryitat.ui.comment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;

import com.app.tryitat.databinding.ActivityCommentBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.helper.UIHelper;
import com.app.tryitat.ui.comment.adapter.CommentAdapter;
import com.app.tryitat.ui.comment.model.CommentDataModel;
import com.app.tryitat.ui.home.adapter.DashboardPostAdapter;
import com.app.tryitat.ui.home.model.PostResponse;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {
    private ActivityCommentBinding binding;
    private FirebaseDatabase database;
    private CommentAdapter commentAdapter;
    private List<CommentDataModel> commentDataModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        initClickListener();
        initCommentRcv();
        getComments(Constant.postResponse.getObjectId());
        setPostData(Constant.postResponse);
    }

    private void initCommentRcv(){
        commentAdapter = new CommentAdapter(this, commentDataModelList);
        binding.commentRcv.setLayoutManager(new LinearLayoutManager(this));
        binding.commentRcv.setAdapter(commentAdapter);
    }

    private void setPostData(PostResponse postData) {
        binding.userNameTv.setText(postData.getUserName());
        Glide.with(this).load(postData.getPhoto()).into(binding.postImage);
        binding.likeCountTv.setText(String.valueOf(postData.getLikes()));
        binding.commentCountTv.setText(String.valueOf(postData.getComments()));
        binding.triedCountTv.setText(String.valueOf(postData.getTries()));
        UIHelper.getUsersPhotos(this, postData.getUserID(), binding.userImg);
    }

    private void initClickListener(){
        binding.icBack.setOnClickListener(v->{
            finish();
        });

        binding.sendBtn.setOnClickListener(v->{
            String commentText = binding.commentEt.getText().toString();
            addUserComment(commentText, Constant.postResponse.getObjectId());
            binding.commentEt.getText().clear();
        });
    }

    private void addUserComment(String commentText, String postId) {
        DatabaseReference commentRef = database.getReference("Comment").child(postId).push();
        HashMap<String, String> commentsData = new HashMap<>();
        commentsData.put("UserName", Constant.userData.getName());
        commentsData.put("UserId", Constant.userData.getUserId());
        commentsData.put("Commentxt", commentText);
        commentsData.put("objectId", commentRef.getKey());
        commentRef.setValue(commentsData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("SMD", "onComplete: comment added success");
                Map map = new HashMap();
                map.put("createdAt", ServerValue.TIMESTAMP);
                map.put("updatedAt", ServerValue.TIMESTAMP);
                commentRef.updateChildren(map);
            }
        });
    }

    private void getComments(String postId){
        database.getReference("Comment").child(postId).orderByChild("createdAt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    commentDataModelList.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                        String commentTxt = dataSnapshot.child("Commentxt").getValue(String.class);
                        String userId = dataSnapshot.child("UserId").getValue(String.class);
                        String userName = dataSnapshot.child("UserName").getValue(String.class);
                        String objectId = dataSnapshot.child("objectId").getValue(String.class);
                        if (dataSnapshot.child("createdAt").getValue(Long.class) != null)
                            Log.e("SMD", "onDataChange: " + UIHelper.getTimeDate(dataSnapshot.child("createdAt").getValue(Long.class))  );

                        CommentDataModel commentDataModel = new CommentDataModel();
                        commentDataModel.setCommentxt(commentTxt);
                        commentDataModel.setObjectId(objectId);
                        commentDataModel.setUserId(userId);
                        commentDataModel.setUserName(userName);
                        commentDataModelList.add(commentDataModel);
                    }
                }
                Collections.reverse(commentDataModelList);
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}