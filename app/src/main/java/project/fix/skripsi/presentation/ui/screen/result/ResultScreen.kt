package project.fix.skripsi.presentation.ui.screen.result

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.model.SavedScoreHistory
import project.fix.skripsi.domain.model.SiswaData
import project.fix.skripsi.presentation.ui.components.CustomTopHeader
import project.fix.skripsi.presentation.ui.components.CustomDetailTopBar
import project.fix.skripsi.presentation.ui.screen.result.components.EmptyResultView
import project.fix.skripsi.presentation.ui.screen.result.components.ErrorView
import project.fix.skripsi.presentation.ui.screen.result.components.LoadingEvaluationAnimation
import project.fix.skripsi.presentation.ui.screen.result.components.ResultHeader
import project.fix.skripsi.presentation.ui.screen.result.components.SaveDataButton
import project.fix.skripsi.presentation.ui.screen.result.components.StudentSelector
import project.fix.skripsi.presentation.ui.screen.result.state.ResultScreenState
import project.fix.skripsi.presentation.ui.screen.result.tab.allsummary.AllSummaryTab
import project.fix.skripsi.presentation.ui.screen.result.tab.analisisjawaban.AnalisisJawabanTab
import project.fix.skripsi.presentation.ui.screen.result.tab.detailevaluasi.DetailEvaluasiTab
import project.fix.skripsi.presentation.utils.common.base.state.StateHandler
import project.fix.skripsi.presentation.utils.common.base.state.UiState
import project.fix.skripsi.presentation.utils.common.extension.ActionUtils
import project.fix.skripsi.presentation.viewmodel.EssayViewModel
import project.fix.skripsi.presentation.viewmodel.SavedScoreHistoryViewModel

