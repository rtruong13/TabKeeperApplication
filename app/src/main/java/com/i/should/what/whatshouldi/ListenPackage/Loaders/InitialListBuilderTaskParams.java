package com.i.should.what.whatshouldi.ListenPackage.Loaders;

import android.content.Context;

import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by ryan on 7/29/2015.
 */
public class InitialListBuilderTaskParams {
    InitialListBuilderTask.InitialListBuilderTaskCallback callback;
    List<LastFMArtist> artists;
    WeakReference<Context> context;

    public InitialListBuilderTaskParams(InitialListBuilderTask.InitialListBuilderTaskCallback parent, List<LastFMArtist> selList,
                                        Context c)
    {
        context = new WeakReference<>(c);
        callback = parent;
        artists = selList;
    }
}