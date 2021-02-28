package com.app.tryitat.ui.clientnotificationsend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ActivityNotificationListBinding;
import com.app.tryitat.databinding.ActivityNotificationSendBinding;
import com.app.tryitat.utils.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NotificationSendActivity extends AppCompatActivity {
    ActivityNotificationSendBinding binding;
    String FCM_TOKEN;
    String USER_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationSendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FCM_TOKEN = getIntent().getStringExtra("FCM_TOKEN");
        USER_NAME = getIntent().getStringExtra("USER_NAME");
        binding.txtTitle.setText("Send notification to "+ USER_NAME);
        initClickListener();
    }

    private void initClickListener() {
        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.updateBtn.setOnClickListener(v -> {

            if (Common.isInternetAvailable(NotificationSendActivity.this)) {
                if (Common.edvalidateName(binding.edTitle.getText().toString(), binding.edTitle, "Enter Notification title") || Common.edvalidateName(binding.edNotMsg.getText().toString(), binding.edNotMsg, "Enter Notification message")) {
                    return;
                }
                pushNotification(binding.edTitle.getText().toString(), binding.edNotMsg.getText().toString());
            }

        });
    }

    private void pushNotification(String toString, String toString1) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", toString);
            jNotification.put("body", toString1);
//            jNotification.put("click_action", "OPEN_ACTIVITY_1");
//            jNotification.put("icon", "ic_notification");

//            jData.put("picture", "https://miro.medium.com/max/1400/1*QyVPcBbT_jENl8TGblk52w.png");
            jPayload.put("to", FCM_TOKEN);

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "AAAAOuokWEs:APA91bH1w9JBD7ZXTFWzyCLfzmdGQUUWggJ15e_TNLsnk22PbgRWMwhNTGJ45o9jVR-TIKDrLrrqZWuikwmilGxW6Q7ZwRBkj5sBFi7iiS-RQe33mRetkF07bZgVzBDQQTRRaKqklILt");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    //  mTextView.setText(resp);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
}