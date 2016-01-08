package com.i.should.what.whatshouldi.SayPackage;


import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.i.should.what.whatshouldi.MainActivity;
import com.i.should.what.whatshouldi.R;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class SayDetail extends Fragment {

    public SayDetail() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SayModel sayModel = (SayModel) getArguments().getSerializable("sayModel");

        Bundle bundle = getArguments();
        String transText = "";

        if (bundle != null) {
            transText = bundle.getString("TRANS_TEXT");
        }

        //getActivity().setTitle(actionTitle);
        View view = inflater.inflate(R.layout.fragment_say_detail, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.findViewById(R.id.phraseTopic).setTransitionName(transText);
        }

        (view.findViewById(R.id.detailRippleView)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFragmentManager().popBackStack();
                    }
                }, 200);
            }
        });
        ((TextView) view.findViewById(R.id.phraseTopic)).setText(sayModel.name);

        String text;
        if (sayModel.phrases.size() > 0)
            text = sayModel.phrases
                    .get(MainActivity.random.nextInt(sayModel.phrases.size()));
        else
            text = "something went wrong";

        ((TextView) view.findViewById(R.id.phraseTextView)).setText(text);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String fontPath = "fonts/Dosis-ExtraLight.otf";
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), fontPath);

        ((TextView) getView().findViewById(R.id.phraseTextView)).setTypeface(tf);
    }
}
