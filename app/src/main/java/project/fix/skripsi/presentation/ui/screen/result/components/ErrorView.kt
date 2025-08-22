package project.fix.skripsi.presentation.ui.screen.result.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ErrorView(
  errorMessage: String,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  // Parse error message untuk extract emoji dan content
  val (emoji, title, description, tips) = parseErrorMessage(errorMessage)

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
    ),
    border = BorderStroke(
      width = 1.dp,
      color = MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      // Animated Error Icon dengan emoji
      Box(
        modifier = Modifier
          .size(120.dp)
          .background(
            brush = Brush.radialGradient(
              colors = listOf(
                MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                MaterialTheme.colorScheme.error.copy(alpha = 0.05f),
                Color.Transparent
              )
            ),
            shape = CircleShape
          ),
        contentAlignment = Alignment.Center
      ) {
        if (emoji.isNotEmpty()) {
          Text(
            text = emoji,
            fontSize = 48.sp,
            modifier = Modifier.animateContentSize()
          )
        } else {
          Icon(
            imageVector = Icons.Rounded.ErrorOutline,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier
              .size(64.dp)
              .animateContentSize()
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Error Title
      Text(
        text = title.ifEmpty { "Oops! Terjadi Kesalahan" },
        style = MaterialTheme.typography.headlineSmall.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 22.sp
        ),
        color = MaterialTheme.colorScheme.error,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Error Description
      if (description.isNotEmpty()) {
        Text(
          text = description,
          style = MaterialTheme.typography.bodyLarge.copy(
            lineHeight = 24.sp
          ),
          textAlign = TextAlign.Center,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))
      }

      // Tips Section (jika ada)
      if (tips.isNotEmpty()) {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
          )
        ) {
          Column(
            modifier = Modifier.padding(16.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Rounded.Lightbulb,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Tips Solusi",
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.primary
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            tips.forEach { tip ->
              Row(
                modifier = Modifier.padding(vertical = 2.dp),
                verticalAlignment = Alignment.Top
              ) {
                Text(
                  text = "•",
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.padding(end = 8.dp, top = 2.dp)
                )
                Text(
                  text = tip,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  lineHeight = 20.sp
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))
      } else {
        Spacer(modifier = Modifier.height(20.dp))
      }

      // Retry Button dengan animasi
      var isPressed by remember { mutableStateOf(false) }

      Button(
        onClick = {
          isPressed = true
          onRetry()
        },
        modifier = Modifier
          .fillMaxWidth(0.7f)
          .height(52.dp)
          .scale(if (isPressed) 0.95f else 1f),
        shape = RoundedCornerShape(26.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary,
          contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(
          defaultElevation = 6.dp,
          pressedElevation = 2.dp
        )
      ) {
        AnimatedContent(
          targetState = isPressed,
          transitionSpec = {
            fadeIn(animationSpec = tween(150)) togetherWith
                    fadeOut(animationSpec = tween(150))
          },
          label = "button_content"
        ) { pressed ->
          if (pressed) {
            CircularProgressIndicator(
              modifier = Modifier.size(20.dp),
              color = MaterialTheme.colorScheme.onPrimary,
              strokeWidth = 2.dp
            )
          } else {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Coba Lagi",
                style = MaterialTheme.typography.bodyLarge.copy(
                  fontWeight = FontWeight.SemiBold
                )
              )
            }
          }
        }
      }

      // Reset pressed state setelah animasi
      LaunchedEffect(isPressed) {
        if (isPressed) {
          delay(1000)
          isPressed = false
        }
      }
    }
  }
}

// Helper function untuk parse error message
private fun parseErrorMessage(errorMessage: String): ErrorMessageParts {
  val lines = errorMessage.split("\n")
  var emoji = ""
  var title = ""
  var description = ""
  val tips = mutableListOf<String>()

  var currentSection = ""

  lines.forEach { line ->
    val trimmedLine = line.trim()
    when {
      // Extract emoji dari title line
      trimmedLine.matches(Regex("^[\\p{So}\\p{Sk}] .+")) -> {
        emoji = trimmedLine.first().toString()
        title = trimmedLine.substring(2)
      }
      // Detect tips section
      trimmedLine.startsWith("💡") -> {
        currentSection = "tips"
      }
      // Extract tips
      currentSection == "tips" && trimmedLine.startsWith("•") -> {
        tips.add(trimmedLine.substring(1).trim())
      }
      // Description (everything else that's not empty)
      trimmedLine.isNotEmpty() &&
              !trimmedLine.startsWith("💡") &&
              !trimmedLine.startsWith("•") &&
              title.isNotEmpty() &&
              description.isEmpty() -> {
        description = trimmedLine
      }
    }
  }

  return ErrorMessageParts(emoji, title, description, tips)
}

private data class ErrorMessageParts(
  val emoji: String,
  val title: String,
  val description: String,
  val tips: List<String>
)