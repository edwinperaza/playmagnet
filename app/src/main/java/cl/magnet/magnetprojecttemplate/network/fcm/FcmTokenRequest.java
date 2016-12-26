package cl.magnet.magnetprojecttemplate.network.fcm;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cl.magnet.magnetprojecttemplate.network.APIManager;
import cl.magnet.magnetprojecttemplate.utils.Do;
import cl.magnet.magnetprojecttemplate.utils.PrefsManager;
import cl.magnet.magnetrestclient.VolleyManager;

/**
 * Created by edwinperaza on 12/26/16.
 */

public class FcmTokenRequest {

    public static final String TAG = FcmTokenRequest.class.getSimpleName();
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_REGISTRATION_ID = "registration_id";
    public static final String KEY_FCM = "fcm";
    private static final String FCM_API_URL = APIManager.GENERAL_URL + "fcm/";

    public Context mContext;

    /**
     * Constructor de la clase
     *
     * @param context Contexto de la aplicación
     */
    public FcmTokenRequest(Context context) {
        mContext = context;
    }

    /**
     * Solicitud de registro del token en el servidor
     */
    public void suscribeToken(final String token) {

        String deviceId = Do.getDeviceId();

        Map<String, String> params = new HashMap<>();

        params.put(KEY_FCM, token);
        params.put(KEY_DEVICE_ID, deviceId);

        String url = FCM_API_URL;
        Log.d(TAG, "User FCM Token: " + token);

        Request request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        PrefsManager.setGCMToken(mContext, token);
                        PrefsManager.setSentTokenToServer(mContext, true);
                        Log.d(TAG, "Upload FCM at server response: " + response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "GCM Registration Token ");

            }
        });

        VolleyManager.getInstance(mContext).addToRequestQueue(request);
    }

    /**
     * Dar de baja el token en el servidor
     */
    public void unsuscribeToken() {

        String token = PrefsManager.getGCMToken(mContext);
        String deviceId = Do.getDeviceId();

        if (token != null && !token.equals("")) {

            Map<String, String> params = new HashMap<>();
            params.put(KEY_DEVICE_ID, deviceId);

            Request request = new JsonObjectRequest(Request.Method.DELETE, FCM_API_URL +
                    token + "/", new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    PrefsManager.setGCMToken(mContext, "");

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            VolleyManager.getInstance(mContext).addToRequestQueue(request);
        }
    }
}
