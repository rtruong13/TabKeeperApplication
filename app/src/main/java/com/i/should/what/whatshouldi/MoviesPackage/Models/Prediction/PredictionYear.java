package com.i.should.what.whatshouldi.MoviesPackage.Models.Prediction;

import java.util.ArrayList;

/**
 * Created by ryan on 7/21/2015.
 */
public class PredictionYear {
    public int years;
    public float usersStars;
    public float votes;
    public float compValue;
    public int count = 0;

    public PredictionYear(int year, float vote, float star) {
        years = year;
        votes = vote;
        usersStars = star;
        count = 1;
    }

    public PredictionYear(PredictionYear year) {
        years = year.years;
        usersStars = year.usersStars;
        votes = year.votes;
        count = year.count;
        compValue = year.compValue;
    }

    public void addStarVote(float star, float vote)
    {
        float newStar = usersStars * count;
        count++;
        newStar = (newStar + star)/count;
        usersStars = newStar;


        float newVote = votes * (count-1);
        newVote = (newVote + vote)/count;
        votes = newVote;
    }

    public static ArrayList<PredictionYear> sortList(ArrayList<PredictionYear> years)
    {
        float averageCount = 0;

        for(int i=0; i<years.size(); i++)
        {
            averageCount+=years.get(i).count;
            //newStars[i] = years.get(i).usersStars * years.get(i).count;
        }
        averageCount = averageCount/years.size();

        for(int i=0; i<years.size(); i++)
        {
            years.get(i).compValue =  years.get(i).usersStars + (3f/4f) * (years.get(i).count / averageCount);
        }

        if(years == null || years.size()<=0) return years;

        ArrayList<PredictionYear> predictionYears = new ArrayList<>();

        int maxPos = 0;
        float maxPredVal = years.get(0).compValue;
        for(int j=0; j<years.size(); j++)
        {
            if(maxPredVal<years.get(j).compValue)
            {
                maxPos = j;
                maxPredVal = years.get(j).compValue;
            }
        }
        predictionYears.add(new PredictionYear(years.get(maxPos)));
        years.get(maxPos).compValue = -1;

        for(int i=1; i<years.size(); i++)
        {
            maxPredVal = -1;
            for(int j=0; j<years.size(); j++)
            {
                if(maxPredVal<years.get(j).compValue)
                {
                    maxPos = j;
                    maxPredVal = years.get(j).compValue;
                }
            }
            predictionYears.add(new PredictionYear(years.get(maxPos)));
            years.get(maxPos).compValue = -2;
        }
        years = null;
        normalize(predictionYears);
        return predictionYears;
    }

    private static void normalize(ArrayList<PredictionYear> years)
    {
        float max = years.get(0).compValue;
        for(int i=0; i<years.size(); i++)
        {
            years.get(i).compValue = years.get(i).compValue * 10f/max;
        }
    }
}
