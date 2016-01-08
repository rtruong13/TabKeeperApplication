package com.i.should.what.whatshouldi.MoviesPackage.Loaders;

import com.i.should.what.whatshouldi.MoviesPackage.Models.DiscoverModel;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBCollectionModel;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBFullMovieModel;
import com.i.should.what.whatshouldi.MoviesPackage.Models.MovieDBSimilarModel;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by ryan on 7/17/2015.
 */
public interface TheMovieDBInterface {

    @GET("discover/{watchType}")
    Call<DiscoverModel> getWatchWithGenre(@Path("watchType") String watchType,
                                          @Query("api_key") String api_key,
                                          @Query("with_genres") int genre,
                                          @Query("page") int page);

    @GET("discover/{watchType}")
    Call<DiscoverModel> getWatch(@Path("watchType") String watchType,
                                          @Query("api_key") String api_key,
                                          @Query("page") int page);

    /**
     * append should include credits!
     */

    @GET("{watchType}/{movie}")
    Call<MovieDBFullMovieModel> getMovieInfo(@Path("watchType") String watchType,
                                             @Path("movie") int movieId,
                                             @Query("api_key") String api_key,
                                             @Query("append_to_response") String append);

    @GET("collection/{collId}")
    Call<MovieDBCollectionModel> getCollection(@Path("collId") int collectionId,
                                               @Query("api_key") String api_key);

    @GET("{watchType}/{movie}/similar")
    Call<MovieDBSimilarModel> getSimilarList(@Path("watchType") String watchType,
                                             @Path("movie") int movieId,
                                             @Query("api_key") String api_key,
                                             @Query("page") int page);

    @GET("{watchType}/popular")
    Call<MovieDBSimilarModel> getTopRated1(@Path("watchType") String watchType,
                                          @Query("api_key") String api_key,
                                          @Query("page") int page);
}