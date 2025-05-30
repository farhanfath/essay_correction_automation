package project.fix.skripsi.presentation.ui.screen.home.components.dialog

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import project.fix.skripsi.domain.model.AnswerKeyItem
import project.fix.skripsi.domain.model.CorrectionType

@Composable
fun EssayInfoSummary(
  imageUris: List<Uri>,
  answerKeyItems: List<AnswerKeyItem>,
  correctionType: CorrectionType,
  onAnswerKeyClick: () -> Unit,
  onCorrectionTypeClick: () -> Unit,
  onCloseDialog: () -> Unit
) {
  Surface(
    modifier = Modifier
      .padding(top = 12.dp)
      .fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
    tonalElevation = 8.dp,
    shadowElevation = 6.dp
  ) {
    Box {
      IconButton(
        onClick = { onCloseDialog() },
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(4.dp)
          .size(28.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Close,
          contentDescription = "Tutup",
          tint = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.size(16.dp)
        )
      }

      // Konten EssayInfoSummary yang ditingkatkan
      Column(
        modifier = Modifier
          .padding(16.dp)
          .padding(top = 4.dp)
      ) {
        Text(
          text = "Informasi Evaluasi Essay",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Kunci Jawaban
        if (correctionType == CorrectionType.ANSWER_KEY) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .clickable { onAnswerKeyClick() }
              .padding(vertical = 10.dp, horizontal = 12.dp)
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Rounded.List,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Kunci Jawaban",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
              )
              Text(
                text = if (answerKeyItems.isEmpty()) "Belum diatur" else "${answerKeyItems.size} item kunci jawaban",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (answerKeyItems.isEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
              )
            }
            Icon(
              imageVector = Icons.Default.ChevronRight,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
          }

          HorizontalDivider(
            modifier = Modifier.padding(vertical = 4.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
          )
        }

        // Tipe Koreksi
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onCorrectionTypeClick() }
            .padding(vertical = 10.dp, horizontal = 12.dp)
        ) {
          Icon(
            imageVector = if (correctionType == CorrectionType.AI)
              Icons.Rounded.Psychology else Icons.Rounded.Key,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Tipe Koreksi",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
              text = when (correctionType) {
                CorrectionType.AI -> "Kecerdasan Buatan (AI)"
                CorrectionType.ANSWER_KEY -> "Menggunakan Kunci Jawaban"
                else -> "Belum dipilih"
              },
              style = MaterialTheme.typography.bodyLarge,
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
          Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
          )
        }

        // Status kesiapan
        val isReady = imageUris.isNotEmpty() &&
          (correctionType == CorrectionType.AI || answerKeyItems.isNotEmpty())

        Spacer(modifier = Modifier.height(16.dp))

        Card(
          colors = CardDefaults.cardColors(
            containerColor = if (isReady)
              MaterialTheme.colorScheme.primaryContainer
            else
              MaterialTheme.colorScheme.errorContainer
          ),
          shape = RoundedCornerShape(8.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp)
          ) {
            Icon(
              imageVector = if (isReady)
                Icons.Default.CheckCircle
              else
                Icons.Default.Warning,
              contentDescription = null,
              tint = if (isReady)
                MaterialTheme.colorScheme.onPrimaryContainer
              else
                MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = if (isReady)
                "Siap untuk mengevaluasi essay"
              else
                "Beberapa pengaturan diperlukan",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Medium,
              color = if (isReady)
                MaterialTheme.colorScheme.onPrimaryContainer
              else
                MaterialTheme.colorScheme.onErrorContainer
            )
          }
        }
      }
    }
  }
}