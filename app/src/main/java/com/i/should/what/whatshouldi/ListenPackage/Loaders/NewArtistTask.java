package com.i.should.what.whatshouldi.ListenPackage.Loaders;

import android.os.AsyncTask;
import android.widget.Toast;

import com.i.should.what.whatshouldi.FileManager;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMSimilarArtist;
import com.i.should.what.whatshouldi.ListenPackage.Models.ListenState;
import com.i.should.what.whatshouldi.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ryan on 7/30/2015.
 */
public class NewArtistTask extends AsyncTask<NewArtistTaskParams, Void, Void> {

    NewArtistTaskParams param;
    List<LastFMArtist> savedArtist;

    @Override
    protected Void doInBackground(NewArtistTaskParams... params) {
        param = params[0];

        if (param.currentlyShowing == null || param.currentlyShowing.size() <= 0)
            param.currentlyShowing = new FileManager().readAllArtists(param.context.get());

        savedArtist = MainActivity.helper.checkAndAddArtist(param.artist, param.state);
        if (param.state == ListenState.LIKE)
            getSimilars(param.artist);
        else {
            Random r = new Random();
            int pos = r.nextInt(savedArtist.size());
            while (savedArtist.get(pos).getState() != ListenState.LIKE) {
                pos = r.nextInt(savedArtist.size());
            }
            getSimilars(savedArtist.get(pos));
        }
        return null;
    }

    private void getSimilars(LastFMArtist artist) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LastFMInterfaceConsts.LASTFM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LastFMInterface callInterface = retrofit.create(LastFMInterface.class);

        Call<LastFMSimilarArtist> call = callInterface
                .similarArtist(LastFMInterfaceConsts.LASTFM_SIMILAR_METHOD,
                        artist.getName(true), 50,
                        LastFMInterfaceConsts.LASTFM_API_KEY,
                        LastFMInterfaceConsts.LASTFM_FORMAT);

        call.enqueue(new Callback<LastFMSimilarArtist>() {
            @Override
            public void onResponse(Response<LastFMSimilarArtist> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    LastFMSimilarArtist search = response.body();
                    List<LastFMArtist> artistsRes = search.getResults();

                    NewArtistTask.this.addArtists(artistsRes);
                } else {
                    NewArtistTask.this.addArtists(null);
//                    if(ListenFirstTimeFragment.this!=null && ListenFirstTimeFragment.this.getActivity()!=null)
//                        Toast.makeText(ListenFirstTimeFragment.this.getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                NewArtistTask.this.addArtists(null);
                Toast.makeText(param.context.get(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addArtists(List<LastFMArtist> artists) {
        if (artists == null || artists.size() <= 0) {
            param.callback.showThisArtist(null, -1);
            return;
        }

        ArrayList<LastFMArtist> artistList = new ArrayList<>();
        for (int i = 0; i < artists.size(); i++) {
            boolean b = true;
            for (int j = 0; j < savedArtist.size(); j++) {
                if (artists.get(i).getMbid() == null || artists.get(i).getMbid().equals(savedArtist.get(j).getMbid())) {
                    b = false;
                    break;
                }
            }

            if (b) {
                for (int j = 0; j < param.currentlyShowing.size(); j++) {
                    if (artists.get(i).getMbid().equals(param.currentlyShowing.get(j).getMbid())) {
                        b = false;
                        break;
                    }
                }
            }

            if (b) {
                artistList.add(artists.get(i));
                break;
            }
        }

        savedArtist = null;

        if (artistList.size() > 0)
            param.callback.showThisArtist(artistList.subList(0, Math.min(param.countToLoad, artistList.size())), param.position);
        else
            param.callback.showThisArtist(null, -1);

        ArrayList<LastFMArtist> copyArtist = new ArrayList<>(param.currentlyShowing);
        for (int i = 0; i < copyArtist.size(); i++) {
            if (copyArtist.get(i).getName(true).equals(param.artist.getName(true))) {
                copyArtist.set(i, artistList.get(0));
                break;
            }
        }

        if (param.saveToFile)
            new FileManager().saveAllArtist(param.context.get(), copyArtist);

        System.gc();
    }

    public interface NewArtistTaskCallback {
        void showThisArtist(List<LastFMArtist> artist, int pos);
    }
}
