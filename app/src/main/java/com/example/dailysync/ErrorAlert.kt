package com.example.dailysync

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErrorAlert(drawableRes: Int, text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
    ) {
        Column {
            Image(
                modifier = Modifier
                    .size(130.dp, 150.dp)
                    .align(Alignment.CenterHorizontally),
                imageVector = ImageVector.vectorResource(drawableRes),
                contentDescription = "",
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = text,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                ),
            )
        }
    }
}