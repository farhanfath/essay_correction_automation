package project.fix.skripsi.presentation.ui.screen.result.tab.allsummary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import project.fix.skripsi.domain.model.SiswaData
import project.fix.skripsi.presentation.ui.screen.result.tab.allsummary.components.StatCard
import project.fix.skripsi.presentation.ui.screen.result.tab.allsummary.components.StudentRankItem
import kotlin.math.roundToInt

@Composable
fun AllSummaryTab(
  modifier: Modifier = Modifier,
  students: List<SiswaData>
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Class Statistics Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      )
    ) {
      Column(
        modifier = Modifier.padding(16.dp)
      ) {
        Text(
          text = "Statistik Kelas",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        val scores = students.map { it.skorAkhir }
        val average = scores.average()
        val highest = scores.maxOrNull() ?: 0.0
        val lowest = scores.minOrNull() ?: 0.0

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          StatCard("Rata-rata", average.roundToInt().toString(), Color(0xFF2196F3))
          StatCard("Tertinggi", highest.roundToInt().toString(), Color(0xFF4CAF50))
          StatCard("Terendah", lowest.roundToInt().toString(), Color(0xFFF44336))
        }
      }
    }

    // Student Rankings
    Card(
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp)
      ) {
        Text(
          text = "Peringkat Siswa",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        val sortedStudents = students.sortedByDescending { it.skorAkhir }

        sortedStudents.forEachIndexed { index, student ->
          StudentRankItem(
            rank = index + 1,
            student = student
          )
          if (index < sortedStudents.size - 1) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
          }
        }
      }
    }
  }
}