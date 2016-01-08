package com.i.should.what.whatshouldi.DoPackage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.i.should.what.whatshouldi.DoPackage.Models.DoDayModel;
import com.i.should.what.whatshouldi.HidingScrollListener;
import com.i.should.what.whatshouldi.MainActivity;
import com.i.should.what.whatshouldi.R;

import java.util.Date;

/**
 * Created by ryan on 7/29/2015.
 */
public class DoTodayFragment extends Fragment{

    private View doTabs;
    TextView dayView, monthView;
    View watchLayout, listenLayout;
    RecyclerView watchRView, listenRView;
    DoDayModel model;

    public DoTodayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.do_item_layout, container, false);

        try {
            doTabs = ((ViewGroup) container.getParent()).findViewById(R.id.doTabsLayout);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        model = MainActivity.helper.getDos(new Date());

        dayView = (TextView) itemView.findViewById(R.id.dayDate);
        monthView = (TextView) itemView.findViewById(R.id.mDate);

        watchLayout = itemView.findViewById(R.id.watchLayout);
        listenLayout = itemView.findViewById(R.id.listenLayout);

        watchRView = (RecyclerView) itemView.findViewById(R.id.watchList);
        listenRView = (RecyclerView) itemView.findViewById(R.id.listenList);

        if(model!=null) {
            if (model.listenList == null || model.listenList.size() <= 0) {
                listenLayout.setVisibility(View.GONE);
            } else {
                listenLayout.setVisibility(View.VISIBLE);
                DoListenRecyclerAdapter listenAdapter = new DoListenRecyclerAdapter(model.listenList, getContext());
                listenRView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                listenRView.setAdapter(listenAdapter);
            }

            if (model.watchList == null || model.watchList.size() <= 0) {
                watchLayout.setVisibility(View.GONE);
            } else {
                watchLayout.setVisibility(View.VISIBLE);
                DoWatchRecyclerAdapter watchAdapter = new DoWatchRecyclerAdapter(model.watchList, getContext());
                watchRView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                watchRView.setAdapter(watchAdapter);
            }
        }

        return itemView;
    }
}
