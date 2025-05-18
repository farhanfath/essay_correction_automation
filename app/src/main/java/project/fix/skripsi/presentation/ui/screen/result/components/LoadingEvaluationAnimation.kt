package project.fix.skripsi.presentation.ui.screen.result.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LoadingEvaluationAnimation(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanner_animation")

    // Animation for scanning line
    val scanAnimation by infiniteTransition.animateFloat(
      initialValue = 0f,
      targetValue = 1f,
      animationSpec = infiniteRepeatable(
        animation = tween(1500, easing = LinearEasing),
        repeatMode = RepeatMode.Restart
      ),
      label = "scan_line_animation"
    )

    // Animation for scan glow effect
    val glowAnimation by infiniteTransition.animateFloat(
      initialValue = 0f,
      targetValue = 1f,
      animationSpec = infiniteRepeatable(
        animation = tween(1500, easing = LinearOutSlowInEasing),
        repeatMode = RepeatMode.Reverse
      ),
      label = "glow_animation"
    )

    // Animation for text changing
    val textChangeAnimation by infiniteTransition.animateFloat(
      initialValue = 0f,
      targetValue = 5f, // 5 different text states
      animationSpec = infiniteRepeatable(
        animation = tween(5000, easing = LinearEasing),
        repeatMode = RepeatMode.Restart
      ),
      label = "text_change_animation"
    )

    // Pulse animation for document
    val pulseAnimation by infiniteTransition.animateFloat(
      initialValue = 0.97f,
      targetValue = 1.03f,
      animationSpec = infiniteRepeatable(
        animation = tween(1000, easing = FastOutSlowInEasing),
        repeatMode = RepeatMode.Reverse
      ),
      label = "pulse_animation"
    )

    Box(
      modifier = Modifier
        .size(240.dp, 200.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .padding(16.dp),
      contentAlignment = Alignment.Center
    ) {
      // Document background with subtle pulse
      Box(
        modifier = Modifier
          .size(180.dp * pulseAnimation, 140.dp * pulseAnimation)
          .shadow(
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp)
          )
          .background(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(8.dp)
          )
      )

      // Document content lines
      Column(
        modifier = Modifier
          .size(140.dp, 100.dp)
          .scale(pulseAnimation),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        repeat(5) {
          Box(
            modifier = Modifier
              .fillMaxWidth(if (it % 2 == 0) 0.9f else 0.7f)
              .height(4.dp)
              .background(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(2.dp)
              )
          )
        }
      }

      // Scanning effect
      Box(
        modifier = Modifier
          .size(180.dp, 140.dp)
          .border(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            shape = RoundedCornerShape(8.dp)
          )
      ) {
        // Scanning line
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(3.dp)
            .offset(y = (140 * scanAnimation - 70).dp)
            .background(
              brush = Brush.horizontalGradient(
                colors = listOf(
                  MaterialTheme.colorScheme.primary.copy(alpha = 0.0f),
                  MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                  MaterialTheme.colorScheme.primary,
                  MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                  MaterialTheme.colorScheme.primary.copy(alpha = 0.0f)
                )
              )
            )
            .align(Alignment.Center)
        )

        // Glow effect following scan line
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .offset(y = (140 * scanAnimation - 70).dp)
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(
                  MaterialTheme.colorScheme.primary.copy(alpha = 0.0f),
                  MaterialTheme.colorScheme.primary.copy(alpha = 0.2f * glowAnimation),
                  MaterialTheme.colorScheme.primary.copy(alpha = 0.0f)
                )
              )
            )
            .align(Alignment.Center)
        )
      }

      // Magnifying glass icon at scan position
      Box(
        modifier = Modifier
          .offset(
            x = 40.dp,
            y = (140 * scanAnimation - 70).dp
          )
      ) {
        Icon(
          imageVector = Icons.Outlined.Search,
          contentDescription = "Scanning",
          modifier = Modifier.size(24.dp),
          tint = MaterialTheme.colorScheme.primary
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Animated progress indicators
    Row(
      modifier = Modifier.padding(8.dp),
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      repeat(5) { index ->
        val delayedAnimation by animateFloatAsState(
          targetValue = if ((textChangeAnimation.toInt() + index) % 5 == 0) 1f else 0.3f,
          animationSpec = tween(durationMillis = 300),
          label = "dot_animation_$index"
        )

        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary.copy(alpha = delayedAnimation))
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Text that changes based on animation state
    AnimatedContent(
      targetState = textChangeAnimation.toInt(),
      transitionSpec = {
        fadeIn(animationSpec = tween(300)) togetherWith
                fadeOut(animationSpec = tween(300))
      },
      label = "text_animation"
    ) { state ->
      Text(
        text = when(state) {
          0 -> "Memindai essai..."
          1 -> "Menganalisis konten..."
          2 -> "Mengevaluasi struktur..."
          3 -> "Memeriksa tata bahasa..."
          else -> "Menghitung skor akhir..."
        },
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center
      )
    }

    // Progress percentage
    val progressPercentage = (textChangeAnimation / 5 * 100).toInt()
    Text(
      text = "$progressPercentage%",
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.primary,
      modifier = Modifier.padding(top = 8.dp)
    )
  }
}