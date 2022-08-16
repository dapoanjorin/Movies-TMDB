package com.dapo.movies.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dapo.movies.data.model.ui.Movie
import com.dapo.movies.ui.components.EmptyListStateComponent
import com.dapo.movies.ui.components.MovieList
import com.dapo.movies.ui.theme.appDark
import com.dapo.movies.ui.theme.appWhite
import com.dapo.movies.ui.viewmodel.MovieListViewModel
import com.dapo.movies.R

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun MoviesHomeScreen() {

    val viewModel: MovieListViewModel = viewModel()

    val listScrollState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()

    val selectedMovie = remember { mutableStateOf<Movie?>(null) }
    var isDialogVisible by remember { mutableStateOf(false) }
    val isListEmpty = remember { MutableTransitionState(false) }

    val toolbarHeight = 105.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                // Returning Zero so we just observe the scroll but don't execute it
                return Offset.Zero
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = appDark)
            .padding(start = 21.dp, end = 21.dp)
    ) {

        Spacer(modifier = Modifier.padding(top = 20.dp))

        Text(
            text = stringResource(id = R.string.popular_movies),
            color = appWhite,
            fontSize = 22.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold
        )

        MovieList(
            flowData = viewModel.movies,
            lazyListState = listScrollState,
            nestedScrollConnection = nestedScrollConnection,
            isDataReturnedEmpty = {
                isListEmpty.targetState = it
            },
            onItemClicked = {
                selectedMovie.value = it
                isDialogVisible = true
            },
            onItemLongClicked = {

            }
        )

        AnimatedVisibility(
            visibleState = isListEmpty,
            enter = fadeIn(initialAlpha = 0.4f),
            exit = fadeOut(tween(durationMillis = 250))
        ) {
            EmptyListStateComponent(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
            )
        }

        if (isDialogVisible) {
            MoviePreviewDialog(
                selectedMovie.value
            ) {
                isDialogVisible = false
            }
        }

    }
}
