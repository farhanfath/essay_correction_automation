package project.fix.skripsi.presentation.ui.screen.result.tab.detailevaluasi.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import project.fix.skripsi.domain.model.CorrectionType
import project.fix.skripsi.domain.model.CorrectionType.AI
import project.fix.skripsi.domain.model.PerSoal

@Composable
fun ResultItemCard(
  perSoal: PerSoal,
  nomorSoal: Int,
  maxBobot: Int,
  kunciJawaban: String? = null,
  tipeEvaluasi: CorrectionType = AI
) {
  var expanded by remember { mutableStateOf(false) }
  var showDetailDialog by remember { mutableStateOf(false) }
  val isCorrect = perSoal.penilaian == "Benar"

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    onClick = { expanded = !expanded }
  ) {
    Column(
      modifier = Modifier.padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          modifier = Modifier.weight(3f),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Nomor soal dengan badge
          Box(
            modifier = Modifier
              .weight(1f)
              .size(40.dp)
              .clip(CircleShape)
              .background(
                if (isCorrect) {
                  Color(0xFF4CAF50).copy(alpha = 0.2f)
                } else {
                  Color(0xFFE91E63).copy(alpha = 0.2f)
                }
              ),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "$nomorSoal",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = if (isCorrect) {
                Color(0xFF4CAF50)
              } else {
                Color(0xFFE91E63)
              }
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Box(
            modifier = Modifier.weight(4f)
          ) {
            Column {
              Text(
                text = "Soal",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
              )
              Text(
                text = perSoal.soal,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis
              )
            }
          }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Score badge - menampilkan bobot yang didapat
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(
              if (isCorrect) {
                Color(0xFF4CAF50)
              } else {
                Color(0xFFE91E63)
              }
            )
            .padding(horizontal = 12.dp, vertical = 6.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "${perSoal.skor} poin",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Status row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(
            if (isCorrect) {
              Color(0xFF4CAF50).copy(alpha = 0.1f)
            } else {
              Color(0xFFE91E63).copy(alpha = 0.1f)
            }
          )
          .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
          contentDescription = null,
          tint = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFE91E63)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
          text = if (isCorrect) "Jawaban Benar" else "Jawaban Perlu Diperbaiki",
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Medium,
          color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFE91E63)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Menampilkan info bobot maksimal
        if (maxBobot > 0) {
          Text(
            text = "dari $maxBobot poin",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
          )
        }
      }

      // Expanded content with animation
      AnimatedVisibility(
        visible = expanded,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
      ) {
        Column(
          modifier = Modifier.padding(top = 16.dp)
        ) {
          HorizontalDivider()

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "Feedback Evaluasi",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            verticalAlignment = Alignment.Top
          ) {
            Icon(
              imageVector = AutoMirrored.Default.Comment,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
              text = perSoal.alasan,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
          ) {
            Button(
              onClick = { showDetailDialog = true },
              colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
              )
            ) {
              Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = null
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text("Lihat Jawaban")
            }
          }
        }
      }

      // Collapse/expand indicator
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
          contentDescription = if (expanded) "Collapse" else "Expand",
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(24.dp)
        )
      }
    }
  }

  if (showDetailDialog) {
    DetailAnswerDialog(
      perSoal = perSoal,
      nomorSoal = nomorSoal,
      kunciJawaban = kunciJawaban,
      tipeEvaluasi = tipeEvaluasi,
      onDismiss = { showDetailDialog = false }
    )
  }
}

@Preview
@Composable
fun PreviewResultItemCard(modifier: Modifier = Modifier) {
  ResultItemCard(
    perSoal = PerSoal(
      penilaian = "Bagus",
      soal = "apa hayo nama kebun binatang yang ada di Jakarta",
      jawaban = "bagus",
      skor = 20,
      alasan = "jawaban benar"
    ),
    nomorSoal = 1,
    maxBobot = 1
  )
}