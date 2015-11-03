package cl.magnet.magnetprojecttemplate.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import cl.magnet.magnetprojecttemplate.R;
import cl.magnet.magnetprojecttemplate.activities.LoginActivity;
import cl.magnet.magnetprojecttemplate.activities.UpgradeRequiredActivity;
import cl.magnet.magnetrestclient.MagnetErrorListener;

/**
 * Created by yaniv on 11/3/15.
 */
public class AppErrorListener extends MagnetErrorListener {

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

    //Finish current activty and clear from back stack. Then start next activity.
    public void finishAndGoTo(Class klass){
        Intent intent = new Intent(mContext, klass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        ((Activity) mContext).finish();
    }

    @Override
    public <T> void onUnauthorizedError(VolleyError volleyError, Request<T> request) {
        Toast.makeText(mContext, R.string.error_unauthorized, Toast.LENGTH_SHORT).show();

        (new cl.magnet.usermanager.UserManager<>(mContext)).logout();

        finishAndGoTo(LoginActivity.class);

    }

    @Override
    public void onUpgradeRequiredError(VolleyError volleyError) {
        finishAndGoTo(UpgradeRequiredActivity.class);
    }

    @Override
    public void onUnhandledError(VolleyError volleyError) {

    }
}
