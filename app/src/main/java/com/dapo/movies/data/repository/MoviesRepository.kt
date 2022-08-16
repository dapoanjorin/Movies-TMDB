package com.dapo.movies.data.repository

import com.dapo.movies.networking.ApiInterface
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val api: ApiInterface) {
    suspend fun getMoviesSearchResult(page: Int, apiKey: String) =
        api.fetchMovies(page, apiKey)
}