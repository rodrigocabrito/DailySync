package com.example.dailysync.home.read

import android.content.Intent
import android.util.Log
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.dailysync.BookViewModel
import com.example.dailysync.R
import com.example.dailysync.bookModels.Items
import com.example.dailysync.bookModels.Status
import com.example.dailysync.navigation.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.time.Instant


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
                        contentDescription = "Image not available",
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
                        color = Color(0xFF362305)
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
                    color = Color(0xFF362305)
                )
            }
        }

        Divider(
            modifier = Modifier.padding(top = 15.dp),
            color = Color(0xFF362305)
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
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = Color(0xFF362305)
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
                            color = Color(0xFF362305)
                        )
                    } else {
                        Text(
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable { expanded = !expanded },
                            text = item.volumeInfo.description,
                            color = Color(0xFF362305)
                        )
                    }
                } else {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "Not available",
                        color = Color(0xFF362305)
                    )
                }
            }

            Divider(
                color = Color(0xFF362305),
            )

            Column(
                modifier = Modifier.padding(horizontal = 10.dp),
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = "Details",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF362305)
                    )
                )

                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "ISBN",
                        color = Color(0xFF362305)
                    )
                    if (item != null) {
                        if(!item?.volumeInfo?.industryIdentifiers.isNullOrEmpty() &&
                            item.volumeInfo.industryIdentifiers[0]?.identifier != null) {
                            Text(
                                text = item.volumeInfo.industryIdentifiers[0].identifier,
                                color = Color(0xFF362305)
                            )
                        } else {
                            Text(
                                text = "-",
                                color = Color(0xFF362305)
                            )
                        }
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Page Count",
                        color = Color(0xFF362305)
                    )
                    if(item?.volumeInfo?.pageCount != null) {
                        Text(
                            text = item.volumeInfo.pageCount.toString(),
                            color = Color(0xFF362305)
                        )
                    } else {
                        Text(
                            text = "-",
                            color = Color(0xFF362305)
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Date",
                        color = Color(0xFF362305)
                    )
                    if(item?.volumeInfo?.publishedDate != null) {
                        Text(
                            text = item.volumeInfo.publishedDate,
                            color = Color(0xFF362305)
                        )
                    } else {
                        Text(
                            text = "-",
                            color = Color(0xFF362305)
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Publisher",
                        color = Color(0xFF362305)
                    )
                    if(item?.volumeInfo?.publisher != null) {
                        Text(
                            text = item.volumeInfo.publisher,
                            color = Color(0xFF362305)
                        )
                    } else {
                        Text(
                            text = "-",
                            color = Color(0xFF362305)
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Language",
                        color = Color(0xFF362305)
                    )
                    if(item?.volumeInfo?.language != null) {
                        Text(
                            text = item.volumeInfo.language,
                            color = Color(0xFF362305)
                        )
                    } else {
                        Text(
                            text = "-",
                            color = Color(0xFF362305)
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Categories",
                        color = Color(0xFF362305)
                    )
                    if(item?.volumeInfo?.categories != null) {
                        item.volumeInfo.categories.let { it ->
                            val value = it.joinToString(
                                separator = ", ",
                            ) {
                                it
                            }
                            Text(text = value,
                                color = Color(0xFF362305))
                        }
                    } else {
                        Text(
                            text = "-",
                            color = Color(0xFF362305)
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Link",
                        color = Color(0xFF362305)
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
                            text = "-",
                            color = Color(0xFF362305)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun AddedBookDetails(item: Items?) {

    val currentPage = item?.currentPage
    val totalPages = item?.volumeInfo?.pageCount
    var pagesLeft: Int? = null
    var percentage: Int? = null

    if (totalPages != null && totalPages > 0) {
        pagesLeft = totalPages - (currentPage ?: 0)
        percentage = (currentPage?.times(100))!! / totalPages
    }

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
                        contentDescription = "Image not available",
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
                        color = Color(0xFF362305)
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
                    color = Color(0xFF362305)
                )
            }
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

        Divider(
            modifier = Modifier.padding(top = 15.dp),
            color = Color(0xFF362305)
        )

        Column(
            modifier = Modifier.padding(0.dp, 15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            Row (modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .background(
                    Color(0xFFF5D4A2),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(2.dp, color = Color(0xFF362305), shape = RoundedCornerShape(8.dp))
                ) {
                Column (modifier = Modifier.padding(top = 5.dp,start = 5.dp)){
                    if(totalPages != null) {
                        Text(
                            text = "Reading Progress",
                            color = Color(0xFF362305)
                        )
                        Text(text = "$currentPage of $totalPages",
                            color = Color(0xFF362305),
                            fontWeight = FontWeight.Bold
                        )
                        if (pagesLeft != null) {
                            if(pagesLeft > 0) {
                                Text(
                                    text = "$pagesLeft pages remaining",
                                    color = Color.Gray
                                )
                            }
                        }
                    }else{
                        Text(
                            text = "Number of pages not available",
                            color = Color(0xFF362305)
                        )
                    }
                }

                    Spacer(modifier = Modifier.width(50.dp))

                    CircularRing(percentage)
                }


            Column(
                modifier = Modifier.padding(horizontal = 10.dp),
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = "Details",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF362305)
                    )
                )

                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "ISBN",
                        color = Color(0xFF362305)
                    )
                    if (item != null) {
                        if(!item?.volumeInfo?.industryIdentifiers.isNullOrEmpty() &&
                            item.volumeInfo.industryIdentifiers[0]?.identifier != null) {
                            Text(
                                text = item.volumeInfo.industryIdentifiers[0].identifier,
                                color = Color(0xFF362305)
                            )
                        } else {
                            Text(
                                text = "-",
                                color = Color(0xFF362305)
                            )
                        }
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Page Count",
                        color = Color(0xFF362305)
                    )
                    if(item?.volumeInfo?.pageCount != null) {
                        Text(
                            text = item.volumeInfo.pageCount.toString(),
                            color = Color(0xFF362305)
                        )
                    } else {
                        Text(
                            text = "-",
                            color = Color(0xFF362305)
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Date",
                        color = Color(0xFF362305)
                    )
                    if(item?.volumeInfo?.publishedDate != null) {
                        Text(
                            text = item.volumeInfo.publishedDate,
                            color = Color(0xFF362305)
                        )
                    } else {
                        Text(
                            text = "-",
                            color = Color(0xFF362305)
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Publisher",
                        color = Color(0xFF362305)
                    )
                    if(item?.volumeInfo?.publisher != null) {
                        Text(
                            text = item.volumeInfo.publisher,
                            color = Color(0xFF362305)
                        )
                    } else {
                        Text(
                            text = "-",
                            color = Color(0xFF362305)
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Language",
                        color = Color(0xFF362305)
                    )
                    if(item?.volumeInfo?.language != null) {
                        Text(
                            text = item.volumeInfo.language,
                            color = Color(0xFF362305)
                        )
                    } else {
                        Text(
                            text = "-",
                            color = Color(0xFF362305)
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Categories",
                        color = Color(0xFF362305)
                    )
                    if(item?.volumeInfo?.categories != null) {
                        item.volumeInfo.categories.let { it ->
                            val value = it.joinToString(
                                separator = ", ",
                            ) {
                                it
                            }
                            Text(text = value,
                                color = Color(0xFF362305))
                        }
                    } else {
                        Text(
                            text = "-",
                            color = Color(0xFF362305)
                        )
                    }
                }
                Row {
                    Text(
                        modifier = Modifier.width(120.dp),
                        text = "Link",
                        color = Color(0xFF362305)
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
                            text = "-",
                            color = Color(0xFF362305)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CircularRing(percentage: Int?) {
    Box(
        modifier = Modifier
            .width(50.dp)
            .height(70.dp)
            .padding(top = 20.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 15f // Adjust the stroke width as needed
            val center = size.width / 2f
            val radius = size.width / 2f - strokeWidth / 2
            val startAngle = 270f // Start angle at the top
            val sweepAngle = percentage?.toFloat()?.let { it * 3.6f } ?: 0f // Convert percentage to degrees (100% -> 360f)

            drawArc(
                color = Color(0xFFFBF8DF),
                startAngle = startAngle,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(strokeWidth)
            )

            drawArc(
                color = if (percentage != null) Color(0xFF3FB03D) else Color(0xFFFBF8DF),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(strokeWidth)
            )
        }

        Text(
            text = if (percentage != null) {
                "$percentage%"
            } else {
                "0%"
            },
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.Transparent)
                .padding(4.dp),
            color = Color(0xFF362305),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        )
    }
}

@Composable
fun ReadingSessionBookDetails(item: Items, bookViewModel: BookViewModel, navController: NavHostController) {

    val currentPage = item.currentPage
    val totalPages = item.volumeInfo?.pageCount

    var elapsedTime by remember { mutableLongStateOf(0L) }
    var isChronometerRunning by remember { mutableStateOf(false) }
    var isChronometerPaused by remember { mutableStateOf(true) }

    var saveReadingSessionPopupVisible by remember { mutableStateOf(false) }

    var cancelDialog by remember { mutableStateOf(false) }

    Chronometer(isRunning = !isChronometerPaused) { elapsedMillis ->
        elapsedTime = elapsedMillis
    }

    Column(
        modifier = Modifier
            .padding(25.dp, 40.dp)
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val imageLinks = item?.volumeInfo?.imageLinks

            Text("Reading Session",
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF945E07)
                ),
                maxLines = 3
            )

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
                        contentDescription = "Image not available",
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
                        color = Color(0xFF362305)
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
                    color = Color(0xFF362305)
                )
            }


        }

        Divider(
            modifier = Modifier.padding(top = 15.dp),
            color = Color(0xFF362305)
        )

        Column(
            modifier = Modifier.padding(0.dp, 40.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {


            Column(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
                    .background(
                        Color(0xFFF5D4A2),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(2.dp, color = Color(0xFF362305), shape = RoundedCornerShape(8.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Last Page Registered: ")
                        }
                        append("${item.currentPage}")
                    },
                    color = Color(0xFF362305),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Time: ")
                        }
                        append(formatTime(elapsedTime))
                    },
                    color = Color(0xFF362305),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
                )
            {
                val buttonName = if (isChronometerRunning) {
                    if (isChronometerPaused) "Resume" else "Pause"
                } else {
                    "Start"
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .background(
                            Color(0xFF362305),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            cancelDialog = true
                        }
                        .border(2.dp, color =  Color(0xFFF5D4A2) , shape = RoundedCornerShape(8.dp))
                ) {
                    Text(
                        "Cancel",
                        color = Color(0xFFF5D4A2),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .background(
                            Color(0xFF362305),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            if (isChronometerRunning) {
                                isChronometerPaused = !isChronometerPaused
                            } else {
                                isChronometerRunning = true
                                isChronometerPaused = false
                            }
                        }
                        .border(2.dp, color =  Color(0xFFF5D4A2) , shape = RoundedCornerShape(8.dp))
                ) {
                    Text(
                        buttonName,
                        color = Color(0xFFF5D4A2),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .background(
                            Color(0xFF362305),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            isChronometerPaused = true
                            saveReadingSessionPopupVisible = true
                        }
                        .border(2.dp, color =  Color(0xFFF5D4A2) , shape = RoundedCornerShape(8.dp))
                ) {
                    Text(
                        "Finish",
                        color = Color(0xFFF5D4A2),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center),
                    )
                }
            }
        }
        if (saveReadingSessionPopupVisible) {
            SaveReadingSessionPopup(
                onDismiss = { saveReadingSessionPopupVisible = false },
                bookViewModel, item, navController, elapsedTime
            )
        }
        if (cancelDialog) {
            AlertDialog(
                containerColor = Color(0xFFF5D4A2),
                onDismissRequest = {
                    // Dismiss the dialog when clicking outside of it
                    cancelDialog = false
                },
                title = {
                    Text("Cancel Reading Session",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF362305))

                },
                text = {
                    Text("Do you want to cancel your reading session?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set("item", item)
                            navController.navigate(Screens.BookDetails.route)
                            cancelDialog = false

                        }
                    ) {
                        Text("Yes",color = Color(0xFF362305))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            cancelDialog = false
                        }
                    ) {
                        Text("No",color = Color(0xFF362305))
                    }
                }
            )
        }
    }
}

@Composable
fun SaveReadingSessionPopup(
    onDismiss: () -> Unit,
    bookViewModel: BookViewModel, item: Items, navController: NavHostController, elapsedTime: Long
) {
    var textField1Value by remember { mutableStateOf(item.currentPage.toString()) }


    AlertDialog(
        containerColor = Color(0xFFF5D4A2),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Register Reading Session",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF362305)
            )
        },
        text = {
            Column {
                // Add your text fields and other UI elements here
                OutlinedTextField(
                    value = textField1Value,
                    textStyle = TextStyle(
                        color = Color(0xFF362305)
                    ),
                    placeholder = { Text("The current page you're on", color = Color.Gray) },
                    onValueChange = { textField1Value = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    label = { Text("Last Page Read", color = Color(0xFF362305)) }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if(textField1Value.isNotEmpty()){
                        if(textField1Value.toInt() > item.currentPage) {
                            val previousPage = item.currentPage
                            if (item.status == Status.TO_READ && textField1Value.toInt() > 0) {
                                item.status = Status.READING
                                item.currentPage = textField1Value.toInt()
                            } else if (textField1Value.toInt() >= item.volumeInfo.pageCount) {//Check if the input surpasses the amount of pages on the book
                                item.status = Status.FINISHED
                                item.currentPage = item.volumeInfo.pageCount
                            } else {
                                item.currentPage = textField1Value.toInt()
                            }
                            item.let { bookViewModel.updateStatus(it) }
                            bookViewModel.registerReadingSession(
                                item,
                                textField1Value.toInt() - previousPage,
                                (elapsedTime/ 60000).toInt(),
                                Instant.now().toEpochMilli()
                            )
                            onDismiss()
                            navController.currentBackStackEntry?.savedStateHandle?.set("item", item)
                            navController.navigate(Screens.BookDetails.route)
                        }else{
                            Log.e("Current Page", "Page inserted is less than what it was")//TODO change to warning
                        }
                    }else{
                        Log.e("Empty Fields", "Need to fill the 2 fields")//TODO change to warning
                    }

                }
            ) {
                Text("Save",color = Color(0xFF362305))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cancel",color = Color(0xFF362305))
            }
        }
    )
}

@Composable
private fun Chronometer(
    isRunning: Boolean,
    onTick: (Long) -> Unit
) {
    var elapsedTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            // Flow to emit elapsed time every second
            val timeFlow: Flow<Long> = flow {
                while (true) {
                    delay(1000)
                    elapsedTime += 1000
                    emit(elapsedTime)
                }
            }

            // Collect the elapsed time from the flow and update the UI
            launch {
                timeFlow.collect { elapsedMillis ->
                    onTick(elapsedMillis)
                }
            }
        }
    }
}

private fun formatTime(elapsedTime: Long): String {
    val totalSeconds = elapsedTime / 1000
    val hours = totalSeconds / 3600
    val remainingSeconds = totalSeconds % 3600
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}