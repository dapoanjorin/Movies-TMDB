package com.dapo.movies.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.transform.RoundedCornersTransformation
import com.dapo.movies.data.model.ui.Movie
import com.dapo.movies.ui.theme.color
import com.dapo.movies.ui.theme.complementary
import com.dapo.movies.R

@Composable
fun MoviePreviewDialog(movie: Movie?, onDismissCLicked: () -> Unit) {

    Dialog(
        onDismissRequest = onDismissCLicked,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        DialogContent(
            imageUrl = movie?.url?.attributionUrl,
            authorOrDescriptionText = null
        )

    }
}

@Preview
@Composable
private fun DialogContent(
    imageUrl: String? = null,
    imageColor: String? = null,
    authorOrDescriptionText: String? = null
) {
    val imageColorParsed = (imageColor?.color ?: Color(0xFF212121))
    val imageColorParseComplementary = imageColorParsed.complementary()
    val isShowProgress = MutableTransitionState(true)


    val imageRequestListener = object : ImageRequest.Listener {
        override fun onSuccess(
            request: ImageRequest,
            metadata: ImageResult.Metadata
        ) {
            super.onSuccess(request, metadata)
            isShowProgress.targetState = false
        }

        override fun onStart(request: ImageRequest) {
            super.onStart(request)
            isShowProgress.targetState = true

        }
    }

    Box(
        modifier = Modifier
            .width(400.dp)
            .height(500.dp)

    ) {

        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = rememberImagePainter(imageUrl) {
                    transformations(RoundedCornersTransformation(10f))
                    listener(imageRequestListener)
                },
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(
                    imageColorParsed,
                    shape = RoundedCornerShape(
                        bottomStart = 10.dp,
                        bottomEnd = 10.dp
                    )
                )
                .align(Alignment.BottomCenter)
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_image),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.CenterVertically),
                colorFilter = ColorFilter.tint(
                    color = imageColorParseComplementary
                )
            )

            Text(
                text = authorOrDescriptionText ?: "",
                color = imageColorParseComplementary,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
                    .align(Alignment.CenterVertically)
            )

        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.Center),
            visibleState = isShowProgress,
            enter = fadeIn(initialAlpha = 0.4f),
            exit = fadeOut(tween(durationMillis = 250))

        ) {
            CircularProgressIndicator(
                color = imageColorParseComplementary
            )
        }
    }


}