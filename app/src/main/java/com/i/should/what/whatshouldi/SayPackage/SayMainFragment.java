package com.i.should.what.whatshouldi.SayPackage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.i.should.what.whatshouldi.R;

/**
 * Created by ryan on 7.7.2015.
 */
public class SayMainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.empty_frag_content, container, false);

        SayFragment fragment = new SayFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_content, fragment).commit();

        return v;
    }
}
