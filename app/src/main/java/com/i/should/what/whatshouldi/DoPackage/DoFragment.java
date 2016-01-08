package com.i.should.what.whatshouldi.DoPackage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.i.should.what.whatshouldi.R;

public class DoFragment extends Fragment{

    private DoCalendarFragment calendarFragment;
    private DoTodayFragment todayFragment;
    private View showCalendarBtn;
    private View showSurpriseBtn;
    private View showTodayBtn;

    public DoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_do, container, false);

        calendarFragment = new DoCalendarFragment();
        todayFragment = new DoTodayFragment();

        showCalendarBtn = v.findViewById(R.id.showCalendar);
        showCalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.doMainContainer, calendarFragment).commit();
                showCalendarBtn.setEnabled(false);
                showSurpriseBtn.setEnabled(true);
                showTodayBtn.setEnabled(true);
            }
        });

        showTodayBtn = v.findViewById(R.id.showToday);
        showTodayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.doMainContainer, todayFragment).commit();
                showCalendarBtn.setEnabled(true);
                showSurpriseBtn.setEnabled(true);
                showTodayBtn.setEnabled(false);
            }
        });

        showSurpriseBtn = v.findViewById(R.id.showSurprise);
        showSurpriseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.doMainContainer, todayFragment).commit();
                showCalendarBtn.setEnabled(true);
                showSurpriseBtn.setEnabled(false);
                showTodayBtn.setEnabled(true);
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        calendarFragment = new DoCalendarFragment();
        todayFragment = new DoTodayFragment();

        getFragmentManager().beginTransaction().replace(R.id.doMainContainer, calendarFragment).commit();
        showCalendarBtn.setEnabled(false);
        showSurpriseBtn.setEnabled(true);
        showTodayBtn.setEnabled(true);
    }
}
