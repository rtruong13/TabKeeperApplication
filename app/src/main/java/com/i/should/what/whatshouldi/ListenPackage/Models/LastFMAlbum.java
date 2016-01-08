package com.i.should.what.whatshouldi.ListenPackage.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 7/30/2015.
 */
public class LastFMAlbum {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("playcount")
    @Expose
    private Integer playcount;
    @SerializedName("mbid")
    @Expose
    private String mbid;
    @SerializedName("url")
    @Expose
    private String url;
    private String artistName;
    @SerializedName("image")
    @Expose
    private List<LastFMImage> image = new ArrayList<LastFMImage>();

    private String releaseDate;
    private String largeImage;
    private ListenState listenState = ListenState.NOTHING;
    public boolean playing;


    public LastFMAlbum(String name, String mbid, Integer playcount, String url, String artistName,
                       String largeImage, String releaseDate, ListenState state) {
        image = null;
        this.name = name;
        this.mbid = mbid;
        this.playcount = playcount;
        this.url = url;
        this.releaseDate = releaseDate;
        this.artistName = artistName;
        this.largeImage = largeImage;
        listenState = state;
        playing = false;
    }

    /**
     * @return The name
     */
    public String getName(boolean full) {
        if (full || name.length() <= 15)
            return name;

        return name.substring(0, 13)+"...";
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The playcount
     */
    public Integer getPlaycount() {
        return playcount;
    }

    /**
     * @param playcount The playcount
     */
    public void setPlaycount(Integer playcount) {
        this.playcount = playcount;
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
     * @return The artist
     */
    public String getArtist() {
        return artistName;
    }

    /**
     * @param artist The artist
     */
    public void setArtist(String artist) {
        this.artistName = artist;
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
        if (largeImage != null && !largeImage.isEmpty())
            return largeImage;
        else if (image != null) {
            largeImage = image.get(2).getPath();
            return largeImage;
        } else return null;
    }

    public ListenState getState() {
        if(listenState== null)
            listenState = ListenState.NOTHING;
        return listenState;
    }

    public void setState(ListenState state) {
        this.listenState = state;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Integer getDateYear() {
        if(getReleaseDate() == null) return 0;
        if(!getReleaseDate().equals("")) {
            String year = getReleaseDate().substring(0, 4);
            return Integer.parseInt(year);
        }
        return 0;
    }

    public Integer getDateMonth() {
        if(getReleaseDate() == null) return 0;
        if(!getReleaseDate().equals("")) {
            String year = getReleaseDate().substring(5, 7);
            return Integer.parseInt(year);
        }
        return 0;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}