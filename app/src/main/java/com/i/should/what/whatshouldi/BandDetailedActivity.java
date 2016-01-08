package com.i.should.what.whatshouldi;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.i.should.what.whatshouldi.DoPackage.Models.DoModel;
import com.i.should.what.whatshouldi.ListenPackage.ListenAlbumRecyclerAdapter;
import com.i.should.what.whatshouldi.ListenPackage.Loaders.GetAlbumsTask;
import com.i.should.what.whatshouldi.ListenPackage.Loaders.GetAlbumsTaskParam;
import com.i.should.what.whatshouldi.ListenPackage.Loaders.NewArtistTask;
import com.i.should.what.whatshouldi.ListenPackage.Loaders.NewArtistTaskParams;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMAlbum;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.ListenPackage.Models.ListenState;
import com.i.should.what.whatshouldi.ListenPackage.PlayService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BandDetailedActivity extends AppCompatActivity
        implements GetAlbumsTask.GetAlbumsTaskCallback, NewArtistTask.NewArtistTaskCallback {

    ListenAlbumRecyclerAdapter adapter;
    LastFMArtist artist;
    private View dislikeBtn;
    private View likeBtn;
    private View addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_detailed);

        artist = (LastFMArtist) getIntent().getSerializableExtra("artist");
        if (artist == null) {
            finish();
            return;
        }

        ImageView imageView = (ImageView) findViewById(R.id.artistImage);
        Picasso.with(this).load(artist.getLargeImage())
                .placeholder(R.drawable.ic_group).into(imageView);

        TextView textView = (TextView) findViewById(R.id.bandNameText);
        textView.setText(artist.getName(true).toUpperCase());

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dislikeBtn = findViewById(R.id.thumbDown);
        dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (artist.getState() == null || artist.getState() == ListenState.NOTHING)
                            changeArtist(artist, ListenState.DISLIKE);
                        else
                            saveToDB(artist, ListenState.DISLIKE);


                        artist.setState(ListenState.DISLIKE);
                        changeLikeDisImages();
                    }
                }).run();
            }
        });

        likeBtn = findViewById(R.id.thumbUp);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (artist.getState() == null || artist.getState() == ListenState.NOTHING)
                            changeArtist(artist, ListenState.LIKE);
                        else
                            saveToDB(artist, ListenState.LIKE);

                        artist.setState(ListenState.LIKE);
                        changeLikeDisImages();
                    }
                }).run();
            }
        });

        findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoModel.CreateDo(artist);
                Toast.makeText(BandDetailedActivity.this, "Added to do section!", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.popularAlbums).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.sortPopular();
            }
        });

        findViewById(R.id.newAlbums).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.sortNew();
            }
        });

        if (getSupportActionBar() != null) getSupportActionBar().hide();
        else if (getActionBar() != null) getActionBar().hide();

        adapter = new ListenAlbumRecyclerAdapter(this, new ArrayList<LastFMAlbum>());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.albumsRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        if (artist != null) {
            new GetAlbumsTask().execute(new GetAlbumsTaskParam(artist, this, this));
            changeLikeDisImages();
        }
    }

    private void changeLikeDisImages() {
        if (artist.getState() == ListenState.DISLIKE) {
            Picasso.with(this)
                    .load(R.drawable.ic_thumb_down_checked)
                    .into((ImageView) dislikeBtn);
            Picasso.with(this)
                    .load(R.drawable.ic_thumb_up_unchecked)
                    .into((ImageView) likeBtn);
        } else if (artist.getState() == ListenState.LIKE) {
            Picasso.with(this)
                    .load(R.drawable.ic_thumb_down_unchecked)
                    .into((ImageView) dislikeBtn);
            Picasso.with(this)
                    .load(R.drawable.ic_thumb_up_checked)
                    .into((ImageView) likeBtn);
        }
    }

    private void saveToDB(LastFMArtist artist, ListenState rate) {
        MainActivity.helper.updateArtist(artist, rate);
    }

    private void changeArtist(LastFMArtist artist, ListenState state) {
        new NewArtistTask().execute(
                new NewArtistTaskParams(this, -1, 1, true, this, artist, state, null));
    }

    @Override
    public void showAlbums(List<LastFMAlbum> albums) {
        adapter.addItemsInfo(albums, false);
    }

    @Override
    public void onBackPressed() {
        PlayService.startActionStop(this);
        super.onBackPressed();
    }

    @Override
    public void showThisArtist(List<LastFMArtist> artist, int pos) {

    }


}
