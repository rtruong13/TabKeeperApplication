package com.i.should.what.whatshouldi.DoPackage.Models;

import android.database.sqlite.SQLiteDatabase;

import com.i.should.what.whatshouldi.DBHelpers.DBHelper;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMAlbum;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.MainActivity;
import com.i.should.what.whatshouldi.MoviesPackage.Loaders.LoadSimilarMovieTasksParams;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;

import java.util.Date;

/**
 * Created by ryan on 7/28/2015.
 */
public class DoModel {
    public Date dateToDo;
    public DoModelType doType;
    public long mID, bID, aID;
    public String doImage;

    /***
     *
     * @param type movie/band/album
     * @param date date when added
     * @param image poster
     */
    public DoModel(DoModelType type, Date date, long mID, long bID, long aID, String image) {
        dateToDo = date;
        doType = type;

        this.mID = mID;
        this.bID = bID;
        this.aID = aID;

        doImage = image;
    }

    /**
     * Create obj of DoModel and save instance to db if needed; for movie
     * @param model
     * @return
     */
    public static DoModel CreateDo(MovieDBFullMovieModel model, LoadSimilarMovieTasksParams.WatchType watchType) {
        long id = MainActivity.helper.saveAddedFilm(model, watchType);
        DoModel doModel = new DoModel(DoModelType.Watch, new Date(), id, -1, -1, model.getPosterImage());
        MainActivity.helper.addDoItem(doModel);
        return doModel;
    }

    /**
     * Create obj of DoModel and save instance to db if needed; for band
     * @param model
     * @return
     */
    public static DoModel CreateDo(LastFMArtist model) {
        SQLiteDatabase database = MainActivity.helper.getReadableDatabase();
        long id = MainActivity.helper.getArtistID(model.getName(true), database);
        DoModel doModel = new DoModel(DoModelType.ListenBand, new Date(), -1, id, -1, model.getLargeImage());
        database.close();

        MainActivity.helper.addDoItem(doModel);
        return doModel;
    }

    public static DoModel CreateDo(LastFMAlbum model) {
        SQLiteDatabase database = MainActivity.helper.getReadableDatabase();
        long bid = MainActivity.helper.getArtistID(model.getArtist(), database);
        long aid = MainActivity.helper.getAlbumID(model.getName(true), database);
        DoModel doModel = new DoModel(DoModelType.ListenAlbum, new Date(), -1, bid, aid, model.getLargeImage());
        database.close();

        MainActivity.helper.addDoItem(doModel);
        return doModel;
    }

    public enum DoModelType{
        Watch(0), ListenBand(1), ListenAlbum(2);

        private final int value;

        DoModelType(int v){this.value = v;}

        public static DoModelType getStateFromInt(int i) {
            if (i == 0) return Watch;
            if (i == 1) return ListenBand;
            return ListenAlbum;
        }

        public int getValue() {
            return value;
        }
    }
}