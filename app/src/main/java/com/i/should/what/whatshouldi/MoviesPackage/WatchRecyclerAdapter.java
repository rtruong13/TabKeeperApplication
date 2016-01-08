package com.i.should.what.whatshouldi.MoviesPackage;

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
import android.widget.Toast;

import com.i.should.what.whatshouldi.DoPackage.Models.DoModel;
import com.i.should.what.whatshouldi.FileManager;
import com.i.should.what.whatshouldi.MainActivity;
import com.i.should.what.whatshouldi.MoviesPackage.Loaders.LoadSimilarMovieTasksParams;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;
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
public class WatchRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    private static int countOfHeaders = 2;

    private MovieDBFullMovieModel changeModelReq;
    private WeakReference<Context> context;
    private ArrayList<MovieDBFullMovieModel> watchList;
    private LoadSimilarMovieTasksParams.WatchType currentWatchType;

    public WatchRecyclerAdapter(Context ctx, ArrayList<MovieDBFullMovieModel> models, LoadSimilarMovieTasksParams.WatchType type, MovieDBFullMovieModel changeModelReq) {
        watchList = new ArrayList<>(models);
        currentWatchType = type;
        context = new WeakReference<Context>(ctx);
        this.changeModelReq = changeModelReq;
    }

    public void addItemsInfo(List<MovieDBFullMovieModel> list, boolean append, LoadSimilarMovieTasksParams.WatchType type) {
        if (append)
            watchList.addAll(list);
        else {
            watchList = new ArrayList<>(list);
            currentWatchType = type;
        }
        notifyDataSetChanged();
    }

