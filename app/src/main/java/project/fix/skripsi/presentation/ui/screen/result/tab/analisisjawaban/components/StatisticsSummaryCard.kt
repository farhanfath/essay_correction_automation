package project.fix.skripsi.presentation.ui.screen.result.tab.analisisjawaban.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import project.fix.skripsi.domain.model.PerSoal

@Composable
fun StatisticsSummaryCard(
  hasilKoreksi: List<PerSoal>,
  modifier: Modifier = Modifier
) {
  val totalSoal = hasilKoreksi.size
  val averageScore = hasilKoreksi.map { it.skor }.average().toFloat()
  val highestScore = hasilKoreksi.maxOfOrNull { it.skor } ?: 0
  val lowestScore = hasilKoreksi.minOfOrNull { it.skor } ?: 0

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Text(
        text = "Statistik Penilaian",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(16.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Rata-rata
        StatBox(
          icon = Icons.Default.Star,
          value = "%.1f".format(averageScore),
          label = "Rata-rata",
          tintColor = MaterialTheme.colorScheme.primary,
          modifier = Modifier.weight(1f)
        )

        // Nilai Tertinggi
        StatBox(
          icon = Icons.Default.ThumbUp,
          value = "$highestScore",
          label = "Tertinggi",
          tintColor = Color(0xFF4CAF50),
          modifier = Modifier.weight(1f)
        )

        // Nilai Terendah
        StatBox(
          icon = Icons.Default.Warning,
          value = "$lowestScore",
          label = "Terendah",
          tintColor = Color(0xFFE91E63),
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Distribusi nilai dalam kategori
      val excellent = hasilKoreksi.count { it.skor >= 90 }
      val good = hasilKoreksi.count { it.skor in 70..89 }
      val average = hasilKoreksi.count { it.skor in 50..69 }
      val poor = hasilKoreksi.count { it.skor < 50 }

      Text(
        text = "Distribusi Kategori",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Category bars
      CategoryBar(
        label = "Sangat Baik",
        count = excellent,
        total = totalSoal,
        color = Color(0xFF4CAF50)
      )

      Spacer(modifier = Modifier.height(6.dp))

      CategoryBar(
        label = "Baik",
        count = good,
        total = totalSoal,
        color = Color(0xFF2196F3)
      )

      Spacer(modifier = Modifier.height(6.dp))

      CategoryBar(
        label = "Cukup",
        count = average,
        total = totalSoal,
        color = Color(0xFFFFC107)
      )

      Spacer(modifier = Modifier.height(6.dp))

      CategoryBar(
        label = "Perlu Perbaikan",
        count = poor,
        total = totalSoal,
        color = Color(0xFFE91E63)
      )
    }
  }
}