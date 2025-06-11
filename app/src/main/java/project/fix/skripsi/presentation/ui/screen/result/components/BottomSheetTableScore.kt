package project.fix.skripsi.presentation.ui.screen.result.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.model.SiswaData
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetTableScore(
  onDismiss: () -> Unit,
  dataTable: HasilKoreksi
) {
  val bottomSheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true
  )

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = bottomSheetState,
    modifier = Modifier.fillMaxHeight(0.95f),
    containerColor = MaterialTheme.colorScheme.surface,
    contentColor = MaterialTheme.colorScheme.onSurface,
    dragHandle = {
      Surface(
        modifier = Modifier.padding(vertical = 12.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
        shape = RoundedCornerShape(16.dp)
      ) {
        Box(
          modifier = Modifier
            .size(width = 40.dp, height = 4.dp)
        )
      }
    },
    windowInsets = WindowInsets(0)
  ) {
    HasilKoreksiTable(
      data = dataTable,
      onDismiss = onDismiss,
      modifier = Modifier.padding(16.dp)
    )
  }
}

@Composable
fun HasilKoreksiTable(
  data: HasilKoreksi,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedStudent by remember { mutableStateOf<SiswaData?>(null) }
  var showDetailDialog by remember { mutableStateOf(false) }
  var sortBy by remember { mutableStateOf(SortOption.NAME) }
  var isAscending by remember { mutableStateOf(true) }
  var searchQuery by remember { mutableStateOf("") }

  // Sort and filter data
  val filteredAndSortedData = remember(data.resultData, sortBy, isAscending, searchQuery) {
    var filtered = data.resultData

    // Filter by search query
    if (searchQuery.isNotBlank()) {
      filtered = filtered.filter {
        it.nama.contains(searchQuery, ignoreCase = true)
      }
    }

    // Sort data
    when (sortBy) {
      SortOption.NAME -> {
        if (isAscending) filtered.sortedBy { it.nama }
        else filtered.sortedByDescending { it.nama }
      }
      SortOption.SCORE -> {
        if (isAscending) filtered.sortedBy { it.skorAkhir }
        else filtered.sortedByDescending { it.skorAkhir }
      }
      SortOption.CORRECT_ANSWERS -> {
        if (isAscending) filtered.sortedBy { siswa ->
          siswa.hasilKoreksi.count { it.penilaian.lowercase() == "benar" }
        }
        else filtered.sortedByDescending { siswa ->
          siswa.hasilKoreksi.count { it.penilaian.lowercase() == "benar" }
        }
      }
    }
  }

  Column(
    modifier = modifier.fillMaxSize()
  ) {
    // Header with close button and title
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Rangkuman Hasil Koreksi",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "${data.resultData.size} siswa",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      IconButton(
        onClick = onDismiss,
        modifier = Modifier
          .size(40.dp)
          .background(
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f),
            CircleShape
          )
      ) {
        Icon(
          imageVector = Icons.Default.Close,
          contentDescription = "Tutup",
          tint = MaterialTheme.colorScheme.error
        )
      }
    }

    if (data.resultData.isEmpty()) {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(
            imageVector = Icons.Default.TableChart,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
          )
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "Tidak ada data hasil koreksi",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
          )
        }
      }
      return
    }

    // Search and Controls
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
      )
    ) {
      Column(
        modifier = Modifier.padding(16.dp)
      ) {
        // Search bar
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          modifier = Modifier.fillMaxWidth(),
          placeholder = { Text("Cari nama siswa...") },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = null
            )
          },
          trailingIcon = {
            if (searchQuery.isNotBlank()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(
                  imageVector = Icons.Default.Clear,
                  contentDescription = "Hapus pencarian"
                )
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Sort controls
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Urutkan berdasarkan : ",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
          )

          LazyRow (
            verticalAlignment = Alignment.CenterVertically
          ) {
            items(SortOption.entries) { option ->
              FilterChip(
                onClick = {
                  if (sortBy == option) {
                    isAscending = !isAscending
                  } else {
                    sortBy = option
                    isAscending = true
                  }
                },
                label = {
                  Row(
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = option.label,
                      fontSize = 12.sp
                    )
                    if (sortBy == option) {
                      Spacer(modifier = Modifier.width(4.dp))
                      Icon(
                        imageVector = if (isAscending) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                      )
                    }
                  }
                },
                selected = sortBy == option,
                modifier = Modifier.padding(horizontal = 2.dp)
              )
            }
          }
        }
      }
    }

    // Statistics Cards
    LazyRow(
      modifier = Modifier.padding(bottom = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      item {
        StatCard(
          title = "Rata-rata",
          value = String.format(Locale("id","ID"), "%.1f",
            data.resultData.map { it.skorAkhir }.average()),
          icon = Icons.AutoMirrored.Default.TrendingUp,
          color = MaterialTheme.colorScheme.primary
        )
      }
      item {
        val maxScore = data.resultData.maxOfOrNull { it.skorAkhir } ?: 0f
        StatCard(
          title = "Tertinggi",
          value = String.format(Locale("id","ID"), "%.1f", maxScore),
          icon = Icons.Default.EmojiEvents,
          color = Color(0xFF4CAF50)
        )
      }
      item {
        val minScore = data.resultData.minOfOrNull { it.skorAkhir } ?: 0f
        StatCard(
          title = "Terendah",
          value = String.format(Locale("id","ID"), "%.1f", minScore),
          icon = Icons.AutoMirrored.Default.TrendingDown,
          color = Color(0xFFE91E63)
        )
      }
    }

    // Get max number of questions
    val maxQuestions = data.resultData.maxOfOrNull { it.hasilKoreksi.size } ?: 0

    // Table
    Card(
      modifier = Modifier.fillMaxSize(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .horizontalScroll(rememberScrollState())
      ) {
        // Header
        ImprovedTableHeader(maxQuestions = maxQuestions)

        // Scrollable table
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
          itemsIndexed(filteredAndSortedData) { index, siswa ->
            ImprovedTableRow(
              siswa = siswa,
              maxQuestions = maxQuestions,
              index = index,
              onClick = {
                selectedStudent = siswa
                showDetailDialog = true
              }
            )
          }
        }
      }
    }
  }

  // Detail Dialog
  if (showDetailDialog && selectedStudent != null) {
    StudentDetailDialog(
      student = selectedStudent!!,
      onDismiss = {
        showDetailDialog = false
        selectedStudent = null
      }
    )
  }
}

