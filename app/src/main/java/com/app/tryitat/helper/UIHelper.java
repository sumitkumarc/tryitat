package com.app.tryitat.helper;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.app.tryitat.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.text.DateFormat.getDateTimeInstance;

public class UIHelper {

    public static void getUsersPhotos(Context context, String userId, CircleImageView imageView){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("User").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String photoUrl = snapshot.child("picture").getValue(String.class);
                    Glide.with(context).load(photoUrl).placeholder(R.drawable.ic_person).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String getTimeDate(long timestamp){

        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sfd.format(new Date(timestamp));

//        try{
//            DateFormat dateFormat = getDateTimeInstance();
//            Date netDate = (new Date(timestamp));
//            Log.e("SMD", "getTimeDate: " + dateFormat.format(netDate));
//            return dateFormat.format(netDate);
//        } catch(Exception e) {
//            return "date";
//        }
    }

}
