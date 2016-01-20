package cl.magnet.magnetprojecttemplate.network.gcm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import cl.magnet.magnetprojecttemplate.models.user.User;
import cl.magnet.magnetprojecttemplate.utils.PrefsManager;

/**
 * Created by Tito_Leiva on 28-09-15.
 */
public class GcmManager {

    public static final String TAG = GcmManager.class.getSimpleName();
    public static final String KEY_USER = "user";
    public static final String KEY_PASSWORD = "password";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private Context mContext;
    private Activity mActivity;
    private BroadcastReceiver mRegistrationReceiver;

    public GcmManager(Activity activity) {
        mActivity = activity;
        mContext = activity.getApplicationContext();
    }

    public void setGCMReceiver() {

        mRegistrationReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                boolean sentToken = PrefsManager.getSentTokenToServer(mContext);

            }
        };
    }

    public void startGCMService(User user, Bundle extras) {

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(mContext, GcmRegistrationIntentService.class);

            intent.putExtra(KEY_USER, user.getUsername());
            intent.putExtra(KEY_PASSWORD, user.getPassword());

            if (extras != null) {
                intent.putExtras(extras);
            }

            mContext.startService(intent);
        }
    }

    public boolean checkPlayServices() {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);

        if (resultCode != ConnectionResult.SUCCESS) {

            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, mActivity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();

            } else {

                Log.i(TAG, "This device is not supported.");
                mActivity.finish();
            }

            return false;
        }

        return true;
    }

    private void unsubscribeToken() {

//        String token = PrefsManager.getGCMToken(mContext);
//        TokenRequest tokenRequest = new TokenRequest(token, this, false, mContext);
//
//        tokenRequest.request();

    }

    public void registerGcmReceiver() {

        LocalBroadcastManager.getInstance(mContext).registerReceiver(mRegistrationReceiver,
                new IntentFilter(GcmRegistrationIntentService.GCM_REGISTRATION_COMPLETE));

    }

    public void unregisterGcmReceiver() {

        if (mRegistrationReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mRegistrationReceiver);
        }
    }




}
