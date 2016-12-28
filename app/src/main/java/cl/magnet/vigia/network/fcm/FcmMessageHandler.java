package cl.magnet.vigia.network.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import cl.magnet.vigia.R;
import cl.magnet.vigia.activities.DrawerActivity;

/**
 * Created by edwinperaza on 12/26/16.
 */

public class FcmMessageHandler extends FirebaseMessagingService {

    public static final int MESSAGE_NOTIFICATION_ID = 435345;
    public static final String NOTIFICATION_TYPE = "notification_type";
    public static final String NOTIFICATION_ID = "notification_id";
    public static final String TAG = FcmMessageHandler.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();

        String notificationId = data.get(NOTIFICATION_ID);
        String notificationType = data.get(NOTIFICATION_TYPE);
        Log.d("NOTIFICATION", "RECEIVED");
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        createNotification(notification);
    }


    private void createNotification(RemoteMessage.Notification notification) {

        Intent intent = new Intent(this, DrawerActivity.class);

        int requestID = (int) System.currentTimeMillis();
        int flags = PendingIntent.FLAG_CANCEL_CURRENT; // cancel old intent and create new one
        PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, flags);

        Notification notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(0, notificationBuilder);
    }

}
