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

    private Class<Activity> activityClass;


    /**
     * @return el mensaje de la notificación
     */
    public abstract String getBody();

    /**
     * @param context
     * @return el sonido de la notificación
     */
    public abstract Uri getSound(Context context);

    /**
     * @return el ícono de la notificación
     */
    public abstract int getIcon();

    /**
     * @return el título de la notificación
     */
    public abstract String getTitle();

    public void sendNotification(Context context, Bundle extras) {

        Intent intent = new Intent(context, activityClass);

        intent.putExtras(extras);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(getIcon())
                .setContentTitle(getTitle())
                .setContentText(getBody())
                .setAutoCancel(true)
                .setSound(getSound(context))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
