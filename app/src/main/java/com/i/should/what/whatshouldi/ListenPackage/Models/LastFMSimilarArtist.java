package com.i.should.what.whatshouldi.ListenPackage.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 7/29/2015.
 */
public class LastFMSimilarArtist {


    @SerializedName("similarartists")
    @Expose
    private Similarartists similarartists;

    public List<LastFMArtist> getResults()
    {
        return similarartists.artist;
    }

    /**
     * @return The similarartists
     */
    public Similarartists getSimilarartists() {
        return similarartists;
    }

    /**
     * @param similarartists The similarartists
     */
    public void setSimilarartists(Similarartists similarartists) {
        this.similarartists = similarartists;
    }


    public class Similarartists {

        @SerializedName("artist")
        @Expose
        private List<LastFMArtist> artist = new ArrayList<LastFMArtist>();

        /**
         * @return The artist
         */
        public List<LastFMArtist> getArtist() {
            return artist;
        }

        /**
         * @param artist The artist
         */
        public void setArtist(List<LastFMArtist> artist) {
            this.artist = artist;
        }
    }
}
