package cl.magnet.magnetprojecttemplate.network.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Tito_Leiva on 30-07-15.
 */
public class GcmIdListenerService extends InstanceIDListenerService {

    public static final String TAG = GcmIdListenerService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {

        Intent intent = new Intent(this, GcmRegistrationIntentService.class);
        startService(intent);
    }
}
