package project.fix.skripsi.presentation.ui.screen.result.tab.analisisjawaban

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.fix.skripsi.domain.model.PerSoal
import project.fix.skripsi.presentation.ui.screen.result.tab.analisisjawaban.components.RecommendationsCard
import project.fix.skripsi.presentation.ui.screen.result.tab.analisisjawaban.components.StatisticsSummaryCard

@Composable
fun AnalisisJawabanTab(hasilKoreksi: List<PerSoal>) {
  Column(modifier = Modifier.fillMaxWidth()) {
    // Pie chart for correct vs incorrect answers
    val correctAnswers = hasilKoreksi.count { it.penilaian == "Benar" }
    val incorrectAnswers = hasilKoreksi.size - correctAnswers

    StatisticsSummaryCard(hasilKoreksi = hasilKoreksi)

    Spacer(modifier = Modifier.height(16.dp))

    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "Distribusi Jawaban",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Simple pie chart with Canvas
        Box(
          modifier = Modifier
            .size(200.dp)
            .padding(8.dp),
          contentAlignment = Alignment.Center
        ) {
          Canvas(modifier = Modifier.size(200.dp)) {
            val correctRatio = correctAnswers.toFloat() / hasilKoreksi.size
            val incorrectRatio = incorrectAnswers.toFloat() / hasilKoreksi.size

            // Correct answers slice (green)
            drawArc(
              color = Color(0xFF4CAF50),
              startAngle = 0f,
              sweepAngle = 360f * correctRatio,
              useCenter = true
            )

            // Incorrect answers slice (red)
            drawArc(
              color = Color(0xFFE91E63),
              startAngle = 360f * correctRatio,
              sweepAngle = 360f * incorrectRatio,
              useCenter = true
            )
          }

          // Center text with total
          Column(
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "${hasilKoreksi.size}",
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Soal",
              fontSize = 14.sp,
              color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          // Correct answers legend
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(16.dp)
                .background(Color(0xFF4CAF50), CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Benar ($correctAnswers)",
              style = MaterialTheme.typography.bodyMedium
            )
          }

          // Incorrect answers legend
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(16.dp)
                .background(Color(0xFFE91E63), CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Perlu Perbaikan ($incorrectAnswers)",
              style = MaterialTheme.typography.bodyMedium
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Strength and weakness analysis
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Text(
          text = "Analisis Kekuatan & Kelemahan",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Strengths section
        if (correctAnswers > 0) {
          Text(
            text = "🌟 Kekuatan",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF4CAF50)
          )

          Spacer(modifier = Modifier.height(8.dp))

          hasilKoreksi.filter { it.penilaian == "Benar" }.take(2).forEach { perSoal ->
            Row(
              modifier = Modifier.padding(vertical = 4.dp),
              verticalAlignment = Alignment.Top
            ) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier
                  .size(16.dp)
                  .padding(top = 4.dp)
              )

              Spacer(modifier = Modifier.width(8.dp))

              Text(
                text = "Soal ${perSoal.soal}: ${perSoal.alasan.split(".").firstOrNull() ?: perSoal.alasan}",
                style = MaterialTheme.typography.bodyMedium
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weaknesses section
        if (incorrectAnswers > 0) {
          Text(
            text = "📌 Area Perbaikan",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFE91E63)
          )

          Spacer(modifier = Modifier.height(8.dp))

          hasilKoreksi.filter { it.penilaian != "Benar" }.take(2).forEach { perSoal ->
            Row(
              modifier = Modifier.padding(vertical = 4.dp),
              verticalAlignment = Alignment.Top
            ) {
              Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = Color(0xFFE91E63),
                modifier = Modifier
                  .size(16.dp)
                  .padding(top = 4.dp)
              )

              Spacer(modifier = Modifier.width(8.dp))

              Text(
                text = "Soal ${perSoal.soal}: ${perSoal.alasan.split(".").firstOrNull() ?: perSoal.alasan}",
                style = MaterialTheme.typography.bodyMedium
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Rekomendasi perbaikan
    RecommendationsCard(hasilKoreksi = hasilKoreksi)

    Spacer(modifier = Modifier.height(16.dp))

    // Bar chart for score distribution
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Text(
          text = "Distribusi Nilai per Soal",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Simple horizontal bar chart
        hasilKoreksi.forEach { perSoal ->
          val scoreRatio = perSoal.skor.toFloat() / 100f

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Soal ${perSoal.soal}",
              style = MaterialTheme.typography.bodyMedium,
              modifier = Modifier.width(60.dp)
            )

            Box(
              modifier = Modifier
                .weight(1f)
                .height(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
              Box(
                modifier = Modifier
                  .fillMaxHeight()
                  .fillMaxWidth(scoreRatio)
                  .clip(RoundedCornerShape(12.dp))
                  .background(
                    when {
                      perSoal.skor >= 80 -> Color(0xFF4CAF50)
                      perSoal.skor >= 60 -> Color(0xFFFFC107)
                      else -> Color(0xFFE91E63)
                    }
                  )
              )
            }

            Text(
              text = "${perSoal.skor}",
              style = MaterialTheme.typography.bodyMedium,
              modifier = Modifier.width(40.dp),
              textAlign = TextAlign.End
            )
          }
        }
      }
    }
  }
}