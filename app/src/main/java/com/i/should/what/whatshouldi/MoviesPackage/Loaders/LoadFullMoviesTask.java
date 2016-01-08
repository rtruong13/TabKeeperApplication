package com.i.should.what.whatshouldi.MoviesPackage.Loaders;

import android.os.AsyncTask;
import android.util.Log;

import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ryan on 7/25/2015.
 */
public class LoadFullMoviesTask extends AsyncTask<LoadFullMovieParams, Void, MovieDBFullMovieModel> {
    LoadFullMovieParams param;

    private String api_key = "84ad8653c6f9e64b838d7f50f91a0211";
    String BASE_URL = "http://api.themoviedb.org/3/";

    private TheMovieDBInterface getMovieDBInterface() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TheMovieDBInterface callInterface = retrofit.create(TheMovieDBInterface.class);
        return callInterface;
    }

    boolean forceTwo;

    @Override
    protected MovieDBFullMovieModel doInBackground(LoadFullMovieParams... params) {
        param = params[0];
        forceTwo = false;

        TheMovieDBInterface callInterface = getMovieDBInterface();

        Log.e("Start loading", param.idOfMovieToLoad+"");
        Call<MovieDBFullMovieModel> call =
                callInterface.getMovieInfo
                        ((param.watchType == LoadSimilarMovieTasksParams.WatchType.tvshow? "tv":"movie"),
                                param.idOfMovieToLoad,
                                api_key,
                                "credits");

        Response<MovieDBFullMovieModel> response = null;
        try {
            response = call.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (response == null) return null;

        int statusCode = response.code();
        if (statusCode == 200) {
            MovieDBFullMovieModel fullInfo = new MovieDBFullMovieModel(response.body());
            fullInfo.clean();
            Log.e("loadedMovie", fullInfo.getId().toString()+" "+param.positionInTheList);
            return fullInfo;
        }

        if (statusCode != 404)
            forceTwo = true;

        Log.e("statusCode", Integer.toString(statusCode));
        return null;
    }

    @Override
    protected void onPostExecute(MovieDBFullMovieModel model) {
        super.onPostExecute(model);

        if(param.ForceWithMovie)
        {
            param.addMovieToListInterface.ForceSecondTierWithFilm(model);
        }else if (model != null && param.addMovieToListInterface!=null) {
            param.addMovieToListInterface.AddMovieInfo(model, param.positionInTheList);
        }
        else if(forceTwo){
            param.addMovieToListInterface.ForceSecondTier();
            Log.e("Error!! LoadFullM", "error occurred");
        }
    }
}
