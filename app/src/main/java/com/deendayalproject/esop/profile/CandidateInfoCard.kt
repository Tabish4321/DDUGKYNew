package com.deendayalproject.esop.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.remember

import com.deendayalproject.R
import com.deendayalproject.util.AppUtil.base64ToBitmap

@Composable
fun CandidateInfoCard(
    name: String,
    age: String,
    designation: String,
    mobileNo: String,
    gender: String,
    imageUrl: String?
) {
    val bitmap = remember(imageUrl) {
        base64ToBitmap(imageUrl.toString())
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Ajit Ranjan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = stringResource(R.string.age, age),
                    color = Color.DarkGray
                )

                Text(
                    text = stringResource(R.string.designation, designation),
                    color = Color.DarkGray
                )

                Text(
                    text = stringResource(R.string.mobile_no, mobileNo),
                    color = Color.DarkGray
                )

                Text(
                    text = stringResource(R.string.gender, designation),
                    color = Color.DarkGray
                )
            }

            Spacer(
                modifier = Modifier.width(16.dp)
            )
            if (bitmap != null) {

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Candidate Image",

                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(
                            2.dp,
                            Color(0xFF075CE8),
                            CircleShape
                        ),

                    contentScale = ContentScale.Crop
                )

            } else {

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

//            AsyncImage(
//                model = imageUrl,
//                contentDescription = "Candidate Image",
//
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//                    .border(
//                        width = 2.dp,
//                        color = Color(0xFF075CE8),
//                        shape = CircleShape
//                    ),
//
//                contentScale = ContentScale.Crop
//            )
        }
    }
}