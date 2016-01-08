package com.i.should.what.whatshouldi;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.MoviesPackage.Loaders.LoadSimilarMovieTasksParams;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 7/18/2015.
 */
public class FileManager {

    private static final String MOVIES_PATH = "movies.data";
    private static final String CARTOONS_PATH = "cartoons.data";
    private static final String TVSHOWS_PATH = "tvShows.data";
    private static final String ARTIST_PATH = "artists.data";

    public boolean saveToFile(Context context, String nameOfFile, ArrayList<MovieDBFullMovieModel> data, boolean append, LoadSimilarMovieTasksParams.WatchType type) {
        ArrayList<MovieDBFullMovieModel> newList = new ArrayList<>();

        if (append)
            newList.addAll(readAllWatchs(context, type));

        newList.addAll(data);

        boolean saved = false;
        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(
                    context.openFileOutput(nameOfFile, Context.MODE_PRIVATE));

            String strData = new Gson().toJson(newList.toArray());
            outputStreamWriter.write(strData);
            saved = true;
        } catch (Exception e) {
            e.printStackTrace();
            saved = false;
        } finally {
            try {
                outputStreamWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return saved;
    }

    public boolean saveToFile(Context context, String nameOfFile, List<LastFMArtist> data,
                              boolean append) {
        ArrayList<LastFMArtist> newList = new ArrayList<>();

        if (append)
            newList.addAll(readAllArtists(context));

        newList.addAll(data);

        boolean saved = false;
        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(
                    context.openFileOutput(nameOfFile, Context.MODE_PRIVATE));

            String strData = new Gson().toJson(newList.toArray());
            outputStreamWriter.write(strData);
            saved = true;
        } catch (Exception e) {
            e.printStackTrace();
            saved = false;
        } finally {
            try {
                outputStreamWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return saved;
    }

    public String readFromFile(Context context, String nameOfFile) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(nameOfFile);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public ArrayList<MovieDBFullMovieModel> readAllWatchs(Context context, LoadSimilarMovieTasksParams.WatchType watchType) {
        String path = getPathFromType(watchType);

        String savedData = readFromFile(context, path);
        MovieDBFullMovieModel[] savedMovies =
                new Gson().fromJson(savedData, MovieDBFullMovieModel[].class);

        ArrayList<MovieDBFullMovieModel> newList = new ArrayList<>();
        if (savedMovies != null)
            for (int i = 0; i < savedMovies.length; i++) {
                newList.add(savedMovies[i]);
            }

        return newList;
    }

    public ArrayList<LastFMArtist> readAllArtists(Context context) {
        String path = ARTIST_PATH;

        String savedData = readFromFile(context, path);
        LastFMArtist[] savedMovies =
                new Gson().fromJson(savedData, LastFMArtist[].class);

        ArrayList<LastFMArtist> newList = new ArrayList<>();
        if (savedMovies != null)
            for (int i = 0; i < savedMovies.length; i++) {
                newList.add(savedMovies[i]);
            }

        return newList;
    }

    public boolean saveAllMovies(Context context, ArrayList<MovieDBFullMovieModel> movies, LoadSimilarMovieTasksParams.WatchType type) {
        String path = getPathFromType(type);

        return saveToFile(context, path, movies, false, type);
    }

    public boolean appendAllMovies(Context context, ArrayList<MovieDBFullMovieModel> movies, LoadSimilarMovieTasksParams.WatchType type) {
        String path = getPathFromType(type);

        return saveToFile(context, path, movies, true, type);
    }

    private String getPathFromType(LoadSimilarMovieTasksParams.WatchType type) {
        String path = MOVIES_PATH;
        if (type == LoadSimilarMovieTasksParams.WatchType.cartoon)
            path = CARTOONS_PATH;
        if (type == LoadSimilarMovieTasksParams.WatchType.tvshow)
            path = TVSHOWS_PATH;
        return path;
    }

    public void copyAllRaws(Context c)
    {
        copyRawFile(CARTOONS_PATH, c, R.raw.cartoons);
        copyRawFile(MOVIES_PATH, c, R.raw.movies);
        copyRawFile(TVSHOWS_PATH, c, R.raw.tv_shows);
    }

    public void copyRawFile(String outputFile, Context context, Integer resource) {

        OutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(outputFile, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Resources resources = context.getResources();
        byte[] largeBuffer = new byte[1024 * 4];
        int totalBytes = 0;
        int bytesRead = 0;

        try {
            InputStream inputStream = resources.openRawResource(resource.intValue());

            while ((bytesRead = inputStream.read(largeBuffer)) > 0) {
                if (largeBuffer.length == bytesRead) {
                    outputStream.write(largeBuffer);
                } else {
                    final byte[] shortBuffer = new byte[bytesRead];
                    System.arraycopy(largeBuffer, 0, shortBuffer, 0, bytesRead);
                    outputStream.write(shortBuffer);
                }
                totalBytes += bytesRead;
            }

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void saveAllArtist(Context context, List<LastFMArtist> artists) {
        saveToFile(context, ARTIST_PATH, artists, false);
    }
}
