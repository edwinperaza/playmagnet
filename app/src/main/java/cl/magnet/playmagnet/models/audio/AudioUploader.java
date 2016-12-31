package cl.magnet.playmagnet.models.audio;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.List;

import cl.magnet.magnetrestclient.utils.PersistentCookieStore;
import cl.magnet.magnetrestclient.utils.UserAgentUtils;
import cl.magnet.playmagnet.network.APIManager;

/**
 * Created by edwinperaza on 12/30/16.
 */

public class AudioUploader {

    private static final String TAG = AudioUploader.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    private static final MediaType MEDIA_TYPE_MP4 = MediaType.parse("video/mp4");
    private static final MediaType MEDIA_TYPE_3GP = MediaType.parse("video/3gp");
    public static final String USER_AGENT = "User-Agent";
    private Context mContext;
    private Audio mAudio;
    private OnMediaFileUploadedListener mListener;

    public AudioUploader(Context context, Audio audio, OnMediaFileUploadedListener listener) {
        this.mContext = context;
        this.mAudio = audio;
        this.mListener = listener;
    }

    public AudioUploader(Context context, Audio audio) {
        this.mContext = context;
        this.mAudio = audio;
    }

    public void upload() {
//        Lot lot = Lot.getLotById(mLotId);
//        List<MediaFile> list = new ArrayList<>(lot.getMediaFiles());
        new UploadAudioTask().execute("");
    }

    private Request makeRequest(Audio audio) {
        MediaType currentMediaType;
        currentMediaType = MEDIA_TYPE_3GP;
        Log.d(TAG, audio.getTitle());
        Log.d(TAG, audio.getComment());
        Log.d(TAG, audio.getAudioUrl());
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("title", audio.getTitle())
                .addFormDataPart("comment", audio.getComment())
                .addFormDataPart(
                        "audio",
                        audio.getTitle(),
                        RequestBody.create(currentMediaType, new File(audio.getAudioUrl()))
                ).build();

        return new Request.Builder()
                .url(AudioRequestManager.API_AUDIO_URL)
                .addHeader(USER_AGENT, UserAgentUtils.getUserAgent(mContext))
                .post(requestBody)
                .build();
    }

    public interface OnMediaFileUploadedListener {
        void onMediaFileUploaded(String filename);
        void onMediaFileUploadError(String filename);
        void onAllMediaFinished();
    }

    private class UploadAudioTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Log.d(TAG, "Starting to upload the audio ");
            // Iterate over the list of media files
                    // Make an Ok http request to upload the file
                    Request request;
                    request = makeRequest(mAudio);
                    boolean hasError;
                    Response response;
                    try {
                        // Get the response from the request
                        OkHttpClient client = new OkHttpClient();
//                        CookieManager cookieManager = new CookieManager(
//                                new PersistentCookieStore(mContext),
//                                CookiePolicy.ACCEPT_ORIGINAL_SERVER
//                        );
//                        client.setCookieHandler(cookieManager);
                        response = client.newCall(request).execute();
                        Log.d(
                                TAG,
                                "Got response from the MediaFile "
                        );
                        hasError = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                        response = null;
                        hasError = true;

                    }
                    Log.d("AudioUploader", response.toString());
                    Log.d("AudioUploader", String.valueOf(hasError));
//                    MediaFileState mediaFileState = new MediaFileState(
//                            mediaFileAsync.getFilename(), response, hasError
//                    );
                    // The response is handled in the onProgressUpdate method
//                    publishProgress(mediaFileState);
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
//            mListener.onAllMediaFinished();
        }
    }

}
