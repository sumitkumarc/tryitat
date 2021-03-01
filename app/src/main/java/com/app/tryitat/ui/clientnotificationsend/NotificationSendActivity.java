package com.app.tryitat.ui.clientnotificationsend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.app.tryitat.R;
import com.app.tryitat.databinding.ActivityNotificationListBinding;
import com.app.tryitat.databinding.ActivityNotificationSendBinding;
import com.app.tryitat.ui.clientcustomerlist.CustomerListActivity;
import com.app.tryitat.utils.Common;
import com.kaopiz.kprogresshud.KProgressHUD;

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
            showProgress();
            if (Common.isInternetAvailable(NotificationSendActivity.this)) {
                if (!Common.edvalidateName(binding.edTitle.getText().toString(), binding.edTitle, "Enter Notification title") || !Common.edvalidateName(binding.edNotMsg.getText().toString(), binding.edNotMsg, "Enter Notification message")) {
                    return;
                }

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        // your async action
//                        pushNotification(binding.edTitle.getText().toString(), binding.edNotMsg.getText().toString());
                        JSONObject jPayload = new JSONObject();
                        JSONObject jNotification = new JSONObject();
                        JSONObject jData = new JSONObject();
                        try {
                            jNotification.put("title", binding.edTitle.getText().toString());
                            jNotification.put("body", binding.edNotMsg.getText().toString());
                            jPayload.put("to", FCM_TOKEN);

                            jPayload.put("priority", "high");
                            jPayload.put("notification", jNotification);
                            jPayload.put("data", jData);

                            URL url = new URL("https://fcm.googleapis.com/fcm/send");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Authorization", "key=AAAAOuokWEs:APA91bH1w9JBD7ZXTFWzyCLfzmdGQUUWggJ15e_TNLsnk22PbgRWMwhNTGJ45o9jVR-TIKDrLrrqZWuikwmilGxW6Q7ZwRBkj5sBFi7iiS-RQe33mRetkF07bZgVzBDQQTRRaKqklILt");
                            conn.setRequestProperty("Content-Type", "application/json");
                            conn.setDoOutput(true);

                            // Send FCM message content.
                            OutputStream outputStream = conn.getOutputStream();
                            outputStream.write(jPayload.toString().getBytes());

                            // Read FCM response.
                            InputStream inputStream = conn.getInputStream();
                            final String resp = convertStreamToString(inputStream);

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    dismissProgress();
                                    binding.edTitle.setText("");
                                    binding.edNotMsg.setText("");
                                    finish();
                                    Toast.makeText(NotificationSendActivity.this, "Notification send successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    dismissProgress();
                                    Toast.makeText(NotificationSendActivity.this, "Notification not send successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        // update the UI (this is executed on UI thread)
                        super.onPostExecute(aVoid);
                    }
                }.execute();

            }

        });
    }
    KProgressHUD progressHUD;

    private void showProgress() {
        progressHUD = KProgressHUD.create(NotificationSendActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.3f)
                .show();
    }

    private void dismissProgress() {
        if (progressHUD != null) {
            progressHUD.dismiss();
        }
    }




    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
}