//    public void addItemsFullInfo(List<MovieDBFullMovieModel> list)
//    {
//        ArrayList<MovieDBFullMovieModel> movieModels = new ArrayList<>();
//
//        for (MovieDBFullMovieModel fullInfo :
//                list) {
//            movieModels.add(new MovieDBMovieModel(fullInfo));
//        }
//
//        watchList.addAll(movieModels);
//        notifyDataSetChanged();
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        if (viewType == TYPE_ITEM) {
            final View view = LayoutInflater.from(context).inflate(R.layout.watch_item_layout, parent, false);
            return new WatchViewHolder(view);
        } else {
            final View view = LayoutInflater.from(context).inflate(R.layout.empty_recycler_header, parent, false);
            return new EmptyHeaderVH(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!isPositionHeader(position)) {
            ((WatchViewHolder) holder).BindValues(watchList.get(position - countOfHeaders)
                    , position - countOfHeaders, context.get());
        }
    }

    @Override
    public int getItemCount() {
        return watchList.size() + countOfHeaders;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    public void changeItem(MovieDBFullMovieModel model) {
        changeModelReq = model;
        notifyDataSetChanged();
    }

    protected enum WatchState {
        openPost, openSeen, openingNew, closed
    }

    private boolean isPositionHeader(int position) {
        return position < countOfHeaders;
    }

    public class WatchViewHolder extends RecyclerView.ViewHolder implements ShowNewMovieInterface {

        MovieDBFullMovieModel model;

        WatchState state;

        ImageView imageView, newPoster;
        View infoView;
        View seenView;
        RatingBar ratingBar;

        View normalLayout, seenLayout, postLayout, newWatchLayout, openingNewLayout, newPostAnimLayout;
        private View addPostView;
        private View infoPostView;
        private View seenPostView;

        public WatchViewHolder(View view) {
            super(view);


            imageView = (ImageView) view.findViewById(R.id.posterImage);
            infoView = view.findViewById(R.id.infoButton);
            seenView = view.findViewById(R.id.seenButton);
            newPoster = (ImageView) view.findViewById(R.id.newPosterImage);

            addPostView = (view.findViewById(R.id.addPostView));
            infoPostView = (view.findViewById(R.id.infoPostView));
            seenPostView = (view.findViewById(R.id.seenPostView));

            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

            normalLayout = view.findViewById(R.id.normalWatchView);
            seenLayout = view.findViewById(R.id.seenWatchView);
            postLayout = view.findViewById(R.id.posterWatchView);
            newWatchLayout = view.findViewById(R.id.newMovieWatchView);
            openingNewLayout = view.findViewById(R.id.loadingView);
            newPostAnimLayout = view.findViewById(R.id.newPosterAnimLayout);
        }

        public void BindValues(final MovieDBFullMovieModel m, final int pos, final Context context) {
            model = m;
            state = WatchState.closed;

            openingNewLayout.setVisibility(View.INVISIBLE);
            seenLayout.setVisibility(View.INVISIBLE);

            Picasso.with(context)
                    .load(model.getPosterImage())
                    .placeholder(R.drawable.poster_place_holder)
                    .into(imageView);

            postLayout.setVisibility(View.INVISIBLE);
            imageView.setOnClickListener(null);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (state == WatchState.openingNew) return;
                    if (state == WatchState.openSeen) {
                        state = WatchState.closed;
                        int cx = seenLayout.getWidth();
                        int cy = seenLayout.getHeight();

                        // get the final radius for the clipping circle
                        int finalRadius = seenLayout.getWidth() + seenLayout.getHeight();
                        runCircularRevealAnim(seenLayout, cx, cy, finalRadius, true);
                    }

                    state = WatchState.openPost;
                    int cx = postLayout.getWidth() / 2;
                    int cy = postLayout.getHeight() / 2;

                    // get the final radius for the clipping circle
                    int finalRadius = Math.max(postLayout.getWidth(), postLayout.getHeight());

                    boolean closeView = postLayout.getVisibility() == View.VISIBLE;
                    runCircularRevealAnim(postLayout, cx, cy, finalRadius, closeView);
                }
            });

            infoView.setOnClickListener(null);
            infoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (state == WatchState.openingNew) return;
                    Intent intent = new Intent(context, WatchDetailedActivity.class);
                    intent.putExtra("watchItem", model);
                    ((Activity)context).startActivityForResult(intent, MainActivity.WATCH_DETAIL_REQ);
                }
            });

            ratingBar.setOnRatingBarChangeListener(null);
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    state = WatchState.openingNew;
//                    int cx = seenLayout.getWidth();
//                    int cy = seenLayout.getHeight();
//
//                    // get the final radius for the clipping circle
//                    int finalRadius = seenLayout.getWidth() + seenLayout.getHeight();
//                    //runCircularRevealAnim(seenLayout, cx, cy, finalRadius, true);
//                    //todo show new movie
//                    showNewMovie(watchList.get(0), pos, rating);
                    openingNewLayout.setVisibility(View.VISIBLE);
                    seenLayout.setVisibility(View.INVISIBLE);
                    new MoviesAPIHelper()
                            .getNextMovieToWatch(context, WatchViewHolder.this, pos, model, rating, currentWatchType, WatchRecyclerAdapter.this.watchList);
                }
            });

            seenLayout.setVisibility(View.INVISIBLE);
            seenView.setOnClickListener(null);
            seenView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (state == WatchState.openingNew) return;
                    if (state == WatchState.openPost) {
                        state = WatchState.closed;
                        int cx = postLayout.getWidth();
                        int cy = postLayout.getHeight();

                        // get the final radius for the clipping circle
                        int finalRadius = postLayout.getWidth() + postLayout.getHeight();
                        runCircularRevealAnim(postLayout, cx, cy, finalRadius, true);
                    }
                    state = WatchState.openSeen;
