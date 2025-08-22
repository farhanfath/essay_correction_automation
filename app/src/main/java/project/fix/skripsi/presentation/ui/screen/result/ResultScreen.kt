package project.fix.skripsi.presentation.ui.screen.result

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import project.fix.skripsi.domain.model.SiswaData
import project.fix.skripsi.presentation.ui.components.CustomDetailTopBar
import project.fix.skripsi.presentation.ui.components.CustomTopHeader
import project.fix.skripsi.presentation.ui.screen.result.state.ResultScreenState
import project.fix.skripsi.presentation.ui.screen.result.state.handler.HandleFreshResultState
import project.fix.skripsi.presentation.ui.screen.result.state.handler.HandleHistoryDetailState
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