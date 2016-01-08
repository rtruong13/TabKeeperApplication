package com.i.should.what.whatshouldi.MoviesPackage.Loaders;

import android.os.AsyncTask;
import android.util.Log;

import com.i.should.what.whatshouldi.MainActivity;
import com.i.should.what.whatshouldi.MoviesPackage.AddMovieToListInterface;
import com.i.should.what.whatshouldi.MoviesPackage.Models.DiscoverModel;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBCollectionModel;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBSimilarModel;
import com.i.should.what.whatshouldi.MoviesPackage.Models.Prediction.PredictionModel;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ryan on 7/19/2015.
 */
public class LoadSimilarMovieTask
        extends AsyncTask<LoadSimilarMovieTasksParams, Void, Void> implements AddMovieToListInterface {

    LoadSimilarMovieTasksParams param;
    int countToLoad = -1, fullLoaded = 0;
    boolean collectionLoaded = false, similarLoaded = false, topRatedLoaded = false;
    ArrayList<MovieDBFullMovieModel> loadedList;
    private String api_key = "84ad8653c6f9e64b838d7f50f91a0211";
    String BASE_URL = "http://api.themoviedb.org/3/";
    boolean noNetwork = false;

    float[] predictionMarks = null;
    int[] positions = null;

    @Override
    protected Void doInBackground(LoadSimilarMovieTasksParams... params) {
        param = params[0];
        saveMovie();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getSimilarMovies();
            }
        }).run();
        return null;
    }

    private void saveMovie() {
        if(param.starNum!=-1){
            MainActivity.helper.saveWatchedFilm(param.movie, param.starNum, param.watchType);//run in another thread!!!!!
        }
    }


    public void getSimilarMovies() {

        TheMovieDBInterface callInterface = getMovieDBInterface();

        loadedList = new ArrayList<>();

        getSimilarWithPage(1);
        getTopWithPage(1);
        if (param.watchType != LoadSimilarMovieTasksParams.WatchType.tvshow)
            getFromCollection(callInterface);
        else {
            collectionLoaded = true;
            if (collectionLoaded && similarLoaded && topRatedLoaded)
                StartLoadingMovies();
        }

    }

    private void getTopWithPage(final int page) {
        TheMovieDBInterface callInterface = getMovieDBInterface();

        Call<DiscoverModel> call = null;
        if (param.watchType != LoadSimilarMovieTasksParams.WatchType.cartoon)
            call = callInterface.getWatch((param.watchType == LoadSimilarMovieTasksParams.WatchType.tvshow ? "tv" : "movie"), api_key, page);
        else
            call = callInterface.getWatchWithGenre("movie", api_key, 16, page);

        call.enqueue(new Callback<DiscoverModel>() {

            @Override
            public void onResponse(Response<DiscoverModel> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    DiscoverModel discoverModel = response.body();

                    loadedList.addAll(discoverModel.getResults());
                    int pagesToLoad = 3;
                    if (discoverModel.getTotalPages() > page && page < pagesToLoad)//todo check this count of pages
                        getTopWithPage(page + 1);
                    else {
                        topRatedLoaded = true;

                        if (topRatedLoaded && collectionLoaded && similarLoaded) {
                            StartLoadingMovies();
                        }
                    }
                } else {
                    Log.e("loading top error", "error!response.code()" + response.code());
                    topRatedLoaded = true;
                    if (topRatedLoaded && collectionLoaded && similarLoaded) {
                        StartLoadingMovies();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                Log.e("Shit happens!", t.getMessage());
                noNetwork = true;
                topRatedLoaded = true;
                if (topRatedLoaded && collectionLoaded && similarLoaded) {
                    StartLoadingMovies();
                }
                //todo no network!
            }
        });
    }

    private void getFromCollection(TheMovieDBInterface callInterface) {
        if (param.movie.getBelongsToCollection() != null && param.movie.getBelongsToCollection().getParts() != null && param.movie.getBelongsToCollection().getParts().size() > 0) {
            Call<MovieDBCollectionModel> collectionModelCall =
                    callInterface.getCollection(param.movie.getBelongsToCollection().getId(), api_key);

            collectionModelCall.enqueue(new Callback<MovieDBCollectionModel>() {
                @Override
                public void onResponse(Response<MovieDBCollectionModel> response, Retrofit retrofit) {
                    if (response.code() == 200) {
                        MovieDBCollectionModel collModel = response.body();

                        collectionLoaded = true;
                        if (topRatedLoaded && collectionLoaded && similarLoaded) {
                            StartLoadingMovies();
                        }

                        loadedList.addAll(collModel.getParts());
//                        for (MovieDBFullMovieModel model : collModel.getParts())
//                            getFullInfoForMovie(model.getId());
                    } else {
                        collectionLoaded = true;
                        if (topRatedLoaded && collectionLoaded && similarLoaded) {
                            StartLoadingMovies();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    // Log error here since request failed
                    Log.e("Shit happens!", t.getMessage());
                    noNetwork = true;
                    collectionLoaded = true;
                    if (topRatedLoaded && collectionLoaded && similarLoaded) {
                        StartLoadingMovies();
                    }
                    //todo no network!
                }
            });
        } else {
            collectionLoaded = true;
            if (topRatedLoaded && collectionLoaded && similarLoaded) {
                StartLoadingMovies();
            }
        }
    }

    private TheMovieDBInterface getMovieDBInterface() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TheMovieDBInterface callInterface = retrofit.create(TheMovieDBInterface.class);
        return callInterface;
    }

    private void getSimilarWithPage(final int page) {
        TheMovieDBInterface callInterface = getMovieDBInterface();
        final Call<MovieDBSimilarModel> call = callInterface.getSimilarList(
                (param.watchType == LoadSimilarMovieTasksParams.WatchType.tvshow ? "tv" : "movie"),
                param.movie.getId(), api_key, page);

        call.enqueue(new Callback<MovieDBSimilarModel>() {

            @Override
            public void onResponse(Response<MovieDBSimilarModel> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    MovieDBSimilarModel discoverModel = response.body();

                    loadedList.addAll(discoverModel.getResults());

                    if (discoverModel.getTotalPages() > page)
                        getSimilarWithPage(page + 1);
                    else {
                        similarLoaded = true;
                        if (topRatedLoaded && collectionLoaded && similarLoaded) {
                            StartLoadingMovies();
                        }
                    }
                } else {
                    similarLoaded = true;
                    if (topRatedLoaded && collectionLoaded && similarLoaded) {
                        StartLoadingMovies();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                Log.e("Shit happens!", t.getMessage());
                noNetwork = true;
                similarLoaded = true;
                if (topRatedLoaded && collectionLoaded && similarLoaded) {
                    StartLoadingMovies();
                }
                //todo no network!
            }
        });
    }

    private boolean checkIsRightType(MovieDBFullMovieModel model) {
        if (model.isAnimation() && param.watchType == LoadSimilarMovieTasksParams.WatchType.movie)
            return false;

        if (!model.isAnimation() && param.watchType == LoadSimilarMovieTasksParams.WatchType.cartoon)
            return false;

        return true;
    }

    private void StartLoadingMovies() {

        //todo clean list based on marks votes year etc..

        ArrayList<MovieDBFullMovieModel> models = new ArrayList<>();
        int currentDateYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentDateMonth = Calendar.getInstance().get(Calendar.MONTH);

        for (int i = 0; i < loadedList.size(); i++) {
            int year = MovieDBFullMovieModel.getYear(loadedList.get(i).getReleaseDate());
            int month = MovieDBFullMovieModel.getMonth(loadedList.get(i).getReleaseDate());
            if (year <= currentDateYear && month < currentDateMonth && checkIsRightType(loadedList.get(i))) //todo change to checking all date not only uear
            {
                models.add(loadedList.get(i));
            }
        }

        if(models.size()<=0)return;

        ArrayList<MovieDBFullMovieModel> newModelsList = new ArrayList<>();

        double maxVote = -1;
        int pos = -1;
        int size = Math.min(models.size(), 200);
        for (int i = 0; i < size; i++) {
            maxVote = -1;
            for (int j = 0; j < models.size(); j++) {
                if (models.get(j) != null && models.get(j).getVoteAverage() > maxVote) {
                    maxVote = models.get(j).getVoteAverage();
                    pos = j;
                }
            }

            if (models.get(pos) != null)
                newModelsList.add(new MovieDBFullMovieModel(models.get(pos)));
            models.set(pos, null);
        }
        models = new ArrayList<>(newModelsList);

        for(int i=0; i<models.size(); i++)
        {
            for(int j=0; j<param.allMovies.size(); j++)
            {
                if(models.get(i)!=null && models.get(i).getId().equals(param.allMovies.get(j).getId()))
                {
                    models.set(i, null);
                    break;
                }
            }
        }
        newModelsList = new ArrayList<>();
        for(int i=0; i<models.size(); i++){
            if(models.get(i) != null)
            {
                newModelsList.add(models.get(i));
            }
        }
        models = new ArrayList<>(newModelsList);
        newModelsList = null;

        models = MainActivity.helper.getNotViewedMovies(models, param.watchType);


        predictionMarks = new PredictionModel().predictMarksTierOne(models, param.watchType);
        positions = new int[predictionMarks.length];
        for (int i = 0; i < positions.length; i++) positions[i] = i;

        for (int q = 0; q < predictionMarks.length; q++) {
            for (int i = 0; i < predictionMarks.length - q - 1; i++) {
                if (predictionMarks[i] < predictionMarks[i + 1]) {
                    float temp = predictionMarks[i];
                    predictionMarks[i] = predictionMarks[i + 1];
                    predictionMarks[i + 1] = temp;

                    int tempP = positions[i];
                    positions[i] = positions[i + 1];
                    positions[i + 1] = tempP;
                }
            }
        }


        countToLoad = Math.min(predictionMarks.length, 20);//// TODO: 10/25/2015 check the number

        ArrayList<MovieDBFullMovieModel> newList = new ArrayList<>();
        for (int i = 0; i < countToLoad; i++) {
            newList.add(models.get(positions[i]));
        }

        loadedList = newList;

        //load full
        for (int i = 0; i < countToLoad; i++) {
            LoadFullMovieParams params = new LoadFullMovieParams(this, loadedList.get(i).getId(), i, param.watchType, false);

            new LoadFullMoviesTask().execute(params);
        }
    }


    private void showNextMovie() {
        Log.e("Size before cleaning",predictionMarks.length+"");
        countToLoad = Math.min(predictionMarks.length, 20);
        if(loadedList == null || loadedList.size() <= 0)
        {
            ForceSecondTier();
        }

        int maxPos = countToLoad;
        ArrayList<MovieDBFullMovieModel> models = new ArrayList<>();
        //todo add logic to clean prediction marks too!
        for (int i = 0; i < loadedList.size(); i++) {
            if (loadedList.get(i).getCredits() != null )//&& loadedList.get(i).getId().equals(param.movie.getId()))
                models.add(loadedList.get(i));
        }

        if(models.size() == 0)
        {
            LoadFullMovieParams params = new LoadFullMovieParams(this, loadedList.get(0).getId(), 0, param.watchType, true);
            new LoadFullMoviesTask().execute(params);

            return;
        }


        Log.e("Size after cleaning",models.size()+"");
        if (predictionMarks != null)
            predictionMarks = new PredictionModel().predictMarksTierTwo(models, predictionMarks, param.watchType);

        float maxVal = 0;
        int maxValPos = 0;
        for (int i = 0; i < predictionMarks.length; i++) {
            if (maxVal < predictionMarks[i]) {
                maxVal = predictionMarks[i];
                maxValPos = i;
            }
        }

        int desiredPos = maxValPos;


        final MovieDBFullMovieModel fullInfo = new MovieDBFullMovieModel(models.get(desiredPos));
        Log.e("loaded", "loaded");
        for (int i = 0; i < models.size(); i++)
            models.set(i, null);
        models = null;

        param.showNew.showNewMovie(fullInfo, param.pos, param.starNum);

        System.gc();
        //param = null;
        try {
            finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.d("ASYNC_TASK", "Finalize");
    }

    @Override
    public void AddMovieInfo(MovieDBFullMovieModel fullModel, int position) {
//add full to list and calc 2 tier if loaded
        loadedList.set(position, fullModel);
        fullLoaded++;
        if (fullLoaded >= countToLoad) showNextMovie();
    }

    @Override
    public void ForceSecondTierWithFilm(MovieDBFullMovieModel model) {
        if(model==null) return;

        param.showNew.showNewMovie(model, param.pos, param.starNum);

        System.gc();
        //param = null;
        try {
            finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    int errorOccured = 0;
    boolean errorShowed = false;
    @Override
    public void ForceSecondTier() {
        //calc 2 tier
        errorOccured ++;
        if(fullLoaded == 0 && (errorOccured >= countToLoad/2 || noNetwork))
        {
            if(!errorShowed)
            {
                errorShowed = true;
                param.showNew.errorOccurred(noNetwork?"No network connection!":"Free API problem...");
            }
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            return;
        }
        if(fullLoaded!=0)
            showNextMovie();
    }
}
