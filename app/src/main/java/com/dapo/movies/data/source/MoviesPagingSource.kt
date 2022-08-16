package com.dapo.movies.data.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dapo.movies.data.mapper.MovieMapper
import com.dapo.movies.data.model.ui.Movie
import com.dapo.movies.data.repository.MoviesRepository
import com.dapo.movies.env.Env
import retrofit2.HttpException
import java.io.IOException


private const val STARTING_PAGE_INDEX = 1

class MoviesPagingSource(
    private val repository: MoviesRepository,
    private val movieMapper: MovieMapper,
) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = repository.getMoviesSearchResult(position, Env.API_KEY)
            val photos = movieMapper.mapToUIList(response.results)

            LoadResult.Page(
                data = photos,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}