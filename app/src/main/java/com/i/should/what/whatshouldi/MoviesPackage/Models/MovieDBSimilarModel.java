package com.i.should.what.whatshouldi.MoviesPackage.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 7/19/2015.
 */
public class MovieDBSimilarModel{
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<MovieDBFullMovieModel> results = new ArrayList<MovieDBFullMovieModel>();
    @SerializedName("total_pages")
    @Expose
    private Integer total_pages;
    @SerializedName("total_results")
    @Expose
    private Integer total_results;

    /**
     * @return The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return The page
     */
    public Integer getTotalPages() {
        return total_pages;
    }

    /**
     * @param pages The page
     */
    public void setTotalPages(Integer pages) {
        this.total_pages = pages;
    }

    /**
     * @return The page
     */
    public Integer getTotalCount() {
        return total_results;
    }

    /**
     * @param total_results The page
     */
    public void setTotalCount(Integer total_results) {
        this.total_results = total_results;
    }

    /**
     * @return The results
     */
    public List<MovieDBFullMovieModel> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<MovieDBFullMovieModel> results) {
        this.results = results;
    }
}