@Composable
fun StatCard(
  title: String,
  value: String,
  icon: ImageVector,
  color: Color
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = color.copy(alpha = 0.1f)
    ),
    modifier = Modifier.width(120.dp)
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = color,
        modifier = Modifier.size(24.dp)
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = color
      )
      Text(
        text = title,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun ImprovedTableHeader(
  maxQuestions: Int,
) {
  Row(
    modifier = Modifier
      .background(
        MaterialTheme.colorScheme.primaryContainer,
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
      )
      .padding(vertical = 4.dp)
  ) {
    // Nama column
    ImprovedTableCell(
      text = "Nama\nSiswa",
      isHeader = true,
      width = 140.dp
    )

    // Dynamic columns for each question
    repeat(maxQuestions) { index ->
      val questionNumber = index + 1

      // Jawaban column
      ImprovedTableCell(
        text = "Jawaban\n(Soal $questionNumber)",
        isHeader = true,
        width = 150.dp
      )

      // Penilaian column
      ImprovedTableCell(
        text = "Penilaian\n(Soal $questionNumber)",
        isHeader = true,
        width = 100.dp
      )

      // Skor column
      ImprovedTableCell(
        text = "Skor\n(Soal $questionNumber)",
        isHeader = true,
        width = 80.dp
      )

      // Alasan column
      ImprovedTableCell(
        text = "Alasan\n(Soal $questionNumber)",
        isHeader = true,
        width = 200.dp
      )
    }

    // Summary columns
    ImprovedTableCell(
      text = "Jawaban\nBenar",
      isHeader = true,
      width = 80.dp
    )

    ImprovedTableCell(
      text = "Jawaban\nSalah",
      isHeader = true,
      width = 80.dp
    )

    ImprovedTableCell(
      text = "Persentase\nKebenaran",
      isHeader = true,
      width = 100.dp
    )

    // Skor Akhir column
    ImprovedTableCell(
      text = "Skor\nAkhir",
      isHeader = true,
      width = 90.dp
    )

    // Status column
    ImprovedTableCell(
      text = "Status",
      isHeader = true,
      width = 100.dp
    )
  }
}

@Composable
fun ImprovedTableRow(
  siswa: SiswaData,
  maxQuestions: Int,
  index: Int,
  onClick: () -> Unit
) {
  val correctAnswers = siswa.hasilKoreksi.count { it.penilaian.lowercase() == "benar" }
  val totalQuestions = siswa.hasilKoreksi.size
  val percentage = if (totalQuestions > 0) (correctAnswers * 100f / totalQuestions) else 0f

  val backgroundColor = if (index % 2 == 0) {
    MaterialTheme.colorScheme.surface
  } else {
    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
  }

  Row(
    modifier = Modifier
      .background(backgroundColor)
      .clickable { onClick() }
      .padding(vertical = 2.dp)
  ) {
    // Nama column
    ImprovedTableCell(
      text = siswa.nama,
      isHeader = false,
      width = 140.dp
    )

    // Dynamic columns for each question
    repeat(maxQuestions) { questionIndex ->
      val perSoal = siswa.hasilKoreksi.getOrNull(questionIndex)

      if (perSoal != null) {
        // Jawaban column
        ImprovedTableCell(
          text = perSoal.jawaban,
          isHeader = false,
          width = 150.dp
        )

        // Penilaian column
        ImprovedTableCell(
          text = perSoal.penilaian,
          isHeader = false,
          width = 100.dp,
          backgroundColor = when (perSoal.penilaian.lowercase()) {
            "benar" -> Color(0xFF4CAF50).copy(alpha = 0.15f)
            "salah" -> Color(0xFFE91E63).copy(alpha = 0.15f)
            else -> Color.Transparent
          },
          textColor = when (perSoal.penilaian.lowercase()) {
            "benar" -> Color(0xFF2E7D32)
            "salah" -> Color(0xFFC2185B)
            else -> null
          }
        )

        // Skor column
        ImprovedTableCell(
          text = perSoal.skor.toString(),
          isHeader = false,
          width = 80.dp,
          backgroundColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
        )

        // Alasan column
        ImprovedTableCell(
          text = if (perSoal.alasan.length > 50) {
            perSoal.alasan.take(47) + "..."
          } else {
            perSoal.alasan
          },
          isHeader = false,
          width = 200.dp
        )
      } else {
        // Empty cells if question doesn't exist for this student
        repeat(4) { cellIndex ->
          ImprovedTableCell(
            text = "-",
            isHeader = false,
            width = when (cellIndex) {
              0 -> 150.dp // Jawaban
              1 -> 100.dp // Penilaian
              2 -> 80.dp  // Skor
              3 -> 200.dp // Alasan
              else -> 80.dp
            },
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
          )
        }
      }
    }

    // Summary columns
    // Correct answers
    ImprovedTableCell(
      text = correctAnswers.toString(),
      isHeader = false,
      width = 80.dp,
      backgroundColor = Color(0xFF4CAF50).copy(alpha = 0.1f),
      textColor = Color(0xFF2E7D32)
    )

    // Incorrect answers
    ImprovedTableCell(
      text = (totalQuestions - correctAnswers).toString(),
      isHeader = false,
      width = 80.dp,
      backgroundColor = Color(0xFFE91E63).copy(alpha = 0.1f),
      textColor = Color(0xFFC2185B)
    )

    // Percentage
    ImprovedTableCell(
      text = "${percentage.toInt()}%",
      isHeader = false,
      width = 100.dp,
      backgroundColor = when {
        percentage >= 80 -> Color(0xFF4CAF50).copy(alpha = 0.15f)
        percentage >= 60 -> Color(0xFFFFC107).copy(alpha = 0.15f)
        else -> Color(0xFFE91E63).copy(alpha = 0.15f)
      },
      textColor = when {
        percentage >= 80 -> Color(0xFF2E7D32)
        percentage >= 60 -> Color(0xFFF57F17)
        else -> Color(0xFFC2185B)
      }
    )

    // Skor Akhir column
    ImprovedTableCell(
      text = String.format(Locale("id","ID"),"%.1f", siswa.skorAkhir),
      isHeader = false,
      width = 90.dp,
      backgroundColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
    )

    // Status
    val status = when {
      siswa.skorAkhir >= 80 -> "Lulus" to Color(0xFF4CAF50)
      siswa.skorAkhir >= 60 -> "Cukup" to Color(0xFFFFC107)
      else -> "Perlu Perbaikan" to Color(0xFFE91E63)
    }

    ImprovedTableCell(
      text = status.first,
      isHeader = false,
      width = 100.dp,
      backgroundColor = status.second.copy(alpha = 0.1f),
      textColor = when {
        siswa.skorAkhir >= 80 -> Color(0xFF2E7D32)
        siswa.skorAkhir >= 60 -> Color(0xFFF57F17)
        else -> Color(0xFFC2185B)
      }
    )
  }
}

@Composable
fun ImprovedTableCell(
  text: String,
  isHeader: Boolean,
  width: Dp,
  backgroundColor: Color = Color.Transparent,
  textColor: Color? = null
) {
  Box(
    modifier = Modifier
      .width(width)
      .height(if (isHeader) 70.dp else 60.dp)
      .background(backgroundColor)
      .border(
        width = 0.5.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
      )
      .padding(8.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      style = if (isHeader) {
        MaterialTheme.typography.bodyMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
      } else {
        MaterialTheme.typography.bodyMedium.copy(
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium
        )
      },
      textAlign = TextAlign.Center,
      color = textColor ?: if (isHeader) {
        MaterialTheme.colorScheme.onPrimaryContainer
      } else {
        MaterialTheme.colorScheme.onSurface
      },
      maxLines = if (isHeader) 2 else 1,
      overflow = TextOverflow.Ellipsis
    )
  }
}

@Composable
fun StudentDetailDialog(
  student: SiswaData,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Detail ${student.nama}",
          style = MaterialTheme.typography.headlineSmall
        )
      }
    },
    text = {
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        item {
          Card(
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            )
          ) {
            Column(
              modifier = Modifier.padding(16.dp)
            ) {
              Text(
                text = "Skor Akhir: ${String.format(Locale("id","ID"), "%.1f", student.skorAkhir)}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "Total Soal: ${student.hasilKoreksi.size}",
                style = MaterialTheme.typography.bodyMedium
              )
            }
          }
        }

        itemsIndexed(student.hasilKoreksi) { index, perSoal ->
          Card(
            colors = CardDefaults.cardColors(
              containerColor = if (perSoal.penilaian.lowercase() == "benar") {
                Color(0xFF4CAF50).copy(alpha = 0.1f)
              } else {
                Color(0xFFE91E63).copy(alpha = 0.1f)
              }
            )
          ) {
            Column(
              modifier = Modifier.padding(12.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "Soal ${index + 1}",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold
                )
                Surface(
                  color = if (perSoal.penilaian.lowercase() == "benar") {
                    Color(0xFF4CAF50)
                  } else {
                    Color(0xFFE91E63)
                  },
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Text(
                    text = perSoal.penilaian,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }

              Spacer(modifier = Modifier.height(8.dp))

              Text(
                text = "Jawaban: ${perSoal.jawaban}",
                style = MaterialTheme.typography.bodyMedium
              )

              Text(
                text = "Skor: ${perSoal.skor}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
              )

              if (perSoal.alasan.isNotBlank()) {
                Text(
                  text = "Alasan: ${perSoal.alasan}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("Tutup")
      }
    }
  )
}

enum class SortOption(val label: String) {
  NAME("Nama"),
  SCORE("Skor"),
  CORRECT_ANSWERS("Benar")
}