package project.fix.skripsi.presentation.ui.screen.history

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.model.SavedScoreHistory
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HasilKoreksiHistoryScreen(
  hasilList: List<SavedScoreHistory>,
  onItemClick: (SavedScoreHistory) -> Unit = {},
  onDeleteItem: (SavedScoreHistory) -> Unit = {}
) {
  var expandedItems by remember { mutableStateOf(setOf<Long>()) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
          )
        )
      )
  ) {
    // Header
    Surface(
      modifier = Modifier.fillMaxWidth(),
      color = MaterialTheme.colorScheme.primaryContainer,
      shadowElevation = 4.dp
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.History,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onPrimaryContainer,
          modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
          text = "Riwayat Penilaian",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.weight(1f))
        Badge(
          containerColor = MaterialTheme.colorScheme.primary
        ) {
          Text(
            text = "${hasilList.size}",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.labelMedium
          )
        }
      }
    }

    if (hasilList.isEmpty()) {
      EmptyStateView()
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(hasilList, key = { it.id }) { hasil ->
          HasilKoreksiCard(
            hasil = hasil,
            isExpanded = expandedItems.contains(hasil.id),
            onExpandToggle = {
              expandedItems = if (expandedItems.contains(hasil.id)) {
                expandedItems - hasil.id
              } else {
                expandedItems + hasil.id
              }
            },
            onClick = { onItemClick(hasil) },
            onDelete = { onDeleteItem(hasil) }
          )
        }
      }
    }
  }
}

