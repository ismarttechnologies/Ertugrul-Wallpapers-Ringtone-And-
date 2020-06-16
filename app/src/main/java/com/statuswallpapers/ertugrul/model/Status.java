package com.statuswallpapers.ertugrul.model;

import android.graphics.Bitmap;

public class Status {

    String id;
    String name;
    String thumbnail;
    String url;
    String created_at;
    String total_shared;
    Bitmap bitmap;
    String type;

    public Status(String name, String thumbnail, String url) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.url = url;
    }

    public Status(String name, String thumbnail, String url, String type) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.url = url;
        this.type = type;
    }

    public Status(String thumbnail, String url) {
        this.thumbnail = thumbnail;
        this.url = url;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
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

    public String getTotal_shared() {
        return total_shared;
    }

    public void setTotal_shared(String total_shared) {
        this.total_shared = total_shared;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
