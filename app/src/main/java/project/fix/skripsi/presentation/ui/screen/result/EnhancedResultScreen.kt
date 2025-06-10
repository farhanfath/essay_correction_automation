package project.fix.skripsi.presentation.ui.screen.result

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import project.fix.skripsi.domain.model.SiswaData
import project.fix.skripsi.presentation.ui.screen.result.components.ErrorView
import project.fix.skripsi.presentation.ui.screen.result.components.LoadingEvaluationAnimation
import project.fix.skripsi.presentation.ui.screen.result.components.ResultHeader
import project.fix.skripsi.presentation.ui.screen.result.components.StudentSelector
import project.fix.skripsi.presentation.ui.screen.result.tab.analisisjawaban.AnalisisJawabanTab
import project.fix.skripsi.presentation.ui.screen.result.tab.detailevaluasi.DetailEvaluasiTab
import project.fix.skripsi.presentation.utils.common.base.state.StateHandler
import project.fix.skripsi.presentation.utils.common.base.state.UiState
import project.fix.skripsi.presentation.viewmodel.EssayViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedResultScreen(
  navController: NavController,
  viewModel: EssayViewModel
) {
  val scrollState = rememberScrollState()
  var showContent by remember { mutableStateOf(false) }
  var selectedTabIndex by remember { mutableIntStateOf(0) }
  var selectedStudentIndex by remember { mutableIntStateOf(0) }
  val resultState by viewModel.result.collectAsState()

  // Animasi untuk entrance
  val contentAlpha by animateFloatAsState(
    targetValue = if (showContent) 1f else 0f,
    animationSpec = tween(durationMillis = 500)
  )

  // Animasi untuk skor
  val scoreProgress = remember { Animatable(0f) }

  LaunchedEffect(key1 = Unit) {
    delay(300) // Small delay for animation
    showContent = true
  }

  // Menyiapkan animasi skor saat hasil berhasil ditampilkan
  LaunchedEffect(key1 = resultState, selectedStudentIndex) {
    if (resultState is UiState.Success) {
      val hasil = (resultState as UiState.Success).data
      val currentStudent = hasil.resultData[selectedStudentIndex]
      scoreProgress.animateTo(
        targetValue = currentStudent.skorAkhir.toFloat() / 100f,
        animationSpec = tween(1500, easing = FastOutSlowInEasing)
      )
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = if (resultState is UiState.Success) {
              val hasil = (resultState as UiState.Success).data
              if (hasil.resultData.size == 1) "Hasil Evaluasi Essay"
              else "Hasil Evaluasi Essay (${hasil.resultData.size} Siswa)"
            } else "Hasil Evaluasi Essay"
          )
        },
        navigationIcon = {
          IconButton(onClick = {
            viewModel.resetState()
            navController.navigateUp()
          }) {
            Icon(
              imageVector = Icons.AutoMirrored.Default.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer,
          titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        actions = {
          IconButton(onClick = { /* Share functionality */ }) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Bagikan Hasil"
            )
          }
          IconButton(onClick = { /* PDF export */ }) {
            Icon(
              imageVector = Icons.Default.Download,
              contentDescription = "Unduh PDF"
            )
          }
        }
      )
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      StateHandler(
        state = resultState,
        onLoading = {
          LoadingEvaluationAnimation(modifier = Modifier.align(Alignment.Center))
        },
        onSuccess = { hasil ->
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(horizontal = 16.dp)
              .alpha(contentAlpha)
              .verticalScroll(rememberScrollState())
          ) {
            // Multi-student selector (if more than one student)
            if (hasil.resultData.size > 1) {
              StudentSelector(
                students = hasil.resultData,
                selectedIndex = selectedStudentIndex,
                onStudentSelected = {
                  selectedStudentIndex = it
                  // Reset tab to detail when switching students
                  selectedTabIndex = 0
                }
              )
              Spacer(modifier = Modifier.height(16.dp))
            }

            // Hasil section with animation for current student
            val currentStudent = hasil.resultData[selectedStudentIndex]
            ResultHeader(
              siswaData = currentStudent,
              scoreProgress = scoreProgress.value,
              showStudentName = hasil.resultData.size > 1
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tab navigation
            TabRow(
              selectedTabIndex = selectedTabIndex,
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
              contentColor = MaterialTheme.colorScheme.primary,
              indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                  modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                  height = 3.dp,
                  color = MaterialTheme.colorScheme.primary
                )
              }
            ) {
              Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text("Detail Evaluasi") }
              )
              Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text("Analisis Jawaban") }
              )
              if (hasil.resultData.size > 1) {
                Tab(
                  selected = selectedTabIndex == 2,
                  onClick = { selectedTabIndex = 2 },
                  text = { Text("Rangkuman Kelas") }
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tab content
            when (selectedTabIndex) {
              0 -> {
                DetailEvaluasiTab(
                  currentStudent.hasilKoreksi,
                  hasil.listAnswerKey,
                  hasil.evaluationType
                )
              }
              1 -> AnalisisJawabanTab(currentStudent.hasilKoreksi)
              2 -> if (hasil.resultData.size > 1) {
                AllSummaryTab(hasil.resultData)
              }
            }

            Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
          }

          // Floating Action Button
          ExtendedFloatingActionButton(
            onClick = { /* Action for sharing or discussing results */ },
            icon = { Icon(Icons.AutoMirrored.Default.Chat, contentDescription = null) },
            text = { Text("Diskusikan Hasil") },
            modifier = Modifier
              .align(Alignment.BottomEnd)
              .padding(16.dp)
              .alpha(contentAlpha),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
          )
        },
        onError = {
          ErrorView(
            errorMessage = it,
            onRetry = {
              navController.popBackStack()
            },
            modifier = Modifier.align(Alignment.Center)
          )
        }
      )
    }
  }
}

