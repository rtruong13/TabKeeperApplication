package com.i.should.what.whatshouldi.MoviesPackage.Loaders;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.i.should.what.whatshouldi.FileManager;
import com.i.should.what.whatshouldi.MoviesPackage.AddMovieToListInterface;
import com.i.should.what.whatshouldi.MoviesPackage.Models.DiscoverModel;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MoviesLoadingDataTasksParams;
import com.i.should.what.whatshouldi.MoviesPackage.WatchRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ryan on 7/19/2015.
 */
public class MoviesLoadingDataTask
        extends AsyncTask<MoviesLoadingDataTasksParams, Void, ArrayList<MovieDBFullMovieModel>>
        implements AddMovieToListInterface {

    MoviesLoadingDataTasksParams param;
    ArrayList<MovieDBFullMovieModel> realList;
    int count = 10;

    @Override
    protected ArrayList<MovieDBFullMovieModel> doInBackground(MoviesLoadingDataTasksParams... params) {
        realList = new ArrayList<>();

        param = params[0];
        ArrayList<MovieDBFullMovieModel> list = getMovies(param.adapter, param.context, param.watchType);
        return list;
    }

    public ArrayList<MovieDBFullMovieModel> getMovies(final WatchRecyclerAdapter adapter,
                                                      final Context context, LoadSimilarMovieTasksParams.WatchType watchType) {

        ArrayList<MovieDBFullMovieModel> movies = new FileManager().readAllWatchs(context, watchType);

        if (movies != null && movies.size() != 0) {
            //adapter.addItemsInfo(movies);
            return movies;
        }

        discoverWatch(context, 1);

        return null;
    }

    private boolean firstTime = true;
    private int loadedPages = 0, totalFullLoaded;

    private void discoverWatch(final Context context, final int page) {

        String BASE_URL = "http://api.themoviedb.org/3/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TheMovieDBInterface callInterface = retrofit.create(TheMovieDBInterface.class);

        Call<DiscoverModel> call = null;

        if (param.watchType == LoadSimilarMovieTasksParams.WatchType.cartoon)
            call = callInterface
                    .getWatchWithGenre((param.watchType == LoadSimilarMovieTasksParams.WatchType.tvshow ? "tv" : "movie"),
                            "84ad8653c6f9e64b838d7f50f91a0211", 16,//animation!
                            page);
        else
            call = callInterface
                    .getWatch((param.watchType == LoadSimilarMovieTasksParams.WatchType.tvshow ? "tv" : "movie"),
                            "84ad8653c6f9e64b838d7f50f91a0211",
                            page);

        call.enqueue(new Callback<DiscoverModel>() {

            @Override
            public void onResponse(Response<DiscoverModel> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    DiscoverModel discoverModel = response.body();
                    int countOfPagesToLoad = 3;//(param.watchType == LoadSimilarMovieTasksParams.WatchType.tvshow ? 1 : 5);
                    if (firstTime) {
                        for (int i = 2; i < Math.min(discoverModel.getTotalPages(), countOfPagesToLoad); i++)
                            discoverWatch(context, i);

                        firstTime = false;
                    }

                    loadedPages++;
                    displayDataAndLoadFull(context, discoverModel.getResults(), (loadedPages == Math.min(discoverModel.getTotalPages(), countOfPagesToLoad) - 1));
                }
                //Toast.makeText(MainActivity.this, statusCode + " !! " + user.page, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                Log.e("Shit happens!", t.getMessage());

                ForceSecondTier();
                //todo no network!
            }
        });
    }

    private void displayDataAndLoadFull(final Context context, List<MovieDBFullMovieModel> models, boolean last) {
        if (!last) {
            realList.addAll(models);
            return;
        }

        realList.addAll(models);

        if (param.watchType == LoadSimilarMovieTasksParams.WatchType.tvshow) {
            List<MovieDBFullMovieModel> list = new ArrayList<>();
            for (int i = 0; i < count; i++)
                list.add(realList.get(i));

            realList = new ArrayList<>(list);
        } else {
            List<MovieDBFullMovieModel> list = new ArrayList<>();
            for (int i = 0; i < count; i++)
                if ((param.watchType == LoadSimilarMovieTasksParams.WatchType.cartoon && realList.get(i).isAnimation()) ||
                        (param.watchType == LoadSimilarMovieTasksParams.WatchType.movie && !realList.get(i).isAnimation())) {
                    list.add(realList.get(i));
                    if (list.size() == count) break;
                }

            realList = new ArrayList<>(list);
        }
        count = realList.size();
        totalFullLoaded = 0;
        for (int i = 0; i < count; i++)
            new LoadFullMoviesTask().execute(new LoadFullMovieParams(this, realList.get(i).getId(), i, param.watchType, false));
    }

    @Override
    protected void onPostExecute(ArrayList<MovieDBFullMovieModel> movieDBFullMovieModels) {
        if (movieDBFullMovieModels != null)
            param.adapter.addItemsInfo(movieDBFullMovieModels, false, param.watchType);
    }

    @Override
    public void AddMovieInfo(MovieDBFullMovieModel fullModel, int position) {
        realList.set(position, fullModel);
        totalFullLoaded++;
        if (totalFullLoaded >= count) {
            ForceSecondTier();
        }
    }

    @Override
    public void ForceSecondTierWithFilm(MovieDBFullMovieModel model) {

    }

    @Override
    public void ForceSecondTier() {
        if (realList != null && realList.size()>0) {
            Log.e("wer", "working2");
            for (int i = 0; i < count; i++) {
                if (realList.get(i).getCredits() == null) {
                    Log.e("fuck the API", "WTF");
                }
            }
            param.adapter.addItemsInfo(realList, false, param.watchType);
            new FileManager().saveAllMovies(param.context, realList, param.watchType);
        }else {
            Log.e("wer", "working1");
            new FileManager().copyAllRaws(param.context);

            ArrayList<MovieDBFullMovieModel> movies = new FileManager().readAllWatchs(param.context, param.watchType);

            if (movies != null && movies.size() != 0) {
                //adapter.addItemsInfo(movies);
                if (movies != null)
                    param.adapter.addItemsInfo(movies, false, param.watchType);
            }

            Toast.makeText(param.context, "You don`t have network connection.\nTo load images plaease turn on wifi/celluar network.", Toast.LENGTH_LONG).show();
        }
    }
}
