package project.fix.skripsi.presentation.ui.screen.result.state.handler

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import project.fix.skripsi.domain.model.SavedScoreHistory
import project.fix.skripsi.presentation.ui.screen.result.components.EmptyResultView
import project.fix.skripsi.presentation.ui.screen.result.components.ErrorView
import project.fix.skripsi.presentation.ui.screen.result.components.EvaluationSelector
import project.fix.skripsi.presentation.ui.screen.result.components.ResultContent

@Composable
fun HandleHistoryDetailState(
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
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
                                onStudentIndexChange(0)
                                onTabIndexChange(0)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

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