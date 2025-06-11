package project.fix.skripsi.presentation.ui.screen.result.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import project.fix.skripsi.domain.model.SiswaData
import kotlin.math.roundToInt

@Composable
fun StudentSelector(
  modifier: Modifier = Modifier,
  students: List<SiswaData>,
  selectedIndex: Int,
  onStudentSelected: (Int) -> Unit
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.secondaryContainer
    )
  ) {
    Column(
      modifier = Modifier.padding(16.dp)
    ) {
      Text(
        text = "Pilih Siswa",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSecondaryContainer
      )

      Spacer(modifier = Modifier.height(8.dp))

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        itemsIndexed(students) { index, student ->
          FilterChip(
            selected = selectedIndex == index,
            onClick = { onStudentSelected(index) },
            label = {
              Text(
                text = student.nama,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            },
            leadingIcon = if (selectedIndex == index) {
              { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
            } else null,
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primary,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            )
          )
        }
      }

      // Quick stats
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Rata-rata kelas: ${students.map { it.skorAkhir }.average().roundToInt()}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
        )
        Text(
          text = "Skor tertinggi: ${students.maxByOrNull { it.skorAkhir }?.skorAkhir?.roundToInt() ?: 0}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
        )
      }
    }
  }
}