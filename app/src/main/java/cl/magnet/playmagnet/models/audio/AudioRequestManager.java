package cl.magnet.playmagnet.models.audio;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import cl.magnet.magnetrestclient.requests.MagnetJsonObjectRequest;
import cl.magnet.playmagnet.network.APIManager;
import cl.magnet.playmagnet.network.AppResponseListener;

/**
 * Created by edwinperaza on 12/30/16.
 */

public class AudioRequestManager extends APIManager {

    public static final String API_AUDIO_URL = GENERAL_URL + "audios/";

    public static MagnetJsonObjectRequest getAudioList(AppResponseListener<JSONObject> responseListener) {

        JSONObject params = new JSONObject();
        Log.d("Audio Request", API_AUDIO_URL);
        return new MagnetJsonObjectRequest(Request.Method.GET, API_AUDIO_URL, params, responseListener, responseListener);

    }
}
