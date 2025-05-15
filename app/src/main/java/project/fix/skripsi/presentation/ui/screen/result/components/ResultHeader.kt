package project.fix.skripsi.presentation.ui.screen.result.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbsUpDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.fix.skripsi.domain.model.HasilKoreksi

@Composable
fun ResultHeader(
  hasil: HasilKoreksi,
  scoreProgress: Float
) {
  // Header animation
  val scale by animateFloatAsState(
    targetValue = 1f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    )
  )

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .scale(scale),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
  ) {
    Column(
      modifier = Modifier
        .padding(24.dp)
        .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Avatar icon
      Box(
        modifier = Modifier
          .size(80.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(40.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = hasil.nama,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onPrimaryContainer
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "Hasil Evaluasi Essay",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Animated circular progress for score
      Box(
        modifier = Modifier.size(140.dp),
        contentAlignment = Alignment.Center
      ) {
        // Backdrop circle
        val color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        Canvas(modifier = Modifier.size(140.dp)) {
          drawArc(
            color = color,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = 12f, cap = StrokeCap.Round)
          )
        }

        // Progress arc
        Canvas(modifier = Modifier.size(140.dp)) {
          drawArc(
            color = when {
              hasil.skorAkhir >= 80 -> Color(0xFF4CAF50) // Green for high score
              hasil.skorAkhir >= 60 -> Color(0xFFFFC107) // Yellow for medium score
              else -> Color(0xFFE91E63) // Pink/Red for low score
            },
            startAngle = -90f,
            sweepAngle = 360f * scoreProgress,
            useCenter = false,
            style = Stroke(width = 12f, cap = StrokeCap.Round)
          )
        }

        // Center text
        Column(
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "${(scoreProgress * 100).toInt()}",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
          Text(
            text = "Nilai",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Score interpretation
      val scoreInterpretation = when {
        hasil.skorAkhir >= 90 -> "Luar Biasa"
        hasil.skorAkhir >= 80 -> "Sangat Baik"
        hasil.skorAkhir >= 70 -> "Baik"
        hasil.skorAkhir >= 60 -> "Cukup"
        else -> "Perlu Perbaikan"
      }

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
          .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = when {
            hasil.skorAkhir >= 80 -> Icons.Default.ThumbUp
            hasil.skorAkhir >= 60 -> Icons.Default.ThumbsUpDown
            else -> Icons.Default.Warning
          },
          contentDescription = null,
          tint = when {
            hasil.skorAkhir >= 80 -> Color(0xFF4CAF50)
            hasil.skorAkhir >= 60 -> Color(0xFFFFC107)
            else -> Color(0xFFE91E63)
          },
          modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
          text = scoreInterpretation,
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.Medium,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )
      }
    }
  }
}