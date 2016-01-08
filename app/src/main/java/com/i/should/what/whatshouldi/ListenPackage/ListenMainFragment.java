package com.i.should.what.whatshouldi.ListenPackage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.i.should.what.whatshouldi.FileManager;
import com.i.should.what.whatshouldi.HidingScrollListener;
import com.i.should.what.whatshouldi.ListenPackage.Loaders.LoadNewAlbumsTask;
import com.i.should.what.whatshouldi.ListenPackage.Loaders.LoadNewAlbumsTaskParams;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMAlbum;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 7/29/2015.
 */
public class ListenMainFragment extends Fragment
        implements LoadListeningData.OnLoadingCompleteCallback, LoadNewAlbumsTask.LoadNewAlbumsTaskCallback {

    private View listenTabs;
    private RecyclerView recyclerView;

    private ItemTouchHelper touchHelper;
    private SimpleItemTouchCallback callback;
    private ListenArtistRecyclerAdapter adapterArtist;
    private ListenListeningAdapter adapterList;
    private ListenAlbumFullRecyclerAdapter adapterAlbums;
    private HidingScrollListener listener;
    private View showArtists;
    private View showAlbums;
    private View showListening;

    public ListenMainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_listen, container, false);

        listenTabs = v.findViewById(R.id.listenTabsLayout);

        recyclerView = (RecyclerView) v.findViewById(R.id.listenMainRecyclerView);

        listener = new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        };
        recyclerView.addOnScrollListener(listener);

        showArtists = (v.findViewById(R.id.showArtistButton));
        showArtists.setEnabled(false);
        showArtists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showArtists();
                System.gc();
                showArtists.setEnabled(false);
                showAlbums.setEnabled(true);
                showListening.setEnabled(true);
            }
        });
        showAlbums = (v.findViewById(R.id.showNewAlbumsButton));
        showAlbums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new LoadNewAlbumsTask().execute(new LoadNewAlbumsTaskParams(getContext(), ListenMainFragment.this));
//                System.gc();
//                Toast.makeText(ListenMainFragment.this.getContext(), "Wait for it... It takes ~10sec... will try to fix it later.", Toast.LENGTH_SHORT).show();
                Toast.makeText(ListenMainFragment.this.getContext(), "Sorry this section is not ready yet", Toast.LENGTH_SHORT).show();
            }
        });
        showListening = (v.findViewById(R.id.showListeningButton));
        showListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadListeningData().execute(ListenMainFragment.this);
                System.gc();
                showArtists.setEnabled(true);
                showAlbums.setEnabled(true);
                showListening.setEnabled(false);
            }
        });

        showArtists();

        return v;
    }

    private void showArtists() {
        callback = null;
        touchHelper = null;

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapterArtist = null;
        adapterArtist = new ListenArtistRecyclerAdapter(getActivity(), new FileManager().readAllArtists(getActivity()));
        recyclerView.setAdapter(adapterArtist);
    }

    private void hideViews() {
        listenTabs.animate().translationY(-listenTabs.getHeight()).setInterpolator(new AccelerateInterpolator(2));//todo make sure that is hiding
    }

    private void showViews() {
        listenTabs.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    public void reloadList() {
        try {
            List<LastFMArtist> artists = new FileManager().readAllArtists(getActivity());
            adapterArtist.addItemsInfo(artists, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showData(ArrayList<LastFMArtist> artists) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterList = new ListenListeningAdapter(getActivity(), artists);
        recyclerView.setAdapter(adapterList);
        if (touchHelper == null) {
            callback = new SimpleItemTouchCallback(adapterList);
            touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);
        } else {
            callback.changeAdapter(adapterList);
        }
    }

    @Override
    public void errorOccured() {
        Toast.makeText(getContext(), "Can`t load data!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayThisAlbums(final ArrayList<LastFMAlbum> albums) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback = null;
                touchHelper = null;
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                adapterAlbums = new ListenAlbumFullRecyclerAdapter(getActivity(), albums.subList(0, 10));
                recyclerView.setAdapter(adapterAlbums);
            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showArtists.setEnabled(true);
                showAlbums.setEnabled(false);
                showListening.setEnabled(true);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.gc();
            }
        }).run();
    }
}
