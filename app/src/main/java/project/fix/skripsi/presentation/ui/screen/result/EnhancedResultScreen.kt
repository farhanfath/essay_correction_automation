package project.fix.skripsi.presentation.ui.screen.result

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import project.fix.skripsi.domain.model.PerSoal
import project.fix.skripsi.presentation.ui.screen.result.components.ErrorView
import project.fix.skripsi.presentation.ui.screen.result.components.LoadingEvaluationAnimation
import project.fix.skripsi.presentation.ui.screen.result.components.ResultHeader
import project.fix.skripsi.presentation.ui.screen.result.tab.analisisjawaban.AnalisisJawabanTab
import project.fix.skripsi.presentation.ui.screen.result.tab.detailevaluasi.DetailEvaluasiTab
import project.fix.skripsi.presentation.utils.common.base.state.StateHandler
import project.fix.skripsi.presentation.utils.common.base.state.UiState
import project.fix.skripsi.presentation.viewmodel.EssayViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedResultScreen(
  navController: NavController,
  viewModel: EssayViewModel
) {
  val scrollState = rememberScrollState()
  var showContent by remember { mutableStateOf(false) }
  var selectedTabIndex by remember { mutableIntStateOf(0) }
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
  LaunchedEffect(key1 = resultState) {
    if (resultState is UiState.Success) {
      val hasil = (resultState as UiState.Success).data
      scoreProgress.animateTo(
        targetValue = hasil.skorAkhir.toFloat() / 100f,
        animationSpec = tween(1500, easing = FastOutSlowInEasing)
      )
    }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Hasil Evaluasi Essay") },
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
            // Hasil section with animation
            ResultHeader(
              hasil = hasil,
              scoreProgress = scoreProgress.value
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tab content
            when (selectedTabIndex) {
              0 -> DetailEvaluasiTab(hasil.hasilKoreksi)
              1 -> AnalisisJawabanTab(hasil.hasilKoreksi)
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