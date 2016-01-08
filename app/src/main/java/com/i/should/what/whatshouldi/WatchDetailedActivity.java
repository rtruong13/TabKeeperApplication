package com.i.should.what.whatshouldi;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;
import com.i.should.what.whatshouldi.MoviesPackage.MoviesAPIHelper;
import com.i.should.what.whatshouldi.MoviesPackage.WatchRecyclerAdapter;
import com.squareup.picasso.Picasso;

//if return 1 no need to change movie if return 2 need to change movie
public class WatchDetailedActivity extends AppCompatActivity {

    MovieDBFullMovieModel movieModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_detailed);

        movieModel = (MovieDBFullMovieModel) getIntent().getSerializableExtra("watchItem");
        InitViews(movieModel);
    }

    public void exitScreen(View view) {
        finish();
    }
    
    public void InitViews(MovieDBFullMovieModel model) {
        Picasso.with(this).load(model.getPosterImage())
                .fit()
                .into((ImageView) findViewById(R.id.posterImage));

        String fontPath = "fonts/Dosis-Medium.otf";
        Typeface tfM = Typeface.createFromAsset(getAssets(), fontPath);
        String fontPathSB = "fonts/Dosis-SemiBold.otf";
        Typeface tfSB = Typeface.createFromAsset(getAssets(), fontPathSB);

        ((TextView) findViewById(R.id.descriptionText)).setText(model.getOverview());
        ((TextView) findViewById(R.id.descriptionText)).setTypeface(tfM);

        if (model.getCredits() != null) {
            ((TextView) findViewById(R.id.directorText)).setText(model.getDirectoString());
            ((TextView) findViewById(R.id.directorText)).setTypeface(tfM);
        }


        String date = model.getReleaseDate();
        ((TextView) findViewById(R.id.yearText)).setText(String.valueOf(MovieDBFullMovieModel.getYear(date)));
        ((TextView) findViewById(R.id.yearText)).setTypeface(tfM);

        ((TextView) findViewById(R.id.movieTitle)).setText(model.getOriginalTitle().toUpperCase());
        ((TextView) findViewById(R.id.movieTitle)).setTypeface(tfSB);

        if (model.getGenres() != null) {
            ((TextView) findViewById(R.id.categoriesTitle)).setText(model.getGenresString());
            ((TextView) findViewById(R.id.categoriesTitle)).setTypeface(tfM);
        }

        if (model.getCredits() != null) {
            ((TextView) findViewById(R.id.castText)).setText(model.getCredits().getCastString());
            ((TextView) findViewById(R.id.castText)).setTypeface(tfM);
        }

        findViewById(R.id.backWatchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                WatchDetailedActivity.this.finish();
            }
        });

        findViewById(R.id.bottomButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra("watchItem", movieModel);
                        setResult(2, intent);
                        ///// TODO: 10/27/2015 add movie to do and change it in prev activity
                        WatchDetailedActivity.this.finish();
                    }
                }, 150);
            }
        });

    }

    @Override
    public void onBackPressed() {
        setResult(1);
        super.onBackPressed();
    }
}
