package cl.magnet.vigia.network.fcm;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by edwinperaza on 12/26/16.
 */

public class FcmIdListenerService extends FirebaseInstanceIdService {

    public static final String TAG = FcmIdListenerService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {

        Intent intent = new Intent(this, FcmRegistrationIntentService.class);
        startService(intent);
    }
}
