package cl.magnet.playmagnet.models.audio;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by edwinperaza on 12/28/16.
 */

public class Audio {

    private int id;
    private String Title;
    private String audioUrl;
    private String comment;

    public Audio() {
    }

    public Audio(int id, String title, String audioUrl, String comment) {
        this.id = id;
        Title = title;
        this.audioUrl = audioUrl;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static ArrayList<Audio> createAudioList(int numContacts) {
        ArrayList<Audio> audios = new ArrayList<>();

        audios.add(
                new Audio(1, "Chantaje - Shakira ft Maluma",
                        "http://insta.magnet.cl/uploads/audios/audios/BLH9CKShakira%20-%20Chantaje%20ft.%20Maluma.mp3", ""));
        audios.add(
                new Audio(2, "RadioHead",
                        "http://insta.magnet.cl/uploads/audios/audios/00N38102-radiohead-the_trickster-eNT.mp3", ""));
        return audios;
    }

    public static ArrayList<Audio> fromJsonArray(JSONArray jsonArrayAudio) {
        ArrayList<Audio> audios = new ArrayList<>();

        for (int i = 0; i < jsonArrayAudio.length(); i++) {
            JSONObject jsonObjectAudio = null;
            Audio audioTemporal = new Audio();
            try {
                jsonObjectAudio = jsonArrayAudio.getJSONObject(i);

                audioTemporal.setId(jsonObjectAudio.getInt("pk"));
                audioTemporal.setTitle(jsonObjectAudio.getString("title"));
                audioTemporal.setAudioUrl(jsonObjectAudio.getString("audio"));
                audioTemporal.setComment(jsonObjectAudio.getString("comment"));

                audios.add(audioTemporal);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return audios;
    }
}
