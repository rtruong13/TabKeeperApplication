package com.i.should.what.whatshouldi.ListenPackage.Loaders;

import android.content.Context;

import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtist;

import java.lang.ref.WeakReference;

/**
 * Created by ryan on 7/30/2015.
 */
public class GetAlbumsTaskParam {
    public LastFMArtist artist;
    public WeakReference<Context> context;
    public GetAlbumsTask.GetAlbumsTaskCallback callback;

    public GetAlbumsTaskParam(LastFMArtist artist, Context c, GetAlbumsTask.GetAlbumsTaskCallback parent)
    {
        this.artist=artist;
        context = new WeakReference<>(c);
        callback = parent;
    }
}
