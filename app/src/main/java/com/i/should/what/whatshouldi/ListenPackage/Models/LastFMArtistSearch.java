package com.i.should.what.whatshouldi.ListenPackage.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 7/28/2015.
 */
public class LastFMArtistSearch {

    @SerializedName("results")
    @Expose
    private LastFMResults results;

    public LastFMResults  getResults() {
        return results;
    }

    public void setResults(LastFMResults results) {
        this.results = results;
    }

    public List<LastFMArtist> getArtistsResults() {
        if(results == null || results.artistmatches == null
                || results.artistmatches.artist == null || results.artistmatches.artist.size()<=0)
            return new ArrayList<>();

        return results.artistmatches.artist;
    }

    public class LastFMResults  {
        @SerializedName("artistmatches")
        @Expose
        private Artistmatches artistmatches;
        public Artistmatches getArtistmatches() {
            return artistmatches;
        }
        public void setArtistmatches(Artistmatches artistmatches) {
            this.artistmatches = artistmatches;
        }
    }

    public class Artistmatches {

        @SerializedName("artist")
        @Expose
        private List<LastFMArtist> artist = new ArrayList<LastFMArtist>();
        public List<LastFMArtist> getArtist() {
            return artist;
        }
        public void setArtist(List<LastFMArtist> artist) {
            this.artist = artist;
        }

    }

}
