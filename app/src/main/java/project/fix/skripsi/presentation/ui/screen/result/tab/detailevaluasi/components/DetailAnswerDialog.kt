package project.fix.skripsi.presentation.ui.screen.result.tab.detailevaluasi.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import project.fix.skripsi.domain.model.constants.CorrectionType
import project.fix.skripsi.domain.model.constants.CorrectionType.AI
import project.fix.skripsi.domain.model.constants.CorrectionType.ANSWER_KEY
import project.fix.skripsi.domain.model.PerSoal

@Composable
fun DetailAnswerDialog(
  perSoal: PerSoal,
  nomorSoal: Int,
  kunciJawaban: String?,
  tipeEvaluasi: CorrectionType,
  onDismiss: () -> Unit
) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(
      usePlatformDefaultWidth = false,
      dismissOnBackPress = true,
      dismissOnClickOutside = true
    )
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .fillMaxHeight(0.85f)
        .padding(8.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
      Column(
        modifier = Modifier.fillMaxSize()
      ) {
        // Header dengan close button
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 16.dp, 16.dp, 0.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Detail Jawaban Soal $nomorSoal",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Tutup",
              tint = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        // Content yang bisa di-scroll
        LazyColumn(
          modifier = Modifier
            .weight(1f)
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          // Status penilaian dengan badge
          item {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                  if (perSoal.penilaian == "Benar") {
                    Color(0xFF4CAF50).copy(alpha = 0.1f)
                  } else {
                    Color(0xFFE91E63).copy(alpha = 0.1f)
                  }
                )
                .padding(16.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (perSoal.penilaian == "Benar") Icons.Default.CheckCircle else Icons.Default.Cancel,
                contentDescription = null,
                tint = if (perSoal.penilaian == "Benar") Color(0xFF4CAF50) else Color(0xFFE91E63),
                modifier = Modifier.size(24.dp)
              )

              Spacer(modifier = Modifier.width(12.dp))

              Column {
                Text(
                  text = if (perSoal.penilaian == "Benar") "Jawaban Benar" else "Jawaban Kurang Tepat",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.SemiBold,
                  color = if (perSoal.penilaian == "Benar") Color(0xFF4CAF50) else Color(0xFFE91E63)
                )
                Text(
                  text = "Skor: ${perSoal.skor} poin",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
              }

              Spacer(modifier = Modifier.weight(1f))

              // Badge jenis evaluasi
              Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(
                    imageVector = if (tipeEvaluasi == AI) Icons.Default.Psychology else Icons.Default.Key,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = tipeEvaluasi.badge,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                  )
                }
              }
            }
          }

          // Soal
          item {
            DetailSection(
              title = "Pertanyaan",
              icon = Icons.Default.Quiz,
              content = perSoal.soal,
              backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            )
          }

          // Kunci jawaban (jika ada)
          if (!kunciJawaban.isNullOrBlank()) {
            item {
              DetailSection(
                title = "Kunci Jawaban",
                icon = Icons.Default.Key,
                content = kunciJawaban,
                backgroundColor = Color(0xFFFFC107).copy(alpha = 0.2f)
              )
            }
          }

          // Jawaban siswa
          item {
            DetailSection(
              title = "Jawaban Siswa",
              icon = Icons.Default.Person,
              content = perSoal.jawaban,
              backgroundColor = Color(0xFF2196F3).copy(alpha = 0.1f)
            )
          }

          // Feedback AI
          item {
            DetailSection(
              title = "Feedback Evaluasi",
              icon = AutoMirrored.Default.Comment,
              content = perSoal.alasan,
              backgroundColor = Color(0xFF9C27B0).copy(alpha = 0.1f)
            )
          }
        }

        // Bottom buttons
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
              contentColor = MaterialTheme.colorScheme.primary
            )
          ) {
            Text("Tutup")
          }

          Button(
            onClick = {
              // Implementasi untuk copy atau share detail
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary
            )
          ) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Salin Detail")
          }
        }
      }
    }
  }
}

@Composable
private fun DetailSection(
  title: String,
  icon: ImageVector,
  content: String,
  backgroundColor: Color
) {
  Column {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(bottom = 8.dp)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }

    Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(12.dp),
      color = backgroundColor
    ) {
      Text(
        text = content,
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        lineHeight = 20.sp
      )
    }
  }
}

@Preview
@Composable
fun PreviewDetailAnswerDialog(modifier: Modifier = Modifier) {
  DetailAnswerDialog(
    perSoal = PerSoal(
      penilaian = "Benar",
      soal = "apa hayo nama kebun binatang yang ada di Jakarta",
      jawaban = "bagus",
      skor = 20,
      alasan = "jawaban benar"
    ),
    nomorSoal = 1,
    kunciJawaban = "jawaban benar",
    tipeEvaluasi = ANSWER_KEY,
    onDismiss = {}

  )
}