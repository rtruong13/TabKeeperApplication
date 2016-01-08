package com.i.should.what.whatshouldi.ListenPackage.Loaders;

import android.content.Context;

/**
 * Created by ryan on 11/2/2015.
 */
public class LoadNewAlbumsTaskParams {
    public Context context;
    public LoadNewAlbumsTask.LoadNewAlbumsTaskCallback parent;

    public LoadNewAlbumsTaskParams(Context context, LoadNewAlbumsTask.LoadNewAlbumsTaskCallback callback)
    {
        this.context = context;
        parent = callback;
    }
}
