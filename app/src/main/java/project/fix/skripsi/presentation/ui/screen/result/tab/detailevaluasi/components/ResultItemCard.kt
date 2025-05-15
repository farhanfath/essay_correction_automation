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
import androidx.compose.ui.unit.dp
import project.fix.skripsi.domain.model.PerSoal

@Composable
fun ResultItemCard(
  perSoal: PerSoal,
  nomorSoal: Int
) {
  var expanded by remember { mutableStateOf(false) }

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
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Nomor soal dengan badge
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(
                when {
                  perSoal.skor >= 80 -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                  perSoal.skor >= 60 -> Color(0xFFFFC107).copy(alpha = 0.2f)
                  else -> Color(0xFFE91E63).copy(alpha = 0.2f)
                }
              ),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "$nomorSoal",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = when {
                perSoal.skor >= 80 -> Color(0xFF4CAF50)
                perSoal.skor >= 60 -> Color(0xFFFFC107)
                else -> Color(0xFFE91E63)
              }
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Box(
            modifier = Modifier.width(200.dp)
          ) {
            Text(
              modifier = Modifier.basicMarquee(),
              text = "Soal ${perSoal.soal}",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        // Score badge
        val sample = 100
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(
              when {
                perSoal.skor >= 80 -> Color(0xFF4CAF50)
                perSoal.skor >= 60 -> Color(0xFFFFC107)
                else -> Color(0xFFE91E63)
              }
            ),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "${perSoal.skor}",
            style = MaterialTheme.typography.titleMedium,
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
            when {
              perSoal.penilaian == "Benar" -> Color(0xFF4CAF50).copy(alpha = 0.1f)
              else -> Color(0xFFE91E63).copy(alpha = 0.1f)
            }
          )
          .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = if (perSoal.penilaian == "Benar") Icons.Default.Check else Icons.Default.Close,
          contentDescription = null,
          tint = if (perSoal.penilaian == "Benar") Color(0xFF4CAF50) else Color(0xFFE91E63)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
          text = if (perSoal.penilaian == "Benar") "Jawaban Benar" else "Jawaban Perlu Diperbaiki",
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Medium,
          color = if (perSoal.penilaian == "Benar") Color(0xFF4CAF50) else Color(0xFFE91E63)
        )
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
          Divider()

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
              imageVector = Icons.AutoMirrored.Default.Comment,
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
              onClick = { /* Tindakan untuk melihat detail jawaban */ },
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
}