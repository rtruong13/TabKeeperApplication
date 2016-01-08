package com.i.should.what.whatshouldi.ListenPackage.Loaders;

import android.os.AsyncTask;
import android.util.Log;

import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMAlbum;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.ListenPackage.Models.ListenState;
import com.i.should.what.whatshouldi.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by ryan on 11/2/2015.
 */
public class LoadNewAlbumsTask extends AsyncTask<LoadNewAlbumsTaskParams, Void, Void>
        implements GetAlbumsTask.GetAlbumsTaskCallback, NewArtistTask.NewArtistTaskCallback {

    LoadNewAlbumsTaskParams param;
    ArrayList<LastFMAlbum> allAlbums;

    @Override
    protected Void doInBackground(LoadNewAlbumsTaskParams... params) {
        param = params[0];

        allAlbums = new ArrayList<>();
        getLikedArtistAlbums();

        return null;
    }

    protected void getLikedArtistAlbums() {
        ArrayList<LastFMArtist> likedArtist = MainActivity.helper.getArtists(ListenState.LIKE);
        for (int i = 0; i < likedArtist.size(); i++) {
            new GetAlbumsTask().execute(new GetAlbumsTaskParam(likedArtist.get(i), param.context, this));
            new NewArtistTask().execute(new NewArtistTaskParams(param.context, 0, 5,
                    false, this, likedArtist.get(i), ListenState.NOTHING, likedArtist));
        }
    }

    boolean send = false;
    @Override
    public void showAlbums(List<LastFMAlbum> albums) {
        if(send){
            albums = null;
            return;
        }

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < albums.size(); i++) {
            boolean add = true;

            for(int j=0; j<allAlbums.size(); j++)
            {
                if(allAlbums.get(j).getArtist().equals(albums.get(i).getArtist()) &&
                        allAlbums.get(j).getName(true).equals(albums.get(i).getName(true)))
                {
                    add = false;
                    break;
                }
            }

            if (add && albums.get(i).getState() == ListenState.NOTHING &&
                    albums.get(i).getDateYear() >= currentYear - 1) {
                allAlbums.add(albums.get(i));
            }
        }
        Log.e("qwe", allAlbums.size()+"");
        albums = null;
        if(allAlbums.size()>20 && !send)
        {
            Collections.sort(allAlbums, new Comparator<LastFMAlbum>() {
                @Override
                public int compare(LastFMAlbum lhs, LastFMAlbum rhs) {
                    int compYears = -1*lhs.getDateYear().compareTo(rhs.getDateYear());
                    if(compYears != 0) return compYears;

                    return -1*lhs.getDateMonth().compareTo(rhs.getDateMonth());
                }
            });
            param.parent.displayThisAlbums(allAlbums);
            try {
                this.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    @Override
    public void showThisArtist(List<LastFMArtist> artist, int pos) {
        for (int i = 0; i < artist.size(); i++)
            new GetAlbumsTask().execute(new GetAlbumsTaskParam(artist.get(i), param.context, this));
    }

    public interface LoadNewAlbumsTaskCallback{
        void displayThisAlbums(ArrayList<LastFMAlbum> albums);
    }
}
