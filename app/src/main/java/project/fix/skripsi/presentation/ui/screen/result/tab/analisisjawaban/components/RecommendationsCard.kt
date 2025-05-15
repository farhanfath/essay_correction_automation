package project.fix.skripsi.presentation.ui.screen.result.tab.analisisjawaban.components

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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.fix.skripsi.domain.model.PerSoal

@Composable
fun RecommendationsCard(
  hasilKoreksi: List<PerSoal>,
  modifier: Modifier = Modifier
) {
  // Filter jawaban yang bermasalah
  val jawabanBermasalah = hasilKoreksi
    .filter { it.skor < 70 }
    .sortedBy { it.skor }

  if (jawabanBermasalah.isEmpty()) {
    return
  }

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Warning,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.error,
          modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
          text = "Fokus Perbaikan",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onErrorContainer
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Berdasarkan hasil evaluasi, berikut area yang perlu ditingkatkan:",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onErrorContainer
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Daftar rekomendasi
      jawabanBermasalah.take(3).forEachIndexed { index, perSoal ->
        Row(
          modifier = Modifier.padding(vertical = 6.dp),
          verticalAlignment = Alignment.Top
        ) {
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.error),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "${index + 1}",
              color = MaterialTheme.colorScheme.onError,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = "Soal ${perSoal.soal} (Nilai: ${perSoal.skor})",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onErrorContainer
            )

            Spacer(modifier = Modifier.height(4.dp))

            val recommendation = perSoal.alasan
              .split(".")
              .filter { it.isNotBlank() }
              .joinToString(". ") { it.trim() }
              .plus(".")

            Text(
              text = recommendation,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
            )
          }
        }

        if (index < jawabanBermasalah.size - 1 && index < 2) {
          HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.2f)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Button(
        onClick = { /* Tampilkan semua rekomendasi */ },
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.error,
          contentColor = MaterialTheme.colorScheme.onError
        ),
        modifier = Modifier.align(Alignment.End)
      ) {
        Text("Lihat Semua")
      }
    }
  }
}