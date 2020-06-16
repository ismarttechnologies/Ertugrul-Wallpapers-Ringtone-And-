package com.statuswallpapers.ertugrul.model;

public class Ringtone {

    String id;
    String name;
    String url;
    String created_at;
    String total_downloads;

    public Ringtone(String id, String name, String url, String total_downloads) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.total_downloads = total_downloads;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTotal_downloads() {
        return total_downloads;
    }

    public void setTotal_downloads(String total_downloads) {
        this.total_downloads = total_downloads;
    }
}

