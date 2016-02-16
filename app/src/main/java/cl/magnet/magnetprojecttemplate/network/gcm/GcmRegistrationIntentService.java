package cl.magnet.magnetprojecttemplate.network.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONObject;

import cl.magnet.magnetprojecttemplate.R;
import cl.magnet.magnetprojecttemplate.utils.PrefsManager;

/**
 * Created by Tito_Leiva on 30-07-15.
 */
public class GcmRegistrationIntentService extends IntentService {

    public static final String TAG = GcmRegistrationIntentService.class.getSimpleName();
    public static final String GCM_REGISTRATION_COMPLETE = "gcm_registration_complete";
    private static final String[] TOPICS = {"global"};

    public GcmRegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                // [START register_for_gcm]
                // Initially this call goes out to the network to retrieve the token, subsequent calls
                // are local.
                // [START get_token]
                InstanceID instanceID = InstanceID.getInstance(this);

                //FIXME Add the google-services.json and uncomment the getString(R.string.gcm_defaultSenderId)
                String token = instanceID.getToken("" //getString(R.string.gcm_defaultSenderId)
                        , GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                // [END get_token]
                Log.i(TAG, "GCM Registration Token: " + token);

                sendRegistrationToServer(token);

                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.
                PrefsManager.setSentTokenToServer(this, true);
                // [END register_for_gcm]
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            PrefsManager.setSentTokenToServer(this, false);
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(GCM_REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }

    private void sendRegistrationToServer(String token) {

        GcmTokenRequest gcmTokenRequest = new GcmTokenRequest(this);
        gcmTokenRequest.suscribeToken(token);

    }
}
