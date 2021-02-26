package com.app.tryitat.ui.localbusiness.fragments.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ItemClientListBinding;
import com.app.tryitat.ui.clientprofile.model.ClientDataModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class ShowAllAdapter extends RecyclerView.Adapter<ShowAllAdapter.ViewHolder> {
    Context context;
    List<ClientDataModel> clientDataModels;
    public ShowAllAdapter(Context context, List<ClientDataModel> clientDataModels){
        this.context = context;
        this.clientDataModels = clientDataModels;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemClientListBinding binding = ItemClientListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(clientDataModels.get(position).getPicture()).placeholder(R.drawable.ic_person).into(holder.binding.rewardsImg);

        if (clientDataModels.get(position).getAddress()!=null){
            holder.binding.rewardsTxt.setText(clientDataModels.get(position).getAddress());
        } else {
            holder.binding.rewardsTxt.setVisibility(View.GONE);
        }

        holder.binding.pointTxt.setText(clientDataModels.get(position).getStoreName());
    }

    @Override
    public int getItemCount() {
        return clientDataModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemClientListBinding binding;
        public ViewHolder(@NonNull ItemClientListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
