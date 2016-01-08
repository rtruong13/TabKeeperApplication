package com.i.should.what.whatshouldi.ListenPackage;

import android.os.AsyncTask;
import android.view.View;

import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.ListenPackage.Models.ListenState;
import com.i.should.what.whatshouldi.MainActivity;

import java.util.ArrayList;

/**
 * Created by ryan on 8/1/2015.
 */
public class LoadListeningData extends AsyncTask<LoadListeningData.OnLoadingCompleteCallback, Void, ArrayList<LastFMArtist>> {

    OnLoadingCompleteCallback callback;

    @Override
    protected ArrayList<LastFMArtist> doInBackground(OnLoadingCompleteCallback... params) {
        callback = params[0];

        return MainActivity.helper.getArtists(ListenState.LIKE);
    }

    @Override
    protected void onPostExecute(ArrayList<LastFMArtist> list) {
        if(list!=null)
        callback.showData(list);
        else callback.errorOccured();
    }

    public interface OnLoadingCompleteCallback{
        void showData(ArrayList<LastFMArtist> artists);
        void errorOccured();
    }
}
