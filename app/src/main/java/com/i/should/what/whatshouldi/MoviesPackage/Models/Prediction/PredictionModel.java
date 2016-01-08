package com.i.should.what.whatshouldi.MoviesPackage.Models.Prediction;

import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;

import com.i.should.what.whatshouldi.MainActivity;
import com.i.should.what.whatshouldi.MoviesPackage.Loaders.LoadSimilarMovieTasksParams;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;

/**
 * Created by ryan on 7/21/2015.
 */
public class PredictionModel {

    private ArrayList<PredictionYear> years;
    private ArrayList<PredictionGenre> genres;
    private ArrayList<PredictionPerson> crew;
    private ArrayList<PredictionPerson> cast;

    public PredictionModel() {
    }

    public PredictionModel(ArrayList<PredictionYear> years,
                           ArrayList<PredictionGenre> genres,
                           ArrayList<PredictionPerson> crew,
                           ArrayList<PredictionPerson> cast) {
        this.years = years;
        this.genres = genres;
        this.crew = crew;
        this.cast = cast;
    }


    private void getNeededInfo(SQLiteDatabase database, boolean includeCredits, LoadSimilarMovieTasksParams.WatchType watchType) {
        PredictionModel info = MainActivity.helper.getNeededInfo(database, watchType);
        if (!includeCredits) {
            years = info.years;
            genres = info.genres;
        } else {
            crew = info.crew;
            cast = info.cast;
        }
    }

    public float[] predictMarksTierOne(ArrayList<MovieDBFullMovieModel> arrayList, LoadSimilarMovieTasksParams.WatchType watchType) {
        SQLiteDatabase database = MainActivity.helper.getReadableDatabase();
        getNeededInfo(database, false, watchType);
        database.close();

        float[] compVals = new float[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            compVals[i] = getResult(arrayList.get(i), false, -1);
        }

        return compVals;
    }

    public float[] predictMarksTierTwo(ArrayList<MovieDBFullMovieModel> arrayList, float[] previousMarks, LoadSimilarMovieTasksParams.WatchType watchType) {
        SQLiteDatabase database = MainActivity.helper.getReadableDatabase();
        getNeededInfo(database, true,watchType);
        database.close();
        int size = Math.min(arrayList.size(), previousMarks.length); //todo for now!! change later!
        float[] compVals = new float[size];
        for (int i = 0; i < size; i++) {
            compVals[i] = getResult(arrayList.get(i), true, previousMarks[i]);
        }

        return compVals;
    }

    private PredictionGenre findGenreById(int genreId) {
        for (int i = 0; i < genres.size(); i++) {
            if (genres.get(i).genreId == genreId)
                return genres.get(i);
        }
        return null;
    }

    private PredictionYear findYear(int year) {
        for (int i = 0; i < years.size(); i++) {
            if (years.get(i).years == year)
                return years.get(i);
        }
        return null;
    }


    /**
     * @param model
     * @param includeCast if true method will not calculate basic things again
     *                    it will only change prev val based on credits prediction
     * @param prevVal     previous value without cast and crew prediction
     * @return
     */
    private float getResult(MovieDBFullMovieModel model, boolean includeCast, float prevVal) {
        if (!includeCast) {
            float genresAv = getGenresVal(model);
            float kofG = 0.25f;

            float yearsAv = getYearVal(model);
            float kofY = 0.05f;
            float overallResult = genresAv * kofG + yearsAv * kofY;
            return overallResult;
        } else {
            float castAv = getPersonVal(model, true);
            float kofCA = 0.3f;
            float crewAv = getPersonVal(model, false);
            float kofCR = 0.4f;

            float overallResult = prevVal + castAv * kofCA + crewAv * kofCR;

            return overallResult;
        }
    }

    private float getPersonVal(MovieDBFullMovieModel model, boolean isCast) {
        float personsVal = 0;
        int countP = 0;

        if (isCast) {
            if (model.getCredits() != null && model.getCredits().getCast() != null && model.getCredits().getCast().size() > 0) {
                for (int i = 0; i < model.getCredits().getCast().size(); i++) {
                    PredictionPerson predPerson = findPersonInArray(model.getCredits().getCast().get(i).getId(), cast);
                    if (predPerson != null) {
                        personsVal += predPerson.compValue;
                        countP++;
                    }
                }

                if (countP > 0)
                    personsVal = personsVal / countP;
                else
                    personsVal = 7f;
            } else personsVal = 7f;
            return personsVal;

        } else {
            if (model.getCredits() != null && model.getCredits().getCrew() != null && model.getCredits().getCrew().size() > 0) {
                for (int i = 0; i < model.getCredits().getCrew().size(); i++) {
                    PredictionPerson predPerson = findPersonInArray(model.getCredits().getCrew().get(i).getId(), crew);
                    if (predPerson != null) {
                        personsVal += predPerson.compValue;
                        countP++;
                    }
                }

                if (countP > 0)
                    personsVal = personsVal / countP;
                else
                    personsVal = 7f;
            } else personsVal = 7f;
            return personsVal;
        }
    }

    private PredictionPerson findPersonInArray(int id, ArrayList<PredictionPerson> persons) {

        for (PredictionPerson person : persons) {
            if (person.idOfPerson == id) {
                return person;
            }
        }

        return null;
    }

    private float getYearVal(MovieDBFullMovieModel model) {
        float yearAv = 0;
        int countY = 0;
        if (model.getReleaseDate() != null && !model.getReleaseDate().isEmpty()) {
            PredictionYear predYear = findYear(MovieDBFullMovieModel.getYear(model.getReleaseDate()));
            if (predYear != null) {
                yearAv += predYear.compValue;
                countY++;
            }

            if (countY > 0)
                yearAv = yearAv / countY;
            else yearAv = 7f;
        } else yearAv = 7f;

        return yearAv;
    }

    private float getGenresVal(MovieDBFullMovieModel model) {
        float genresAv = 0;
        int countG = 0;
        if (model.getGenres() != null && model.getGenres().size() > 0) {
            for (int i = 0; i < model.getGenres().size(); i++) {
                PredictionGenre predGenre = findGenreById(model.getGenres().get(i).getId());
                if (predGenre != null) {
                    genresAv += predGenre.compValue;
                    countG++;
                }
            }

            if (countG > 0)
                genresAv = genresAv / countG;
            else genresAv = 7f;
        } else
        if (model.getGenresDiscoveryIds() != null && model.getGenresDiscoveryIds().size() > 0) {
            for (int i = 0; i < model.getGenresDiscoveryIds().size(); i++) {
                PredictionGenre predGenre = findGenreById(model.getGenresDiscoveryIds().get(i));
                if (predGenre != null) {
                    genresAv += predGenre.compValue;
                    countG++;
                }
            }

            if (countG > 0)
                genresAv = genresAv / countG;
            else genresAv = 7f;
        } else genresAv = 7f;
        return genresAv;
    }
}
