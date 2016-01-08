package com.i.should.what.whatshouldi.DoPackage;

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

import com.i.should.what.whatshouldi.DoPackage.Models.DoDayModel;
import com.i.should.what.whatshouldi.FileManager;
import com.i.should.what.whatshouldi.HidingScrollListener;
import com.i.should.what.whatshouldi.ListenPackage.ListenAlbumFullRecyclerAdapter;
import com.i.should.what.whatshouldi.ListenPackage.ListenArtistRecyclerAdapter;
import com.i.should.what.whatshouldi.ListenPackage.ListenListeningAdapter;
import com.i.should.what.whatshouldi.ListenPackage.LoadListeningData;
import com.i.should.what.whatshouldi.ListenPackage.Loaders.LoadNewAlbumsTask;
import com.i.should.what.whatshouldi.ListenPackage.Loaders.LoadNewAlbumsTaskParams;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMAlbum;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.ListenPackage.SimpleItemTouchCallback;
import com.i.should.what.whatshouldi.MainActivity;
import com.i.should.what.whatshouldi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 7/29/2015.
 */
public class DoCalendarFragment extends Fragment{

    private View doTabs;
    private RecyclerView recyclerView;

    private DoRecyclerAdapter adapter;
    private HidingScrollListener listener;

    public DoCalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_do_calendar, container, false);

        try {
            doTabs = ((ViewGroup) container.getParent()).findViewById(R.id.doTabsLayout);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) v.findViewById(R.id.calendarList);
        adapter = new DoRecyclerAdapter(DoDayModel.getAllDays(MainActivity.helper.getDos()), getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

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

        return v;
    }

    private void hideViews() {
        //todo make sure that is hiding
        if(doTabs == null) return;
        doTabs.animate().translationY(-doTabs.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showViews() {
        if(doTabs == null) return;
        doTabs.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }
}
