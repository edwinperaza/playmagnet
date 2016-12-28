package cl.magnet.vigia.network.fcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import cl.magnet.vigia.R;
import cl.magnet.vigia.utils.PrefsManager;

/**
 * Created by edwinperaza on 12/26/16.
 */

public class FcmRegistrationIntentService extends IntentService {

    public static final String TAG = FcmRegistrationIntentService.class.getSimpleName();
    public static final String FCM_REGISTRATION_COMPLETE = "fcm_registration_complete";

    public FcmRegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Make a call to Instance API
        FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();
//        String senderId = getResources().getString(R.string.gcm_defaultSenderId);
        try {
            // request token that will be used by the server to send push notifications
            String token = instanceID.getToken();
            Log.d(TAG, "FCM Registration Token: " + token);
            PrefsManager.setGCMToken(this, token);
            sendRegistrationToServer(token);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to complete token refresh", e);
//             If an exception happens while fetching the new token or updating our registration data
//             on a third-party server, this ensures that we'll attempt the update at a later time.
            PrefsManager.setSentTokenToServer(this, false);
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(FCM_REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }

    private void sendRegistrationToServer(String token) {

        FcmTokenRequest fcmTokenRequest = new FcmTokenRequest(this);
        fcmTokenRequest.suscribeToken(token);

    }
}
