package project.fix.skripsi.presentation.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun LoadingAnimation() {
  val infiniteTransition = rememberInfiniteTransition(label = "loading_animation")
  val scale by infiniteTransition.animateFloat(
    initialValue = 0.6f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(600, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "scale_animation"
  )

  val rotation by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(1500, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "rotation_animation"
  )

  Box(
    modifier = Modifier
      .graphicsLayer {
        rotationZ = rotation
        scaleX = scale
        scaleY = scale
      }
      .size(24.dp)
      .background(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(50)
      )
  )
}