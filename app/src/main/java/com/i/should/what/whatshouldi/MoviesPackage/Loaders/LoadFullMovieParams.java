package com.i.should.what.whatshouldi.MoviesPackage.Loaders;

import com.i.should.what.whatshouldi.MoviesPackage.AddMovieToListInterface;

/**
 * Created by ryan on 7/25/2015.
 */
public class LoadFullMovieParams {
    public AddMovieToListInterface addMovieToListInterface;
    public int idOfMovieToLoad;
    public int positionInTheList;
    public LoadSimilarMovieTasksParams.WatchType watchType;
    public boolean ForceWithMovie;

    public LoadFullMovieParams(AddMovieToListInterface mInterface, int id, int positionInTheList, LoadSimilarMovieTasksParams.WatchType type, boolean forceWithMovie)
    {
        addMovieToListInterface = mInterface;
        idOfMovieToLoad = id;
        this.positionInTheList = positionInTheList;
        watchType = type;
        this.ForceWithMovie = forceWithMovie;
    }
}
