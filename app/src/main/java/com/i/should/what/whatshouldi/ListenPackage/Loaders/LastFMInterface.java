package com.i.should.what.whatshouldi.ListenPackage.Loaders;

import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMAlbumsSearch;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMArtistSearch;
import com.i.should.what.whatshouldi.ListenPackage.Models.LastFMSimilarArtist;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by ryan on 7/28/2015.
 * http://ws.audioscrobbler.com/ - base
 */
public interface LastFMInterface {

    @GET("2.0/")
    Call<LastFMArtistSearch> searchArtist(@Query("method") String method, //artist.search!!!!
                                          @Query("artist") String searchString, //search
                                          @Query("limit") int limit,//5
                                          @Query("api_key") String key,
                                          @Query("format") String format);//json

    @GET("2.0/")
    Call<LastFMSimilarArtist> similarArtist(@Query("method") String method, //artist.getSimilar!!!!
                                            @Query("artist") String searchString, //search
                                            @Query("limit") int limit,
                                            @Query("api_key") String key,
                                            @Query("format") String format);//json

    @GET("2.0/")
    Call<LastFMAlbumsSearch> topAlbums(@Query("method") String method, //artist.getTopAlbums!!!!
                                        @Query("artist") String searchString, //search
                                        @Query("limit") int limit,
                                        @Query("api_key") String key,
                                        @Query("format") String format);//json

//    @GET("2.0/")
//    Call<LastFMAlbumInfoSearch> albumsInfo(@Query("method") String method, //album.getInfo!!!!
//                                        @Query("mbid") String mbid, //search
//                                        @Query("api_key") String key,
//                                        @Query("format") String format);//json
}