@Composable
fun StatItem(
  label: String,
  value: String,
  icon: ImageVector
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
      modifier = Modifier.size(20.dp)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = value,
      style = MaterialTheme.typography.titleMedium,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onPrimaryContainer
    )
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
    )
  }
}

@Composable
fun AllSummaryTab(students: List<SiswaData>) {
  Column(
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Class Statistics Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      )
    ) {
      Column(
        modifier = Modifier.padding(16.dp)
      ) {
        Text(
          text = "Statistik Kelas",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        val scores = students.map { it.skorAkhir }
        val average = scores.average()
        val highest = scores.maxOrNull() ?: 0.0
        val lowest = scores.minOrNull() ?: 0.0

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          StatCard("Rata-rata", average.roundToInt().toString(), Color(0xFF2196F3))
          StatCard("Tertinggi", highest.roundToInt().toString(), Color(0xFF4CAF50))
          StatCard("Terendah", lowest.roundToInt().toString(), Color(0xFFF44336))
        }
      }
    }

    // Student Rankings
    Card(
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier.padding(16.dp)
      ) {
        Text(
          text = "Peringkat Siswa",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        val sortedStudents = students.sortedByDescending { it.skorAkhir }

        sortedStudents.forEachIndexed { index, student ->
          StudentRankItem(
            rank = index + 1,
            student = student
          )
          if (index < sortedStudents.size - 1) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
          }
        }
      }
    }
  }
}

@Composable
fun StatCard(label: String, value: String, color: Color) {
  Card(
    colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = value,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = color
      )
      Text(
        text = label,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
      )
    }
  }
}

@Composable
fun StudentRankItem(rank: Int, student: SiswaData) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Rank badge
    Card(
      colors = CardDefaults.cardColors(
        containerColor = when (rank) {
          1 -> Color(0xFFFFD700) // Gold
          2 -> Color(0xFFC0C0C0) // Silver
          3 -> Color(0xFFCD7F32) // Bronze
          else -> MaterialTheme.colorScheme.secondary
        }
      ),
      modifier = Modifier.size(32.dp)
    ) {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
      ) {
        Text(
          text = rank.toString(),
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }
    }

    Spacer(modifier = Modifier.width(12.dp))

    // Student info
    Column(
      modifier = Modifier.weight(1f)
    ) {
      Text(
        text = student.nama,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Medium
      )
      Text(
        text = "${student.hasilKoreksi.size} soal dijawab",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
      )
    }

    // Score
    Text(
      text = "${student.skorAkhir.roundToInt()}",
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      color = when {
        student.skorAkhir >= 80 -> Color(0xFF4CAF50)
        student.skorAkhir >= 60 -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
      }
    )
  }
}