package com.dapo.movies.data.mapper

import com.dapo.movies.data.model.remote.MoviesRemote
import com.dapo.movies.data.model.ui.Movie
import com.dapo.movies.data.model.ui.MovieUrl
import javax.inject.Inject

class MovieMapper  @Inject constructor(): UIModelMapper<MoviesRemote, Movie>() {
    override fun mapToUI(entity: MoviesRemote): Movie {
        return with(entity) {
            Movie(
                id,
                MovieUrl(poster_path)
            )
        }
    }

    override fun mapFromUI(model: Movie): MoviesRemote {
        throw Exception("Doesn't work that way")
    }
}