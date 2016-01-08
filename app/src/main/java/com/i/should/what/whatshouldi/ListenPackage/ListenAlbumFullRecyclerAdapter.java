package com.i.should.what.whatshouldi.ListenPackage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMAlbum;
import com.i.should.what.whatshouldi.ListenPackage.Models.ListenState;
import com.i.should.what.whatshouldi.MainActivity;
import com.i.should.what.whatshouldi.R;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ryan on 13.7.2015.
 */
public class ListenAlbumFullRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    private WeakReference<Context> context;
    private ArrayList<LastFMAlbum> albums;

    private static int countOfHeaders = 2;

    public ListenAlbumFullRecyclerAdapter(Context ctx, List<LastFMAlbum> models) {
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
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return albums.size() + countOfHeaders;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position < countOfHeaders;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        if (viewType == TYPE_ITEM) {
            final View view = LayoutInflater.from(context).inflate(R.layout.album_item_layout, parent, false);
            return new ArtistViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.empty_recycler_header, parent, false);
            return new EmptyHeaderVH(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!isPositionHeader(position))
            ((ArtistViewHolder) holder).BindValues(albums.get(position-countOfHeaders),
                    position-countOfHeaders, context.get());
    }

    public void sortPopular() {
        Collections.sort(albums, new Comparator<LastFMAlbum>() {
            @Override
            public int compare(LastFMAlbum lhs, LastFMAlbum rhs) {
                return -1 * lhs.getPlaycount().compareTo(rhs.getPlaycount());
            }
        });
        notifyDataSetChanged();
    }

    public void sortNew() {
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
        notifyDataSetChanged();
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
            dislikeBtn = (ImageView) view.findViewById(R.id.dislike);
            likeBtn = (ImageView) view.findViewById(R.id.like);
            addBtn = view.findViewById(R.id.add);
        }

        public void BindValues(LastFMAlbum m, final int pos, final Context context) {
            model = m;

            if (m.getLargeImage() != null && !m.getLargeImage().isEmpty())
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

            albumImage.setOnClickListener(null);
            albumImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlayService.startActionPlay(context, model.getArtist(), model.getName(true));
                    ListenAlbumFullRecyclerAdapter.this.putPlaying(pos);
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
            if (model.getState() == ListenState.LIKE) {
                dislikeBtn.setImageResource(R.drawable.ic_albums_down_unchecked);
                likeBtn.setImageResource(R.drawable.ic_albums_up_checked);
            } else if (model.getState() == ListenState.DISLIKE) {
                dislikeBtn.setImageResource(R.drawable.ic_albums_down_checked);
                likeBtn.setImageResource(R.drawable.ic_albums_up_unchecked);
            } else {
                dislikeBtn.setImageResource(R.drawable.ic_albums_down_unchecked);
                likeBtn.setImageResource(R.drawable.ic_albums_up_unchecked);
            }
        }
    }

    public class EmptyHeaderVH extends RecyclerView.ViewHolder {

        public EmptyHeaderVH(View itemView) {
            super(itemView);
        }
    }
}
