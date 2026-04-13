package com.deendayalproject.fragments.composeui.LocationHandle

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

@Composable
fun rememberGeofenceChecker(
    apiCoordinate: String,
    radius: Float = 100f,
    onResult: (Boolean, Float?) -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(activity)
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                getLocationAndCheck(
                    fusedLocationClient,
                    apiCoordinate,
                    radius,
                    onResult
                )
            } else {
                onResult(false, null)
            }
        }

    LaunchedEffect(Unit) {

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            getLocationAndCheck(
                fusedLocationClient,
                apiCoordinate,
                radius,
                onResult
            )

        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}

fun getLocationAndCheck(
    fusedLocationClient: FusedLocationProviderClient,
    apiCoordinate: String,
    radius: Float,
    onResult: (Boolean, Float?) -> Unit
) {

    val parts = apiCoordinate.split("&")
    if (parts.size < 2) {
        Log.e("GEOFENCE", "Invalid coordinate")
        return
    }

    val centerLat = parts[0].trim().toDoubleOrNull() ?: return
    val centerLng = parts[1].trim().toDoubleOrNull() ?: return

    Log.e("GEOFENCE Cordinate ", "$centerLat \n $centerLng" )

    fusedLocationClient.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        null
    ).addOnSuccessListener { location ->

        if (location != null) {

            val result = FloatArray(1)

            Location.distanceBetween(
                location.latitude,
                location.longitude,
                centerLat,
                centerLng,
                result
            )

            val distance = result[0]

            onResult(distance <= radius, distance)

        } else {
            onResult(false, null)
        }

    }.addOnFailureListener {
        onResult(false, null)
    }
}

@Composable
fun LocationMismatchDialog(
    distance: Float?,
    onOkClick: () -> Unit
) {

    AlertDialog(
        onDismissRequest = {},
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFD32F2F), // red warning
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Location Mismatch",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
            }
        },

        text = {
            Column {

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (distance != null)
                        "You are ${distance.toInt()} meters away from the required location."
                    else
                        "You are not in the required location.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Please move within 100 meters to continue.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        },

        confirmButton = {
            Button(
                onClick = onOkClick,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F) // red
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Go Back",
                    color = Color.White
                )
            }
        }
    )
}