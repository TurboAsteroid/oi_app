package com.example.lvo.alertnotifications;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;


public class incidentModel {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("datetime")
    @Expose
    private String datetime;
    @SerializedName("solution")
    @Expose
    private String solution;
    @SerializedName("incident_id")
    @Expose
    private String incident_id;
    @SerializedName("files")
    @Expose
    private List<String> files = null;

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> notification) {
        this.files = notification;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getIncidentId() {
        return incident_id;
    }

    public void setIncidentId(String solution) {
        this.incident_id = incident_id;
    }
}