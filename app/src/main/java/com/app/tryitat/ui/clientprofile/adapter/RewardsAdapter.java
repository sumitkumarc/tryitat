package com.app.tryitat.ui.clientprofile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tryitat.databinding.ItemRewardsBinding;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.ViewHolder> {
    Context context;
    public RewardsAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRewardsBinding binding = ItemRewardsBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemRewardsBinding binding;
        public ViewHolder(@NonNull ItemRewardsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
