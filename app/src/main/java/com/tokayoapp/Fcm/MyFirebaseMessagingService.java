package com.tokayoapp.Fcm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tokayoapp.Activities.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.tokayoapp.Fcm.Config.PUSH_NOTIFICATION;


public class MyFirebaseMessagingService  extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    Bitmap bitmap;
    Intent resultIntent;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());


        String body = "uererer";
        Intent myIntent = new Intent("FBR-IMAGE");
        myIntent.putExtra("action", body);
        this.sendBroadcast(myIntent);

        if (remoteMessage == null)
            return;

        if (remoteMessage.getNotification() != null) {
            Log.e("fbdfgegdg", "Notification Body: " + remoteMessage.getNotification().getBody());

        }
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e("fbdfgegdg", "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
                Log.e("jbxjs", json.toString());
            } catch (Exception e) {
                Log.e("fbdfgegdg", "Exception: " + e.getMessage());
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            Intent pushNotification = new Intent(PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());


        try {
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");

            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e("jkfhdjkfh", imageUrl);
            Log.e("jgithoig", "title: " + title);
            Log.e("jgithoig", "message: " + message);
            Log.e("jgithoig", "isBackground: " + isBackground);
            Log.e("jgithoig", "payload: " + payload.toString());
            Log.e("jgithoig", "imageUrl: " + imageUrl);
            Log.e("jgithoig", "timestamp: " + timestamp);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();

                resultIntent = new Intent(this, MainActivity.class);
                resultIntent.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);



                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(),
                            title, message.split("/")[0], timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(),
                            title, message.split("/")[0], timestamp, resultIntent, imageUrl);
                }
            } else {

                NotificationUtils notificationUtils1 = new NotificationUtils(getApplicationContext());
                notificationUtils1.playNotificationSound();
                resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message.split("/")[0], timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message.split("/")[0], timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);

        final NotificationCompat.Builder builder =new NotificationCompat.Builder(this);

        Log.e("shdsjcf", imageUrl+"" );

    }
}