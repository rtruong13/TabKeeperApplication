package com.i.should.what.whatshouldi.DBHelpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.i.should.what.whatshouldi.DoPackage.Models.DoDayModel;
import com.i.should.what.whatshouldi.DoPackage.Models.DoModel;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMAlbum;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMImage;
import com.i.should.what.whatshouldi.ListenPackage.Models.ListenState;
import com.i.should.what.whatshouldi.MoviesPackage.Loaders.LoadSimilarMovieTasksParams;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBCastModel;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBCrewModel;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBGenreModel;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MoviesState;
import com.i.should.what.whatshouldi.MoviesPackage.Models.Prediction.PredictionGenre;
import com.i.should.what.whatshouldi.MoviesPackage.Models.Prediction.PredictionModel;
import com.i.should.what.whatshouldi.MoviesPackage.Models.Prediction.PredictionPerson;
import com.i.should.what.whatshouldi.MoviesPackage.Models.Prediction.PredictionYear;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by ryan on 7/20/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "MoviesDB";
    private static final int DB_VERSION = 2;
    //private SQLiteDatabase database;

    private static final String DB_MOVIE_TAB_NAME = "Movies";
    private static final String DB_MOVIE_MOVIEID = "MoviesID";
    private static final String DB_MOVIE_STARNUM = "StarNums";
    private static final String DB_MOVIE_WATCHDATE = "DateWatched";
    private static final String DB_MOVIE_POSTER = "PosterPath";
    private static final String DB_MOVIE_MOVIENAME = "Name";
    private static final String DB_MOVIE_RELEASEDATE = "ReleaseDate";
    private static final String DB_MOVIE_OVERVIEW = "Overview";
    private static final String DB_MOVIE_STATE = "State";
    private static final String DB_MOVIE_VOTEFROMDB = "DBVote";
    private static final String DB_MOVIE_GENREIDs = "GenresIDs";
    private static final String DB_MOVIE_MOVIETYPE = "MovieType";

    private static final String DB_PERSON_TAB_NAME = "Persons";
    private static final String DB_PERSON_PERSONID = "PersonID";
    private static final String DB_PERSON_PERSNAME = "PersonName";
    private static final String DB_PERSON_JOB = "PersonJob";
    private static final String DB_PERSON_COUNTSEEN = "PersonCounter";
    private static final String DB_PERSON_STAR = "PersStar";

    private static final String DB_GENRE_TAB_NAME = "Genres";
    private static final String DB_GENRE_GENREID = "GenreID";
    private static final String DB_GENRE_GENRENAME = "GenreName";
    private static final String DB_GENRE_GENRECOUNTSEEN = "GenreConter";
    private static final String DB_GENRE_GENRESTARS = "GenreStars";

    private static final String DB_DOTABLE_TAB_NAME = "DoTable";
    private static final String DB_DOTABLE_MOVIEID = "MID";
    private static final String DB_DOTABLE_BANDID = "BID";
    private static final String DB_DOTABLE_ALBUMID = "AID";
    private static final String DB_DOTABLE_DATE = "DoDate";
    private static final String DB_DOTABLE_PIC = "DoPicture";
    private static final String DB_DOTABLE_TYPE = "DoType";

    private static final String DB_ARTIST_TAB_NAME = "Artists";
    private static final String DB_ARTIST_MBID = "ArtistMBID";
    private static final String DB_ARTIST_NAME = "ArtistName";
    private static final String DB_ARTIST_URL = "ArtistURL";
    private static final String DB_ARTIST_STATE = "ArtistState";
    private static final String DB_ARTIST_IMAGE_LARGE_URL = "ArtistImage";

    private static final String DB_ALBUM_TAB_NAME = "Albums";
    private static final String DB_ALBUM_NAME = "AlbumName";
    private static final String DB_ALBUM_PLAYCOUNT = "AlbumPlayCount";
    private static final String DB_ALBUM_MBID = "AlbumMBID";
    private static final String DB_ALBUM_URL = "AlbumURL";
    private static final String DB_ALBUM_RELEASE_DATE = "AlbumRelease";
    private static final String DB_ALBUM_STATE = "AlbumsState";
    private static final String DB_ALBUM_ARTISTNAME = "AlbumArtistName";
    private static final String DB_ALBUM_IMAGE = "AlbumImage";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDataBase(db);
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    private static final String DATABASE_CREATE_MOVIES_TAB = "create table "
            + DB_MOVIE_TAB_NAME
            + " ( MainID integer primary key autoincrement, "
            + DB_MOVIE_MOVIEID + " integer not null,"
            + DB_MOVIE_MOVIENAME + " text not null,"
            + DB_MOVIE_STARNUM + " float not null,"
            + DB_MOVIE_WATCHDATE + " text not null,"
            + DB_MOVIE_POSTER + " text not null,"
            + DB_MOVIE_RELEASEDATE + " text not null,"
            + DB_MOVIE_OVERVIEW + " text,"
            + DB_MOVIE_STATE + " int not null,"//0 - added, 1 - watched
            + DB_MOVIE_VOTEFROMDB + " float,"
            + DB_MOVIE_GENREIDs + " text not null,"
            + DB_MOVIE_MOVIETYPE + " int not null"//1-movie; 2-tvshow; 3-cartoon
            + ");";

    private static final String DATABASE_CREATE_PERSON_TAB = "create table "
            + DB_PERSON_TAB_NAME
            + "( MainID integer primary key autoincrement, "
            + DB_PERSON_PERSONID + " integer not null,"
            + DB_PERSON_PERSNAME + " text not null,"
            + DB_PERSON_JOB + " text not null,"
            + DB_PERSON_COUNTSEEN + " integer not null,"
            + DB_PERSON_STAR + " float not null"
            + ");";

    private static final String DATABASE_CREATE_GENRES_TAB = "create table "
            + DB_GENRE_TAB_NAME
            + "( MainID integer primary key autoincrement, "
            + DB_GENRE_GENREID + " integer not null,"
            + DB_GENRE_GENRENAME + " text not null,"
            + DB_GENRE_GENRESTARS + " float not null,"
            + DB_GENRE_GENRECOUNTSEEN + " integer not null"
            + ");";

    private static final String DATABASE_CREATE_ARTIST_TAB = "create table "
            + DB_ARTIST_TAB_NAME
            + "( MainID integer primary key autoincrement, "
            + DB_ARTIST_MBID + " text not null,"
            + DB_ARTIST_NAME + " text not null,"
            + DB_ARTIST_STATE + " integer not null,"
            + DB_ARTIST_URL + " text not null,"
            + DB_ARTIST_IMAGE_LARGE_URL + " text not null"
            + ");";

    private static final String DATABASE_CREATE_ALBUMS_TAB = "create table "
            + DB_ALBUM_TAB_NAME
            + "( MainID integer primary key autoincrement, "
            + DB_ALBUM_NAME + " text not null,"
            + DB_ALBUM_PLAYCOUNT + " integer not null,"
            + DB_ALBUM_MBID + " text not null,"
            + DB_ALBUM_URL + " text not null,"
            + DB_ALBUM_RELEASE_DATE + " text not null,"
            + DB_ALBUM_STATE + " integer not null,"
            + DB_ALBUM_ARTISTNAME + " text not null,"
            + DB_ALBUM_IMAGE + " text not null"
            + ");";

    private static final String DATABASE_CREATE_DOTABLE_TAB = "create table "
            + DB_DOTABLE_TAB_NAME
            + " ( MainID integer primary key autoincrement, "
            + DB_DOTABLE_MOVIEID + " integer,"
            + DB_DOTABLE_BANDID + " integer,"
            + DB_DOTABLE_ALBUMID + " integer,"
            + DB_DOTABLE_DATE + " text,"
            + DB_DOTABLE_PIC + " text,"
            + DB_DOTABLE_TYPE + " integer not null"
            + ");";

    private void createDataBase(SQLiteDatabase sqLiteDatabase) {

        try {
            sqLiteDatabase.execSQL(DATABASE_CREATE_MOVIES_TAB);
            sqLiteDatabase.execSQL(DATABASE_CREATE_GENRES_TAB);
            sqLiteDatabase.execSQL(DATABASE_CREATE_PERSON_TAB);
            sqLiteDatabase.execSQL(DATABASE_CREATE_ARTIST_TAB);
            sqLiteDatabase.execSQL(DATABASE_CREATE_ALBUMS_TAB);
            sqLiteDatabase.execSQL(DATABASE_CREATE_DOTABLE_TAB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + DB_GENRE_TAB_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DB_PERSON_TAB_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DB_MOVIE_TAB_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DB_ARTIST_TAB_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DB_ALBUM_TAB_NAME);

        onCreate(db);
    }

    private long addMovie(MovieDBFullMovieModel movieModel, float starNum, SQLiteDatabase database,
                          LoadSimilarMovieTasksParams.WatchType watchType) {
        long pMainID;
        Cursor cursor = database.query(DB_MOVIE_TAB_NAME,
                new String[]{"MainID",DB_MOVIE_MOVIEID},
                DB_MOVIE_MOVIEID + "=?", new String[]{String.valueOf(movieModel.getId())}, null, null, null);

        if (cursor == null || cursor.getCount() <= 0) {
            //create new item
            ContentValues values = new ContentValues();

            values.put(DB_MOVIE_MOVIEID, movieModel.getId());// " integer not null"
            values.put(DB_MOVIE_MOVIENAME, movieModel.getOriginalTitle());// " text not null"
            values.put(DB_MOVIE_STARNUM, starNum);//" float not null"
            values.put(DB_MOVIE_WATCHDATE, (new Date().toString()));//" text not null"
            values.put(DB_MOVIE_POSTER, movieModel.getPosterImage());// " text not null"
            values.put(DB_MOVIE_RELEASEDATE, movieModel.getReleaseDate().toString());//" text not null"
            values.put(DB_MOVIE_OVERVIEW, movieModel.getOverview());//" text"
            values.put(DB_MOVIE_STATE, movieModel.state.getValue());//" int not null"
            values.put(DB_MOVIE_VOTEFROMDB, movieModel.getVoteAverage());//" text"
            values.put(DB_MOVIE_GENREIDs, movieModel.getGenresIds());//" text not null"
            int type = getTypeId(watchType);
            values.put(DB_MOVIE_MOVIETYPE, type);

            pMainID = database.insert(DB_MOVIE_TAB_NAME, null, values);
        }else{
            //update existing
            cursor.moveToFirst();
            pMainID = cursor.getLong(cursor.getColumnIndex("MainID"));

            ContentValues values = new ContentValues();
            values.put(DB_MOVIE_STATE, movieModel.state.getValue());

            database.update(DB_MOVIE_TAB_NAME, values, "MainID=" + pMainID, null);
            cursor.close();
        }
        return pMainID;
    }

    private int getTypeId(LoadSimilarMovieTasksParams.WatchType watchType) {
        int type = 1;
        if (watchType == LoadSimilarMovieTasksParams.WatchType.tvshow)
            type = 2;
        else if (watchType == LoadSimilarMovieTasksParams.WatchType.cartoon)
            type = 3;
        return type;
    }

    public synchronized PredictionModel getNeededInfo(SQLiteDatabase database, LoadSimilarMovieTasksParams.WatchType watchType) {
        ArrayList<PredictionYear> years = yearsInfo(database, watchType);
        years = PredictionYear.sortList(years);

        ArrayList<PredictionGenre> genres = genresInfo(database);
        genres = PredictionGenre.sortList(genres);

        ArrayList<PredictionPerson> crew = getPersonsInfo(database, true);
        crew = PredictionPerson.sortList(crew);

        ArrayList<PredictionPerson> cast = getPersonsInfo(database, false);
        cast = PredictionPerson.sortList(cast);

        PredictionModel model = new PredictionModel(years, genres, crew, cast);
        return model;
    }

    private ArrayList<PredictionPerson> getPersonsInfo(SQLiteDatabase database, boolean crew) {
        Cursor cursor = database.query(DB_PERSON_TAB_NAME,
                new String[]{"MainID", DB_PERSON_PERSONID, DB_PERSON_STAR, DB_PERSON_COUNTSEEN},
                DB_PERSON_JOB + (crew ? "<>" : "=") + "?", new String[]{"actor"}
                , null, null, null);

        if (cursor == null || cursor.getCount() == 0) {
            return new ArrayList<PredictionPerson>();
        }
        int columnIndexGenreID = cursor.getColumnIndex(DB_PERSON_PERSONID);
        int columnIndexGenreStar = cursor.getColumnIndex(DB_PERSON_STAR);
        int columnIndexGenreSeen = cursor.getColumnIndex(DB_PERSON_COUNTSEEN);

        ArrayList<PredictionPerson> persons = new ArrayList<>();
        cursor.moveToFirst();
        do {
            int personId = cursor.getInt(columnIndexGenreID);
            float star = cursor.getFloat(columnIndexGenreStar);
            int count = cursor.getInt(columnIndexGenreSeen);

            persons.add(new PredictionPerson(personId, star, count));
        }
        while (cursor.moveToNext());
        cursor.close();

        return persons;
    }

    private ArrayList<PredictionGenre> genresInfo(SQLiteDatabase database) {
        Cursor cursor = database.query(DB_GENRE_TAB_NAME,
                new String[]{"MainID", DB_GENRE_GENREID, DB_GENRE_GENRESTARS, DB_GENRE_GENRECOUNTSEEN},
                null, null, null, null, null);

        if (cursor == null || cursor.getCount() == 0) {
            Log.e("DBHelper", "can`t change user");
            return new ArrayList<PredictionGenre>();
        }
        int columnIndexGenreID = cursor.getColumnIndex(DB_GENRE_GENREID);
        int columnIndexGenreStar = cursor.getColumnIndex(DB_GENRE_GENRESTARS);
        int columnIndexGenreSeen = cursor.getColumnIndex(DB_GENRE_GENRECOUNTSEEN);

        ArrayList<PredictionGenre> genres = new ArrayList<>();
        cursor.moveToFirst();
        do {
            int genreId = cursor.getInt(columnIndexGenreID);
            float star = cursor.getFloat(columnIndexGenreStar);
            int count = cursor.getInt(columnIndexGenreSeen);

            genres.add(new PredictionGenre(genreId, star, count));
        }
        while (cursor.moveToNext());
        cursor.close();
        return genres;
    }

    private ArrayList<PredictionYear> yearsInfo(SQLiteDatabase database, LoadSimilarMovieTasksParams.WatchType watchType) {
        Cursor cursor = database.query(DB_MOVIE_TAB_NAME,
                new String[]{"MainID", DB_MOVIE_MOVIEID, DB_MOVIE_RELEASEDATE, DB_MOVIE_VOTEFROMDB, DB_MOVIE_STARNUM},
                DB_MOVIE_MOVIETYPE + "=?&"+DB_MOVIE_STATE+"=?",
                new String[]{Integer.toString(getTypeId(watchType)),
                        String.valueOf(MoviesState.ADDED.getValue())}, null, null, null);

        if (cursor == null || cursor.getCount() == 0) {
            Log.e("DBHelper", "can`t change user");
            return new ArrayList<PredictionYear>();
        }
        int columnIndexDate = cursor.getColumnIndex(DB_MOVIE_RELEASEDATE);
        int columnIndexVote = cursor.getColumnIndex(DB_MOVIE_VOTEFROMDB);
        int columnIndexStar = cursor.getColumnIndex(DB_MOVIE_STARNUM);

        ArrayList<PredictionYear> years = new ArrayList<>();
        cursor.moveToFirst();
        do {
            int year = MovieDBFullMovieModel.getYear(cursor.getString(columnIndexDate));
            float vote = cursor.getFloat(columnIndexVote);
            float star = cursor.getFloat(columnIndexStar);
            boolean added = false;

            for (int i = 0; i < years.size(); i++)
                if (years.get(i).years == year) {
                    years.get(i).addStarVote(star, vote);
                    added = true;
                }

            if (!added)
                years.add(new PredictionYear(year, vote, star));
        }
        while (cursor.moveToNext());
        cursor.close();
        return years;
    }

    public ArrayList<MovieDBFullMovieModel> getNotViewedMovies(ArrayList<MovieDBFullMovieModel> allMovies, LoadSimilarMovieTasksParams.WatchType watchType) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(DB_MOVIE_TAB_NAME,
                new String[]{DB_MOVIE_MOVIEID},
                DB_MOVIE_MOVIETYPE + "=?",
                new String[]{Integer.toString(getTypeId(watchType))}, null, null, null);
        ArrayList<MovieDBFullMovieModel> fullMovieModels = new ArrayList<>(allMovies);

        if (cursor != null && cursor.getCount() > 0) {
            int idIndex = cursor.getColumnIndex(DB_MOVIE_MOVIEID);
            cursor.moveToFirst();
            do {
                for (int i = 0; i < fullMovieModels.size(); i++) {
                    if (fullMovieModels.get(i) != null && fullMovieModels.get(i).getId().equals(cursor.getInt(idIndex)))
                        fullMovieModels.set(i, null);
                }
            } while (cursor.moveToNext());
        } else
            fullMovieModels = new ArrayList<>(allMovies);

        ArrayList<MovieDBFullMovieModel> retVals = new ArrayList<>();
        for (MovieDBFullMovieModel model : fullMovieModels) {
            if (model != null)
                retVals.add(model);
        }

        cursor.close();
        database.close();
        return retVals;
    }

    public long saveAddedFilm(MovieDBFullMovieModel movieModel, LoadSimilarMovieTasksParams.WatchType watchType) {

        SQLiteDatabase database = getWritableDatabase();

        movieModel.state = MoviesState.ADDED;
        long id = addMovie(movieModel, -1, database, watchType);
        database.close();
        return id;
    }

    public void saveWatchedFilm(MovieDBFullMovieModel movieModel, float starNum, LoadSimilarMovieTasksParams.WatchType watchType) {

        SQLiteDatabase database = getWritableDatabase();


        movieModel.state = MoviesState.WATCHED;
        addMovie(movieModel, starNum, database, watchType);

        for (MovieDBCastModel cast : movieModel.getCredits().getCast()) {
            int pMainId = getMainIdOfPerson(cast.getId(), "actor", database);
            if (pMainId != -1) {
                //todo update
                updatePersonInfo(pMainId, starNum, database);
            } else {
                //todo create new one
                addPerson(starNum, cast, database);
            }
        }

        for (MovieDBCrewModel crew : movieModel.getCredits().getCrew()) {
            int pMainId = getMainIdOfPerson(crew.getId(), crew.getJob(), database);
            if (pMainId != -1) {
                //todo update
                updatePersonInfo(pMainId, starNum, database);
            } else {
                //todo create new one
                addPerson(starNum, crew, database);
            }
        }

        for (MovieDBGenreModel genre : movieModel.getGenres()) {
            int pMainId = getMainIdOfGenre(genre.getId(), database);
            if (pMainId != -1) {
                //todo update
                updateGenre(pMainId, starNum, database);
            } else {
                //todo create new one
                addGenre(genre, starNum, database);
            }
        }


        database.close();
    }

    private float calcNewStarVal(float starNew, float starOld, int count) {
        float newStarCount = starOld * (count - 1);
        newStarCount += starNew;
        return newStarCount / count;
    }

    private void updateGenre(int pMainId, float starNum, SQLiteDatabase database) {

        Cursor cursor = database.query(DB_GENRE_TAB_NAME,
                new String[]{"MainID", DB_GENRE_GENRESTARS, DB_GENRE_GENRECOUNTSEEN},
                "MainID=" + pMainId, null, null, null, null);

        if (cursor == null || cursor.getCount() == 0) {
            return;
        }
        cursor.moveToFirst();
        float starVal = cursor.getFloat(cursor.getColumnIndex(DB_GENRE_GENRESTARS));
        int countVal = cursor.getInt(cursor.getColumnIndex(DB_GENRE_GENRECOUNTSEEN)) + 1;
        cursor.close();

        starVal = calcNewStarVal(starNum, starVal, countVal);

        ContentValues values = new ContentValues();
        values.put(DB_GENRE_GENRESTARS, starVal);
        values.put(DB_GENRE_GENRECOUNTSEEN, countVal);

        database.update(DB_GENRE_TAB_NAME, values, "MainID=" + pMainId, null);
    }


    private int getMainIdOfGenre(Integer id, SQLiteDatabase database) {
        String sql = "select MainID from " + DB_GENRE_TAB_NAME + " where " +
                DB_GENRE_GENREID + " = " + id;
        Cursor cursor = database.rawQuery(sql, null);
        //null, null, null, null);

        cursor.moveToFirst();
        if (cursor == null || cursor.isAfterLast()) {
            return -1;
        }
        int retVal = cursor.getInt(0);

        cursor.close();
        return retVal;
    }

    public int getMainIdOfPerson(int persId, String job, SQLiteDatabase database) {

        String sql = "select MainID from " + DB_PERSON_TAB_NAME + " where " +
                DB_PERSON_PERSONID + " = " + persId + " and "
                + DB_PERSON_JOB + " = \"" + job + "\"";
        Cursor cursor = database.rawQuery(sql, null);
        //null, null, null, null);

        cursor.moveToFirst();
        if (cursor == null || cursor.isAfterLast()) {
            return -1;
        }

        int retVal = cursor.getInt(0);

        cursor.close();

        return retVal;
    }

    public void updatePersonInfo(int pMainID, float star, SQLiteDatabase database) {

        Cursor cursor = database.query(DB_PERSON_TAB_NAME,
                new String[]{"MainID", DB_PERSON_STAR, DB_PERSON_COUNTSEEN},
                "MainID=" + pMainID, null, null, null, null);


        if (cursor == null || cursor.getCount() < 1) {
            Log.e("DBHelper", "can`t change user");
            return;
        }
        cursor.moveToFirst();
        float starVal = cursor.getFloat(cursor.getColumnIndex(DB_PERSON_STAR));
        int countVal = cursor.getInt(cursor.getColumnIndex(DB_PERSON_COUNTSEEN)) + 1;

        starVal = calcNewStarVal(star, starVal, countVal);

        ContentValues values = new ContentValues();
        values.put(DB_PERSON_STAR, starVal);
        values.put(DB_PERSON_COUNTSEEN, countVal);

        database.update(DB_PERSON_TAB_NAME, values, "MainID=" + pMainID, null);
        cursor.close();
    }

    private void addGenre(MovieDBGenreModel genreModel, float star, SQLiteDatabase database) {
        ContentValues values = new ContentValues();

        values.put(DB_GENRE_GENREID, genreModel.getId());//" integer not null,"
        values.put(DB_GENRE_GENRENAME, genreModel.getName());//" text not null,"
        values.put(DB_GENRE_GENRESTARS, star);//" float not null,"
        values.put(DB_GENRE_GENRECOUNTSEEN, 1);//" integer not null"

        database.insert(DB_GENRE_TAB_NAME, null, values);
    }

    private void addPerson(float starNum, MovieDBCastModel cast, SQLiteDatabase database) {
        ContentValues values = new ContentValues();

        values.put(DB_PERSON_PERSONID, cast.getId());//+ " integer not null,"
        values.put(DB_PERSON_PERSNAME, "" + cast.getName());//+ " text not null,"
        values.put(DB_PERSON_JOB, "actor");//+ " text not null,"
        values.put(DB_PERSON_COUNTSEEN, 1);//+ " integer not null,"
        values.put(DB_PERSON_STAR, starNum);//+ " float not null"

        database.insert(DB_PERSON_TAB_NAME, null, values);
    }

    private void addPerson(float starNum, MovieDBCrewModel crew, SQLiteDatabase database) {
        ContentValues values = new ContentValues();

        values.put(DB_PERSON_PERSONID, crew.getId());//+ " integer not null,"
        values.put(DB_PERSON_PERSNAME, crew.getName());//+ " text not null,"
        values.put(DB_PERSON_JOB, crew.getJob());//+ " text not null,"
        values.put(DB_PERSON_COUNTSEEN, 1);//+ " integer not null,"
        values.put(DB_PERSON_STAR, starNum);//+ " float not null"

        database.insert(DB_PERSON_TAB_NAME, null, values);
    }

    public void addArtist(LastFMArtist artistModel, SQLiteDatabase database, ListenState state) {
        ContentValues values = new ContentValues();

        values.put(DB_ARTIST_MBID, artistModel.getMbid());
        values.put(DB_ARTIST_NAME, artistModel.getName(true));
        values.put(DB_ARTIST_STATE, state.getValue());
        values.put(DB_ARTIST_IMAGE_LARGE_URL, artistModel.getLargeImage());
        values.put(DB_ARTIST_URL, artistModel.getUrl());

        database.insert(DB_ARTIST_TAB_NAME, null, values);
    }

    public List<LastFMArtist> checkAndAddArtist(LastFMArtist artistModel, ListenState state) {
        SQLiteDatabase database = getWritableDatabase();

        Cursor cursor = database.query(DB_ARTIST_TAB_NAME,
                new String[]{DB_ARTIST_MBID,
                        DB_ARTIST_IMAGE_LARGE_URL,
                        DB_ARTIST_NAME,
                        DB_ARTIST_STATE,
                        DB_ARTIST_URL},
                null, null, null, null, null);

        if (cursor == null || cursor.getCount() <= 0) return null;

        ArrayList<LastFMArtist> allArtists = new ArrayList<>();
        int mbidInd = cursor.getColumnIndex(DB_ARTIST_MBID);
        int nameInd = cursor.getColumnIndex(DB_ARTIST_NAME);
        int urlInd = cursor.getColumnIndex(DB_ARTIST_URL);
        int stateInd = cursor.getColumnIndex(DB_ARTIST_STATE);
        int imageInd = cursor.getColumnIndex(DB_ARTIST_IMAGE_LARGE_URL);

        cursor.moveToFirst();
        boolean add = true;
        do {
            String mbid = cursor.getString(mbidInd);
            String name = cursor.getString(nameInd);
            String url = cursor.getString(urlInd);
            String image = cursor.getString(imageInd);
            ListenState stateR = ListenState.getStateFromInt(cursor.getInt(stateInd));

            if(mbid.equals(artistModel.getMbid()))
                add = false;

            LastFMArtist artist = new LastFMArtist(name, mbid, url, image, stateR);
            allArtists.add(artist);
        } while (cursor.moveToNext());

        cursor.close();
        if(add)
            addArtist(artistModel, database, state);

        database.close();

        return allArtists;
    }

    public void addArtists(List<LastFMArtist> artistModels, ListenState stateForEach) {
        SQLiteDatabase database = getWritableDatabase();

        for (int i = 0; i < artistModels.size(); i++)
            addArtist(artistModels.get(i), database, stateForEach);

        database.close();
    }

    public List<LastFMAlbum> getAlbums(LastFMArtist artist) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.query(DB_ALBUM_TAB_NAME,
                new String[]{
                        DB_ALBUM_NAME,
                        DB_ALBUM_MBID,
                        DB_ALBUM_ARTISTNAME,
                        DB_ALBUM_IMAGE,
                        DB_ALBUM_PLAYCOUNT,
                        DB_ALBUM_STATE,
                        DB_ALBUM_URL,
                        DB_ALBUM_RELEASE_DATE
                }, DB_ALBUM_ARTISTNAME + "=?",
                new String[]{artist.getName(true)}, null, null, null);
        if (c == null || c.getCount() <= 0)
            return null;

        c.moveToFirst();
        int nameInd = c.getColumnIndex(DB_ALBUM_NAME);
        int artistInd = c.getColumnIndex(DB_ALBUM_ARTISTNAME);
        int imageInd = c.getColumnIndex(DB_ALBUM_IMAGE);
        int mbidInd = c.getColumnIndex(DB_ALBUM_MBID);
        int playcountInd = c.getColumnIndex(DB_ALBUM_PLAYCOUNT);
        int stateInd = c.getColumnIndex(DB_ALBUM_STATE);
        int urlInd = c.getColumnIndex(DB_ALBUM_URL);
        int dateInd = c.getColumnIndex(DB_ALBUM_RELEASE_DATE);

        ArrayList<LastFMAlbum> albums = new ArrayList<>();
        do{
            String name = c.getString(nameInd);
            String artistName = c.getString(artistInd);
            String image = c.getString(imageInd);
            String mbid = c.getString(mbidInd);
            String url = c.getString(urlInd);
            String date = c.getString(dateInd);
            ListenState state = ListenState.getStateFromInt(c.getInt(stateInd));
            Integer playcount = c.getInt(playcountInd);

            albums.add(new LastFMAlbum(name, mbid, playcount, url, artistName, image, date, state));
        }while (c.moveToNext());

        c.close();
        database.close();

        return albums;
    }

    public synchronized void addAlbum(LastFMAlbum album, SQLiteDatabase database, ListenState state) {
        ContentValues values = new ContentValues();

        values.put(DB_ALBUM_NAME, album.getName(true));// + " text not null,"
        values.put(DB_ALBUM_PLAYCOUNT, album.getPlaycount());// + " integer not null,"
        values.put(DB_ALBUM_MBID, album.getMbid());// + " text not null,"
        values.put(DB_ALBUM_URL, album.getUrl());// + " text not null,"
        values.put(DB_ALBUM_RELEASE_DATE, album.getReleaseDate());// + " text not null,"
        values.put(DB_ALBUM_STATE, 2);// + " integer not null,"
        values.put(DB_ALBUM_ARTISTNAME, album.getArtist());// + " text not null,"
        values.put(DB_ALBUM_IMAGE, album.getLargeImage());// + " text not null"

        database.insert(DB_ALBUM_TAB_NAME, null, values);
    }

    public void updateArtist(LastFMArtist artist, ListenState newState) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DB_ARTIST_STATE, newState.getValue());// + " integer not null,"

        database.update(DB_ARTIST_TAB_NAME, values, DB_ARTIST_NAME + "=?", new String[]{artist.getName(true)});

        database.close();
    }

    public void updateAlbum(LastFMAlbum album, ListenState newState) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DB_ALBUM_STATE, newState.getValue());// + " integer not null,"

        database.update(DB_ALBUM_TAB_NAME, values, DB_ALBUM_NAME + "=?", new String[]{album.getName(true)});

        database.close();
    }

    public synchronized void addAlbums(List<LastFMAlbum> albums)
    {
        SQLiteDatabase database = getWritableDatabase();

        for (LastFMAlbum album : albums) {
            addAlbum(album, database, ListenState.NOTHING);
        }

        database.close();
    }

    public ArrayList<LastFMArtist> getArtists(ListenState state) {
        SQLiteDatabase database = getWritableDatabase();

        Cursor cursor = database.query(DB_ARTIST_TAB_NAME,
                new String[]{DB_ARTIST_MBID,
                        DB_ARTIST_IMAGE_LARGE_URL,
                        DB_ARTIST_NAME,
                        DB_ARTIST_STATE,
                        DB_ARTIST_URL},
                null, null, null, null, null);

        if (cursor == null || cursor.getCount() <= 0) return null;

        ArrayList<LastFMArtist> allArtists = new ArrayList<>();
        int mbidInd = cursor.getColumnIndex(DB_ARTIST_MBID);
        int nameInd = cursor.getColumnIndex(DB_ARTIST_NAME);
        int urlInd = cursor.getColumnIndex(DB_ARTIST_URL);
        int stateInd = cursor.getColumnIndex(DB_ARTIST_STATE);
        int imageInd = cursor.getColumnIndex(DB_ARTIST_IMAGE_LARGE_URL);

        cursor.moveToFirst();

        do {
            String mbid = cursor.getString(mbidInd);
            String name = cursor.getString(nameInd);
            String url = cursor.getString(urlInd);
            String image = cursor.getString(imageInd);
            ListenState stateR = ListenState.getStateFromInt(cursor.getInt(stateInd));

            LastFMArtist artist = new LastFMArtist(name, mbid, url, image, stateR);
            if(state != ListenState.NOTHING && state == stateR)
                allArtists.add(artist);

            if(allArtists.size()>=100) break;
        } while (cursor.moveToNext());

        cursor.close();

        database.close();

        return allArtists;
    }

    public void addDoItem(DoModel doModel)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DB_DOTABLE_ALBUMID, doModel.aID);
        values.put(DB_DOTABLE_BANDID, doModel.bID);
        values.put(DB_DOTABLE_MOVIEID, doModel.mID);
        values.put(DB_DOTABLE_PIC, doModel.doImage);
        values.put(DB_DOTABLE_DATE, new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(doModel.dateToDo));
        values.put(DB_DOTABLE_TYPE, doModel.doType.getValue());

        database.insert(DB_DOTABLE_TAB_NAME, null, values);
        database.close();
    }

    public List<DoModel> getDos()
    {
        SQLiteDatabase database = getReadableDatabase();

        Cursor cursor = database.query(DB_DOTABLE_TAB_NAME,
                new String[]{DB_DOTABLE_ALBUMID,
                        DB_DOTABLE_BANDID,
                        DB_DOTABLE_MOVIEID,
                        DB_DOTABLE_DATE,
                        DB_DOTABLE_PIC,
                        DB_DOTABLE_TYPE},
                null, null, null, null, null);

        if (cursor == null || cursor.getCount() <= 0) return null;
        List<DoModel> models = new ArrayList<>();

        int aIDind = cursor.getColumnIndex(DB_DOTABLE_ALBUMID);
        int bIDind = cursor.getColumnIndex(DB_DOTABLE_BANDID);
        int mIDind = cursor.getColumnIndex(DB_DOTABLE_MOVIEID);
        int dateind = cursor.getColumnIndex(DB_DOTABLE_DATE);
        int imageInd = cursor.getColumnIndex(DB_DOTABLE_PIC);
        int typeInd = cursor.getColumnIndex(DB_DOTABLE_TYPE);

        cursor.moveToFirst();

        do {
            int mID = cursor.getInt(mIDind);
            int aID = cursor.getInt(aIDind);
            int bID = cursor.getInt(bIDind);
            String image = cursor.getString(imageInd);
            String s = cursor.getString(dateind);
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(s, new ParsePosition(0));
            //Date d = new Date(cursor.getString(dateind));
            DoModel.DoModelType doType = DoModel.DoModelType.getStateFromInt(cursor.getInt(typeInd));

            DoModel model = new DoModel(doType, d, mID, bID, aID, image);
            models.add(model);

            if(models.size()>=300) break;
        } while (cursor.moveToNext());

        cursor.close();

        database.close();

        return models;
    }

    public long getArtistID(String artistName, SQLiteDatabase database) {
        Cursor cursor = database.query(DB_ARTIST_TAB_NAME, new String[]{"MainID"}, DB_ARTIST_NAME + "=?",
                new String[]{artistName}, null, null, null, null);

        if (cursor == null || cursor.getCount() <= 0) return -1;

        cursor.moveToFirst();
        long ret = cursor.getLong(cursor.getColumnIndex("MainID"));
        cursor.close();

        return ret;
    }

    public long getAlbumID(String albumName, SQLiteDatabase database) {
        Cursor cursor = database.query(DB_ALBUM_TAB_NAME, new String[]{"MainID"}, DB_ALBUM_NAME + "=?",
                new String[]{albumName}, null,null,null,null);

        if (cursor == null || cursor.getCount() <= 0) return -1;

        cursor.moveToFirst();
        long ret = cursor.getLong(cursor.getColumnIndex("MainID"));
        cursor.close();

        return ret;
    }

    public DoDayModel getDos(Date date) {
        SQLiteDatabase database = getReadableDatabase();


        String dStr = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date);

        Cursor cursor = database.query(DB_DOTABLE_TAB_NAME,
                new String[]{DB_DOTABLE_ALBUMID,
                        DB_DOTABLE_BANDID,
                        DB_DOTABLE_MOVIEID,
                        DB_DOTABLE_DATE,
                        DB_DOTABLE_PIC,
                        DB_DOTABLE_TYPE},
                DB_DOTABLE_DATE + "=?", new String[]{dStr}, null, null, null);

        if (cursor == null || cursor.getCount() <= 0) return null;
        List<DoModel> models = new ArrayList<>();

        int aIDind = cursor.getColumnIndex(DB_DOTABLE_ALBUMID);
        int bIDind = cursor.getColumnIndex(DB_DOTABLE_BANDID);
        int mIDind = cursor.getColumnIndex(DB_DOTABLE_MOVIEID);
        int dateind = cursor.getColumnIndex(DB_DOTABLE_DATE);
        int imageInd = cursor.getColumnIndex(DB_DOTABLE_PIC);
        int typeInd = cursor.getColumnIndex(DB_DOTABLE_TYPE);

        cursor.moveToFirst();

        do {
            int mID = cursor.getInt(mIDind);
            int aID = cursor.getInt(aIDind);
            int bID = cursor.getInt(bIDind);
            String image = cursor.getString(imageInd);

            String s = cursor.getString(dateind);
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(s, new ParsePosition(0));

            DoModel.DoModelType doType = DoModel.DoModelType.getStateFromInt(cursor.getInt(typeInd));

            DoModel model = new DoModel(doType, d, mID, bID, aID, image);
            models.add(model);

            if(models.size()>=300) break;
        } while (cursor.moveToNext());

        cursor.close();

        database.close();

        return DoDayModel.getAllDays(models).get(0);
    }
}