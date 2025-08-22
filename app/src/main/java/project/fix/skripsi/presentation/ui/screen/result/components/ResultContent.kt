package project.fix.skripsi.presentation.ui.screen.result.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.model.SavedScoreHistory
import project.fix.skripsi.domain.model.SiswaData
import project.fix.skripsi.presentation.ui.screen.result.tab.allsummary.AllSummaryTab
import project.fix.skripsi.presentation.ui.screen.result.tab.analisisjawaban.AnalisisJawabanTab
import project.fix.skripsi.presentation.ui.screen.result.tab.detailevaluasi.DetailEvaluasiTab

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ResultContent(
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

        stickyHeader {
            TabRow(
                selectedTabIndex = selectedTabIndex,
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
                                style = MaterialTheme.typography.titleSmall.copy(
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Tab Content
        item {
            Column {
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