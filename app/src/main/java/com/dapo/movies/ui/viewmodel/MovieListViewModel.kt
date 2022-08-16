package com.dapo.movies.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import com.dapo.movies.data.mapper.MovieMapper
import com.dapo.movies.data.repository.MoviesRepository
import com.dapo.movies.data.source.MoviesPagingSource
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MoviesRepository,
    private val movieMapper: MovieMapper,
    state: SavedStateHandle
) : ViewModel() {

    var selectedChipState = mutableStateOf("")
    var textFieldState = mutableStateOf("")

    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    private fun setSearchTerm(query: String) {
        currentQuery.value = query.ifEmpty { DEFAULT_QUERY }
    }

    fun searchCurrentQuery() {
        setSearchTerm(textFieldState.value)
    }

    fun updateSelectedChipState(term: String) {
        selectedChipState.value = term
        textFieldState.value = term
        setSearchTerm(term)
    }

    val movies = getMoviesSearchResult().cachedIn(viewModelScope).asFlow()

    private fun getMoviesSearchResult() = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 200,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            MoviesPagingSource(
                repository = repository,
                movieMapper = movieMapper,
            )
        }
    ).liveData

    companion object {
        private const val CURRENT_QUERY = "current_query"
        const val DEFAULT_QUERY = "corgi"
    }
}
