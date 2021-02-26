package com.app.tryitat.ui.home.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ItemPostBinding;
import com.app.tryitat.helper.UIHelper;
import com.app.tryitat.ui.home.interfaces.HomeAdapterListener;
import com.app.tryitat.ui.home.model.PostResponse;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DashboardPostAdapter extends RecyclerView.Adapter<DashboardPostAdapter.ViewHolder> {
    Context context;
    List<PostResponse> postResponseList;
    HomeAdapterListener listener;
    public DashboardPostAdapter(Context context, List<PostResponse> postResponseList, HomeAdapterListener listener){
        this.context = context;
        this.postResponseList = postResponseList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = ItemPostBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostResponse post = postResponseList.get(position);
        holder.binding.userNameTv.setText(post.getUserName());
        Glide.with(context).load(post.getPhoto()).into(holder.binding.postImage);
        holder.binding.postDesc.setText(post.getInyourmind());
        holder.binding.likeCountTv.setText(String.valueOf(post.getLikes()));
        holder.binding.commentCountTv.setText(String.valueOf(post.getComments()));
        UIHelper.getUsersPhotos(context, post.getUserID(), holder.binding.userImg);

        holder.binding.likeView.setOnClickListener(v->{
            boolean isUserAlreadyLikedIt=false;
            if (post.getLikedUser()!=null){
                Iterator it = post.getLikedUser().entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry)it.next();
                    if (pairs.getValue().equals(FirebaseAuth.getInstance().getUid())){
                        isUserAlreadyLikedIt = true;
                        holder.binding.likedImg.setImageResource(R.drawable.ic_baseline_favorite);
                        holder.binding.likedImg.setColorFilter(ContextCompat.getColor(context, R.color.red_100), android.graphics.PorterDuff.Mode.SRC_IN);
                    }
                }
            }

            if (isUserAlreadyLikedIt){
                Toast.makeText(context, "You already liked this post", Toast.LENGTH_SHORT).show();
            } else {
                listener.onLikedBtnClick(position, post);
            }
        });

        holder.binding.commentView.setOnClickListener(v->{
            listener.onCommentBtnClick(post);
        });

        holder.binding.shareView.setOnClickListener(v->{
            listener.onTriedBtnClick(position, post);
        });

        holder.binding.btnFollow.setOnClickListener(v->{
            listener.onFollowBtnClick();
        });

        // User like check
        if (post.getLikedUser()!=null){
            Iterator it = post.getLikedUser().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                if (pairs.getValue().equals(FirebaseAuth.getInstance().getUid())){
                    holder.binding.likedImg.setImageResource(R.drawable.ic_baseline_favorite);
                    holder.binding.likedImg.setColorFilter(ContextCompat.getColor(context, R.color.red_100), android.graphics.PorterDuff.Mode.SRC_IN);
                }
            }
        }



    }

    @Override
    public int getItemCount() {
        return postResponseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemPostBinding binding;
        public ViewHolder(@NonNull ItemPostBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
