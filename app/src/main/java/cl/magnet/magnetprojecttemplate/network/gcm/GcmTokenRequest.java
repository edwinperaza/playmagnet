package cl.magnet.magnetprojecttemplate.network.gcm;

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
 * Clase que maneja la suscripción del Token de GCM en el servidor
 *
 * Created by Tito_Leiva on 31-07-15.
 */
public class GcmTokenRequest {

    public static final String TAG = GcmTokenRequest.class.getSimpleName();
    private static final String KEY_REGISTRATION_ID = "registration_id";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String GCM_API_URL = APIManager.API_URL + "gcm/";

    public Context mContext;

    /**
     * Constructor de la clase
     *
     * @param context Contexto de la aplicación
     */
    public GcmTokenRequest(Context context) {
        mContext = context;
    }

    /**
     * Solicitud de registro del token en el servidor
     */
    public void suscribeToken(final String token) {

        String deviceId = Do.getDeviceId();

        Map<String, String> params = new HashMap<>();

        params.put(KEY_REGISTRATION_ID, token);
        params.put(KEY_DEVICE_ID, deviceId);

        String url = GCM_API_URL;
        Request request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        PrefsManager.setGCMToken(mContext, token);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

            Request request = new JsonObjectRequest(Request.Method.DELETE, GCM_API_URL +
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