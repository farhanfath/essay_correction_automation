package project.fix.skripsi.presentation.ui.screen.result.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbsUpDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.model.SiswaData
import project.fix.skripsi.presentation.ui.screen.result.StatItem
import kotlin.math.roundToInt

@Composable
fun ResultHeader(
  siswaData: SiswaData,
  scoreProgress: Float,
  showStudentName: Boolean = false
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer
    )
  ) {
    Column(
      modifier = Modifier.padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      if (showStudentName) {
        Text(
          text = siswaData.nama,
          style = MaterialTheme.typography.headlineSmall,
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
      }

      // Circular Progress Indicator untuk skor
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(120.dp)
      ) {
        CircularProgressIndicator(
          progress = { scoreProgress },
          modifier = Modifier.fillMaxSize(),
          color = when {
            siswaData.skorAkhir >= 80 -> Color(0xFF4CAF50) // Green
            siswaData.skorAkhir >= 60 -> Color(0xFFFF9800) // Orange
            else -> Color(0xFFF44336) // Red
          },
          strokeWidth = 8.dp,
          trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
        )

        Column(
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "${siswaData.skorAkhir.roundToInt()}",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
          Text(
            text = "/ 100",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Grade dan deskripsi
      val grade = when {
        siswaData.skorAkhir >= 90 -> "A" to "Excellent"
        siswaData.skorAkhir >= 80 -> "B" to "Good"
        siswaData.skorAkhir >= 70 -> "C" to "Fair"
        siswaData.skorAkhir >= 60 -> "D" to "Poor"
        else -> "F" to "Fail"
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Card(
          colors = CardDefaults.cardColors(
            containerColor = when {
              siswaData.skorAkhir >= 80 -> Color(0xFF4CAF50)
              siswaData.skorAkhir >= 60 -> Color(0xFFFF9800)
              else -> Color(0xFFF44336)
            }
          )
        ) {
          Text(
            text = grade.first,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }

        Text(
          text = grade.second,
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Statistics
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        StatItem(
          label = "Soal Dijawab",
          value = "${siswaData.hasilKoreksi.size}",
          icon = Icons.AutoMirrored.Default.Assignment
        )
        StatItem(
          label = "Rata-rata",
          value = "${(siswaData.hasilKoreksi.map { it.skor }.average()).roundToInt()}",
          icon = Icons.AutoMirrored.Default.TrendingUp
        )
        StatItem(
          label = "Tertinggi",
          value = "${siswaData.hasilKoreksi.maxByOrNull { it.skor }?.skor ?: 0}",
          icon = Icons.Default.Star
        )
      }
    }
  }
}