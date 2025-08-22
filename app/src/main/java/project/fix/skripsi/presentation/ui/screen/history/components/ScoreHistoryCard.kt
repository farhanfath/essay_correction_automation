package project.fix.skripsi.presentation.ui.screen.history.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import project.fix.skripsi.domain.model.SavedScoreHistory
import project.fix.skripsi.domain.model.constants.CorrectionType
import project.fix.skripsi.presentation.ui.screen.history.formatDate

@Composable
fun ScoreHistoryCard(
  history: SavedScoreHistory,
  onClick: () -> Unit,
  onDelete: () -> Unit
) {
  var expanded by remember { mutableStateOf(false) }
  var showDeleteDialog by remember { mutableStateOf(false) }

  val animatedElevation by animateDpAsState(
    targetValue = if (expanded) 12.dp else 4.dp,
    animationSpec = tween(300)
  )

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .clickable { onClick() },
    elevation = CardDefaults.cardElevation(defaultElevation = animatedElevation),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    )
  ) {
    Column(
      modifier = Modifier.padding(20.dp)
    ) {
      // Header Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = history.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = formatDate(history.createdAt),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
          )

          // Evaluation Type Badge
//          if (history.hasilKoreksi.isNotEmpty()) {
//            Spacer(modifier = Modifier.height(8.dp))
//            EvaluationTypeBadge(history.hasilKoreksi.first().evaluationType)
//          }
        }

        Row {
          IconButton(
            onClick = { expanded = !expanded }
          ) {
            Icon(
              imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
              contentDescription = if (expanded) "Tutup" else "Buka",
              tint = MaterialTheme.colorScheme.primary
            )
          }

          IconButton(
            onClick = { showDeleteDialog = true }
          ) {
            Icon(
              imageVector = Icons.Default.Delete,
              contentDescription = "Hapus",
              tint = MaterialTheme.colorScheme.error
            )
          }
        }
      }

      // Stats Overview
      if (history.hasilKoreksi.isNotEmpty()) {
        Spacer(modifier = Modifier.height(16.dp))
        ScoreStatsRow(history)
      }

      // Expanded Content
      AnimatedVisibility(
        visible = expanded,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
      ) {
        Column {
          Spacer(modifier = Modifier.height(16.dp))
          HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
          Spacer(modifier = Modifier.height(16.dp))

          if (history.hasilKoreksi.isNotEmpty()) {
            StudentResultsList(history)
          }
        }
      }
    }
  }

  // Delete Confirmation Dialog
  if (showDeleteDialog) {
    AlertDialog(
      onDismissRequest = { showDeleteDialog = false },
      title = { Text("Hapus Riwayat") },
      text = { Text("Apakah Anda yakin ingin menghapus riwayat \"${history.title}\"?") },
      confirmButton = {
        TextButton(
          onClick = {
            onDelete()
            showDeleteDialog = false
          },
          colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.error
          )
        ) {
          Text("Hapus")
        }
      },
      dismissButton = {
        TextButton(onClick = { showDeleteDialog = false }) {
          Text("Batal")
        }
      }
    )
  }
}