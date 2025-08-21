package project.fix.skripsi.presentation.ui.screen.result

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.TableView
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import project.fix.skripsi.presentation.ui.components.CustomDetailTopBar
import project.fix.skripsi.presentation.ui.components.CustomTopHeader
import project.fix.skripsi.presentation.ui.screen.result.components.BottomSheetTableScore
import project.fix.skripsi.presentation.ui.screen.result.components.EmptyResultView
import project.fix.skripsi.presentation.ui.screen.result.components.ErrorView
import project.fix.skripsi.presentation.ui.screen.result.components.LoadingEvaluationAnimation
import project.fix.skripsi.presentation.ui.screen.result.components.ResultHeader
import project.fix.skripsi.presentation.ui.screen.result.components.SaveDataButton
import project.fix.skripsi.presentation.ui.screen.result.components.StudentSelector
import project.fix.skripsi.presentation.ui.screen.result.tab.allsummary.AllSummaryTab
import project.fix.skripsi.presentation.ui.screen.result.tab.analisisjawaban.AnalisisJawabanTab
import project.fix.skripsi.presentation.ui.screen.result.tab.detailevaluasi.DetailEvaluasiTab
import project.fix.skripsi.presentation.utils.common.base.state.StateHandler
import project.fix.skripsi.presentation.utils.common.base.state.UiState
import project.fix.skripsi.presentation.utils.common.extension.ActionUtils
import project.fix.skripsi.presentation.viewmodel.EssayViewModel
import project.fix.skripsi.presentation.viewmodel.SavedScoreHistoryViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResultScreen(
  onBackClick: () -> Unit,
  essayViewModel: EssayViewModel,
  savedScoreHistoryViewModel: SavedScoreHistoryViewModel,
) {
  val context = LocalContext.current
  val resultState by essayViewModel.result.collectAsState()

  var showContent by remember { mutableStateOf(false) }
  var selectedTabIndex by remember { mutableIntStateOf(0) }
  var selectedStudentIndex by remember { mutableIntStateOf(0) }
  var showTableBottomSheet by remember { mutableStateOf(false) }

  // Animasi untuk skor
  val scoreProgress = remember { Animatable(0f) }

  LaunchedEffect(key1 = Unit) {
    delay(300)
    showContent = true
  }

  // Reset animasi skor saat student berubah
//  LaunchedEffect(key1 = resultState, selectedStudentIndex) {
//    if (resultState is UiState.Success) {
//      val hasil = (resultState as UiState.Success).data
//      val currentStudent = hasil.resultData[selectedStudentIndex]
//      scoreProgress.animateTo(
//        targetValue = currentStudent.skorAkhir.toFloat() / 100f,
//        animationSpec = tween(1500, easing = FastOutSlowInEasing)
//      )
//    }
//  }

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

        if (selectedStudentIndex >= hasil.resultData.size) {
          selectedStudentIndex = 0
        }

        val currentStudent = hasil.resultData.getOrNull(selectedStudentIndex)
          ?: hasil.resultData.first()

        if (currentStudent == null) {
          Box(
            modifier = Modifier
              .padding(innerPadding)
              .fillMaxSize()
          ) {
            EmptyResultView(
              message = "Data siswa tidak valid. Silakan coba lagi.",
              onRetry = { onBackClick() },
              modifier = Modifier.align(Alignment.Center)
            )
          }
          return@StateHandler
        }

        LaunchedEffect(key1 = selectedStudentIndex, key2 = hasil.resultData.size) {
          try {
            val studentData = hasil.resultData.getOrNull(selectedStudentIndex) ?: return@LaunchedEffect
            scoreProgress.animateTo(
              targetValue = (studentData.skorAkhir.toFloat() / 100f).coerceIn(0f, 1f),
              animationSpec = tween(1500, easing = FastOutSlowInEasing)
            )
          } catch (e: Exception) {
            Log.e("ResultScreen", "Error animating score: ${e.message}")
          }
        }

        LazyColumn(
          modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
          if (hasil.resultData.size > 1) {
            item {
              StudentSelector(
                modifier = Modifier.padding(horizontal = 16.dp),
                students = hasil.resultData,
                selectedIndex = selectedStudentIndex.coerceIn(0, hasil.resultData.size - 1),
                onStudentSelected = { newIndex ->
                  val safeIndex = newIndex.coerceIn(0, hasil.resultData.size - 1)
                  selectedStudentIndex = safeIndex
                  selectedTabIndex = 0
                }
              )
              Spacer(modifier = Modifier.height(16.dp))
            }
          }

          item {
            ResultHeader(
              modifier = Modifier.padding(horizontal = 16.dp),
              siswaData = currentStudent,
              scoreProgress = scoreProgress.value,
              showStudentName = hasil.resultData.size > 1
            )
            Spacer(modifier = Modifier.height(24.dp))
          }

          item {
            SaveDataButton(
              onSaveClick = { title ->
                if (resultState is UiState.Success) {
                  val dataHasil = (resultState as UiState.Success).data
                  savedScoreHistoryViewModel.addSavedScoreHistory(title, listOf(dataHasil))
                  ActionUtils.showToast(context, "Data Berhasil Disimpan")
                }
              },
              modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
          }

          stickyHeader {
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

          item {
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
                penilaian = currentStudent.hasilKoreksi
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

        if (showTableBottomSheet && hasil.resultData.isNotEmpty()) {
          BottomSheetTableScore(
            onDismiss = { showTableBottomSheet = false },
            dataTable = hasil
          )
        }
      },
      onError = { errorMessage ->
        val (title, description, advice) = essayViewModel.getDetailedErrorMessage(Exception(errorMessage))
        Box(
          modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
          ErrorView(
            modifier = Modifier.align(Alignment.Center),
            errorTitle = title,
            errorMessage = description,
            actionableAdvice = advice,
            onRetry = onBackClick
          )
        }
      }
    )
  }
}

@Composable
fun AnimatedFloatingActionButton(
  onClick: () -> Unit,
  isVisible: Boolean,
  scale: Float,
  translationY: Float,
  contentAlpha: Float,
  onToggleVisibility: () -> Unit
) {
  Box(
    modifier = Modifier
      .padding(16.dp)
      .fillMaxSize(),
    contentAlignment = Alignment.BottomEnd
  ) {
    // Main FAB
    ExtendedFloatingActionButton(
      onClick = onClick,
      modifier = Modifier
        .graphicsLayer {
          scaleX = scale
          scaleY = scale
          this.translationY = translationY
          alpha = contentAlpha * scale
        },
      icon = {
        Icon(
          Icons.Outlined.TableView,
          contentDescription = null,
          modifier = Modifier.scale(scale)
        )
      },
      text = {
        Text(
          "Rangkuman Nilai",
          modifier = Modifier.alpha(scale)
        )
      },
      containerColor = MaterialTheme.colorScheme.primary,
      contentColor = MaterialTheme.colorScheme.onPrimary
    )

    // Small toggle button yang muncul saat FAB tersembunyi
    AnimatedVisibility(
      visible = !isVisible && scale < 0.1f,
      enter = slideInVertically(
        initialOffsetY = { it },
        animationSpec = spring(
          dampingRatio = Spring.DampingRatioMediumBouncy,
          stiffness = Spring.StiffnessMedium
        )
      ) + fadeIn(animationSpec = tween(300)),
      exit = slideOutVertically(
        targetOffsetY = { it },
        animationSpec = spring(
          dampingRatio = Spring.DampingRatioNoBouncy,
          stiffness = Spring.StiffnessMedium
        )
      ) + fadeOut(animationSpec = tween(200)),
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .offset(y = (-100).dp) // Position above where FAB would be
    ) {
      SmallFloatingActionButton(
        onClick = onToggleVisibility,
        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
          .shadow(
            elevation = 6.dp,
            shape = CircleShape
          )
      ) {
        Icon(
          imageVector = Icons.Default.KeyboardArrowUp,
          contentDescription = "Tampilkan menu",
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}

@Composable
fun SubtleToggleButton(
  onToggleVisibility: () -> Unit,
  isVisible: Boolean
) {
  AnimatedVisibility(
    visible = !isVisible,
    enter = scaleIn(
      animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
      )
    ) + fadeIn(),
    exit = scaleOut(
      animationSpec = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessHigh
      )
    ) + fadeOut()
  ) {
    Surface(
      onClick = onToggleVisibility,
      shape = CircleShape,
      color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
      shadowElevation = 4.dp,
      modifier = Modifier
        .size(40.dp)
        .padding(4.dp)
    ) {
      Box(
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.ExpandLess,
          contentDescription = "Tampilkan menu",
          tint = MaterialTheme.colorScheme.onPrimary,
          modifier = Modifier.size(18.dp)
        )
      }
    }
  }
}

@Composable
fun PulsingToggleButton(
  onToggleVisibility: () -> Unit,
  isVisible: Boolean
) {
  val infiniteTransition = rememberInfiniteTransition()

  val pulse by infiniteTransition.animateFloat(
    initialValue = 0.8f,
    targetValue = 1.2f,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    )
  )

  AnimatedVisibility(
    visible = !isVisible,
    enter = scaleIn(
      animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy
      )
    ) + fadeIn(),
    exit = scaleOut() + fadeOut()
  ) {
    Surface(
      onClick = onToggleVisibility,
      shape = CircleShape,
      color = MaterialTheme.colorScheme.primary,
      shadowElevation = 6.dp,
      modifier = Modifier
        .size(48.dp)
        .scale(pulse) // Pulse effect
    ) {
      Box(
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.KeyboardArrowUp,
          contentDescription = "Tampilkan rangkuman nilai",
          tint = MaterialTheme.colorScheme.onPrimary,
          modifier = Modifier.size(24.dp)
        )
      }
    }
  }
}