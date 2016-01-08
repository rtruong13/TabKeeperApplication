package com.i.should.what.whatshouldi.ListenPackage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ryan on 7/28/2015.
 */
public class SearchResultsAdapter extends ArrayAdapter<LastFMArtist> {
    List<LastFMArtist> models;

    public SearchResultsAdapter(Context context, List<LastFMArtist> objects) {
        super(context, R.layout.artist_simple_row, objects);
        models = objects;
    }

    public void ChangeResults(List<LastFMArtist> newList) {
        models.clear();
        models.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.artist_simple_row, parent, false);
            SearchArtistViewHolder viewHolder = new SearchArtistViewHolder(row);
            row.setTag(viewHolder);
        }

        SearchArtistViewHolder holder = (SearchArtistViewHolder) row.getTag();
        holder.PutData(getItem(position));
        return row;
    }

    protected class SearchArtistViewHolder {
        ImageView imageView;
        TextView textView;

        public SearchArtistViewHolder(View v) {
            imageView = (ImageView) v.findViewById(R.id.foundArtistImage);
            textView = (TextView) v.findViewById(R.id.foundArtistName);
        }

        public void PutData(LastFMArtist artist) {
            if (artist.getLargeImage() != null && !artist.getLargeImage().isEmpty())
                Picasso.with(getContext()).load(artist.getLargeImage()).placeholder(R.drawable.ic_group).into(imageView);
            else Picasso.with(getContext()).load(R.drawable.ic_group).into(imageView);
            textView.setText(artist.getName(false));
        }
    }
}
