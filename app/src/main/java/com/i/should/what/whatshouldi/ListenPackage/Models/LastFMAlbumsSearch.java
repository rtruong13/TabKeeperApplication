package com.i.should.what.whatshouldi.ListenPackage.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 7/30/2015.
 */
public class LastFMAlbumsSearch {

    @SerializedName("topalbums")
    @Expose
    private Topalbums topalbums;

    public Topalbums getTopalbums() {
        return topalbums;
    }

    public int getCountOfAlbums()
    {
        int count = 0;
        List<LastFMAlbum> artistsRes = getResults();
        for (int i = 0; i < artistsRes.size(); i++) {
            if (artistsRes.get(i) != null && artistsRes.get(i).getMbid() != null &&
                    !artistsRes.get(i).getMbid().isEmpty() &&
                    artistsRes.get(i).getLargeImage() != null) {
                count++;
            }
        }

        return count;
    }

    public List<LastFMAlbum> getResults()
    {
        if(topalbums!=null)
            return topalbums.albums;
        else return new ArrayList<>();
    }

    public void setTopalbums(Topalbums topalbums) {
        this.topalbums = topalbums;
    }

    public class Topalbums {

        @SerializedName("album")
        @Expose
        private List<LastFMAlbum> albums = new ArrayList<LastFMAlbum>();
        @SerializedName("@attr")
        @Expose
        private Attributes Attr;

        public List<LastFMAlbum> getAlbum() {
            return albums;
        }

        public void setAlbum(List<LastFMAlbum> album) {
            this.albums = album;
        }
    }

    public class Attributes {

        @SerializedName("artist")
        @Expose
        private String artist;
        @SerializedName("page")
        @Expose
        private String page;
        @SerializedName("perPage")
        @Expose
        private String perPage;
        @SerializedName("totalPages")
        @Expose
        private String totalPages;
        @SerializedName("total")
        @Expose
        private String total;

        /**
         *
         * @return
         * The artist
         */
        public String getArtist() {
            return artist;
        }

        /**
         *
         * @param artist
         * The artist
         */
        public void setArtist(String artist) {
            this.artist = artist;
        }

        /**
         *
         * @return
         * The page
         */
        public String getPage() {
            return page;
        }

        /**
         *
         * @param page
         * The page
         */
        public void setPage(String page) {
            this.page = page;
        }

        /**
         *
         * @return
         * The perPage
         */
        public String getPerPage() {
            return perPage;
        }

        /**
         *
         * @param perPage
         * The perPage
         */
        public void setPerPage(String perPage) {
            this.perPage = perPage;
        }

        /**
         *
         * @return
         * The totalPages
         */
        public String getTotalPages() {
            return totalPages;
        }

        /**
         *
         * @param totalPages
         * The totalPages
         */
        public void setTotalPages(String totalPages) {
            this.totalPages = totalPages;
        }

        /**
         *
         * @return
         * The total
         */
        public String getTotal() {
            return total;
        }

        /**
         *
         * @param total
         * The total
         */
        public void setTotal(String total) {
            this.total = total;
        }

    }

}
