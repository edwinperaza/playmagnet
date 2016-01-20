package cl.magnet.magnetprojecttemplate.network.gcm;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cl.magnet.magnetprojecttemplate.network.APIManager;

/**
 * Clase que maneja la suscripción del Token de GCM en el servidor
 *
 * Created by Tito_Leiva on 31-07-15.
 */
public class GcmTokenRequest extends APIManager {

    public static final String TAG = GcmTokenRequest.class.getSimpleName();

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
    public void request() {

        //TODO implement this method
    }

}