package com.example.dailysync.read

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dailysync.R
import com.example.dailysync.navigation.Screens


data class Elements(
    val name: String,
    val job: String,
    val email: String,
    val phone: String
)

@Composable
fun Read(navController: NavController) {
    val myInfo = Elements(
        name = "João Costa",
        job = "Aluno MEI",
        email = "joaommcosta13@gmail.com",
        phone = "938603579"
    )
    CenteredBusinessCard(elements = myInfo, navController)
}

@Composable
fun CenteredBusinessCard(elements: Elements, navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.back),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Button(
            onClick = { navController.navigate(Screens.Home.route)},
        ) {
            Text(text = (stringResource(R.string.back)), fontSize = 24.sp)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            TopSection(elements)
            BottomSection(elements)
        }
    }
}


@Composable
fun TopSection(elements: Elements) {
    Box(
        modifier = Modifier
            .padding(bottom = 50.dp, top = 30.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.55f)
            .background(Color(0xB4F8ECC2)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, Color.DarkGray, CircleShape)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.foto),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1.35f, 1.35f)
                )
            }
            Text(
                text = elements.name,
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color.DarkGray
            )
            Text(
                text = elements.job,
                fontSize = 30.sp,
                color = Color.DarkGray
            )
        }
    }
}


@Composable
fun BottomSection(elements: Elements) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.40f),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color.DarkGray
                )
                Text(
                    text = elements.email,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                    color = Color.DarkGray
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone Icon",
                    modifier = Modifier.size(20.dp),
                    tint = Color.DarkGray
                )
                Text(
                    text = elements.phone,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.DarkGray
                )
            }
        }
    }
}