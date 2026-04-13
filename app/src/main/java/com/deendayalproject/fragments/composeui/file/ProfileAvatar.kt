package com.deendayalproject.fragments.composeui.file

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.asImageBitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import android.graphics.Bitmap

import kotlin.math.abs

@Composable
fun ProfileAvatar(
    name: String,
    base64Image: String?,
    size: Dp = 48.dp
) {

    var showDialog by remember { mutableStateOf(false) }

    val imageBitmap: Bitmap? = remember(base64Image) {
        base64Image?.takeIf { it.isNotBlank() }?.let {
            try {
                val bytes = Base64.decode(it, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: Exception) {
                Log.e("ProfileAvatar", "Error decoding Base64: ${e.message}")
            }
        } as Bitmap?
    }

    // Stable color based on name
    val bgColor = remember(name) {
        val colors = listOf(
            Color(0xFFEF5350),
            Color(0xFFAB47BC),
            Color(0xFF5C6BC0),
            Color(0xFF29B6F6),
            Color(0xFF66BB6A),
            Color(0xFFFFCA28)
        )
        colors[abs(name.hashCode()) % colors.size]
    }

    // Avatar UI
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(bgColor)
            .clickable(enabled = imageBitmap != null) {
                showDialog = true
            },
        contentAlignment = Alignment.Center
    ) {

        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap.asImageBitmap(),
                contentDescription = "Profile",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = name.take(1).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }

    // Fullscreen Image Dialog (WhatsApp style)
    if (showDialog && imageBitmap != null) {
        FullScreenImageDialog(
            bitmap = imageBitmap,
            onDismiss = { showDialog = false }
        )
    }
}