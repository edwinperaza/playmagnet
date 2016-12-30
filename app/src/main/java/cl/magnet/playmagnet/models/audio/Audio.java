package cl.magnet.playmagnet.models.audio;

import java.util.ArrayList;

/**
 * Created by edwinperaza on 12/28/16.
 */

public class Audio {

    private int id;
    private String Title;
    private String audioUrl;

    public Audio() {
    }

    public Audio(int id, String title, String audioUrl) {
        this.id = id;
        Title = title;
        this.audioUrl = audioUrl;
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

    public static ArrayList<Audio> createReportList(int numContacts) {
        ArrayList<Audio> audios = new ArrayList<>();

        for (int i = 1; i <= numContacts; i++) {
            audios.add(new Audio(i, "Reporte Nro. " + String.valueOf(i), ""));
        }

        return audios;
    }
}
