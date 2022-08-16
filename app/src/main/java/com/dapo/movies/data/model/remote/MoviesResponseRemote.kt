package com.dapo.movies.data.model.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MoviesResponseRemote(
    val results: List<MoviesRemote>
)