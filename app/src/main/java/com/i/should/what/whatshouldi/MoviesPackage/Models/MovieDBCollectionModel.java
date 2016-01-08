package com.i.should.what.whatshouldi.MoviesPackage.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 7/18/2015.
 */
public class MovieDBCollectionModel implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("parts")
    @Expose
    private List<MovieDBFullMovieModel> parts = new ArrayList<MovieDBFullMovieModel>();

    public MovieDBCollectionModel(MovieDBCollectionModel collection) {
        if(collection!=null) {
            id = collection.getId();
            name = collection.getName();
            if(collection.getParts()!=null&&collection.getParts().size()>0)
            parts = new ArrayList<>(collection.getParts());
        }
    }

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The parts
     */
    public List<MovieDBFullMovieModel> getParts() {
        return parts;
    }

    /**
     * @param parts The parts
     */
    public void setParts(List<MovieDBFullMovieModel> parts) {
        this.parts = parts;
    }


}