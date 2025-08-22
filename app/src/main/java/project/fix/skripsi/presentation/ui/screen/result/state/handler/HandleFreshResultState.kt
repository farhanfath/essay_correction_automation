package project.fix.skripsi.presentation.ui.screen.result.state.handler

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.model.SavedScoreHistory
import project.fix.skripsi.presentation.ui.screen.result.components.EmptyResultView
import project.fix.skripsi.presentation.ui.screen.result.components.ErrorView
import project.fix.skripsi.presentation.ui.screen.result.components.LoadingEvaluationAnimation
import project.fix.skripsi.presentation.ui.screen.result.components.ResultContent
import project.fix.skripsi.presentation.utils.common.base.state.StateHandler
import project.fix.skripsi.presentation.utils.common.base.state.UiState

@Composable
fun HandleFreshResultState(
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