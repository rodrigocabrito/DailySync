package com.example.dailysync.home.read

import android.content.Intent
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.dailysync.R
import com.example.dailysync.bookModels.Items


@Composable
fun BookDetails(item: Items?) {
    var expanded: Boolean by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(25.dp, 15.dp)
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val imageLinks = item?.volumeInfo?.imageLinks

            if (imageLinks != null) {
                val url: StringBuilder = StringBuilder(imageLinks.thumbnail)
                url.insert(4, "s")

                Image(
                    painter = rememberAsyncImagePainter(model = url.toString()),
                    contentDescription = null,
                    modifier = Modifier
                        .size(133.dp, 200.dp)
                        .padding(5.dp, 0.dp),
                    contentScale = ContentScale.Fit
                )

            } else {
                Box(
                    modifier = Modifier
                        .size(133.dp, 200.dp)
                        .padding(5.dp, 0.dp),
                ) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(100.dp, 200.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.image_not_available),
                        contentDescription = "",
                    )
                }
            }

            item?.volumeInfo?.title?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    ),
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
                )
            }
        }

        Divider(
            modifier = Modifier.padding(top = 15.dp),
            color = Color.Black
        )

        Column(
            modifier = Modifier.padding(0.dp, 15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            Column {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = "Description",
                    style = TextStyle(
                        fontSize = 20.sp,
                    )
                )

                if (item?.volumeInfo?.description != null) {
                    if(!expanded) {
                        Text(
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable { expanded = !expanded },
                            text = item.volumeInfo.description,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 3,
                        )
                    } else {
                        Text(
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable { expanded = !expanded },
                            text = item.volumeInfo.description,
                        )
                    }
                } else {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "Not available",
                    )
                }
            }

            Divider(
                color = Color.Black,
            )

            Column(
                modifier = Modifier.padding(horizontal = 10.dp),
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = "Details",
                    style = TextStyle(
                        fontSize = 20.sp,
                    )
                )

                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "ISBN",
                    )
                    if(item?.volumeInfo?.industryIdentifiers?.get(0)?.identifier != null) {
                        Text(
                            text = item.volumeInfo.industryIdentifiers[0].identifier,
                        )
                    } else {
                        Text(
                            text = "-",
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Page Count",
                    )
                    if(item?.volumeInfo?.pageCount != null) {
                        Text(
                            text = item.volumeInfo.pageCount.toString(),
                        )
                    } else {
                        Text(
                            text = "-",
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Published Date",
                    )
                    if(item?.volumeInfo?.publishedDate != null) {
                        Text(
                            text = item.volumeInfo.publishedDate,
                        )
                    } else {
                        Text(
                            text = "-",
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Publisher",
                    )
                    if(item?.volumeInfo?.publisher != null) {
                        Text(
                            text = item.volumeInfo.publisher,
                        )
                    } else {
                        Text(
                            text = "-",
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Language"
                    )
                    if(item?.volumeInfo?.language != null) {
                        Text(
                            text = item.volumeInfo.language
                        )
                    } else {
                        Text(
                            text = "-"
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Categories"
                    )
                    if(item?.volumeInfo?.categories != null) {
                        item.volumeInfo.categories.let { it ->
                            val value = it.joinToString(
                                separator = ", ",
                            ) {
                                it
                            }
                            Text(text = value)
                        }
                    } else {
                        Text(
                            text = "-"
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Link"
                    )
                    if(item?.volumeInfo?.infoLink != null) {
                        Text(
                            modifier = Modifier.clickable {
                                val customTabsIntent = CustomTabsIntent.Builder()
                                    .setDefaultColorSchemeParams(
                                        CustomTabColorSchemeParams.Builder()
                                            .setToolbarColor(Color(0xFF252525).hashCode())
                                            .build()
                                    )
                                    .build()
                                customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                            },
                            text = "Google Books",
                            color = Color(3, 155, 229, 255),
                            style = TextStyle(
                                fontSize = 16.sp,
                                textDecoration = TextDecoration.Underline,
                            ),
                        )
                    } else {
                        Text(
                            text = "-"
                        )
                    }
                }
            }
        }
    }
}