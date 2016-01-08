package com.i.should.what.whatshouldi.MoviesPackage.Loaders;

import android.content.Context;

import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;
import com.i.should.what.whatshouldi.MoviesPackage.ShowNewMovieInterface;

import java.util.List;

/**
 * Created by ryan on 7/19/2015.
 */
public class LoadSimilarMovieTasksParams {
    public List<MovieDBFullMovieModel> allMovies;
    public MovieDBFullMovieModel movie;
    public int pos;
    public Context context;
    public ShowNewMovieInterface showNew;
    public float starNum;
    public WatchType watchType;

    public LoadSimilarMovieTasksParams(Context c, ShowNewMovieInterface r, MovieDBFullMovieModel model, int p, float starNum, WatchType type, List<MovieDBFullMovieModel> models) {
        context = c;
        allMovies = models;
        movie = model;
        showNew = r;
        pos = p;
        this.starNum = starNum;
        watchType = type;
    }

    public enum WatchType{
        movie, cartoon, tvshow
    }
}
