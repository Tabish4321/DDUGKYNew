package com.deendayalproject.fragments.composeui.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deendayalproject.R

@Composable
fun InspectionProgressHeader(currentStep: Int) {

    val totalSteps = 7

    val completedColor = Color(0xFF16A34A)
    val activeColor = Color(0xFF2563EB)
    val inactiveColor = Color(0xFFE5E7EB)

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(700),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),

    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                text = "Inspection Progress",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                repeat(totalSteps) { index ->

                    val step = index + 1

                    val color = when {
                        step < currentStep -> completedColor
                        step == currentStep -> activeColor
                        else -> inactiveColor
                    }

                    val scale =
                        if (step == currentStep) pulse else 1f

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {

                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .scale(scale)
                                .background(
                                    color = color,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            if (step < currentStep) {

                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )

                            } else {

                                Text(
                                    text = "$step",
                                    color = if (step == currentStep)
                                        Color.White
                                    else
                                        Color.DarkGray,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                    }

                    if (index != totalSteps - 1) {

                        val lineColor =
                            if (step < currentStep)
                                completedColor
                            else
                                inactiveColor

                        Spacer(
                            modifier = Modifier
                                .height(1.dp)
                                .weight(0.8f)
                                .clip(RoundedCornerShape(10))
                                .background(lineColor)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InspectionProgressHeaderPreview() {

    InspectionProgressHeader(
        currentStep = 3
    )
}