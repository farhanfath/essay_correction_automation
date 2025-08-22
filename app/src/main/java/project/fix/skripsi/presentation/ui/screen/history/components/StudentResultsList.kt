package project.fix.skripsi.presentation.ui.screen.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import project.fix.skripsi.domain.model.SavedScoreHistory
import project.fix.skripsi.domain.model.SiswaData
import project.fix.skripsi.presentation.ui.screen.history.getScoreColor

@Composable
fun StudentResultsList(savedScoreHistory: SavedScoreHistory) {
  val students = savedScoreHistory.hasilKoreksi.flatMap { it.resultData }

  Column {
    Text(
      text = "Hasil Siswa",
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(bottom = 12.dp)
    )

    students.take(5).forEach { student ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = student.nama,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.weight(1f)
        )

        Surface(
          color = getScoreColor(student.skorAkhir).copy(alpha = 0.1f),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "%.1f".format(student.skorAkhir),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = getScoreColor(student.skorAkhir),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }
    }

    if (students.size > 5) {
      Text(
        text = "dan ${students.size - 5} siswa lainnya...",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        modifier = Modifier.padding(top = 8.dp)
      )
    }
  }
}