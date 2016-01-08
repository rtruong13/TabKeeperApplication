package com.i.should.what.whatshouldi.MoviesPackage.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 7/18/2015.
 */
public class MovieDBCreditsModel implements Serializable {

        @SerializedName("cast")
        @Expose
        private List<MovieDBCastModel> cast = new ArrayList<MovieDBCastModel>();
        @SerializedName("crew")
        @Expose
        private List<MovieDBCrewModel> crew = new ArrayList<MovieDBCrewModel>();

    public MovieDBCreditsModel(MovieDBCreditsModel credits) {
        cast = new ArrayList<>(credits.cast);
        crew = new ArrayList<>(credits.crew);
    }

    /**
         *
         * @return
         * The cast
         */
        public List<MovieDBCastModel> getCast() {
            return cast;
        }

        /**
         *
         * @param cast
         * The cast
         */
        public void setCast(List<MovieDBCastModel> cast) {
            this.cast = cast;
        }

        /**
         *
         * @return
         * The crew
         */
        public List<MovieDBCrewModel> getCrew() {
            return crew;
        }

        /**
         *
         * @param crew
         * The crew
         */
        public void setCrew(List<MovieDBCrewModel> crew) {
            this.crew = crew;
        }

    private String dirJobTitle = "Producer";

    public String getDirectorString() {
        String dirName = "not loaded";

        if(getCrew() == null) return  dirName;
        for(int i=0; i<getCrew().size(); i++)
        {
            if(getCrew().get(i).getJob().equals(dirJobTitle))
            {
                dirName = getCrew().get(i).getName();
            }
        }

        return dirName;
    }


    public String getCastString() {
        StringBuilder castString = new StringBuilder();

        if(getCast() == null) return "not loaded";
        for(int i=0;i < Math.min(getCast().size(), 3) ; i++) {
            castString.append(getCast().get(i).getName());
            castString.append( "\n");
        }

        return castString.toString();
    }
}