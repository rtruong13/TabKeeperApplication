package com.i.should.what.whatshouldi.MoviesPackage;

import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;

/**
 * Created by ryan on 7/25/2015.
 */
public interface AddMovieToListInterface {

    void AddMovieInfo(MovieDBFullMovieModel fullModel, int position);
    void ForceSecondTierWithFilm(MovieDBFullMovieModel model);
    void ForceSecondTier();
}