@Composable
fun ResultScreen(
  onBackClick: () -> Unit,
  essayViewModel: EssayViewModel? = null,
  savedScoreHistoryViewModel: SavedScoreHistoryViewModel,
  mode: ResultScreenState = ResultScreenState.FRESH_RESULT,
  historyId: Long? = null
) {
  val context = LocalContext.current

  val resultState by (essayViewModel?.result ?: flowOf(UiState.Idle)).collectAsState(UiState.Idle)

  val savedHistory by savedScoreHistoryViewModel.savedScoreHistory.collectAsState()
  val savedHistoryList by savedScoreHistoryViewModel.savedScoreHistoryList.collectAsState()
  val isLoading by savedScoreHistoryViewModel.isLoading.collectAsState()
  val errorMessage by savedScoreHistoryViewModel.errorMessage.collectAsState()

  var showContent by remember { mutableStateOf(false) }
  var selectedTabIndex by remember { mutableIntStateOf(0) }
  var selectedStudentIndex by remember { mutableIntStateOf(0) }
  var selectedEvaluationIndex by remember { mutableIntStateOf(0) }

  // Animasi untuk skor
  val scoreProgress = remember { Animatable(0f) }

  // Load data berdasarkan mode
  LaunchedEffect(mode, historyId) {
    when (mode) {
      ResultScreenState.HISTORY_DETAIL -> {
        if (historyId != null) {
          savedScoreHistoryViewModel.getSavedScoreHistoryById(historyId)
        }
      }
      ResultScreenState.FRESH_RESULT -> {}
    }
    delay(300)
    showContent = true
  }

  val currentData: List<SiswaData>? = when (mode) {
    ResultScreenState.FRESH_RESULT -> {
      if (resultState is UiState.Success) {
        val hasilKoreksi = (resultState as UiState.Success).data
        hasilKoreksi.resultData
      } else null
    }
    ResultScreenState.HISTORY_DETAIL -> {
      savedHistory?.let { history ->
        if (history.hasilKoreksi.isNotEmpty() && selectedEvaluationIndex < history.hasilKoreksi.size) {
          history.hasilKoreksi[selectedEvaluationIndex].resultData
        } else null
      }
    }
  }

  LaunchedEffect(selectedStudentIndex, currentData) {
    currentData?.let { data ->
      if (data.isNotEmpty() && selectedStudentIndex < data.size) {
        try {
          val studentData = data[selectedStudentIndex]
          scoreProgress.animateTo(
            targetValue = (studentData.skorAkhir.toFloat() / 100f).coerceIn(0f, 1f),
            animationSpec = tween(1500, easing = FastOutSlowInEasing)
          )
        } catch (e: Exception) {
          Log.e("ResultScreen", "Error animating score: ${e.message}")
        }
      }
    }
  }

  Scaffold(
    topBar = {
      CustomTopHeader(
        statusBarColor = MaterialTheme.colorScheme.primaryContainer
      ) {
        CustomDetailTopBar(
          onBackClick = onBackClick,
          title = when (mode) {
            ResultScreenState.FRESH_RESULT -> {
              if (resultState is UiState.Success) {
                val hasil = (resultState as UiState.Success).data
                if (hasil.resultData.size == 1) "Hasil Evaluasi Essay"
                else "Hasil Evaluasi Essay (${hasil.resultData.size} Siswa)"
              } else "Hasil Evaluasi Essay"
            }
            ResultScreenState.HISTORY_DETAIL -> {
              savedHistory?.title ?: "Detail Riwayat Penilaian"
            }
          }
        )
      }
    }
  ) { innerPadding ->
    when (mode) {
      ResultScreenState.FRESH_RESULT -> {
        HandleFreshResultState(
          innerPadding = innerPadding,
          resultState = resultState,
          selectedTabIndex = selectedTabIndex,
          selectedStudentIndex = selectedStudentIndex,
          scoreProgress = scoreProgress,
          savedHistoryList = savedHistoryList,
          isLoading = isLoading,
          onTabIndexChange = { selectedTabIndex = it },
          onStudentIndexChange = { selectedStudentIndex = it },
          onBackClick = onBackClick,
          onSaveNew = { title ->
            if (resultState is UiState.Success) {
              val dataHasil = (resultState as UiState.Success).data
              savedScoreHistoryViewModel.addSavedScoreHistory(title, listOf(dataHasil))
              ActionUtils.showToast(context, "Data Berhasil Disimpan")
            }
          },
          onUpdateExisting = { history, title ->
            if (resultState is UiState.Success) {
              val dataHasil = (resultState as UiState.Success).data
              savedScoreHistoryViewModel.mergeWithExistingData(history, title, listOf(dataHasil))
              ActionUtils.showToast(context, "Data Berhasil Ditambahkan")
            }
          }
        )
      }
      ResultScreenState.HISTORY_DETAIL -> {
        HandleHistoryDetailState(
          innerPadding = innerPadding,
          savedHistory = savedHistory,
          isLoading = isLoading,
          errorMessage = errorMessage,
          selectedTabIndex = selectedTabIndex,
          selectedStudentIndex = selectedStudentIndex,
          selectedEvaluationIndex = selectedEvaluationIndex,
          scoreProgress = scoreProgress,
          onTabIndexChange = { selectedTabIndex = it },
          onStudentIndexChange = { selectedStudentIndex = it },
          onEvaluationIndexChange = { selectedEvaluationIndex = it },
          onBackClick = onBackClick,
          onClearError = { savedScoreHistoryViewModel.clearError() }
        )
      }
    }
  }
}

@Composable
private fun HandleFreshResultState(
  innerPadding: PaddingValues,
  resultState: UiState<HasilKoreksi>,
  selectedTabIndex: Int,
  selectedStudentIndex: Int,
  scoreProgress: Animatable<Float, AnimationVector1D>,
  savedHistoryList: List<SavedScoreHistory>,
  isLoading: Boolean,
  onTabIndexChange: (Int) -> Unit,
  onStudentIndexChange: (Int) -> Unit,
  onBackClick: () -> Unit,
  onSaveNew: (String) -> Unit,
  onUpdateExisting: (SavedScoreHistory, String) -> Unit
) {
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
      if (hasil.resultData.isEmpty()) {
        Box(
          modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
          EmptyResultView(
            message = "Tidak ada data siswa yang berhasil diproses. Coba ambil foto ulang dengan kualitas yang lebih baik.",
            onRetry = onBackClick,
            modifier = Modifier.align(Alignment.Center)
          )
        }
        return@StateHandler
      }

      val currentStudentIndex = selectedStudentIndex.coerceIn(0, hasil.resultData.size - 1)

      ResultContent(
        hasilKoreksi = hasil,
        innerPadding = innerPadding,
        siswaDataList = hasil.resultData,
        selectedTabIndex = selectedTabIndex,
        selectedStudentIndex = currentStudentIndex,
        scoreProgress = scoreProgress,
        showSaveButton = true,
        savedHistoryList = savedHistoryList,
        isLoading = isLoading,
        onTabIndexChange = onTabIndexChange,
        onStudentIndexChange = onStudentIndexChange,
        onSaveNew = onSaveNew,
        onUpdateExisting = onUpdateExisting
      )
    },
    onError = { error ->
      Box(
        modifier = Modifier
          .padding(innerPadding)
          .fillMaxSize()
      ) {
        ErrorView(
          errorMessage = error,
          onRetry = onBackClick,
          modifier = Modifier.align(Alignment.Center)
        )
      }
    }
  )
}

