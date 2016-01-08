package com.i.should.what.whatshouldi.MoviesPackage.Models.Prediction;

import java.util.ArrayList;

/**
 * Created by ryan on 7/21/2015.
 */
public class PredictionPerson {
    public int idOfPerson;
    public float avStar;
    public int countOfViews;
    public float compValue;

    public PredictionPerson(int personId, float star, int count) {
        idOfPerson = personId;
        avStar = star;
        countOfViews = count;
    }

    public PredictionPerson(PredictionPerson predictionPerson) {
        idOfPerson = predictionPerson.idOfPerson;
        avStar = predictionPerson.avStar;
        countOfViews = predictionPerson.countOfViews;
        compValue = predictionPerson.compValue;
    }

    public static ArrayList<PredictionPerson> sortList(ArrayList<PredictionPerson> persons) {
        float averageCount = 0;

        for (int i = 0; i < persons.size(); i++) {
            averageCount += persons.get(i).countOfViews;
            //newStars[i] = years.get(i).usersStars * years.get(i).count;
        }
        averageCount = averageCount / persons.size();

        for (int i = 0; i < persons.size(); i++) {
            persons.get(i).compValue = persons.get(i).avStar + (3f / 4f) * (persons.get(i).countOfViews / averageCount);
        }

        if(persons == null || persons.size()<=0) return persons;
        ArrayList<PredictionPerson> predictionPersons = new ArrayList<>();

        int maxPos = 0;
        float maxPredVal = persons.get(0).compValue;
        for (int j = 0; j < persons.size(); j++) {
            if (maxPredVal < persons.get(j).compValue) {
                maxPos = j;
                maxPredVal = persons.get(j).compValue;
            }
        }
        predictionPersons.add(new PredictionPerson(persons.get(maxPos)));
        persons.get(maxPos).compValue = -2;

        for (int i = 1; i < persons.size(); i++) {
            maxPredVal = -1;
            for (int j = 0; j < persons.size(); j++) {
                if (maxPredVal < persons.get(j).compValue) {
                    maxPos = j;
                    maxPredVal = persons.get(j).compValue;
                }
            }
            predictionPersons.add(new PredictionPerson(persons.get(maxPos)));
            persons.get(maxPos).compValue = -2;
        }
        persons = null;
        normalize(predictionPersons);
        return predictionPersons;
    }

    private static void normalize(ArrayList<PredictionPerson> persons)
    {
        float max = persons.get(0).compValue;
        for(int i=0; i<persons.size(); i++)
        {
            persons.get(i).compValue = persons.get(i).compValue * 10f/max;
        }
    }
}
