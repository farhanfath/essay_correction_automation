package project.fix.skripsi.presentation.ui.screen.result.tab.analisisjawaban

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.sp
import project.fix.skripsi.domain.model.PerSoal

@Composable
fun AnalisisJawabanTab(
  modifier: Modifier = Modifier,
  penilaian: List<PerSoal>
) {
  Column(modifier = modifier.fillMaxWidth()) {
    // Pie chart for correct vs incorrect answers
    val correctAnswers = penilaian.count { it.penilaian == "Benar" }
    val incorrectAnswers = penilaian.size - correctAnswers

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
            val correctRatio = correctAnswers.toFloat() / penilaian.size
            val incorrectRatio = incorrectAnswers.toFloat() / penilaian.size

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
              text = "${penilaian.size}",
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
  }
}