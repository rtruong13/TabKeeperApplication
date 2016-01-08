package com.i.should.what.whatshouldi.MoviesPackage;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.i.should.what.whatshouldi.HidingScrollListener;
import com.i.should.what.whatshouldi.MoviesPackage.Loaders.LoadSimilarMovieTasksParams;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;
import com.i.should.what.whatshouldi.R;

import java.util.ArrayList;

public class WatchFragment extends Fragment {

    View watchTabs;
    RecyclerView recyclerView;
    WatchRecyclerAdapter adapter;
    View movieBtn, tvBtn, cartoonBtn;
    MovieDBFullMovieModel modelToChange;

    public WatchFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_watch, container, false);

        watchTabs = v.findViewById(R.id.watchTabsLayout);

        recyclerView = (RecyclerView) v.findViewById(R.id.watchMainRecyclerView);
        adapter = new WatchRecyclerAdapter(getActivity(), new ArrayList<MovieDBFullMovieModel>(), LoadSimilarMovieTasksParams.WatchType.movie, modelToChange);
        recyclerView.setAdapter(adapter);


        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });

        movieBtn = v.findViewById(R.id.showMovieButton);
        movieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadWathcList(LoadSimilarMovieTasksParams.WatchType.movie);
            }
        });

        tvBtn = v.findViewById(R.id.showTVShowButton);
        tvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadWathcList(LoadSimilarMovieTasksParams.WatchType.tvshow);
            }
        });

        cartoonBtn = v.findViewById(R.id.showCartoonsButton);
        cartoonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadWathcList(LoadSimilarMovieTasksParams.WatchType.cartoon);
            }
        });

        LoadWathcList(LoadSimilarMovieTasksParams.WatchType.movie);

        return v;
    }

    private void LoadWathcList(LoadSimilarMovieTasksParams.WatchType type) {
        new MoviesAPIHelper().getWatch(adapter, getActivity(), type);

        System.gc();

        movieBtn.setEnabled(true);
        cartoonBtn.setEnabled(true);
        tvBtn.setEnabled(true);

        if(type == LoadSimilarMovieTasksParams.WatchType.movie)
            movieBtn.setEnabled(false);
        if(type == LoadSimilarMovieTasksParams.WatchType.cartoon)
            cartoonBtn.setEnabled(false);
        if(type == LoadSimilarMovieTasksParams.WatchType.tvshow)
            tvBtn.setEnabled(false);
    }

    private void hideViews() {
        watchTabs.animate().translationY(-watchTabs.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showViews() {
        watchTabs.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    public void changeItem(MovieDBFullMovieModel model) {
        modelToChange = model;
        adapter.changeItem(model);
    }

    public LoadSimilarMovieTasksParams.WatchType getCurrWatchType(){
        return adapter.getCurrentWatchType();
    }
}
