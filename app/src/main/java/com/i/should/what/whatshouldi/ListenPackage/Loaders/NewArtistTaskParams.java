package com.i.should.what.whatshouldi.ListenPackage.Loaders;

import android.content.Context;

import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;
import com.i.should.what.whatshouldi.ListenPackage.Models.ListenState;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by ryan on 7/30/2015.
 */
public class NewArtistTaskParams {
    public WeakReference<Context> context;
    public int position;
    public int countToLoad;
    public boolean saveToFile;
    public NewArtistTask.NewArtistTaskCallback callback;
    public LastFMArtist artist;
    public ListenState state;
    public ArrayList<LastFMArtist> currentlyShowing;

    public NewArtistTaskParams(Context c, int pos, int countToLoad, boolean saveToFile,
                               NewArtistTask.NewArtistTaskCallback parent, LastFMArtist selArtist,
                               ListenState selState, ArrayList<LastFMArtist> currentlyShowing)
    {
        context = new WeakReference<>(c);
        position = pos;
        callback = parent;
        artist = selArtist;
        state = selState;
        this.countToLoad = countToLoad;
        this.saveToFile = saveToFile;
        this.currentlyShowing = currentlyShowing;
    }
}
