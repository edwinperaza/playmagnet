package cl.magnet.magnetprojecttemplate.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import cl.magnet.magnetprojecttemplate.R;
import cl.magnet.magnetprojecttemplate.activities.LoginActivity;
import cl.magnet.magnetprojecttemplate.activities.UpgradeRequiredActivity;
import cl.magnet.magnetprojecttemplate.models.user.User;
import cl.magnet.magnetprojecttemplate.utils.PrefsManager;
import cl.magnet.magnetrestclient.MagnetErrorListener;
import cl.magnet.usermanager.UserManager;

/**
 * Created by yaniv on 11/3/15.
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

    @Override
    public <T> void onUnauthorizedError(VolleyError volleyError, Request<T> request) {
        Toast.makeText(mContext, R.string.error_unauthorized, Toast.LENGTH_SHORT).show();

        //TODO: Use method UserManager.logout. Example: (new UserManager<User>(mContext)).logout();
        PrefsManager.clearPrefs(mContext);

        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(ACTION_UNAUTHORIZED));
    }

    @Override
    public void onUpgradeRequiredError(VolleyError volleyError) {
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(ACTION_UPGRADE_REQUIRED));
    }

    @Override
    public void onUnhandledError(VolleyError volleyError) {
        Toast.makeText(mContext, R.string.error_unknown, Toast.LENGTH_SHORT).show();
    }
}
