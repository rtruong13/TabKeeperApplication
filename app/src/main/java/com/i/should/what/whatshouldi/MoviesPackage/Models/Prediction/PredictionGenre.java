package com.i.should.what.whatshouldi.MoviesPackage.Models.Prediction;

import java.util.ArrayList;

/**
 * Created by ryan on 7/21/2015.
 */
public class PredictionGenre {
    public int genreId;
    public float avStar;
    public int countOfViews;
    public float compValue;

    public PredictionGenre(int genreId, float star, int count) {
        this.genreId = genreId;
        avStar = star;
        countOfViews = count;
    }

    public PredictionGenre(PredictionGenre predictionGenre) {
        this.countOfViews = predictionGenre.countOfViews;
        this.avStar = predictionGenre.avStar;
        this.genreId = predictionGenre.genreId;
        this.compValue = predictionGenre.compValue;

    }

    public static ArrayList<PredictionGenre> sortList(ArrayList<PredictionGenre> genres) {
        float averageCount = 0;

        for (int i = 0; i < genres.size(); i++) {
            averageCount += genres.get(i).countOfViews;
            //newStars[i] = years.get(i).usersStars * years.get(i).count;
        }
        averageCount = averageCount / genres.size();

        for (int i = 0; i < genres.size(); i++) {
            genres.get(i).compValue = genres.get(i).avStar + (3f / 4f) * (genres.get(i).countOfViews / averageCount);
        }

        if(genres == null || genres.size()<=0) return genres;
        ArrayList<PredictionGenre> predictionGenres = new ArrayList<>();

        int maxPos = 0;
        float maxPredVal = genres.get(0).compValue;
        for (int j = 0; j < genres.size(); j++) {
            if (maxPredVal < genres.get(j).compValue) {
                maxPos = j;
                maxPredVal = genres.get(j).compValue;
            }
        }
        predictionGenres.add(new PredictionGenre(genres.get(maxPos)));
        genres.get(maxPos).compValue = -2;

        for (int i = 1; i < genres.size(); i++) {
            maxPredVal = -1;
            for (int j = 0; j < genres.size(); j++) {
                if (maxPredVal < genres.get(j).compValue) {
                    maxPos = j;
                    maxPredVal = genres.get(j).compValue;
                }
            }
            predictionGenres.add(new PredictionGenre(genres.get(maxPos)));
            genres.get(maxPos).compValue = -2;
        }
        genres = null;
        normalize(predictionGenres);
        return predictionGenres;
    }

    private static void normalize(ArrayList<PredictionGenre> genres)
    {
        float max = genres.get(0).compValue;
        for(int i=0; i<genres.size(); i++)
        {
            genres.get(i).compValue = genres.get(i).compValue * 10f/max;
        }
    }
}
