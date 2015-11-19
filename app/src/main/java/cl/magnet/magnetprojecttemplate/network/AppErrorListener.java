package cl.magnet.magnetprojecttemplate.network;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import cl.magnet.magnetprojecttemplate.R;
import cl.magnet.magnetprojecttemplate.utils.PrefsManager;
import cl.magnet.magnetrestclient.MagnetErrorListener;

/**
 * Created by yaniv on 11/3/15.
 */

/**
 * Class in charge of managing HTTP errors
 */
public class AppErrorListener extends MagnetErrorListener {

    public static final String ACTION_UNAUTHORIZED = "AppErrorListener.Unauthorized";
    public static final String ACTION_UPGRADE_REQUIRED = "AppErrorListener.UpgradeRequired";

    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_UPGRADE_REQUIRED = 426;

    private Context mContext;

    public AppErrorListener(Context context) {
        // prevents an activity or broadcast receiver leak by getting the application context
        mContext = context.getApplicationContext();
    }

    /**
     * This method manages every HTTP error and then calls corresponding error method
     * When using a Volley request create a new AppErrorListener and call super.onErrorResponse in the Error Listener
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        NetworkResponse networkResponse = error.networkResponse;

        if(networkResponse != null){

            switch(networkResponse.statusCode){
                case HTTP_UNAUTHORIZED: onUnauthorizedError(error, null); break;
                case HTTP_UPGRADE_REQUIRED: onUpgradeRequiredError(error); break;
                default: onUnhandledError(error); break;
            }

        }
    }

    //If we get a 401 we log out the user and call Login Activity
    @Override
    public <T> void onUnauthorizedError(VolleyError volleyError, Request<T> request) {
        Toast.makeText(mContext, R.string.error_unauthorized, Toast.LENGTH_SHORT).show();

        //TODO: Use method UserManager.logout. Example: (new UserManager<User>(mContext)).logout();
        PrefsManager.clearPrefs(mContext);

        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(ACTION_UNAUTHORIZED));
    }

    //If we get a 426 we close all activities and go to the Upgrade required Activity
    @Override
    public void onUpgradeRequiredError(VolleyError volleyError) {
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(ACTION_UPGRADE_REQUIRED));
    }

    @Override
    public void onUnhandledError(VolleyError volleyError) {
        Toast.makeText(mContext, R.string.error_unknown, Toast.LENGTH_SHORT).show();
    }
}
