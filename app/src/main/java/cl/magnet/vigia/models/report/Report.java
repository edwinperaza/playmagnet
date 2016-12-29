package cl.magnet.vigia.models.report;

import java.util.ArrayList;

/**
 * Created by edwinperaza on 12/28/16.
 */

public class Report {

    private int id;
    private String Title;
    private String imageUrl;

    public Report() {
    }

    public Report(int id, String title, String imageUrl) {
        this.id = id;
        Title = title;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static ArrayList<Report> createReportList(int numContacts) {
        ArrayList<Report> reports = new ArrayList<>();

        for (int i = 1; i <= numContacts; i++) {
            reports.add(new Report(i, "Reporte Nro. " + String.valueOf(i), ""));
        }

        return reports;
    }
}
