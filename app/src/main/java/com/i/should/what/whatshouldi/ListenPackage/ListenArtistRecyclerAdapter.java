package com.i.should.what.whatshouldi.ListenPackage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.i.should.what.whatshouldi.BandDetailedActivity;
import com.i.should.what.whatshouldi.DoPackage.Models.DoModel;
import com.i.should.what.whatshouldi.FileManager;
import com.i.should.what.whatshouldi.ListenPackage.Loaders.NewArtistTask;
import com.i.should.what.whatshouldi.ListenPackage.Loaders.NewArtistTaskParams;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.ListenPackage.Models.ListenState;
import com.i.should.what.whatshouldi.MainActivity;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;
import com.i.should.what.whatshouldi.MoviesPackage.MoviesAPIHelper;
import com.i.should.what.whatshouldi.MoviesPackage.ShowNewMovieInterface;
import com.i.should.what.whatshouldi.R;
import com.i.should.what.whatshouldi.WatchDetailedActivity;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.SupportAnimator;

/**
 * Created by ryan on 13.7.2015.
 */
public class ListenArtistRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    private static int countOfHeaders = 2;
    boolean loadingNew;

    private WeakReference<Context> context;
    private ArrayList<LastFMArtist> artists;

    public ListenArtistRecyclerAdapter(Context ctx, List<LastFMArtist> models) {
        artists = new ArrayList<>(models);
        context = new WeakReference<Context>(ctx);
    }

    public void addItemsInfo(List<LastFMArtist> list, boolean append) {
        if (append)
            artists.addAll(list);
        else {
            artists = new ArrayList<>(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.artist_item_layout, parent, false);
            return new ArtistViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.empty_recycler_header, parent, false);
            return new EmptyHeaderVH(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!isPositionHeader(position)) {
            ((ArtistViewHolder) holder).BindValues(artists.get(position - countOfHeaders)
                    , position - countOfHeaders, context.get());
        }
    }

    @Override
    public int getItemCount() {
        return artists.size() + countOfHeaders;
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

    public class ArtistViewHolder extends RecyclerView.ViewHolder implements NewArtistTask.NewArtistTaskCallback {
        LastFMArtist model;
        ImageView artistImage;//, newArtistImage;
        TextView artistName;
        View dislikeBtn, likeBtn, addBtn;
        View loadingSpinner;
        View normalLayout, loadingLayout;

        public ArtistViewHolder(View view) {
            super(view);

            artistImage = (ImageView) view.findViewById(R.id.artistImage);
            //newArtistImage = (ImageView) view.findViewById(R.id.newArtistImage);
            artistName = (TextView) view.findViewById(R.id.artistName);
            dislikeBtn = view.findViewById(R.id.dislike);
            likeBtn = view.findViewById(R.id.like);
            addBtn = view.findViewById(R.id.add);
            loadingSpinner = view.findViewById(R.id.loadingSpinnerView);

            loadingLayout = view.findViewById(R.id.loadingView);
            normalLayout = view.findViewById(R.id.normalWatchView);
        }

        public void BindValues(LastFMArtist m, final int pos, final Context context) {
            model = m;

            Picasso.with(context)
                    .load(model.getLargeImage())
                    .placeholder(R.drawable.ic_group)
                    .into(artistImage);

            artistImage.setOnClickListener(null);
            artistImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BandDetailedActivity.class);
                    intent.putExtra("artist", model);
                    context.startActivity(intent);
                }
            });

            artistName.setText(m.getName(false).toUpperCase());
            loadingNew = false;

            loadingLayout.setVisibility(View.INVISIBLE);


            if (model.getState() == ListenState.DISLIKE) {
                Picasso.with(context)
                        .load(R.drawable.ic_thumb_down_checked)
                        .into((ImageView) dislikeBtn);
                Picasso.with(context)
                        .load(R.drawable.ic_thumb_up_unchecked)
                        .into((ImageView) likeBtn);
            } else if (model.getState() == ListenState.LIKE) {
                Picasso.with(context)
                        .load(R.drawable.ic_thumb_down_unchecked)
                        .into((ImageView) dislikeBtn);
                Picasso.with(context)
                        .load(R.drawable.ic_thumb_up_checked)
                        .into((ImageView) likeBtn);
            }

            addBtn.setOnClickListener(null);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DoModel.CreateDo(model);
                    Toast.makeText(context, "Added to do section!", Toast.LENGTH_LONG).show();
                }
            });

            likeBtn.setOnClickListener(null);
            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!loadingNew)
                        changeArtist(model, ListenState.LIKE, pos);
                }
            });

            dislikeBtn.setOnClickListener(null);
            dislikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!loadingNew)
                        changeArtist(model, ListenState.DISLIKE, pos);
                }
            });

            if (pos == 0) {
                Thread cleaningThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.gc();
                    }
                });
                cleaningThread.run();
            }
        }

        private void changeArtist(LastFMArtist artist, ListenState state, int pos) {
            loadingNew = true;
            new NewArtistTask().execute(
                    new NewArtistTaskParams(ListenArtistRecyclerAdapter.this.context.get(),
                            pos, 1, false, this, artist, state, ListenArtistRecyclerAdapter.this.artists));
            runCircularRevealAnim(loadingLayout, loadingLayout.getWidth() / 2, loadingLayout.getHeight() / 2, loadingLayout.getHeight(), false);
        }

        private void runCircularRevealAnim(final View v, int cx, int cy, int finalRadius, boolean closeView) {
            if (!closeView)
                v.setVisibility(View.VISIBLE);
            try {
                SupportAnimator animatorSup =
                        io.codetail.animation.ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius);
                animatorSup.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorSup.setDuration(300);

                if (!closeView)
                    animatorSup.start();
                else {
                    try {
                        animatorSup.reverse().start();
                    } catch (Exception e) {
                        return;
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            v.setVisibility(View.INVISIBLE);
                        }
                    }, 300);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void showThisArtist(List<LastFMArtist> artist, int pos) {
            if (pos != -1) {
                ListenArtistRecyclerAdapter.this.changeItem(pos, artist.get(0));
                model = artist.get(0);
//                notifyDataSetChanged();
                runCircularRevealAnim(loadingLayout, loadingLayout.getWidth() / 2, loadingLayout.getHeight() / 2, loadingLayout.getHeight(), true);
                //Picasso.with(context.get()).load(artist.getLargeImage()).placeholder(R.drawable.ic_group).into(newArtistImage);
                //runCircularRevealAnim(newArtistImage, newArtistImage.getWidth() / 2, newArtistImage.getHeight() / 2, newArtistImage.getHeight(), false);
                notifyItemChanged(pos + ListenArtistRecyclerAdapter.countOfHeaders);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //newArtistImage.setVisibility(View.INVISIBLE);
                        loadingLayout.setVisibility(View.INVISIBLE);
                    }
                }, 200);
                loadingNew = false;
                //Picasso.with(context.get()).load(artist.getLargeImage()).into(artistImage);
            }
        }
    }

    private void changeItem(int pos, LastFMArtist artist) {
        artists.set(pos, artist);
        new FileManager().saveAllArtist(context.get(), artists);
    }

    public class EmptyHeaderVH extends RecyclerView.ViewHolder {

        public EmptyHeaderVH(View itemView) {
            super(itemView);
        }
    }
}
