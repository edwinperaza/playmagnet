package cl.magnet.magnetprojecttemplate.utils;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by yaniv on 11/24/15.
 */
public class MagnetJsonObjectRequest extends JsonObjectRequest {

    public MagnetJsonObjectRequest(int method,
                                   String url,
                                   JSONObject jsonRequest,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }


}
