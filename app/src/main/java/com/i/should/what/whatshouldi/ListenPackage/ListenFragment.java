package com.i.should.what.whatshouldi.ListenPackage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.i.should.what.whatshouldi.FileManager;
import com.i.should.what.whatshouldi.ListenPackage.Loaders.InitialListBuilderTask;
import com.i.should.what.whatshouldi.ListenPackage.Loaders.InitialListBuilderTaskParams;
import com.i.should.what.whatshouldi.ListenPackage.Loaders.LastFMInterface;
import com.i.should.what.whatshouldi.ListenPackage.Loaders.LastFMInterfaceConsts;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtistSearch;
import com.i.should.what.whatshouldi.ListenPackage.Models.ListenState;
import com.i.should.what.whatshouldi.MainActivity;
import com.i.should.what.whatshouldi.MoviesPackage.WatchFragment;
import com.i.should.what.whatshouldi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ListenFragment extends Fragment implements ListenFirstTimeFragment.SelectArtistsInterface,
        InitialListBuilderTask.InitialListBuilderTaskCallback {

    public static final String LISTEN_SHARED_PREFERENCE = "ListenPrefs";
    private boolean firstTimeShowed;

    private FrameLayout frameLayout;
    private ListenMainFragment listenMainFragment;

    public ListenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        firstTimeShowed = getActivity()
                .getSharedPreferences(LISTEN_SHARED_PREFERENCE, Context.MODE_PRIVATE).getBoolean("firstTimeShowed", false);

        View v = inflater.inflate(R.layout.fragment_listen_main, container, false);
        frameLayout = (FrameLayout) v.findViewById(R.id.mainListenFrament);

        if (!firstTimeShowed) {
            ListenFirstTimeFragment fragment = new ListenFirstTimeFragment();
            fragment.parent = this;
            getFragmentManager().beginTransaction().replace(R.id.mainListenFrament, fragment).commit();
        } else {
            listenMainFragment = new ListenMainFragment();
            getFragmentManager().beginTransaction().replace(R.id.mainListenFrament, listenMainFragment).commit();
        }

        return v;
    }

    @Override
    public void loadArtists(ArrayList<LastFMArtist> artists) {
        //grab the list
        new InitialListBuilderTask().execute(new InitialListBuilderTaskParams
                (ListenFragment.this, artists, ListenFragment.this.getActivity()));
    }

    @Override
    public void showArtistList(List<LastFMArtist> artists) {
        //save list to internal mem
        new FileManager().saveAllArtist(getContext(), artists);
        //when list loaded open another fragment
        listenMainFragment = new ListenMainFragment();
        getFragmentManager().beginTransaction().replace(R.id.mainListenFrament, listenMainFragment).commit();
    }

    public void dataSetChanged() {
        if (listenMainFragment != null)
            listenMainFragment.reloadList();
    }
}
