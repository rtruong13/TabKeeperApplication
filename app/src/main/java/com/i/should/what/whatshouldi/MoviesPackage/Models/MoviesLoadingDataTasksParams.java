package com.i.should.what.whatshouldi.MoviesPackage.Models;

import android.content.Context;

import com.i.should.what.whatshouldi.MoviesPackage.Loaders.LoadSimilarMovieTasksParams;
import com.i.should.what.whatshouldi.MoviesPackage.WatchRecyclerAdapter;

/**
 * Created by ryan on 7/19/2015.
 */
public class MoviesLoadingDataTasksParams{
    public Context context;
    public WatchRecyclerAdapter adapter;
    public LoadSimilarMovieTasksParams.WatchType watchType;

    public MoviesLoadingDataTasksParams(Context context, WatchRecyclerAdapter watchRecyclerAdapter, LoadSimilarMovieTasksParams.WatchType type)
    {
        watchType = type;
        this.context = context;
        this.adapter = watchRecyclerAdapter;
    }
}
