package com.i.should.what.whatshouldi.ListenPackage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.i.should.what.whatshouldi.BandDetailedActivity;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMAlbum;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.R;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ryan on 8/1/2015.
 */
public class ListenListeningAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchHelperAdapter {

    int HEADER_VIEW_TYPE = 1;
    int NORMAL_VIEW_TYPE = 2;

    private ArrayList<LastFMArtist> allArtist;
    private WeakReference<Context> context;

    public ListenListeningAdapter(Context ctx, ArrayList<LastFMArtist> listening) {
        allArtist = listening;
        context = new WeakReference<>(ctx);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == NORMAL_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.artist_simple_row, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.empty_recycler_header, parent, false);
            return new EmptyHeaderVH(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position != 0) {
            ((ItemViewHolder) holder).BindToModel(context.get(), allArtist.get(position-1), position-1);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER_VIEW_TYPE;
        else return NORMAL_VIEW_TYPE;
    }

    @Override
    public int getItemCount() {
        return allArtist.size() + 1;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            if (fromPosition > allArtist.size() || toPosition > allArtist.size()) return true;
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(allArtist, i, i + 1);
            }
        } else {
            if (fromPosition > allArtist.size() || toPosition > allArtist.size()) return true;
            for (int i = fromPosition-1; i > toPosition; i--) {
                Collections.swap(allArtist, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        allArtist.remove(position);
        //todo put dislike!
        notifyItemRemoved(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        LastFMArtist model;
        ImageView artistImage;
        TextView artistName;
        View layout;

        public ItemViewHolder(View itemView) {
            super(itemView);

            artistImage = (ImageView) itemView.findViewById(R.id.foundArtistImage);
            artistName = (TextView) itemView.findViewById(R.id.foundArtistName);
            layout = itemView.findViewById(R.id.rowLayout);
        }

        public void BindToModel(Context c, LastFMArtist artist, int pos) {
            model = artist;

            if (artist.getLargeImage() != null && !artist.getLargeImage().isEmpty())
                Picasso.with(c).load(artist.getLargeImage()).placeholder(R.drawable.ic_group).into(artistImage);

            artistName.setText(artist.getName(true));
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.get(), BandDetailedActivity.class);
                    intent.putExtra("artist", model);
                    context.get().startActivity(intent);
                }
            });
        }
    }

    public class EmptyHeaderVH extends RecyclerView.ViewHolder {

        public EmptyHeaderVH(View itemView) {
            super(itemView);
        }
    }
}
