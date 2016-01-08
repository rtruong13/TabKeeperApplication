package com.i.should.what.whatshouldi.MoviesPackage;

import android.content.Context;

import com.i.should.what.whatshouldi.MoviesPackage.Loaders.LoadSimilarMovieTask;
import com.i.should.what.whatshouldi.MoviesPackage.Loaders.MoviesLoadingDataTask;
import com.i.should.what.whatshouldi.MoviesPackage.Loaders.LoadSimilarMovieTasksParams;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MoviesLoadingDataTasksParams;

import java.util.List;

/**
 * Created by ryan on 7/17/2015.
 */
public class MoviesAPIHelper { // todo change to work on another thread

    public void getWatch(final WatchRecyclerAdapter adapter, final Context context, LoadSimilarMovieTasksParams.WatchType watchType) {
        new MoviesLoadingDataTask().execute
                (new MoviesLoadingDataTasksParams(context, adapter, watchType));
    }

    public void getNextMovieToWatch(Context context, ShowNewMovieInterface showNewInterface,
                                    int pos, MovieDBFullMovieModel movieModel, float stars,
                                    LoadSimilarMovieTasksParams.WatchType watchType, List<MovieDBFullMovieModel> allMovies){
        new LoadSimilarMovieTask().execute
                (new LoadSimilarMovieTasksParams(context, showNewInterface, movieModel, pos, stars, watchType,allMovies));

    }
}
