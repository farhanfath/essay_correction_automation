package project.fix.skripsi.presentation.ui.screen.result.tab.allsummary.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import project.fix.skripsi.domain.model.SiswaData
import kotlin.math.roundToInt

@Composable
fun StudentRankItem(rank: Int, student: SiswaData) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Rank badge
    Card(
      colors = CardDefaults.cardColors(
        containerColor = when (rank) {
          1 -> Color(0xFFFFD700) // Gold
          2 -> Color(0xFFC0C0C0) // Silver
          3 -> Color(0xFFCD7F32) // Bronze
          else -> MaterialTheme.colorScheme.secondary
        }
      ),
      modifier = Modifier.size(32.dp)
    ) {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
      ) {
        Text(
          text = rank.toString(),
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }
    }

    Spacer(modifier = Modifier.width(12.dp))

    // Student info
    Column(
      modifier = Modifier.weight(1f)
    ) {
      Text(
        text = student.nama,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Medium
      )
      Text(
        text = "${student.hasilKoreksi.size} soal dijawab",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
      )
    }

    // Score
    Text(
      text = "${student.skorAkhir.roundToInt()}",
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      color = when {
        student.skorAkhir >= 80 -> Color(0xFF4CAF50)
        student.skorAkhir >= 60 -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
      }
    )
  }
}