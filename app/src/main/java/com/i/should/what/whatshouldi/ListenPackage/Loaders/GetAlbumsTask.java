package com.i.should.what.whatshouldi.ListenPackage.Loaders;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMAlbum;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMAlbumsSearch;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ryan on 7/30/2015.
 */
public class GetAlbumsTask extends AsyncTask<GetAlbumsTaskParam, Void, Void> implements LoadReleaseDateTask.LoadReleaseDateTaskCallback {
    GetAlbumsTaskParam param;
    List<LastFMAlbum> savedAlbums;

    @Override
    protected Void doInBackground(GetAlbumsTaskParam... params) {
        param = params[0];
        savedAlbums = MainActivity.helper.getAlbums(param.artist);
        if (savedAlbums == null || savedAlbums.size() <= 0) {
            savedAlbums = new ArrayList<>();
            getAlbums(param.artist);
        } else {
            addAlbums(savedAlbums);
        }
        return null;
    }

    private void getAlbums(LastFMArtist artist) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LastFMInterfaceConsts.LASTFM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LastFMInterface callInterface = retrofit.create(LastFMInterface.class);

        Call<LastFMAlbumsSearch> call = callInterface
                .topAlbums(LastFMInterfaceConsts.LASTFM_GETALBUMS_METHOD,
                        artist.getName(true),
                        200,
                        LastFMInterfaceConsts.LASTFM_API_KEY,
                        LastFMInterfaceConsts.LASTFM_FORMAT);

        call.enqueue(new Callback<LastFMAlbumsSearch>() {
            @Override
            public void onResponse(Response<LastFMAlbumsSearch> response, Retrofit retrofit) {
                if (response.code() == 200) {
                    LastFMAlbumsSearch search = response.body();
                    List<LastFMAlbum> artistsRes = search.getResults();
                    List<LastFMAlbum> cleaned = new ArrayList<LastFMAlbum>();

                    if (artistsRes == null) {
                        GetAlbumsTask.this.addAlbums(artistsRes);
                        return;
                    }
                    needToLoad = search.getCountOfAlbums();

                    for (int i = 0; i < artistsRes.size(); i++) {
                        if (artistsRes.get(i) != null && artistsRes.get(i).getMbid() != null &&
                                !artistsRes.get(i).getMbid().isEmpty() &&
                                artistsRes.get(i).getLargeImage() != null) {

                            artistsRes.get(i).setArtist(param.artist.getName(true));
                            savedAlbums.add(artistsRes.get(i));
                            LastFMAlbum album = artistsRes.get(i);

                            LoadReleaseDateTaskParams params =
                                    new LoadReleaseDateTaskParams(GetAlbumsTask.this,
                                            savedAlbums.size() - 1, album);
                            new LoadReleaseDateTask().execute(params);
                        }
                    }

                    param.callback.showAlbums(savedAlbums);
                } else {
                    addAlbums(null);
//                    if(ListenFirstTimeFragment.this!=null && ListenFirstTimeFragment.this.getActivity()!=null)
//                        Toast.makeText(ListenFirstTimeFragment.this.getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(param.context.get(), t.getMessage(), Toast.LENGTH_SHORT).show();
                addAlbums(null);
            }
        });
    }

    int loaded = 0, needToLoad;

    @Override
    public void addReleaseDate(String releaseDate, int pos) {
        loaded++;
        Log.e("added", "loaded" +  pos);
        savedAlbums.get(pos).setReleaseDate(releaseDate);

        if (loaded >= needToLoad) {
            MainActivity.helper.addAlbums(savedAlbums);
            ((Activity)param.context.get()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    param.callback.showAlbums(savedAlbums);
                }
            });
        }
    }

    public void addAlbums(List<LastFMAlbum> albums) {
        param.callback.showAlbums(albums);
    }

    public interface GetAlbumsTaskCallback {
        void showAlbums(List<LastFMAlbum> albums);
    }
}
