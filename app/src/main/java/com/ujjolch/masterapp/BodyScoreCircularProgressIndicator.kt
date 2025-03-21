package com.ujjolch.masterapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.masterapp.R

@Composable
fun BodyScoreCircularProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 2.dp,
    backgroundColor: Color = colorResource(id = R.color.Circular_Progress_Rim),
    progressColor: Color = Color.White,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        contentAlignment = androidx.compose.ui.Alignment.Center,
        modifier = modifier
            .aspectRatio(1f)
            .padding(strokeWidth / 2)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sweepAngle = (progress / 100f) * 360f
            drawArc(
                color = backgroundColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(strokeWidth.toPx())
            )
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(strokeWidth.toPx())
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(strokeWidth)
        ) {
            content()
        }
    }
}