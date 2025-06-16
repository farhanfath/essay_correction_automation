package project.fix.skripsi.presentation.ui.screen.result

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import project.fix.skripsi.presentation.ui.components.CustomDetailTopBar
import project.fix.skripsi.presentation.ui.components.CustomTopHeader
import project.fix.skripsi.presentation.ui.screen.result.components.BottomSheetTableScore
import project.fix.skripsi.presentation.ui.screen.result.components.ErrorView
import project.fix.skripsi.presentation.ui.screen.result.components.FloatingSaveButton
import project.fix.skripsi.presentation.ui.screen.result.components.LoadingEvaluationAnimation
import project.fix.skripsi.presentation.ui.screen.result.components.ResultHeader
import project.fix.skripsi.presentation.ui.screen.result.components.SaveDataButton
import project.fix.skripsi.presentation.ui.screen.result.components.StudentSelector
import project.fix.skripsi.presentation.ui.screen.result.tab.allsummary.AllSummaryTab
import project.fix.skripsi.presentation.ui.screen.result.tab.analisisjawaban.AnalisisJawabanTab
import project.fix.skripsi.presentation.ui.screen.result.tab.detailevaluasi.DetailEvaluasiTab
import project.fix.skripsi.presentation.utils.common.base.state.StateHandler
import project.fix.skripsi.presentation.utils.common.base.state.UiState
import project.fix.skripsi.presentation.viewmodel.EssayViewModel
import project.fix.skripsi.presentation.viewmodel.SavedScoreHistoryViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResultScreen(
  onBackClick: () -> Unit,
  essayViewModel: EssayViewModel,
  savedScoreHistoryViewModel: SavedScoreHistoryViewModel,
) {
  val uiState by essayViewModel.uiState.collectAsState()
  val resultState = uiState.resultState

  var showContent by remember { mutableStateOf(false) }
  var selectedTabIndex by remember { mutableIntStateOf(0) }
  var selectedStudentIndex by remember { mutableIntStateOf(0) }
  var showTableBottomSheet by remember { mutableStateOf(false) }

  // FAB visibility states
  var isFabVisible by remember { mutableStateOf(true) }
  var isScrolling by remember { mutableStateOf(false) }
  val listState = rememberLazyListState()

  // Timer untuk auto-hide FAB
  var lastScrollTime by remember { mutableLongStateOf(0L) }

  // Animasi untuk entrance
  val contentAlpha by animateFloatAsState(
    targetValue = if (showContent) 1f else 0f,
    animationSpec = tween(durationMillis = 500)
  )

  // Animasi untuk FAB
  val fabScale by animateFloatAsState(
    targetValue = if (isFabVisible) 1f else 0f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    )
  )

  val fabTranslationY by animateFloatAsState(
    targetValue = if (isFabVisible) 0f else 200f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioNoBouncy,
      stiffness = Spring.StiffnessMedium
    )
  )

  // Animasi untuk skor
  val scoreProgress = remember { Animatable(0f) }

  LaunchedEffect(key1 = Unit) {
    delay(300)
    showContent = true
  }

  // Monitor scroll state untuk auto-hide FAB
  LaunchedEffect(listState) {
    var previousScrollOffset = 0
    snapshotFlow { listState.firstVisibleItemScrollOffset }
      .collect { currentScrollOffset ->
        val currentTime = System.currentTimeMillis()
        lastScrollTime = currentTime

        // Detect scroll direction
        val isScrollingDown = currentScrollOffset > previousScrollOffset
        val isScrollingUp = currentScrollOffset < previousScrollOffset

        if (isScrollingDown && isFabVisible) {
          isScrolling = true
          isFabVisible = false
        } else if (isScrollingUp && !isFabVisible) {
          isScrolling = false
          isFabVisible = true
        }

        previousScrollOffset = currentScrollOffset
      }
  }

  // Auto-show FAB setelah scroll berhenti selama 3 detik
  LaunchedEffect(lastScrollTime) {
    delay(3000) // 3 detik setelah scroll terakhir
    if (System.currentTimeMillis() - lastScrollTime >= 3000) {
      if (!isFabVisible && !isScrolling) {
        isFabVisible = true
      }
    }
  }

  // Reset animasi skor saat student berubah
  LaunchedEffect(key1 = resultState, selectedStudentIndex) {
    if (resultState is UiState.Success) {
      val hasil = resultState.data
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
            val hasil = resultState.data
            if (hasil.resultData.size == 1) "Hasil Evaluasi Essay"
            else "Hasil Evaluasi Essay (${hasil.resultData.size} Siswa)"
          } else "Hasil Evaluasi Essay"
        )
      }
    },
    floatingActionButton = {
      if (resultState is UiState.Success) {
        val hasil = resultState.data

        // Jika multi-student, tampilkan kedua FAB
        if (hasil.resultData.size > 1) {
          Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.End
          ) {
            // Save button
            FloatingSaveButton(
              onSaveClick = { title  ->
                savedScoreHistoryViewModel.addSavedScoreHistory(title, listOf(hasil))
              },
              isVisible = isFabVisible,
              modifier = Modifier.padding(start = 16.dp)
            )

            // Table button
            AnimatedFloatingActionButton(
              onClick = { showTableBottomSheet = true },
              isVisible = isFabVisible,
              scale = fabScale,
              translationY = fabTranslationY,
              contentAlpha = contentAlpha,
              onToggleVisibility = {
                isFabVisible = !isFabVisible
              }
            )
          }
        } else {
          // Jika single student, hanya tampilkan save button
          FloatingSaveButton(
            onSaveClick = { title ->
              savedScoreHistoryViewModel.addSavedScoreHistory(title, listOf(hasil))
            },
            isVisible = isFabVisible,
            modifier = Modifier.padding(start = 16.dp)
          )
        }
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
          state = listState, // Connect list state untuk scroll monitoring
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
                  val dataHasil = resultState.data
                  savedScoreHistoryViewModel.addSavedScoreHistory(title, listOf(dataHasil))
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

// Alternative version dengan mini FAB yang lebih subtle
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

// Enhanced version dengan pulse animation untuk menarik perhatian
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