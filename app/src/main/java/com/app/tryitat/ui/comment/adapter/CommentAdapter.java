package com.app.tryitat.ui.comment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ItemCommentBinding;
import com.app.tryitat.helper.UIHelper;
import com.app.tryitat.ui.comment.model.CommentDataModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Context context;
    List<CommentDataModel> commentDataList;

    public CommentAdapter(Context context, List<CommentDataModel> commentDataList){
        this.context = context;
        this.commentDataList = commentDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCommentBinding binding = ItemCommentBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentDataModel comment = commentDataList.get(position);
        holder.binding.userName.setText(comment.getUserName());
        holder.binding.commentTv.setText(comment.getCommentxt());
        UIHelper.getUsersPhotos(context, comment.getUserId(), holder.binding.userImage);
    }

    @Override
    public int getItemCount() {
        return commentDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemCommentBinding binding;
        public ViewHolder(@NonNull ItemCommentBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;
        }
    }
}
