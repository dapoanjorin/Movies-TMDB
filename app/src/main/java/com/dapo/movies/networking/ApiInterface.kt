package com.dapo.movies.networking

import com.dapo.movies.data.model.remote.MoviesResponseRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("movie/popular")
    suspend fun fetchMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): MoviesResponseRemote
}