package com.app.tryitat.ui.pointlist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ItemUserListBinding;
import com.app.tryitat.databinding.ItemUserPointListBinding;
import com.app.tryitat.ui.dashboard.interfaces.PickerOptionListener;
import com.app.tryitat.ui.profile.model.UserDataModel;
import com.app.tryitat.ui.userpointbord.UserPointActivity;
import com.app.tryitat.ui.userqrcode.QrCodeActivity;
import com.app.tryitat.utils.Common;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RvPointsCustomerListAdapter extends RecyclerView.Adapter<RvPointsCustomerListAdapter.MyViewHolder> {
    Context mcontext;
    List<PointModel> mStrings;

    public RvPointsCustomerListAdapter(Context context, List<PointModel> customerList) {
        mcontext = context;
        mStrings = customerList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemUserPointListBinding binding = ItemUserPointListBinding.inflate(LayoutInflater.from(mcontext), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        try {
            holder.binding.txtName.setText(Common.isStrempty(mStrings.get(position).getClientName().toString()));
            holder.binding.txtPoints.setText(Common.isStrempty(String.valueOf(mStrings.get(position).getTotalPoints()).toString()) + " Points");
            Glide.with(mcontext).load(Common.isStrempty(mStrings.get(position).getClientPic())).placeholder(mcontext.getResources().getDrawable(R.drawable.default_image_100)).error(mcontext.getResources().getDrawable(R.drawable.default_image_100)).into(holder.binding.ivImg);
            if (mStrings.get(position).getShowCamera().toLowerCase().equals("yes")) {
                holder.binding.ivCamera.setVisibility(View.VISIBLE);
                holder.binding.txtRedeem.setVisibility(View.GONE);
                holder.binding.ivCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.CLIENT_NAME = mStrings.get(position).getClientName();
                        Common.CLIENT_ID = mStrings.get(position).getClientID();
                        Common.POST_OBJECT_ID = mStrings.get(position).getObjectId();
                        Common.TOTAL_POINT = mStrings.get(position).getTotalPoints();
                        Common.UPDATE_POINT_POST = 1;
                        if (mStrings.get(position).getTotalPoints() == 5) {
                            Toast.makeText(mcontext, "Client not Scanning our Qr Code..", Toast.LENGTH_SHORT).show();
                        } else if (mStrings.get(position).getTotalPoints() == 10) {
                            PointListActivity.showImagePickerOptions(mcontext, (PickerOptionListener) mcontext);
                        }

                    }
                });
            } else if (mStrings.get(position).getShowCamera().toLowerCase().equals("no")){
                holder.binding.ivCamera.setVisibility(View.GONE);
                holder.binding.txtRedeem.setVisibility(View.VISIBLE);
                holder.binding.txtRedeem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sIntent = new Intent(mcontext, UserPointActivity.class);
                        sIntent.putExtra("ClientName",mStrings.get(position).getClientName());
                        sIntent.putExtra("TotalPoints",mStrings.get(position).getTotalPoints());
                        sIntent.putExtra("ClientPic",mStrings.get(position).getClientPic());
                        sIntent.putExtra("ObjectId",mStrings.get(position).getObjectId());
                        sIntent.putExtra("Visits",mStrings.get(position).getVisits());
                        mcontext.startActivity(sIntent);
                    }
                });
            }
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ItemUserPointListBinding binding;

        public MyViewHolder(ItemUserPointListBinding bind) {
            super(bind.getRoot());
            binding = bind;

        }

    }
}
