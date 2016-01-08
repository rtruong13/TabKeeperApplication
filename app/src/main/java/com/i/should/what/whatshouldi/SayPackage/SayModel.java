package com.i.should.what.whatshouldi.SayPackage;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ryan on 7.7.2015.
 */
public class SayModel implements Serializable {

    public String name;
    public ArrayList<String> phrases;

    public SayModel(String n, ArrayList<String> p)
    {
        name = n;
        phrases = p;
    }

    public SayModel(String n, String[] p)
    {
        name = n;
        phrases = new ArrayList<>();
        for (String s:p) {
            phrases.add(s);
        }
    }
}
