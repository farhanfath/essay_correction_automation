package project.fix.skripsi.presentation.ui.screen.result.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LoadingEvaluationAnimation(modifier: Modifier = Modifier) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanning_animation")
    val scanAnimation by infiniteTransition.animateFloat(
      initialValue = 0f,
      targetValue = 1f,
      animationSpec = infiniteRepeatable(
        animation = tween(2000, easing = LinearEasing),
        repeatMode = RepeatMode.Restart
      ),
      label = "scan_animation"
    )

    Box(
      modifier = Modifier
        .size(200.dp)
        .background(
          MaterialTheme.colorScheme.surfaceVariant,
          RoundedCornerShape(16.dp)
        ),
      contentAlignment = Alignment.Center
    ) {
      // Document outline
      Box(
        modifier = Modifier
          .size(160.dp, 120.dp)
          .border(
            width = 2.dp,
            color = MaterialTheme.colorScheme.outline,
            shape = RoundedCornerShape(8.dp)
          )
      )

      // Scanning line animation
      Box(
        modifier = Modifier
          .size(160.dp, 2.dp)
          .offset(y = (120 * scanAnimation - 60).dp)
          .background(
            MaterialTheme.colorScheme.primary,
            RoundedCornerShape(1.dp)
          )
      )
    }

    Spacer(modifier = Modifier.height(24.dp))

    CircularProgressIndicator(
      modifier = Modifier.size(48.dp),
      color = MaterialTheme.colorScheme.primary,
      strokeWidth = 4.dp
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Mengevaluasi essay...",
      style = MaterialTheme.typography.titleMedium,
      textAlign = TextAlign.Center
    )
  }
}