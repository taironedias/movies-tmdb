package android.tairone.movies.presenter

import android.tairone.movies.model.MovieResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    /*
     https://api.themoviedb.org/3/discover/movie?api_key=3a002056d6ffbf6148d1918e57ae7849&language=pt-BR&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_genres=28
     */
    @GET("/3/{category}/movie")
    fun getListMoviesGenres(
        @Path("category") category: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("sort_by") sort_by: String,
        @Query("include_adult") include_adult: Boolean,
        @Query("include_video") include_video: Boolean,
        @Query("page") page: Int,
        @Query("with_genres") with_genres: Int
    ): Call<MovieResults>

    /*
    https://api.themoviedb.org/3/search/movie?api_key=3a002056d6ffbf6148d1918e57ae7849&language=pt-BR&query=50%20tons&page=1&include_adult=false
     */
    @GET("/3/{category}/movie")
    fun getListMoviesResearched(
        @Path("category") category: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("include_adult") include_adult: Boolean
    ): Call<MovieResults>

}