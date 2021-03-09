package com.app.tryitat.ui.clientnotificationsend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ItemUserNotificationListBinding;
import com.app.tryitat.ui.clientcustomerlist.RvCustomerListAdapter;
import com.app.tryitat.ui.profile.model.UserDataModel;
import com.app.tryitat.utils.Common;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RvNotCustomerListAdapter extends RecyclerView.Adapter<RvNotCustomerListAdapter.MyViewHolder> {
    Context mcontext;
    List<String> mStrings;


    public RvNotCustomerListAdapter(Context context, List<String> customerList) {
        mcontext = context;
        mStrings = customerList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserNotificationListBinding binding = ItemUserNotificationListBinding.inflate(LayoutInflater.from(mcontext), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FirebaseDatabase.getInstance().getReference("User").child(mStrings.get(position))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            UserDataModel userDataModel = snapshot.getValue(UserDataModel.class);
                            holder.binding.txtName.setText(Common.isStrempty(userDataModel.getName()));
                            holder.binding.txtPoints.setText(Common.isStrempty(String.valueOf(userDataModel.getPoints())) + " Points");
                            Glide.with(mcontext).load(Common.isStrempty(userDataModel.getPicture())).placeholder(mcontext.getResources().getDrawable(R.drawable.default_image_100)).error(mcontext.getResources().getDrawable(R.drawable.default_image_100)).into(holder.binding.ivImg);
                            holder.binding.updateBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!String.valueOf(userDataModel.getFcmToken()).equals("")) {
                                        Intent intent = new Intent(mcontext, NotificationSendActivity.class);
                                        intent.putExtra("FCM_TOKEN", Common.isStrempty(String.valueOf(userDataModel.getFcmToken())));
                                        intent.putExtra("USER_NAME", Common.isStrempty(String.valueOf(userDataModel.getName())));
                                        mcontext.startActivity(intent);
                                    } else {
                                        Toast.makeText(mcontext, "Token not store...", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        } catch (Exception e) {
                            holder.binding.txtName.setText("-");
                            holder.binding.txtPoints.setText("-");
                            Glide.with(mcontext).load("-").placeholder(mcontext.getResources().getDrawable(R.drawable.ic_profile)).into(holder.binding.ivImg);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemUserNotificationListBinding binding;

        public MyViewHolder(ItemUserNotificationListBinding bind) {
            super(bind.getRoot());
            binding = bind;

        }

    }
}

