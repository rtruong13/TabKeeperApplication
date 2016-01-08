package com.i.should.what.whatshouldi;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.i.should.what.whatshouldi.SayPackage.SayModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ryan on 7.7.2015.
 */
public class FileUtilsSaySection {
    public static SayModel[] readSaySection(Context c)
    {
        InputStream is = c.getResources().openRawResource(R.raw.say_section);
        String json = readTextFile(is);

        JsonParser parser = new JsonParser();
        JsonElement object = parser.parse(json);

        SayModel[] models = (new Gson().fromJson(object, SayModel[].class));
        return models;
    }

    private static String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }
}
