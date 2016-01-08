package com.i.should.what.whatshouldi.ListenPackage;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.i.should.what.whatshouldi.DoPackage.Models.DoModel;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMAlbum;
import com.i.should.what.whatshouldi.ListenPackage.Models.ListenState;
import com.i.should.what.whatshouldi.MainActivity;
import com.i.should.what.whatshouldi.R;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by ryan on 13.7.2015.
 */
public class ListenAlbumRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private WeakReference<Context> context;
    private ArrayList<LastFMAlbum> albums;

    public ListenAlbumRecyclerAdapter(Context ctx, List<LastFMAlbum> models) {
        albums = new ArrayList<>(models);
        context = new WeakReference<>(ctx);
    }

    public void putPlaying(int pos) {
        if (albums == null || albums.size() <= 0) return;
        for (int i = 0; i < albums.size(); i++) {
            albums.get(i).playing = false;
        }

        albums.get(pos).playing = true;

        notifyDataSetChanged();
    }

    public void addItemsInfo(List<LastFMAlbum> list, boolean append) {
        if (append)
            albums.addAll(list);
        else {
            albums = new ArrayList<>(list);
        }
        sortPopular();
       // notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        final View view = LayoutInflater.from(context).inflate(R.layout.album_item_layout, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ArtistViewHolder) holder).BindValues(albums.get(position), position, context.get());
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public void sortPopular() {
        Collections.sort(albums, new Comparator<LastFMAlbum>() {
            @Override
            public int compare(LastFMAlbum lhs, LastFMAlbum rhs) {
                return -1 * lhs.getPlaycount().compareTo(rhs.getPlaycount());
            }
        });
        if(context.get() != null)
            ((Activity)context.get()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
    }

    public void sortNew() {
        if(albums.get(0).getDateYear() == 0 && albums.get(1).getDateYear() == 0 &&
                albums.get(2).getDateYear() == 0)
        {
            Toast.makeText(context.get(), "Data is stil loading", Toast.LENGTH_SHORT).show();
        }
        Collections.sort(albums, new Comparator<LastFMAlbum>() {
            @Override
            public int compare(LastFMAlbum lhs, LastFMAlbum rhs) {
//                if(!lhs.getReleaseDate().isEmpty() && !rhs.getReleaseDate().isEmpty()) {
//                    Date date1 = new Date(lhs.getReleaseDate());
//                    Date date2 = new Date(rhs.getReleaseDate());
//                    return date1.compareTo(date2);
//                }
                return -1 * lhs.getDateYear().compareTo(rhs.getDateYear());
            }
        });
        if(context.get() != null)
            ((Activity)context.get()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder {
        LastFMAlbum model;
        ImageView albumImage;//, newArtistImage;
        TextView albumName;
        View stopImage;
        ImageView dislikeBtn, likeBtn;
        View addBtn;

        public ArtistViewHolder(View view) {
            super(view);
            albumImage = (ImageView) view.findViewById(R.id.albumImage);
            albumName = (TextView) view.findViewById(R.id.albumName);
            stopImage = view.findViewById(R.id.stopImage);
            dislikeBtn = (ImageView)view.findViewById(R.id.dislike);
            likeBtn = (ImageView)view.findViewById(R.id.like);
            addBtn = view.findViewById(R.id.add);
        }

        public void BindValues(LastFMAlbum m, final int pos, final Context context) {
            model = m;

            if(m.getLargeImage()!=null && !m.getLargeImage().isEmpty())
                Picasso.with(context).load(m.getLargeImage()).placeholder(R.drawable.ic_group).into(albumImage);
            renewState();

            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.setState(ListenState.LIKE);
                    MainActivity.helper.updateAlbum(model, ListenState.LIKE);
                    renewState();
                }
            });

            dislikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.setState(ListenState.DISLIKE);
                    MainActivity.helper.updateAlbum(model, ListenState.DISLIKE);
                    renewState();
                }
            });

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DoModel.CreateDo(model);
                    Toast.makeText(context, "Added to do section!", Toast.LENGTH_LONG).show();
                }
            });

            albumImage.setOnClickListener(null);
            albumImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlayService.startActionPlay(context, model.getArtist(), model.getName(true));
                    ListenAlbumRecyclerAdapter.this.putPlaying(pos);
                }
            });

            stopImage.setOnClickListener(null);
            stopImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopImage.setVisibility(View.INVISIBLE);
                    PlayService.startActionStop(context);
                    model.playing = false;
                }
            });

            stopImage.setVisibility(model.playing ? View.VISIBLE : View.INVISIBLE);

            albumName.setText(m.getName(false));
        }

        private void renewState() {
            if(model.getState() == ListenState.LIKE)
            {
                dislikeBtn.setImageResource(R.drawable.ic_albums_down_unchecked);
                likeBtn.setImageResource(R.drawable.ic_albums_up_checked);
            }else if(model.getState() == ListenState.DISLIKE)
            {
                dislikeBtn.setImageResource(R.drawable.ic_albums_down_checked);
                likeBtn.setImageResource(R.drawable.ic_albums_up_unchecked);
            }
            else{
                dislikeBtn.setImageResource(R.drawable.ic_albums_down_unchecked);
                likeBtn.setImageResource(R.drawable.ic_albums_up_unchecked);
            }
        }
    }
}
