package com.i.should.what.whatshouldi.ListenPackage.Loaders;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ryan on 7/30/2015.
 */
public class GetSongToPlayTask extends AsyncTask<GetSongToPlayTaskParams, Void, String> {

    GetSongToPlayTaskParams param;
    @Override
    protected String doInBackground(GetSongToPlayTaskParams... params) {

        param = params[0];

        String previevURL = "";
        String album = param.album.replaceAll(" ", "+");
        String artist = param.artist.replaceAll(" ", "+");
        String searchString = artist+"+"+album;

        String urlToDownload = "https://itunes.apple.com/search?term=" + searchString + "&entity=song&limit=1";
        URL downloadURL;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            downloadURL = new URL(urlToDownload);
            conn = (HttpURLConnection) downloadURL.openConnection();
            inputStream = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            String json = sb.toString();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jArray = jsonObject.getJSONArray("results");

            JSONObject songObject = jArray.getJSONObject(0);
            String trackName = songObject.getString("trackName");
            previevURL = songObject.getString("previewUrl");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return previevURL;
    }

    @Override
    protected void onPostExecute(String link) {
        super.onPostExecute(link);
        param.callback.playSong(link);
    }

    public interface GetSongToPlayTaskCallback{
        void playSong(String playLink);
    }
}
