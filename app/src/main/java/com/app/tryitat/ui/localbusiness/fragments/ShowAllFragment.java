package com.app.tryitat.ui.localbusiness.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tryitat.R;
import com.app.tryitat.databinding.FragmentShowAllBinding;
import com.app.tryitat.helper.Constant;
import com.app.tryitat.ui.clientprofile.model.ClientDataModel;
import com.app.tryitat.ui.localbusiness.fragments.adapter.ShowAllAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShowAllFragment extends Fragment {
    private FragmentShowAllBinding binding;
    private ShowAllAdapter allAdapter;
    private List<ClientDataModel> clientDataModels = new ArrayList<>();
    private FirebaseDatabase database;

    public ShowAllFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShowAllBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        initAllRcv();
        getAllClients();
    }

    private void initAllRcv(){
        allAdapter = new ShowAllAdapter(getContext(), clientDataModels);
        binding.clientListRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.clientListRcv.setAdapter(allAdapter);
    }

    private void getAllClients(){
        database.getReference("Clients").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        ClientDataModel clientDataModel = snapshot1.getValue(ClientDataModel.class);
                        clientDataModels.add(clientDataModel);
                    }
                    allAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}