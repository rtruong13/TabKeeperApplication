package com.i.should.what.whatshouldi.SayPackage;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.i.should.what.whatshouldi.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class SayFragment extends Fragment {

    SayAdapter sayAdapter;
    ListView listView;
    ArrayList<SayModel> modelsList;

    public SayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_say, container, false);

        listView = (ListView) v.findViewById(R.id.listView);
        listView.setEmptyView(v.findViewById(R.id.progressPlaceHolder));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowPhrase(view, modelsList.get(position));
            }
        });

        return v;
    }

    private void ShowPhrase(View view, SayModel sayModel) {
        String textTransitionName = "";

        TextView textView = (TextView) view.findViewById(R.id.nameOfSubSec);

        SayDetail sayDetail = new SayDetail();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            setSharedElementReturnTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(R.transition.say_text_transition));

            setExitTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(android.R.transition.slide_top));
            setEnterTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(android.R.transition.explode));

            sayDetail.setSharedElementEnterTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(R.transition.say_text_transition));
            sayDetail.setEnterTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(android.R.transition.slide_bottom));
            sayDetail.setExitTransition(TransitionInflater.from(
                    getActivity()).inflateTransition(android.R.transition.fade));

            textTransitionName = textView.getTransitionName();
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable("sayModel", sayModel);
        bundle.putString("TRANS_TEXT", textTransitionName);
        sayDetail.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_content, sayDetail)
                .addToBackStack("SayDetail")
                .addSharedElement(textView, textTransitionName)
                .commit();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        modelsList = new ArrayList<SayModel>();
        sayAdapter = new SayAdapter(getContext(), R.layout.say_row_item, modelsList, listView);
        listView.setAdapter(sayAdapter);

        ReadSayAsyncParams params = new ReadSayAsyncParams();
        params.context = new WeakReference<Context>(getContext());
        params.adapter = new WeakReference<SayAdapter>(sayAdapter);
        //params.adapterModels = modelsList;

        new ReadSayDataAsyncTask().execute(params);
    }
}
