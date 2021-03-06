package cl.magnet.playmagnet.network;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import cl.magnet.playmagnet.R;
import cl.magnet.playmagnet.models.user.UserRequestManager;
import cl.magnet.playmagnet.utils.PrefsManager;
import cl.magnet.magnetrestclient.MagnetErrorListener;
import cl.magnet.magnetrestclient.VolleyErrorHelper;
import cl.magnet.magnetrestclient.VolleyManager;

/**
 * Created by yaniv on 11/3/15.
 */

/**
 * Class in charge of managing Volley Responses
 */
public class AppResponseListener<T> extends MagnetErrorListener implements Response.Listener<T>{

    public static final String ACTION_UNAUTHORIZED = "AppErrorListener.Unauthorized";
    public static final String ACTION_UPGRADE_REQUIRED = "AppErrorListener.UpgradeRequired";

    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_UPGRADE_REQUIRED = 426;

    private Context mContext;

    public AppResponseListener(Context context) {
        super(context);
        // prevents an activity or broadcast receiver leak by getting the application context
        mContext = context.getApplicationContext();
    }

    @Override
    public void onResponse(T response) {
        onPostResponse();
    }

    /**
     * This method manages every HTTP error and then calls corresponding error method
     * If new implementation is needed, override and call super.onErrorResponse(error) in the Error Listener
     */
    @Override
    public void onErrorResponse(VolleyError error) {

        NetworkResponse networkResponse = error.networkResponse;

        if(networkResponse != null){

            switch(networkResponse.statusCode){
                case HTTP_UNAUTHORIZED: onUnauthorizedError(error); break;
                case HTTP_UPGRADE_REQUIRED: onUpgradeRequiredError(error); break;
                case HTTP_BAD_REQUEST: onBadRequest(error); break;
                default: onUnhandledError(error); break;
            }

        }
        else{
            noInternetError();
        }

        onPostResponse();
    }

    @Override
    public void noInternetConnectionError() {

    }

    /**
     * This must be called whenever we need a common behaviour after either success or failure.
     * It must be overridden on the AppResponseListener instance
     */
    public void onPostResponse(){}

    /**
     * If we get a 401 we try to relogin using the Shared Preferences.
     * If we fail we log out the user and call Login Activity.
     * This method must be overridden only in the LoginActivity request. Nowhere else.
     */
    @Override
    public <T> void onUnauthorizedError(VolleyError volleyError) {

        final String email = PrefsManager.getStringPref(mContext, PrefsManager.PREF_USER_EMAIL);
        final String password = PrefsManager.getStringPref(mContext, PrefsManager.PREF_USER_PASSWORD);

        //We set the response listener with corresponding overridden methods
        AppResponseListener<JSONObject> responseListener = new AppResponseListener<JSONObject>(mContext){

            //On success we update the Shared Preferences of the user
            @Override
            public void onResponse(JSONObject response) {

                String firstName = null;
                String lastName = null;
                try {
                    firstName = response.getString(UserRequestManager.FIRST_NAME);
                    lastName = response.getString(UserRequestManager.LAST_NAME);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //TODO: Use method UserManager.logInUser loading user from database
                PrefsManager.saveUserCredentials(mContext, email, password);
                PrefsManager.setStringPref(mContext, PrefsManager.PREF_USER_FIRST_NAME, firstName);
                PrefsManager.setStringPref(mContext, PrefsManager.PREF_USER_LAST_NAME, lastName);
            }

            //On failure we clear the Preferences and send local broadcast to log out user
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO: Use method UserManager.logout. Example: (new UserManager<User>(mContext)).logout();
                PrefsManager.clearPrefs(mContext);

                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(ACTION_UNAUTHORIZED));
            }

        };

        //We add the request
        JsonObjectRequest reloginRequest = UserRequestManager.userLogInRequest(email, password, responseListener);
        VolleyManager.getInstance(mContext).addToRequestQueue(reloginRequest);


    }

    /**
     * If we get a 426 we close all activities and go to the Upgrade required Activity
     */
    @Override
    public void onUpgradeRequiredError(VolleyError volleyError) {
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(ACTION_UPGRADE_REQUIRED));
    }

    /**
     * If we get a 400 we send and appropriate message (Override for each case).
     */
    public void onBadRequest(VolleyError volleyError) {
    }

    /**
     * If there is no network response we notify there is no internet access
     */
    public void noInternetError() {
        Toast.makeText(mContext, R.string.error_no_internet, Toast.LENGTH_SHORT).show();
    }

    /**
     * For other errors we notify there has been an unknown error
     */
    @Override
    public void onUnhandledError(VolleyError volleyError) {
        String error = VolleyErrorHelper.getMessage(volleyError, mContext);
        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
    }

}
