package com.i.should.what.whatshouldi.ListenPackage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.i.should.what.whatshouldi.ListenPackage.Loaders.LastFMInterface;
import com.i.should.what.whatshouldi.ListenPackage.Loaders.LastFMInterfaceConsts;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtistSearch;
import com.i.should.what.whatshouldi.ListenPackage.Models.ListenState;
import com.i.should.what.whatshouldi.MainActivity;
import com.i.should.what.whatshouldi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ryan on 7/29/2015.
 */
public class ListenFirstTimeFragment extends Fragment {

    private EditText searchBar;
    private SearchResultsAdapter searchListAdapter;
    private ArrayList<LastFMArtist> selectedArtist;
    private ArrayList<SelectHelperViews> selectedArtistViews;

    public SelectArtistsInterface parent;

    public ListenFirstTimeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_listen_first_time, container, false);

        initFirstTimeViews(v);

        return v;
    }

    private void PutOnRightPosition(){
        for(int i=0; i<5; i++)
        {
            if(i<selectedArtist.size())
            {
                selectedArtistViews.get(i).layout.setVisibility(View.VISIBLE);
                selectedArtistViews.get(i).putArtist(selectedArtist.get(i));
            }
            else selectedArtistViews.get(i).layout.setVisibility(View.GONE);
        }
    }

    protected void SelectArtist(LastFMArtist artist)
    {
        selectedArtist.add(artist);

        if(selectedArtist.size() == 5)
        {
            getActivity()
                    .getSharedPreferences(ListenFragment.LISTEN_SHARED_PREFERENCE, Context.MODE_PRIVATE)
                    .edit().putBoolean("firstTimeShowed", true).commit();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.helper.addArtists(selectedArtist, ListenState.LIKE);
                }
            }).run();

            if(parent!=null)
            {
                parent.loadArtists(selectedArtist);
            }
            else Toast.makeText(getContext(), "Can`t find parent of the fragment...", Toast.LENGTH_SHORT).show();

            return;
        }

        PutOnRightPosition();
        System.gc();
    }

    protected void removeArtist(LastFMArtist artist)
    {
        selectedArtist.remove(artist);
        PutOnRightPosition();
        System.gc();
    }

    private void searchForArtist(String artistToSearch) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LastFMInterfaceConsts.LASTFM_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LastFMInterface callInterface = retrofit.create(LastFMInterface.class);

        Call<LastFMArtistSearch> call = callInterface
                .searchArtist(LastFMInterfaceConsts.LASTFM_SEARCH_METHOD,
                        artistToSearch, 5,
                        LastFMInterfaceConsts.LASTFM_API_KEY,
                        LastFMInterfaceConsts.LASTFM_FORMAT);

        call.enqueue(new Callback<LastFMArtistSearch>() {
            @Override
            public void onResponse(Response<LastFMArtistSearch> response, Retrofit retrofit) {
                if(response.code() == 200)
                {
                    LastFMArtistSearch search = response.body();
                    List<LastFMArtist> artists = search.getArtistsResults();
                    if(artists != null)
                    {
                        searchListAdapter.ChangeResults(artists);
                    }
                }
                else {
//                    if(ListenFirstTimeFragment.this!=null && ListenFirstTimeFragment.this.getActivity()!=null)
//                        Toast.makeText(ListenFirstTimeFragment.this.getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(ListenFirstTimeFragment.this.getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initFirstTimeViews(View v) {
        selectedArtist=new ArrayList<>();
        selectedArtistViews = new ArrayList<>();
        selectedArtistViews.add(new SelectHelperViews(v,R.id.selectedArtist1, R.id.removeArtist1, R.id.layoutArtist1));
        selectedArtistViews.add(new SelectHelperViews(v,R.id.selectedArtist2, R.id.removeArtist2, R.id.layoutArtist2));
        selectedArtistViews.add(new SelectHelperViews(v,R.id.selectedArtist3, R.id.removeArtist3, R.id.layoutArtist3));
        selectedArtistViews.add(new SelectHelperViews(v,R.id.selectedArtist4, R.id.removeArtist4, R.id.layoutArtist4));
        selectedArtistViews.add(new SelectHelperViews(v,R.id.selectedArtist5, R.id.removeArtist5, R.id.layoutArtist5));

        searchListAdapter = new SearchResultsAdapter(getContext(), new ArrayList<LastFMArtist>());
        ((ListView) v.findViewById(R.id.foundArtistsList)).setAdapter(searchListAdapter);
        ((ListView) v.findViewById(R.id.foundArtistsList)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LastFMArtist artist = searchListAdapter.models.get(position);
                boolean added = false;

                for (int i=0; i<selectedArtist.size(); i++)
                {
                    if(selectedArtist.get(i).getMbid().equals(artist.getMbid()))
                    {
                        added = true;
                        break;
                    }
                }

                if(added){
                    Toast.makeText(getContext(), "You`ve already added this artist!", Toast.LENGTH_SHORT).show();
                    return;
                }
                SelectArtist(artist);
                searchListAdapter.ChangeResults(new ArrayList<LastFMArtist>());
                searchBar.setText("");
            }
        });

        searchBar = (EditText) v.findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchForArtist(s.toString());
            }
        });
    }

    private class SelectHelperViews{
        View layout;
        ImageView selectedArtist;
        View removeThisArtist;
        LastFMArtist selected;

        public SelectHelperViews(View v, int rAI, int rAR, int rL){
            selectedArtist = (ImageView) v.findViewById(rAI);
            removeThisArtist = v.findViewById(rAR);
            layout = v.findViewById(rL);
        }

        public void putArtist(LastFMArtist artist) {
            selected = artist;
            if (artist.getLargeImage() != null && !artist.getLargeImage().isEmpty())
                Picasso.with(getContext()).load(artist.getLargeImage()).placeholder(R.drawable.ic_group).into(selectedArtist);
            else Picasso.with(getContext()).load(R.drawable.ic_group).into(selectedArtist);

            selectedArtist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeThisArtist.setVisibility(View.VISIBLE);
                }
            });
            removeThisArtist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListenFirstTimeFragment.this.removeArtist(selected);
                    removeThisArtist.setVisibility(View.GONE);
                }
            });
        }
    }

    public interface SelectArtistsInterface{
        void loadArtists(ArrayList<LastFMArtist> artists);
    }
}
