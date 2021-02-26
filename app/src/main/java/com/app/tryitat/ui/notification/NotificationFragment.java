package com.app.tryitat.ui.notification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.tryitat.R;
import com.app.tryitat.databinding.FragmentNotificationBinding;
import com.app.tryitat.ui.notification.adapter.NotificationAdapter;

import com.app.tryitat.ui.notification.model.NotificationResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationFragment extends Fragment {

    private FragmentNotificationBinding binding;
    private NotificationAdapter notificationAdapter;
    private List<NotificationResponse> notificationResponsesLists=new ArrayList<>();
    private FirebaseDatabase notificationDatabase;
    private DatabaseReference notificationRef;
    private NotificationAdapter.RecyclerViewClickListener listener;

    public NotificationFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding=FragmentNotificationBinding.inflate(inflater,container,false);

       return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        notificationDatabase= FirebaseDatabase.getInstance();
        recievePost();
        getAllPost();
        setOnClickListener();
    }

    private void recievePost() {

        setOnClickListener();
        notificationAdapter=new NotificationAdapter(getContext(),notificationResponsesLists,listener);
        binding.recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerViewNotifications.setAdapter(notificationAdapter);

    }

    private void setOnClickListener() {
        listener=new NotificationAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {


               String n= notificationResponsesLists.get(position).getCurrentUserId();
               Toast.makeText(getContext(),n,Toast.LENGTH_LONG).show();

            }
        };
    }

    private void getAllPost() {

        notificationRef= notificationDatabase.getReference("Notification");
        notificationRef.orderByChild(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){

                    NotificationResponse notificationResponses=snapshot1.getValue(NotificationResponse.class);
                    notificationResponsesLists.add(notificationResponses);

                }

                Collections.reverse(notificationResponsesLists);
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}