package com.example.dailysync.home.read

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.dailysync.R
import com.example.dailysync.bookModels.Items


@Composable
fun BookItemCard(item: Items?, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val imageLinks = item?.volumeInfo?.imageLinks
        val selected = remember { mutableStateOf(false) }
        val interactionSource = remember { MutableInteractionSource() }
        val pressedState by interactionSource.collectIsPressedAsState()
        val scale = animateFloatAsState(if (selected.value && pressedState) .95f else 1f)

        if (pressedState) {
            selected.value = true

            DisposableEffect(Unit) {
                onDispose {
                    selected.value = false
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .size(0.dp, 150.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = true,
                    onClick = { onClick() }
                )
                .scale(scale.value)
                .border(2.dp, Color(0xFF362305),shape = RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        ) {
            Row (modifier = Modifier.background(Color(0xFFFBE5C4))){
                if (imageLinks != null) {
                    val url: StringBuilder = StringBuilder(imageLinks.thumbnail)
                    url.insert(4, "s")

                    Image(
                        modifier = Modifier
                            .size(100.dp, 200.dp),
                        painter = // Optional: add crossfade effect
                        rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current).data(data = url.toString())
                                .apply(block = fun ImageRequest.Builder.() {
                                    crossfade(true)
                                }).build()
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(100.dp, 200.dp),
                    ) {
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(70.dp, 200.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.image_not_available),
                            contentDescription = "",
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                ) {
                    item?.volumeInfo?.title?.let {
                        Text(
                            text = it,
                            style = TextStyle(
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF362305)
                            ),
                            maxLines = 3
                        )
                    }
                    item?.volumeInfo?.authors?.let { it ->
                        val value = it.joinToString(
                            separator = ", ",
                        ) {
                            it
                        }
                        Text(
                            text = value,
                            fontSize = 12.sp,
                            color = Color(0xFF794E0B),
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MyLibraryBookItemCard(item: Items?, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val imageLinks = item?.volumeInfo?.imageLinks
        val selected = remember { mutableStateOf(false) }
        val interactionSource = remember { MutableInteractionSource() }
        val pressedState by interactionSource.collectIsPressedAsState()
        val scale = animateFloatAsState(if (selected.value && pressedState) .95f else 1f)

        if (pressedState) {
            selected.value = true

            DisposableEffect(Unit) {
                onDispose {
                    selected.value = false
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .size(0.dp, 150.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = true,
                    onClick = { onClick() }
                )
                .scale(scale.value)
                .border(2.dp, Color(0xFF362305),shape = RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        ) {
            Row (modifier = Modifier.background(Color(0xFFFBE5C4))){
                if (imageLinks != null) {
                    val url: StringBuilder = StringBuilder(imageLinks.thumbnail)
                    url.insert(4, "s")

                    Image(
                        modifier = Modifier
                            .size(100.dp, 200.dp),
                        painter = // Optional: add crossfade effect
                        rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current).data(data = url.toString())
                                .apply(block = fun ImageRequest.Builder.() {
                                    crossfade(true)
                                }).build()
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(100.dp, 200.dp),
                    ) {
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(70.dp, 200.dp),
                            imageVector = ImageVector.vectorResource(R.drawable.image_not_available),
                            contentDescription = "",
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                ) {
                    item?.volumeInfo?.title?.let {
                        Text(
                            text = it,
                            style = TextStyle(
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF362305)
                            ),
                            maxLines = 3
                        )
                    }
                    item?.volumeInfo?.authors?.let { it ->
                        val value = it.joinToString(
                            separator = ", ",
                        ) {
                            it
                        }
                        Text(
                            text = value,
                            fontSize = 12.sp,
                            color = Color(0xFF794E0B),
                            maxLines = 2
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    item?.status?.toString().let {
                        Text(
                            text = when (it) {
                                "TO_READ" -> {
                                    "To Read"
                                }
                                "READING" -> {
                                    "Reading"
                                }
                                else -> {
                                    "Finished"
                                }
                            },
                            style = TextStyle(
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF945E07)
                            ),
                            maxLines = 3
                        )
                    }
                }
            }
        }
    }
}