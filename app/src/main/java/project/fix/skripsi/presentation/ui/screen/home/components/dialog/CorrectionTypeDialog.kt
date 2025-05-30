package project.fix.skripsi.presentation.ui.screen.home.components.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import project.fix.skripsi.domain.model.CorrectionType

@Composable
fun CorrectionTypeDialog(
  currentType: CorrectionType,
  onDismiss: () -> Unit,
  onTypeSelected: (CorrectionType) -> Unit
) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(
      dismissOnBackPress = true,
      dismissOnClickOutside = true
    )
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .wrapContentHeight(),
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.surface
    ) {
      Column(
        modifier = Modifier.padding(24.dp)
      ) {
        Text(
          text = "Tipe Koreksi",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "Pilih metode koreksi yang akan digunakan untuk mengevaluasi essay.",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // AI Correction Option
        CorrectionTypeOption(
          type = CorrectionType.AI,
          title = "Kecerdasan Buatan (AI)",
          description = "AI akan mengevaluasi essay berdasarkan pemahaman kontekstual tanpa kunci jawaban yang spesifik.",
          icon = Icons.Rounded.Psychology,
          isSelected = currentType == CorrectionType.AI,
          onClick = { onTypeSelected(CorrectionType.AI) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Answer Key Correction Option
        CorrectionTypeOption(
          type = CorrectionType.ANSWER_KEY,
          title = "Kunci Jawaban",
          description = "Evaluasi akan dilakukan dengan membandingkan jawaban dengan kunci jawaban yang telah dibuat.",
          icon = Icons.Rounded.Key,
          isSelected = currentType == CorrectionType.ANSWER_KEY,
          onClick = { onTypeSelected(CorrectionType.ANSWER_KEY) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Close button
        Button(
          onClick = onDismiss,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "Tutup",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
          )
        }
      }
    }
  }
}

@Composable
fun CorrectionTypeOption(
  type: CorrectionType,
  title: String,
  description: String,
  icon: ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  val backgroundColor = if (isSelected)
    MaterialTheme.colorScheme.primaryContainer
  else
    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)

  Surface(
    shape = RoundedCornerShape(12.dp),
    color = backgroundColor,
    border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(16.dp)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(32.dp)
      )

      Spacer(modifier = Modifier.width(16.dp))

      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = description,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      if (isSelected) {
        Icon(
          imageVector = Icons.Rounded.Check,
          contentDescription = "Selected",
          tint = MaterialTheme.colorScheme.primary
        )
      }
    }
  }
}