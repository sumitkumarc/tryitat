package com.app.tryitat.ui.clientcustomerlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ItemClientListBinding;
import com.app.tryitat.databinding.ItemNotificationBinding;
import com.app.tryitat.databinding.ItemUserListBinding;
import com.app.tryitat.ui.notification.adapter.NotificationAdapter;
import com.app.tryitat.ui.notification.model.NotificationResponse;
import com.app.tryitat.ui.profile.model.UserDataModel;
import com.app.tryitat.utils.Common;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.List;

public class RvCustomerListAdapter extends RecyclerView.Adapter<RvCustomerListAdapter.MyViewHolder> {
    Context mcontext;
    List<String> mStrings;

    public RvCustomerListAdapter(Context context, List<String> customerList) {
        mcontext = context;
        mStrings = customerList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserListBinding binding = ItemUserListBinding.inflate(LayoutInflater.from(mcontext), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FirebaseDatabase.getInstance().getReference("User").child(mStrings.get(position))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            UserDataModel userDataModel=snapshot.getValue(UserDataModel.class);
                            holder.binding.txtName.setText(Common.isStrempty(userDataModel.getName().toString()));
                            holder.binding.txtPoints.setText(Common.isStrempty(String.valueOf(userDataModel.getPoints()).toString()) + " Points");
                            Glide.with(mcontext).load(Common.isStrempty(userDataModel.getPicture())).placeholder(mcontext.getResources().getDrawable(R.drawable.default_image_100)).error(mcontext.getResources().getDrawable(R.drawable.default_image_100)).into(holder.binding.ivImg);
                        }catch (Exception e){
//                            holder.binding.txtName.setText("-");
//                            holder.binding.txtPoints.setText("-");
//                            Glide.with(mcontext).load("-").placeholder(mcontext.getResources().getDrawable(R.drawable.ic_profile)).into(holder.binding.ivImg);
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

        ItemUserListBinding binding;

        public MyViewHolder(ItemUserListBinding bind) {
            super(bind.getRoot());
            binding = bind;

        }

    }
}