//                    Toast.makeText(context, pos + "clickedSeen", Toast.LENGTH_SHORT).show();

                    int cx = seenLayout.getWidth();
                    int cy = seenLayout.getHeight();

                    // get the final radius for the clipping circle
                    int finalRadius = seenLayout.getWidth() + seenLayout.getHeight();

                    boolean closeView = seenLayout.getVisibility() == View.VISIBLE;
                    runCircularRevealAnim(seenLayout, cx, cy, finalRadius, closeView);

                }
            });

            initPostLayoutClicks(model, context, pos);

            if(changeModelReq != null && changeModelReq.getId().equals(model.getId()))
            {
                MovieDBFullMovieModel fullModel = new MovieDBFullMovieModel(changeModelReq);
                changeModelReq = null;
                state = WatchState.openingNew;
                //todo add to DO
                //todo save to memory


                openingNewLayout.setVisibility(View.VISIBLE);
                seenLayout.setVisibility(View.INVISIBLE);
                new MoviesAPIHelper()
                        .getNextMovieToWatch(context, WatchViewHolder.this, pos, fullModel, -1, currentWatchType, WatchRecyclerAdapter.this.watchList);
            }
        }

        private void initPostLayoutClicks(final MovieDBFullMovieModel model, final Context context, final int pos) {

            addPostView.setOnClickListener(null);
            addPostView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seenLayout.setVisibility(View.INVISIBLE);
                    openingNewLayout.setVisibility(View.INVISIBLE);
                    postLayout.setVisibility(View.INVISIBLE);
                    state = WatchState.closed;

                    DoModel.CreateDo(model, currentWatchType);
                    Toast.makeText(context, "Added to do section!", Toast.LENGTH_LONG).show();
                }
            });

            infoPostView.setOnClickListener(null);
            infoPostView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WatchDetailedActivity.class);
                    intent.putExtra("watchItem", model);
                    ((Activity)context).startActivityForResult(intent, MainActivity.WATCH_DETAIL_REQ);
                }
            });

            seenPostView.setOnClickListener(null);
            seenPostView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (state == WatchState.openPost) {
                        state = WatchState.closed;
                        int cx = postLayout.getWidth();
                        int cy = postLayout.getHeight();

                        // get the final radius for the clipping circle
                        int finalRadius = postLayout.getWidth() + postLayout.getHeight();
                        runCircularRevealAnim(postLayout, cx, cy, finalRadius, true);
                    }
                    state = WatchState.openSeen;
                    // Toast.makeText(context, pos + "clickedSeen", Toast.LENGTH_SHORT).show();

                    int cx = seenLayout.getWidth();
                    int cy = seenLayout.getHeight();

                    // get the final radius for the clipping circle
                    int finalRadius = seenLayout.getWidth() + seenLayout.getHeight();

                    boolean closeView = seenLayout.getVisibility() == View.VISIBLE;
                    runCircularRevealAnim(seenLayout, cx, cy, finalRadius, closeView);
                }
            });
        }

        @Override
        public void showNewMovie(MovieDBFullMovieModel newModel, int pos, final float numOfStars) {
            model = newModel;
            watchList.set(pos, model);

            if (context.get() != null)
                new FileManager().saveAllMovies(context.get(), WatchRecyclerAdapter.this.watchList, currentWatchType);

            seenLayout.setVisibility(View.INVISIBLE);
            openingNewLayout.setVisibility(View.INVISIBLE);

            watchList.set(pos, newModel);

            newPoster.setImageDrawable(imageView.getDrawable());
            newWatchLayout.setVisibility(View.VISIBLE);
            seenLayout.setVisibility(View.INVISIBLE);

            Picasso.with(context.get())
                    .load(newModel.getPosterImage())
                    .placeholder(R.drawable.poster_place_holder)
                    .into(imageView);


            int position = newPostAnimLayout.getWidth() + 50;
            if (pos % 2 == 0)
                position = -1 * position;

            newPostAnimLayout.animate().translationX(position).setDuration(500)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    newWatchLayout.setVisibility(View.INVISIBLE);
                                    newPostAnimLayout.animate().translationX(0).setDuration(10).start();
                                    state = WatchState.closed;
                                    WatchRecyclerAdapter.this.notifyDataSetChanged();

                                }
                            }, 1500);
                        }
                    }).start();
        }

        @Override
        public void errorOccurred(String message) {
            Toast.makeText(context.get(), message, Toast.LENGTH_SHORT).show();
            if (state == WatchState.openingNew)
                openingNewLayout.setVisibility(View.INVISIBLE);

            state = WatchState.closed;
        }

        private void runCircularRevealAnim(final View v, int cx, int cy, int finalRadius, boolean closeView) {
            if (!closeView)
                v.setVisibility(View.VISIBLE);

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
        }
    }

    public LoadSimilarMovieTasksParams.WatchType getCurrentWatchType()
    {
        return currentWatchType;
    }

    public class EmptyHeaderVH extends RecyclerView.ViewHolder {

        public EmptyHeaderVH(View itemView) {
            super(itemView);
        }
    }
}
