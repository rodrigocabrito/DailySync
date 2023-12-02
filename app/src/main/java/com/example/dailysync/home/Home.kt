package com.example.dailysync.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens

@Composable
fun Home(navController: NavController, resultShow: Int = 1, modifier: Modifier =
    Modifier.fillMaxSize().wrapContentSize(
        Alignment.Center)) {
    var result by remember { mutableStateOf(resultShow) }
    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        else -> R.drawable.dice_3
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = imageResource), contentDescription = result.toString())
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            result = (1..3).random()
            if (result == 1) {
                navController.navigate(Screens.Read.route)
            }
            if (result == 2) {
                navController.navigate(
                    Screens.Exercise.route.replace(
                        oldValue = "{result}",
                        newValue = result.toString()
                    )
                )
            }
            if (result == 3) {
                navController.navigate(
                    Screens.Sleep.route.replace(
                        oldValue = "{result}",
                        newValue = result.toString()
                    )
                )
            }
        }) {
            Text(stringResource(R.string.roll))

        }
        Button(
            onClick = {
                navController.navigate(
                    Screens.Exercise.route//Just modify your route accordingly
                        .replace(
                            oldValue = "{result}",
                            newValue = result.toString()
                        )
                )
            },
        ) {
            Text(text = stringResource(R.string.result_screen), fontSize = 24.sp)
        }
    }
}