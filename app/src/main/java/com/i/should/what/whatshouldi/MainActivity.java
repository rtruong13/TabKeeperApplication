package com.i.should.what.whatshouldi;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.i.should.what.whatshouldi.DBHelpers.DBHelper;
import com.i.should.what.whatshouldi.DoPackage.DoFragment;
import com.i.should.what.whatshouldi.DoPackage.Models.DoModel;
import com.i.should.what.whatshouldi.ListenPackage.ListenFragment;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;
import com.i.should.what.whatshouldi.MoviesPackage.WatchFragment;
import com.i.should.what.whatshouldi.SayPackage.SayMainFragment;

import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final int WATCH_DETAIL_REQ = 1;
    public static final int BAND_DETAIL_REQ = 2;
    public static final int SOMETHING_CHANGED = 666;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    public static Random random;

    protected SayMainFragment sayFragment;
    protected WatchFragment watchFragment;
    protected ListenFragment listenFragment;

    public static DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new DBHelper(this);
        random = new Random(new Date().getTime());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Toast.makeText(this, "This app is not finished yet. Sorry about lags and bugs.", Toast.LENGTH_SHORT).show();

        InitFragments();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WATCH_DETAIL_REQ && resultCode == 2 && data != null) {
            if (watchFragment != null) {
                MovieDBFullMovieModel model = (MovieDBFullMovieModel) data.getSerializableExtra("watchItem");
//              changing movie to similar!
//              if (model != null)
//                    watchFragment.changeItem(model);
                DoModel.CreateDo(model, watchFragment.getCurrWatchType());//TODO hide posterlayout!
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (listenFragment != null)
            listenFragment.dataSetChanged();
    }

    public void InitFragments() {
        sayFragment = new SayMainFragment();
        watchFragment = new WatchFragment();
        listenFragment = new ListenFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new DoFragment();
            } else if (position == 1) {
                return watchFragment;
            } else if (position == 2) {
                return listenFragment;
            } else if (position == 3) {
                return sayFragment;
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Do";
                case 1:
                    return "Watch";
                case 2:
                    return "Listen";
                case 3:
                    return "Say";
            }
            return null;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}
