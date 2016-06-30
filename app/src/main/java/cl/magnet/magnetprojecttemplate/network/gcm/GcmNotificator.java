package cl.magnet.magnetprojecttemplate.network.gcm;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

/**
 * Clase que maneja el contenido de las notificaciones
 * <p/>
 * Created by Tito_Leiva on 29-09-15.
 */
public abstract class GcmNotificator {

    /**
     * @return the message to show
     */
    public abstract String getBody();

    /**
     * @param context
     * @return the notification sound
     */
    public abstract Uri getSound(Context context);

    /**
     * @return the notification icon
     */
    public abstract int getIcon();

    /**
     * @return the notification title
     */
    public abstract String getTitle();

    /**
     * @param context
     * @return the color for the background of the notification's icon.
     */
    protected abstract int getColor(Context context);


    /**
     * Creates a notification and shows it
     *
     * @param context
     * @param activityClass
     * @param extras
     */
    public void sendNotification(Context context, Class activityClass, Bundle extras) {

        Intent intent = new Intent(context, activityClass);

        intent.putExtras(extras);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(getIcon())
                .setColor(getColor(context))
                .setContentTitle(getTitle())
                .setContentText(getBody())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getBody()))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(getSound(context))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}