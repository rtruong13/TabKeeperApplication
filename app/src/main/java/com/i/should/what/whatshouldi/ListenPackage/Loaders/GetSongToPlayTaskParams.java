package com.i.should.what.whatshouldi.ListenPackage.Loaders;

/**
 * Created by ryan on 7/30/2015.
 */
public class GetSongToPlayTaskParams {
    String artist;
    String album;
    GetSongToPlayTask.GetSongToPlayTaskCallback callback;

    public GetSongToPlayTaskParams(String album, String artist, GetSongToPlayTask.GetSongToPlayTaskCallback callback)
    {
        this.album = album;
        this.artist = artist;
        this.callback = callback;
    }
}
