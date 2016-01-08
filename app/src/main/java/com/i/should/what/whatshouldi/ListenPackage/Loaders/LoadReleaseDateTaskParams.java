package com.i.should.what.whatshouldi.ListenPackage.Loaders;

import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMAlbum;

/**
 * Created by ryan on 11/2/2015.
 */
public class LoadReleaseDateTaskParams {
    public LoadReleaseDateTask.LoadReleaseDateTaskCallback parent;
    public int position;
    public LastFMAlbum album;

    public LoadReleaseDateTaskParams(LoadReleaseDateTask.LoadReleaseDateTaskCallback callback, int pos,
                                     LastFMAlbum album)
    {
        parent = callback;
        position = pos;
        this.album = album;
    }
}
