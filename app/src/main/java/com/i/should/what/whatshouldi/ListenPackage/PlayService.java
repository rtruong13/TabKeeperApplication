package com.i.should.what.whatshouldi.ListenPackage;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.i.should.what.whatshouldi.ListenPackage.Loaders.GetSongToPlayTask;
import com.i.should.what.whatshouldi.ListenPackage.Loaders.GetSongToPlayTaskParams;

import java.io.IOException;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PlayService extends IntentService implements GetSongToPlayTask.GetSongToPlayTaskCallback {
    private static final String ACTION_PLAY = "com.i.should.what.whatshouldi.ListenPackage.playsong";
    private static final String ACTION_STOP = "com.i.should.what.whatshouldi.ListenPackage.stop";
    private static MediaPlayer player;
    private static final String EXTRA_ARTIST = "com.i.should.what.whatshouldi.ListenPackage.extra.ARTIST";
    private static final String EXTRA_ALBUM = "com.i.should.what.whatshouldi.ListenPackage.extra.ALBUM";

    public static void startActionPlay(Context context, String artist, String album) {
        Intent intent = new Intent(context, PlayService.class);
        intent.setAction(ACTION_PLAY);
        intent.putExtra(EXTRA_ARTIST, artist);
        intent.putExtra(EXTRA_ALBUM, album);
        context.startService(intent);
    }

    public static void startActionStop(Context context) {
        Intent intent = new Intent(context, PlayService.class);
        intent.setAction(ACTION_STOP);
        context.startService(intent);
    }


    public PlayService() {
        super("PlayService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PLAY.equals(action)) {
                final String artist = intent.getStringExtra(EXTRA_ARTIST);
                final String album = intent.getStringExtra(EXTRA_ALBUM);
                handleAction(artist, album);
            }
            else if (ACTION_STOP.equals(action)) {
                handleStop();
            }
        }
    }

    private void handleStop() {
        if(player!=null)
        {
            player.stop();
            player = null;
        }
        stopSelf();
    }

    private void handleAction(String param1, String param2) {
        new GetSongToPlayTask().execute(new GetSongToPlayTaskParams(param1, param2, this));
    }

    @Override
    public void playSong(String playLink) {
        if(player!=null)
        {
            player.stop();
            player = null;
        }

        player = new MediaPlayer();
        Uri myUri = Uri.parse(playLink);

        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            player.setDataSource(getApplicationContext(), myUri);
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp = null;
                    PlayService.this.stopSelf();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
