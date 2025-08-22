package project.fix.skripsi.presentation.ui.screen.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.model.SavedScoreHistory

@Composable
fun ScoreStatsRow(savedScoreHistory: SavedScoreHistory) {
  val allStudents = savedScoreHistory.hasilKoreksi.flatMap { it.resultData }

  val avgScore = if (allStudents.isNotEmpty()) {
    allStudents.map { it.skorAkhir }.average()
  } else 0.0

  val maxScore = allStudents.maxOfOrNull { it.skorAkhir } ?: 0.0
  val minScore = allStudents.minOfOrNull { it.skorAkhir } ?: 0.0

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly
  ) {
    StatItem(
      label = "Siswa",
      value = allStudents.size.toString(),
      icon = Icons.Default.People,
      color = MaterialTheme.colorScheme.primary
    )

    StatItem(
      label = "Rata-rata",
      value = "%.1f".format(avgScore),
      icon = Icons.Default.BarChart,
      color = MaterialTheme.colorScheme.secondary
    )

    StatItem(
      label = "Tertinggi",
      value = "%.1f".format(maxScore),
      icon = Icons.AutoMirrored.Default.TrendingUp,
      color = Color(0xFF4CAF50)
    )

    StatItem(
      label = "Terendah",
      value = "%.1f".format(minScore),
      icon = Icons.AutoMirrored.Default.TrendingDown,
      color = Color(0xFFFF5722)
    )
  }
}

@Composable
fun StatItem(
  label: String,
  value: String,
  icon: ImageVector,
  color: Color
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = color,
      modifier = Modifier.size(20.dp)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = value,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold,
      color = color
    )
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    )
  }
}
