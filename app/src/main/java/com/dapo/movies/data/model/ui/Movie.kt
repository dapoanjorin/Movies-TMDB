package com.dapo.movies.data.model.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val url: MovieUrl
) : Parcelable

@Parcelize
data class MovieUrl(
   val url: String?
) : Parcelable {
    val attributionUrl get() = "https://image.tmdb.org/t/p/w500$url"
}
