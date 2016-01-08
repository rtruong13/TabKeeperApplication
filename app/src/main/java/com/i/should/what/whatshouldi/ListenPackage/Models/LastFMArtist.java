package com.i.should.what.whatshouldi.ListenPackage.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 10/28/2015.
 */
public class LastFMArtist implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mbid")
    @Expose
    private String mbid;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("image")
    @Expose
    private List<LastFMImage> image = new ArrayList<LastFMImage>();
    private ListenState state = ListenState.NOTHING;
    private String imageUrl;

    public LastFMArtist(String name, String mbid, String url, String imageUrl, ListenState state) {
        this.name = name;
        this.mbid = mbid;
        this.url = url;
        this.imageUrl = imageUrl;
        this.state = state;
        image = null;
    }

    /**
     * @return The name
     */
    public String getName(boolean full) {
        if (full || name.length() < 16)
            return name;
        else {
            return name.substring(0, 14) + "...";//todo hardcode!
        }
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The mbid
     */
    public String getMbid() {
        return mbid;
    }

    /**
     * @param mbid The mbid
     */
    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return The image
     */
    public List<LastFMImage> getImage() {
        return image;
    }

    /**
     * @param image The image
     */
    public void setImage(List<LastFMImage> image) {
        this.image = image;
    }

    public String getLargeImage() {
        if (imageUrl != null && !imageUrl.isEmpty())
            return imageUrl;
        else if (image != null) {
            imageUrl = image.get(2).getPath();
            return imageUrl;
        } else return null;
    }

    public ListenState getState() {
        return state;
    }

    public void setState(ListenState state) {
        this.state = state;
    }
}