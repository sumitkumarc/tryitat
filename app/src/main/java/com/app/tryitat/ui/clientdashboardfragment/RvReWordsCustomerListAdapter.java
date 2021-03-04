package com.app.tryitat.ui.clientdashboardfragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ItemUserListBinding;
import com.app.tryitat.databinding.ItemUserNotificationListBinding;
import com.app.tryitat.ui.clientnotificationsend.NotificationSendActivity;
import com.app.tryitat.ui.profile.model.UserDataModel;
import com.app.tryitat.utils.Common;
import com.app.tryitat.utils.rv_interface;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RvReWordsCustomerListAdapter extends RecyclerView.Adapter<RvReWordsCustomerListAdapter.MyViewHolder> {
    Context mcontext;
    List<String> mrewardsImageList;
    List<String> mrewardsPriceList;
    int points = 50;
    int oldpoints = 0;
    Boolean mBoolean;
    rv_interface MrvInterface;

    public RvReWordsCustomerListAdapter(Context context, List<String> mImageList, List<String> mPriceList ,Boolean aBoolean) {
        mcontext = context;
        mrewardsImageList = mImageList;
        mrewardsPriceList = mPriceList;
        mBoolean = aBoolean;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserListBinding binding = ItemUserListBinding.inflate(LayoutInflater.from(mcontext), parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        int iii =(position + 1) * points;
        holder.binding.txtName.setText(String.valueOf(iii) + " Points");
        holder.binding.txtPoints.setText(mrewardsPriceList.get(position));
        Glide.with(mcontext).load(Common.isStrempty(mrewardsImageList.get(position))).placeholder(mcontext.getResources().getDrawable(R.drawable.ic_profile)).into(holder.binding.ivImg);

         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(mBoolean){
                     MrvInterface.OnItemClick(mrewardsPriceList.get(position),mrewardsImageList.get(position),position);
                 }
             }
         });

    }
    public void setOnItemClickListener(rv_interface rvsInterface) {
        MrvInterface = rvsInterface;

    }



    @Override
    public int getItemCount() {
        return mrewardsImageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemUserListBinding binding;

        public MyViewHolder(ItemUserListBinding bind) {
            super(bind.getRoot());
            binding = bind;

        }

    }
}

