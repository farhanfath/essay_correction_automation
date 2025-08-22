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
import androidx.compose.ui.text.style.TextAlign
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
import project.fix.skripsi.presentation.ui.screen.result.components.EvaluationSelector
import project.fix.skripsi.presentation.ui.screen.result.components.LoadingEvaluationAnimation
import project.fix.skripsi.presentation.ui.screen.result.components.ResultContent
import project.fix.skripsi.presentation.ui.screen.result.components.ResultHeader
import project.fix.skripsi.presentation.ui.screen.result.components.SaveDataButton
import project.fix.skripsi.presentation.ui.screen.result.components.StudentSelector
import project.fix.skripsi.presentation.ui.screen.result.state.ResultScreenState
import project.fix.skripsi.presentation.ui.screen.result.state.handler.HandleFreshResultState
import project.fix.skripsi.presentation.ui.screen.result.state.handler.HandleHistoryDetailState
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