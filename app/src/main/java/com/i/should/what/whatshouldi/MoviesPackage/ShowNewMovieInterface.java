package com.i.should.what.whatshouldi.MoviesPackage;

import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;

/**
 * Created by ryan on 7/19/2015.
 */
public interface ShowNewMovieInterface {
    void showNewMovie(MovieDBFullMovieModel newModel, int pos, float stars);
    void errorOccurred(String message);
}
