package project.fix.skripsi.presentation.ui.screen.result

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.TableView
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import kotlinx.coroutines.delay
import project.fix.skripsi.domain.model.SiswaData
import project.fix.skripsi.presentation.ui.components.CustomDetailTopBar
import project.fix.skripsi.presentation.ui.components.CustomTopHeader
import project.fix.skripsi.presentation.ui.screen.result.components.BottomSheetTableScore
import project.fix.skripsi.presentation.ui.screen.result.components.ErrorView
import project.fix.skripsi.presentation.ui.screen.result.components.LoadingEvaluationAnimation
import project.fix.skripsi.presentation.ui.screen.result.components.ResultHeader
import project.fix.skripsi.presentation.ui.screen.result.components.StudentSelector
import project.fix.skripsi.presentation.ui.screen.result.tab.allsummary.AllSummaryTab
import project.fix.skripsi.presentation.ui.screen.result.tab.analisisjawaban.AnalisisJawabanTab
import project.fix.skripsi.presentation.ui.screen.result.tab.detailevaluasi.DetailEvaluasiTab
import project.fix.skripsi.presentation.utils.common.base.state.StateHandler
import project.fix.skripsi.presentation.utils.common.base.state.UiState
import project.fix.skripsi.presentation.viewmodel.EssayViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EnhancedResultScreen(
  onBackClick: () -> Unit,
  viewModel: EssayViewModel
) {
  val resultState by viewModel.result.collectAsState()

  var showContent by remember { mutableStateOf(false) }
  var selectedTabIndex by remember { mutableIntStateOf(0) }
  var selectedStudentIndex by remember { mutableIntStateOf(0) }
  var showTableBottomSheet by remember { mutableStateOf(false) }

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
      CustomTopHeader(
        statusBarColor = MaterialTheme.colorScheme.primaryContainer
      ) {
        CustomDetailTopBar(
          onBackClick = {
            onBackClick()
          },
          title = if (resultState is UiState.Success) {
              val hasil = (resultState as UiState.Success).data
              if (hasil.resultData.size == 1) "Hasil Evaluasi Essay"
              else "Hasil Evaluasi Essay (${hasil.resultData.size} Siswa)"
            } else "Hasil Evaluasi Essay"
        )
      }
    },
    floatingActionButton = {
      if (resultState is UiState.Success && (resultState as UiState.Success).data.resultData.size > 1) {
        ExtendedFloatingActionButton(
          onClick = { showTableBottomSheet = true },
          icon = { Icon(Icons.Outlined.TableView, contentDescription = null) },
          text = { Text("Rangkuman Nilai") },
          modifier = Modifier
            .padding(16.dp)
            .alpha(contentAlpha),
          containerColor = MaterialTheme.colorScheme.primary,
          contentColor = MaterialTheme.colorScheme.onPrimary
        )
      }
    }
  ) { innerPadding ->
    StateHandler(
      state = resultState,
      onLoading = {
        Box(
          modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
          LoadingEvaluationAnimation(modifier = Modifier.align(Alignment.Center))
        }
      },
      onSuccess = { hasil ->
        val currentStudent = hasil.resultData[selectedStudentIndex]

        LazyColumn(
          modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
            // Multi-student selector (if more than one student)
            if (hasil.resultData.size > 1) {
              item {
                StudentSelector(
                  modifier = Modifier.padding(horizontal = 16.dp),
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
            }

            item {
              // Hasil section with animation for current student
              ResultHeader(
                modifier = Modifier.padding(horizontal = 16.dp),
                siswaData = currentStudent,
                scoreProgress = scoreProgress.value,
                showStudentName = hasil.resultData.size > 1
              )

              Spacer(modifier = Modifier.height(24.dp))
            }

            // tab navigation
            stickyHeader {
              // Tab navigation
              TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.primary,
                indicator = { tabPositions ->
                  Box(
                    Modifier
                      .tabIndicatorOffset(tabPositions[selectedTabIndex])
                      .height(5.dp)
                      .padding(horizontal = 16.dp)
                      .width(tabPositions[selectedTabIndex].width)
                      .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(10.dp)
                      )
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
            }

            // tab components
            item {
              // Tab content
            when (selectedTabIndex) {
              0 -> {
                DetailEvaluasiTab(
                  modifier = Modifier.padding(horizontal = 16.dp),
                  hasilKoreksi = currentStudent.hasilKoreksi,
                  bobotNilai = hasil.listAnswerKey,
                  tipeEvaluasi = hasil.evaluationType
                )
              }
              1 -> AnalisisJawabanTab(
                modifier = Modifier.padding(horizontal = 16.dp),
                penilaian = currentStudent.hasilKoreksi,
                onShowTableBottomSheet = { showTableBottomSheet = true }
              )
              2 -> if (hasil.resultData.size > 1) {
                AllSummaryTab(
                  modifier = Modifier.padding(horizontal = 16.dp),
                  students = hasil.resultData
                )
              }
            }
            }
          }

        if (showTableBottomSheet) {
          BottomSheetTableScore(
            onDismiss = { showTableBottomSheet = false },
            dataTable = hasil
          )
        }
      },
      onError = {
        Box(
          modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
          ErrorView(
            errorMessage = it,
            onRetry = {
              onBackClick()
            },
            modifier = Modifier.align(Alignment.Center)
          )
        }
      }
    )
  }
}