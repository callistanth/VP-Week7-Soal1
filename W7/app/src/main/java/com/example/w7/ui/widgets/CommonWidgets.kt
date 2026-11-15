package com.example.w7.ui.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.w7.ui.theme.DarkSurfaceVariant
import com.example.w7.ui.theme.PokeRed
import com.example.w7.ui.theme.TextMuted
import com.example.w7.ui.theme.TextPrimary
import com.example.w7.ui.theme.TextSecondary

@Composable
fun RemoteImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    if (url.isNullOrBlank()) {
        Box(
            modifier = modifier.background(DarkSurfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Filled.Image,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(32.dp),
            )
        }
    } else {
        AsyncImage(
            model = url,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier.background(DarkSurfaceVariant),
        )
    }
}

@Composable
fun PokeballIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(36.dp)) {
        val w = size.width
        val h = size.height
        val radius = w / 2f
        val center = Offset(w / 2f, h / 2f)


        drawArc(
            color = Color(0xFFEF4444),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = true,
            size = Size(w, h),
        )

        drawArc(
            color = Color.White,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = true,
            size = Size(w, h),
        )

        drawCircle(
            color = Color(0xFF1E293B),
            radius = radius,
            center = center,
            style = Stroke(width = 3.dp.toPx()),
        )

        drawLine(
            color = Color(0xFF1E293B),
            start = Offset(0f, center.y),
            end = Offset(w, center.y),
            strokeWidth = 3.dp.toPx(),
        )

        drawCircle(
            color = Color.White,
            radius = radius * 0.35f,
            center = center,
        )
        drawCircle(
            color = Color(0xFF1E293B),
            radius = radius * 0.35f,
            center = center,
            style = Stroke(width = 3.dp.toPx()),
        )

        drawCircle(
            color = Color(0xFF1E293B),
            radius = radius * 0.16f,
            center = center,
        )
    }
}

@Composable
fun LoadingBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(48.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = PokeRed)
    }
}


@Composable
fun ConnectionFailedView(
    errorCode: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFF331B26)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.WifiOff,
                contentDescription = null,
                tint = Color(0xFFF472B6),
                modifier = Modifier.size(32.dp),
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Connection Failed",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = errorCode,
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PokeRed,
                contentColor = Color.White,
            ),
            modifier = Modifier.height(44.dp),
        ) {
            Icon(
                Icons.Filled.Refresh,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text("Try Again", fontWeight = FontWeight.SemiBold)
        }
    }
}