@Composable
fun HasilKoreksiCard(
  hasil: SavedScoreHistory,
  isExpanded: Boolean,
  onExpandToggle: () -> Unit,
  onClick: () -> Unit,
  onDelete: () -> Unit
) {
  var showDeleteDialog by remember { mutableStateOf(false) }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .shadow(
        elevation = if (isExpanded) 8.dp else 4.dp,
        shape = RoundedCornerShape(16.dp)
      )
      .clip(RoundedCornerShape(16.dp))
      .clickable { onClick() },
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    )
  ) {
    Column {
      // Main content
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Type icon and indicator
        Box(
          modifier = Modifier
            .size(48.dp)
            .background(
              color = getTypeColor(hasil.hasilKoreksi.map { it.evaluationType }).copy(alpha = 0.2f),
              shape = RoundedCornerShape(12.dp)
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = getTypeIcon(hasil.hasilKoreksi.map { it.evaluationType }),
            contentDescription = null,
            tint = getTypeColor(hasil.hasilKoreksi.map { it.evaluationType }),
            modifier = Modifier.size(24.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Content info
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = getTypeDisplayName(hasil.hasilKoreksi.map { it.evaluationType }),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(4.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.People,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${hasil.hasilKoreksi.map { it.resultData }.flatten().size} siswa",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
              imageVector = Icons.Default.AccessTime,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = formatDate(hasil.createdAt),
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Average score
          val avgScore = hasil.resultData.map { it.skorAkhir }.average()
          ScoreIndicator(score = avgScore)
        }

        // Actions
        Column {
          IconButton(
            onClick = onExpandToggle,
            modifier = Modifier.size(40.dp)
          ) {
            Icon(
              imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
              contentDescription = if (isExpanded) "Tutup detail" else "Buka detail",
              tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          IconButton(
            onClick = { showDeleteDialog = true },
            modifier = Modifier.size(40.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Delete,
              contentDescription = "Hapus",
              tint = MaterialTheme.colorScheme.error
            )
          }
        }
      }

      // Expanded content
      AnimatedVisibility(
        visible = isExpanded,
        enter = expandVertically(
          animationSpec = tween(300),
          expandFrom = Alignment.Top
        ) + fadeIn(animationSpec = tween(300)),
        exit = shrinkVertically(
          animationSpec = tween(300),
          shrinkTowards = Alignment.Top
        ) + fadeOut(animationSpec = tween(300))
      ) {
        ExpandedContent(hasil = hasil)
      }
    }
  }

  // Delete confirmation dialog
  if (showDeleteDialog) {
    AlertDialog(
      onDismissRequest = { showDeleteDialog = false },
      title = { Text("Hapus Riwayat?") },
      text = { Text("Apakah Anda yakin ingin menghapus riwayat penilaian ini?") },
      confirmButton = {
        TextButton(
          onClick = {
            onDelete()
            showDeleteDialog = false
          }
        ) {
          Text("Hapus", color = MaterialTheme.colorScheme.error)
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

@Composable
fun ExpandedContent(hasil: HasilKoreksi) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
      .padding(16.dp)
  ) {
    Text(
      text = "Detail Siswa",
      style = MaterialTheme.typography.titleSmall,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(8.dp))

    hasil.resultData.take(5).forEach { siswa ->
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = siswa.nama,
          style = MaterialTheme.typography.bodyMedium,
          modifier = Modifier.weight(1f),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        ScoreChip(score = siswa.skorAkhir)
      }
    }

    if (hasil.resultData.size > 5) {
      Text(
        text = "... dan ${hasil.resultData.size - 5} siswa lainnya",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 8.dp)
      )
    }
  }
}

@Composable
fun ScoreIndicator(score: Double) {
  val color = when {
    score >= 80 -> Color(0xFF4CAF50)
    score >= 60 -> Color(0xFFFF9800)
    else -> Color(0xFFf44336)
  }

  Row(verticalAlignment = Alignment.CenterVertically) {
    Text(
      text = "Rata-rata: ",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Surface(
      shape = RoundedCornerShape(12.dp),
      color = color.copy(alpha = 0.2f)
    ) {
      Text(
        text = "${score.toInt()}%",
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = color,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
      )
    }
  }
}

@Composable
fun ScoreChip(score: Double) {
  val color = when {
    score >= 80 -> Color(0xFF4CAF50)
    score >= 60 -> Color(0xFFFF9800)
    else -> Color(0xFFf44336)
  }

  Surface(
    shape = RoundedCornerShape(16.dp),
    color = color.copy(alpha = 0.2f)
  ) {
    Text(
      text = "${score.toInt()}",
      style = MaterialTheme.typography.labelMedium,
      fontWeight = FontWeight.SemiBold,
      color = color,
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
    )
  }
}

@Composable
fun EmptyStateView() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Icon(
      imageVector = Icons.Default.HistoryEdu,
      contentDescription = null,
      modifier = Modifier.size(80.dp),
      tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Belum Ada Riwayat",
      style = MaterialTheme.typography.headlineSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "Riwayat penilaian akan muncul di sini setelah Anda melakukan koreksi",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
  }
}

// Helper functions
fun getTypeIcon(type: CorrectionType): ImageVector {
  return when (type) {
    CorrectionType.MULTIPLE_CHOICE -> Icons.Default.RadioButtonChecked
    CorrectionType.ESSAY -> Icons.Default.Edit
    CorrectionType.MIXED -> Icons.Default.Assignment
  }
}

fun getTypeColor(type: CorrectionType): Color {
  return when (type) {
    CorrectionType.MULTIPLE_CHOICE -> Color(0xFF2196F3)
    CorrectionType.ESSAY -> Color(0xFF9C27B0)
    CorrectionType.MIXED -> Color(0xFF607D8B)
  }
}

fun getTypeDisplayName(type: CorrectionType): String {
  return when (type) {
    CorrectionType.MULTIPLE_CHOICE -> "Pilihan Ganda"
    CorrectionType.ESSAY -> "Essay"
    CorrectionType.MIXED -> "Campuran"
  }
}

fun formatDate(timestamp: Long): String {
  val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
  return sdf.format(Date(timestamp))
}

// Usage example
@Composable
fun PreviewHasilKoreksiHistory() {
  MaterialTheme {
    val sampleData = listOf(
      HasilKoreksi(
        id = 1,
        evaluationType = CorrectionType.MULTIPLE_CHOICE,
        resultData = listOf(
          SiswaData("Ahmad Rizki", 85.0, emptyList()),
          SiswaData("Siti Nurhaliza", 92.0, emptyList()),
          SiswaData("Budi Santoso", 78.0, emptyList())
        ),
        listAnswerKey = emptyList(),
        createdAt = System.currentTimeMillis() - 86400000
      ),
      HasilKoreksi(
        id = 2,
        evaluationType = CorrectionType.ESSAY,
        resultData = listOf(
          SiswaData("Dewi Sartika", 88.0, emptyList()),
          SiswaData("Andi Wijaya", 76.0, emptyList())
        ),
        listAnswerKey = emptyList(),
        createdAt = System.currentTimeMillis() - 172800000
      )
    )

    HasilKoreksiHistoryScreen(
      hasilList = sampleData,
      onItemClick = { /* Handle click */ },
      onDeleteItem = { /* Handle delete */ }
    )
  }
}