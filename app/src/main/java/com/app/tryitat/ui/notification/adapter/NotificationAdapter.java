package com.app.tryitat.ui.notification.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tryitat.databinding.ItemClientListBinding;
import com.app.tryitat.databinding.ItemNotificationBinding;
import com.app.tryitat.helper.Constant;

import com.app.tryitat.ui.notification.model.NotificationResponse;
import com.app.tryitat.ui.profile.model.UserDataModel;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    Context context;
    List<NotificationResponse> notificationResponseList;
    private RecyclerViewClickListener listener;

    public NotificationAdapter(Context context, List<NotificationResponse> notificationResponseList,RecyclerViewClickListener listener) {
        this.context = context;
        this.notificationResponseList = notificationResponseList;
        this.listener=listener;

       }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NotificationResponse notificationResponse= notificationResponseList.get(position);

        FirebaseDatabase.getInstance().getReference("User").child(notificationResponse.getCurrentUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserDataModel userDataModel=snapshot.getValue(UserDataModel.class);
                        holder.binding.userNameNot.setText(userDataModel.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        holder.binding.commentNot.setText(notificationResponse.getType());
        Glide.with(context).load(notificationResponse.getPostimage()).into(holder.binding.postImage);


    }

    @Override
    public int getItemCount() {
        return notificationResponseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ItemNotificationBinding binding;

        public ViewHolder(@NonNull ItemNotificationBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

           itemView.getRoot().setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            listener.onClick(v,getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v,int position);
    }


}
