package com.arunpn.imagesearch.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by a1nagar on 10/18/15.
 */
public class ImageDetails {
    int width;
    int height;
    @SerializedName("unescapedUrl")
    String url;
    @SerializedName("titleNoFormatting")
    String description;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
