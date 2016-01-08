package com.i.should.what.whatshouldi.ListenPackage.Loaders;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtistSearch;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMSimilarArtist;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ryan on 7/29/2015.
 */
public class InitialListBuilderTask extends AsyncTask<InitialListBuilderTaskParams, Void, Void>{

    InitialListBuilderTaskParams param;
    ArrayList<LastFMArtist> artistList;
    int loaded;

    @Override
    protected Void doInBackground(InitialListBuilderTaskParams[] params) {
        param = params[0];
        artistList = new ArrayList<>();
        loaded = 0;

        for (LastFMArtist artist : param.artists)
        {
            getSimilarFor(artist);
        }

        return null;
    }

    private void getSimilarFor(final LastFMArtist artist) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LastFMInterfaceConsts.LASTFM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LastFMInterface callInterface = retrofit.create(LastFMInterface.class);

        Call<LastFMSimilarArtist> call = callInterface
                .similarArtist(LastFMInterfaceConsts.LASTFM_SIMILAR_METHOD,
                        artist.getName(true), 10,
                        LastFMInterfaceConsts.LASTFM_API_KEY,
                        LastFMInterfaceConsts.LASTFM_FORMAT);

        call.enqueue(new Callback<LastFMSimilarArtist>() {
            @Override
            public void onResponse(Response<LastFMSimilarArtist> response, Retrofit retrofit) {
                if(response.code() == 200)
                {
                    LastFMSimilarArtist search = response.body();
                    List<LastFMArtist> artistsRes = search.getResults();
                    if(artistsRes != null)
                    {
                        InitialListBuilderTask.this.addArtists(artistsRes);
                    }
                }
                else {
                    InitialListBuilderTask.this.addArtists(new ArrayList<LastFMArtist>());
//                    if(ListenFirstTimeFragment.this!=null && ListenFirstTimeFragment.this.getActivity()!=null)
//                        Toast.makeText(ListenFirstTimeFragment.this.getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(param.context.get(), t.getMessage(), Toast.LENGTH_SHORT).show();
                InitialListBuilderTask.this.addArtists(new ArrayList<LastFMArtist>());
            }
        });
    }

    public void addArtists(List<LastFMArtist> artists)
    {
        artistList.addAll(artists);
        loaded++;
        if(loaded == 5)
        {
            for(int i=0; i<artistList.size(); i++)
            {
                if(artistList.get(i).getMbid() == null || artistList.get(i).getMbid().isEmpty())
                    artistList.set(i, null);
            }

            for(int i=0; i<artistList.size(); i++)
            {

                for(int k=i+1; k<artistList.size(); k++)
                {
                    if(artistList.get(k)!=null && artistList.get(i)!=null &&
                            artistList.get(k).getMbid().equals(artistList.get(i).getMbid()))
                    {
                        artistList.set(k, null);
                    }
                }
            }

            ArrayList<LastFMArtist> cleanedList = new ArrayList<>();
            for(int i=0; i<artistList.size(); i++)
            {
                if(artistList.get(i) != null)
                {
                    cleanedList.add(artistList.get(i));
                }
            }

            Collections.shuffle(cleanedList);
            param.callback.showArtistList(cleanedList.subList(0, Math.min(cleanedList.size(), 10)));
        }
    }

    public interface InitialListBuilderTaskCallback{
        void showArtistList(List<LastFMArtist> artists);
    }
}