@Composable
private fun HandleHistoryDetailState(
  innerPadding: PaddingValues,
  savedHistory: SavedScoreHistory?,
  isLoading: Boolean,
  errorMessage: String?,
  selectedTabIndex: Int,
  selectedStudentIndex: Int,
  selectedEvaluationIndex: Int,
  scoreProgress: Animatable<Float, AnimationVector1D>,
  onTabIndexChange: (Int) -> Unit,
  onStudentIndexChange: (Int) -> Unit,
  onEvaluationIndexChange: (Int) -> Unit,
  onBackClick: () -> Unit,
  onClearError: () -> Unit
) {
  when {
    isLoading -> {
      Box(
        modifier = Modifier
          .padding(innerPadding)
          .fillMaxSize()
      ) {
        LoadingEvaluationAnimation(modifier = Modifier.align(Alignment.Center))
      }
    }
    errorMessage != null -> {
      Box(
        modifier = Modifier
          .padding(innerPadding)
          .fillMaxSize()
      ) {
        ErrorView(
          errorMessage = errorMessage,
          onRetry = {
            onClearError()
            onBackClick()
          },
          modifier = Modifier.align(Alignment.Center)
        )
      }
    }
    savedHistory != null -> {
      if (savedHistory.hasilKoreksi.isEmpty()) {
        Box(
          modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
          EmptyResultView(
            message = "Tidak ada data hasil penilaian dalam riwayat ini.",
            onRetry = onBackClick,
            modifier = Modifier.align(Alignment.Center)
          )
        }
      } else {
        val currentEvaluationIndex = selectedEvaluationIndex.coerceIn(0, savedHistory.hasilKoreksi.size - 1)
        val currentEvaluation = savedHistory.hasilKoreksi[currentEvaluationIndex]
        val currentStudentIndex = selectedStudentIndex.coerceIn(0, currentEvaluation.resultData.size - 1)

        Column(
          modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
          if (savedHistory.hasilKoreksi.size > 1) {
            EvaluationSelector(
              modifier = Modifier.padding(horizontal = 16.dp),
              evaluations = savedHistory.hasilKoreksi,
              selectedIndex = currentEvaluationIndex,
              onEvaluationSelected = { newIndex ->
                val safeIndex = newIndex.coerceIn(0, savedHistory.hasilKoreksi.size - 1)
                onEvaluationIndexChange(safeIndex)
                onStudentIndexChange(0) // Reset student selection
                onTabIndexChange(0) // Reset tab selection
              }
            )
            Spacer(modifier = Modifier.height(16.dp))
          }

          // Rest of the content
          ResultContent(
            hasilKoreksi = currentEvaluation,
            innerPadding = PaddingValues(0.dp),
            siswaDataList = currentEvaluation.resultData,
            selectedTabIndex = selectedTabIndex,
            selectedStudentIndex = currentStudentIndex,
            scoreProgress = scoreProgress,
            showSaveButton = false,
            savedHistoryList = emptyList(),
            isLoading = false,
            onTabIndexChange = onTabIndexChange,
            onStudentIndexChange = onStudentIndexChange,
            onSaveNew = {},
            onUpdateExisting = { _, _ -> }
          )
        }
      }
    }
    else -> {
      Box(
        modifier = Modifier
          .padding(innerPadding)
          .fillMaxSize()
      ) {
        EmptyResultView(
          message = "Data tidak ditemukan.",
          onRetry = onBackClick,
          modifier = Modifier.align(Alignment.Center)
        )
      }
    }
  }
}

