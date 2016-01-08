package com.i.should.what.whatshouldi.ListenPackage.Loaders;

import android.os.AsyncTask;
import android.util.Log;

import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMAlbum;

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

/**
 * Created by ryan on 7/2/2015.
 */
public class LoadReleaseDateTask extends AsyncTask<LoadReleaseDateTaskParams, Void, String> {
    LoadReleaseDateTaskParams param;
    @Override
    protected String doInBackground(LoadReleaseDateTaskParams... params) {
        param = params[0];
        Log.e("added", "started" + param.position);
        String date = getDate();
        return date;
    }

    private String getDate() {
        String retDate = "";
        String albumName = param.album.getName(true).replaceAll(" ", "+");
        String artistName = param.album.getArtist().replaceAll(" ", "+");
        String searchString = artistName+"+"+albumName;

        String urlToDownload = "https://itunes.apple.com/search?term=" + searchString + "&entity=album&limit=1";
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

            JSONObject albumObject = jArray.getJSONObject(0);
            String date = albumObject.getString("releaseDate");
            //previevURL = albumObject.getString("previewUrl");
            if(date != null && !date.isEmpty())
                retDate = date;

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
        return retDate;
    }

    @Override
    protected void onPostExecute(String date) {
        param.parent.addReleaseDate(date, param.position);
    }

    public interface LoadReleaseDateTaskCallback{
        void addReleaseDate(String date, int pos);
    }
}
