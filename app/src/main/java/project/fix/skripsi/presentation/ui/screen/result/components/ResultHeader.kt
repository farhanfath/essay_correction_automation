package project.fix.skripsi.presentation.ui.screen.result.components

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import project.fix.skripsi.domain.model.SiswaData
import kotlin.math.roundToInt

@Composable
fun ResultHeader(
  modifier: Modifier = Modifier,
  siswaData: SiswaData,
  scoreProgress: Float,
  showStudentName: Boolean = true
) {
  Card(
    modifier = modifier.fillMaxWidth(),
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

@Composable
fun StatItem(
  label: String,
  value: String,
  icon: ImageVector
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
      modifier = Modifier.size(20.dp)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = value,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onPrimaryContainer
    )
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
    )
  }
}