@Composable
private fun EvaluationSelector(
  modifier: Modifier = Modifier,
  evaluations: List<HasilKoreksi>,
  selectedIndex: Int,
  onEvaluationSelected: (Int) -> Unit
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.secondaryContainer
    )
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = "Pilih Evaluasi:",
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSecondaryContainer
      )
      Spacer(modifier = Modifier.height(8.dp))

      evaluations.forEachIndexed { index, evaluation ->
        FilterChip(
          onClick = { onEvaluationSelected(index) },
          label = {
            Text("${evaluation.evaluationType.name} (${evaluation.resultData.size} siswa)")
          },
          selected = selectedIndex == index,
          modifier = Modifier.padding(bottom = 4.dp)
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResultContent(
  hasilKoreksi: HasilKoreksi,
  innerPadding: PaddingValues,
  siswaDataList: List<SiswaData>,
  selectedTabIndex: Int,
  selectedStudentIndex: Int,
  scoreProgress: Animatable<Float, AnimationVector1D>,
  showSaveButton: Boolean,
  savedHistoryList: List<SavedScoreHistory>,
  isLoading: Boolean,
  onTabIndexChange: (Int) -> Unit,
  onStudentIndexChange: (Int) -> Unit,
  onSaveNew: (String) -> Unit,
  onUpdateExisting: (SavedScoreHistory, String) -> Unit
) {
  val currentStudent = siswaDataList[selectedStudentIndex]

  LazyColumn(
    modifier = Modifier
      .padding(innerPadding)
      .fillMaxSize()
  ) {
    if (siswaDataList.size > 1) {
      item {
        StudentSelector(
          modifier = Modifier.padding(horizontal = 16.dp),
          students = siswaDataList,
          selectedIndex = selectedStudentIndex,
          onStudentSelected = { newIndex ->
            val safeIndex = newIndex.coerceIn(0, siswaDataList.size - 1)
            onStudentIndexChange(safeIndex)
            onTabIndexChange(0)
          }
        )
        Spacer(modifier = Modifier.height(16.dp))
      }
    }

    // Result Header
    item {
      ResultHeader(
        modifier = Modifier.padding(horizontal = 16.dp),
        siswaData = currentStudent,
        scoreProgress = scoreProgress.value,
        showStudentName = siswaDataList.size > 1
      )
      Spacer(modifier = Modifier.height(24.dp))
    }

    // Save Data Button (hanya untuk fresh result)
    if (showSaveButton) {
      item {
        SaveDataButton(
          onSaveClick = onSaveNew,
          onUpdateClick = onUpdateExisting,
          existingData = savedHistoryList,
          isLoading = isLoading,
          modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
      }
    }

    // Tab Content
    item {
      Column {
        // Tab Row
        TabRow(
          selectedTabIndex = selectedTabIndex,
          modifier = Modifier.padding(horizontal = 16.dp),
          containerColor = MaterialTheme.colorScheme.surface,
          contentColor = MaterialTheme.colorScheme.onSurface
        ) {
          listOf("Detail Evaluasi", "Analisis Jawaban", "Ringkasan").forEachIndexed { index, title ->
            Tab(
              selected = selectedTabIndex == index,
              onClick = { onTabIndexChange(index) },
              text = {
                Text(
                  text = title,
                  style = MaterialTheme.typography.titleSmall
                )
              }
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tab Content
        when (selectedTabIndex) {
          0 -> DetailEvaluasiTab(
            hasilKoreksi = currentStudent.hasilKoreksi,
            bobotNilai = hasilKoreksi.listAnswerKey,
            tipeEvaluasi = hasilKoreksi.evaluationType,
            modifier = Modifier.padding(horizontal = 16.dp)
          )
          1 -> AnalisisJawabanTab(
            penilaian = currentStudent.hasilKoreksi,
            modifier = Modifier.padding(horizontal = 16.dp)
          )
          2 -> AllSummaryTab(
            students = siswaDataList,
            modifier = Modifier.padding(horizontal = 16.dp)
          )
        }
      }
    }
  }
}