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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.remember

import com.deendayalproject.R
import com.deendayalproject.esop.dashboard.DashboardCard
import com.deendayalproject.util.AppUtil.base64ToBitmap

@Composable
fun CandidateInfoCard(

    loginId: String,
    userName: String,
    mobile: String,
    email: String,
    gender: String
) {

    Column {

        // Candidate Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(5.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Candidate Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Name : $userName",
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Login Id : $loginId",
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Mobile : $mobile",
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Email : $email",
                    fontSize = 15.sp
                )


                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Gender : ${gender.ifEmpty { "N/A" }}",
                    fontSize = 15.sp
                )
            }
        }
    }
}
//@Composable
//fun CandidateInfoCard(
//
//    loginId: String,
//    userName: String,
//    mobile: String,
//    email: String,
//    gender: String
//) {
//
//    Column {
//
//        // Candidate Details Card
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            elevation = CardDefaults.cardElevation(5.dp),
//            colors = CardDefaults.cardColors(Color.White)
//        ) {
//
//            Column(
//                modifier = Modifier.padding(16.dp)
//            ) {
//
//                Text(
//                    text = stringResource(R.string.candidate_details),
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 18.sp
//                )
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Text(
//                    text = stringResource(R.string.name, userName),
//                    fontSize = 15.sp
//                )
//
//                Spacer(modifier = Modifier.height(6.dp))
//
//                Text(
//                    text = stringResource(R.string.login_id, loginId),
//                    fontSize = 15.sp
//                )
//
//                Spacer(modifier = Modifier.height(6.dp))
//
//                Text(
//                    text = stringResource(R.string.mobile_no, mobile),
//                    fontSize = 15.sp
//                )
//                Spacer(modifier = Modifier.height(6.dp))
//
//                Text(
//                    text = stringResource(R.string.email_id, email),
//                    fontSize = 15.sp
//                )
//
//
//                Spacer(modifier = Modifier.height(6.dp))
//
//                Text(
////                    text = "Gender : ${gender.ifEmpty { "N/A" }}",
//                    text = stringResource(R.string.gender, gender.ifEmpty { "N/A" }),
//                    fontSize = 15.sp
//                )
//            }
//        }}}