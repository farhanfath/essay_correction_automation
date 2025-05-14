package project.fix.skripsi.presentation.screen

// Kotlin Standard Library
import kotlin.math.cos
import kotlin.math.sin

// Jetpack Compose Basics
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Compose Animation
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbsUpDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf

// Coroutines
import kotlinx.coroutines.delay

// ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.model.PerSoal
import project.fix.skripsi.domain.utils.ResultResponse
import project.fix.skripsi.presentation.utils.HandlerResponseCompose
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
    if (resultState is ResultResponse.Success) {
      val hasil = (resultState as ResultResponse.Success<HasilKoreksi>).data
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
      HandlerResponseCompose(
        response = resultState,
        onLoading = {
          LoadingEvaluationAnimation(modifier = Modifier.align(Alignment.Center))
        },
        onError = {
          EnhancedErrorView(
            errorMessage = it.message,
            onRetry = {
              navController.popBackStack()
            },
            modifier = Modifier.align(Alignment.Center)
          )
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
            ImprovedResultHeader(
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
                TabRowDefaults.Indicator(
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
        }
      )
    }
  }
}

@Composable
fun ImprovedResultHeader(
  hasil: HasilKoreksi,
  scoreProgress: Float
) {
  // Header animation
  val scale by animateFloatAsState(
    targetValue = 1f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    )
  )

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .scale(scale),
    shape = RoundedCornerShape(24.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
  ) {
    Column(
      modifier = Modifier
        .padding(24.dp)
        .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Avatar icon
      Box(
        modifier = Modifier
          .size(80.dp)
          .clip(CircleShape)
          .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(40.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = hasil.nama,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onPrimaryContainer
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "Hasil Evaluasi Essay",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
      )

      Spacer(modifier = Modifier.height(24.dp))

      // Animated circular progress for score
      Box(
        modifier = Modifier.size(140.dp),
        contentAlignment = Alignment.Center
      ) {
        // Backdrop circle
        val color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        Canvas(modifier = Modifier.size(140.dp)) {
          drawArc(
            color = color,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = 12f, cap = StrokeCap.Round)
          )
        }

        // Progress arc
        Canvas(modifier = Modifier.size(140.dp)) {
          drawArc(
            color = when {
              hasil.skorAkhir >= 80 -> Color(0xFF4CAF50) // Green for high score
              hasil.skorAkhir >= 60 -> Color(0xFFFFC107) // Yellow for medium score
              else -> Color(0xFFE91E63) // Pink/Red for low score
            },
            startAngle = -90f,
            sweepAngle = 360f * scoreProgress,
            useCenter = false,
            style = Stroke(width = 12f, cap = StrokeCap.Round)
          )
        }

        // Center text
        Column(
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "${(scoreProgress * 100).toInt()}",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
          Text(
            text = "Nilai",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Score interpretation
      val scoreInterpretation = when {
        hasil.skorAkhir >= 90 -> "Luar Biasa"
        hasil.skorAkhir >= 80 -> "Sangat Baik"
        hasil.skorAkhir >= 70 -> "Baik"
        hasil.skorAkhir >= 60 -> "Cukup"
        else -> "Perlu Perbaikan"
      }

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
          .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = when {
            hasil.skorAkhir >= 80 -> Icons.Default.ThumbUp
            hasil.skorAkhir >= 60 -> Icons.Default.ThumbsUpDown
            else -> Icons.Default.Warning
          },
          contentDescription = null,
          tint = when {
            hasil.skorAkhir >= 80 -> Color(0xFF4CAF50)
            hasil.skorAkhir >= 60 -> Color(0xFFFFC107)
            else -> Color(0xFFE91E63)
          },
          modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
          text = scoreInterpretation,
          style = MaterialTheme.typography.bodyLarge,
          fontWeight = FontWeight.Medium,
          color = MaterialTheme.colorScheme.onPrimaryContainer
        )
      }
    }
  }
}

@Composable
fun DetailEvaluasiTab(hasilKoreksi: List<PerSoal>) {
  Column(modifier = Modifier.fillMaxWidth()) {
    hasilKoreksi.forEachIndexed { index, perSoal ->
      key(perSoal.soal) {
        AnimatedVisibility(
          visible = true,
          enter = fadeIn() + expandVertically()
        ) {
          ImprovedResultItemCard(
            perSoal = perSoal,
            nomorSoal = index + 1
          )
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}

@Composable
fun AnalisisJawabanTab(hasilKoreksi: List<PerSoal>) {
  Column(modifier = Modifier.fillMaxWidth()) {
    // Pie chart for correct vs incorrect answers
    val correctAnswers = hasilKoreksi.count { it.penilaian == "Benar" }
    val incorrectAnswers = hasilKoreksi.size - correctAnswers

    StatisticsSummaryCard(hasilKoreksi = hasilKoreksi)

    Spacer(modifier = Modifier.height(16.dp))

    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "Distribusi Jawaban",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Simple pie chart with Canvas
        Box(
          modifier = Modifier
            .size(200.dp)
            .padding(8.dp),
          contentAlignment = Alignment.Center
        ) {
          Canvas(modifier = Modifier.size(200.dp)) {
            val correctRatio = correctAnswers.toFloat() / hasilKoreksi.size
            val incorrectRatio = incorrectAnswers.toFloat() / hasilKoreksi.size

            // Correct answers slice (green)
            drawArc(
              color = Color(0xFF4CAF50),
              startAngle = 0f,
              sweepAngle = 360f * correctRatio,
              useCenter = true
            )

            // Incorrect answers slice (red)
            drawArc(
              color = Color(0xFFE91E63),
              startAngle = 360f * correctRatio,
              sweepAngle = 360f * incorrectRatio,
              useCenter = true
            )
          }

          // Center text with total
          Column(
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "${hasilKoreksi.size}",
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Soal",
              fontSize = 14.sp,
              color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          // Correct answers legend
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(16.dp)
                .background(Color(0xFF4CAF50), CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Benar ($correctAnswers)",
              style = MaterialTheme.typography.bodyMedium
            )
          }

          // Incorrect answers legend
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(16.dp)
                .background(Color(0xFFE91E63), CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Perlu Perbaikan ($incorrectAnswers)",
              style = MaterialTheme.typography.bodyMedium
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Strength and weakness analysis
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Text(
          text = "Analisis Kekuatan & Kelemahan",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Strengths section
        if (correctAnswers > 0) {
          Text(
            text = "🌟 Kekuatan",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF4CAF50)
          )

          Spacer(modifier = Modifier.height(8.dp))

          hasilKoreksi.filter { it.penilaian == "Benar" }.take(2).forEach { perSoal ->
            Row(
              modifier = Modifier.padding(vertical = 4.dp),
              verticalAlignment = Alignment.Top
            ) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier
                  .size(16.dp)
                  .padding(top = 4.dp)
              )

              Spacer(modifier = Modifier.width(8.dp))

              Text(
                text = "Soal ${perSoal.soal}: ${perSoal.alasan.split(".").firstOrNull() ?: perSoal.alasan}",
                style = MaterialTheme.typography.bodyMedium
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weaknesses section
        if (incorrectAnswers > 0) {
          Text(
            text = "📌 Area Perbaikan",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFFE91E63)
          )

          Spacer(modifier = Modifier.height(8.dp))

          hasilKoreksi.filter { it.penilaian != "Benar" }.take(2).forEach { perSoal ->
            Row(
              modifier = Modifier.padding(vertical = 4.dp),
              verticalAlignment = Alignment.Top
            ) {
              Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = Color(0xFFE91E63),
                modifier = Modifier
                  .size(16.dp)
                  .padding(top = 4.dp)
              )

              Spacer(modifier = Modifier.width(8.dp))

              Text(
                text = "Soal ${perSoal.soal}: ${perSoal.alasan.split(".").firstOrNull() ?: perSoal.alasan}",
                style = MaterialTheme.typography.bodyMedium
              )
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Rekomendasi perbaikan
    RecommendationsCard(hasilKoreksi = hasilKoreksi)

    Spacer(modifier = Modifier.height(16.dp))

    // Bar chart for score distribution
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Text(
          text = "Distribusi Nilai per Soal",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Simple horizontal bar chart
        hasilKoreksi.forEach { perSoal ->
          val scoreRatio = perSoal.skor.toFloat() / 100f

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Soal ${perSoal.soal}",
              style = MaterialTheme.typography.bodyMedium,
              modifier = Modifier.width(60.dp)
            )

            Box(
              modifier = Modifier
                .weight(1f)
                .height(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
              Box(
                modifier = Modifier
                  .fillMaxHeight()
                  .fillMaxWidth(scoreRatio)
                  .clip(RoundedCornerShape(12.dp))
                  .background(
                    when {
                      perSoal.skor >= 80 -> Color(0xFF4CAF50)
                      perSoal.skor >= 60 -> Color(0xFFFFC107)
                      else -> Color(0xFFE91E63)
                    }
                  )
              )
            }

            Text(
              text = "${perSoal.skor}",
              style = MaterialTheme.typography.bodyMedium,
              modifier = Modifier.width(40.dp),
              textAlign = TextAlign.End
            )
          }
        }
      }
    }
  }
}

@Composable
fun ImprovedResultItemCard(
  perSoal: PerSoal,
  nomorSoal: Int
) {
  var expanded by remember { mutableStateOf(false) }

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    onClick = { expanded = !expanded }
  ) {
    Column(
      modifier = Modifier.padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Nomor soal dengan badge
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(
                when {
                  perSoal.skor >= 80 -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                  perSoal.skor >= 60 -> Color(0xFFFFC107).copy(alpha = 0.2f)
                  else -> Color(0xFFE91E63).copy(alpha = 0.2f)
                }
              ),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "$nomorSoal",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = when {
                perSoal.skor >= 80 -> Color(0xFF4CAF50)
                perSoal.skor >= 60 -> Color(0xFFFFC107)
                else -> Color(0xFFE91E63)
              }
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Box(
            modifier = Modifier.width(200.dp)
          ) {
            Text(
              modifier = Modifier.basicMarquee(),
              text = "Soal ${perSoal.soal}",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        // Score badge
        val sample = 100
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(
              when {
                perSoal.skor >= 80 -> Color(0xFF4CAF50)
                perSoal.skor >= 60 -> Color(0xFFFFC107)
                else -> Color(0xFFE91E63)
              }
            ),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "${perSoal.skor}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Status row
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(
            when {
              perSoal.penilaian == "Benar" -> Color(0xFF4CAF50).copy(alpha = 0.1f)
              else -> Color(0xFFE91E63).copy(alpha = 0.1f)
            }
          )
          .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = if (perSoal.penilaian == "Benar") Icons.Default.Check else Icons.Default.Close,
          contentDescription = null,
          tint = if (perSoal.penilaian == "Benar") Color(0xFF4CAF50) else Color(0xFFE91E63)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
          text = if (perSoal.penilaian == "Benar") "Jawaban Benar" else "Jawaban Perlu Diperbaiki",
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Medium,
          color = if (perSoal.penilaian == "Benar") Color(0xFF4CAF50) else Color(0xFFE91E63)
        )
      }

      // Expanded content with animation
      AnimatedVisibility(
        visible = expanded,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
      ) {
        Column(
          modifier = Modifier.padding(top = 16.dp)
        ) {
          Divider()

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "Feedback Evaluasi",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            verticalAlignment = Alignment.Top
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Default.Comment,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
              text = perSoal.alasan,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
          ) {
            Button(
              onClick = { /* Tindakan untuk melihat detail jawaban */ },
              colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
              )
            ) {
              Icon(
                imageVector = Icons.Default.Visibility,
                contentDescription = null
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text("Lihat Jawaban")
            }
          }
        }
      }

      // Collapse/expand indicator
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
          contentDescription = if (expanded) "Collapse" else "Expand",
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(24.dp)
        )
      }
    }
  }
}

@Composable
fun EnhancedErrorView(
  errorMessage: String,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    // Error animation
//    val composition by rememberLottieComposition(
//      LottieCompositionSpec.RawRes(R.raw.error_animation)
//    )
//    LottieAnimation(
//      composition = composition,
//      iterations = LottieConstants.IterateForever,
//      modifier = Modifier.size(180.dp)
//    )

    // Fallback if Lottie is not available
//    if (composition == null) {
//      Icon(
//        imageVector = Icons.Default.Error,
//        contentDescription = null,
//        tint = MaterialTheme.colorScheme.error,
//        modifier = Modifier.size(80.dp)
//      )
//    }

    Icon(
      imageVector = Icons.Default.Error,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.error,
      modifier = Modifier.size(80.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
      text = "Oops! Terjadi Kesalahan",
      style = MaterialTheme.typography.headlineSmall,
      color = MaterialTheme.colorScheme.error,
      fontWeight = FontWeight.Bold,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
      text = errorMessage,
      style = MaterialTheme.typography.bodyLarge,
      textAlign = TextAlign.Center,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(32.dp))

    Button(
      onClick = onRetry,
      colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary
      ),
      contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Refresh,
        contentDescription = null
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "Coba Lagi",
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
      )
    }
  }
}

/**
 * ================================additonal component==========================================
 */

/**
 * Komponen untuk menampilkan statistik dan rangkuman nilai essay
 */
@Composable
fun StatisticsSummaryCard(
  hasilKoreksi: List<PerSoal>,
  modifier: Modifier = Modifier
) {
  val totalSoal = hasilKoreksi.size
  val averageScore = hasilKoreksi.map { it.skor }.average().toFloat()
  val highestScore = hasilKoreksi.maxOfOrNull { it.skor } ?: 0
  val lowestScore = hasilKoreksi.minOfOrNull { it.skor } ?: 0

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Text(
        text = "Statistik Penilaian",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(16.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // Rata-rata
        StatBox(
          icon = Icons.Default.Star,
          value = "%.1f".format(averageScore),
          label = "Rata-rata",
          tintColor = MaterialTheme.colorScheme.primary,
          modifier = Modifier.weight(1f)
        )

        // Nilai Tertinggi
        StatBox(
          icon = Icons.Default.ThumbUp,
          value = "$highestScore",
          label = "Tertinggi",
          tintColor = Color(0xFF4CAF50),
          modifier = Modifier.weight(1f)
        )

        // Nilai Terendah
        StatBox(
          icon = Icons.Default.Warning,
          value = "$lowestScore",
          label = "Terendah",
          tintColor = Color(0xFFE91E63),
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Distribusi nilai dalam kategori
      val excellent = hasilKoreksi.count { it.skor >= 90 }
      val good = hasilKoreksi.count { it.skor in 70..89 }
      val average = hasilKoreksi.count { it.skor in 50..69 }
      val poor = hasilKoreksi.count { it.skor < 50 }

      Text(
        text = "Distribusi Kategori",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Category bars
      CategoryBar(
        label = "Sangat Baik",
        count = excellent,
        total = totalSoal,
        color = Color(0xFF4CAF50)
      )

      Spacer(modifier = Modifier.height(6.dp))

      CategoryBar(
        label = "Baik",
        count = good,
        total = totalSoal,
        color = Color(0xFF2196F3)
      )

      Spacer(modifier = Modifier.height(6.dp))

      CategoryBar(
        label = "Cukup",
        count = average,
        total = totalSoal,
        color = Color(0xFFFFC107)
      )

      Spacer(modifier = Modifier.height(6.dp))

      CategoryBar(
        label = "Perlu Perbaikan",
        count = poor,
        total = totalSoal,
        color = Color(0xFFE91E63)
      )
    }
  }
}

@Composable
fun StatBox(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  value: String,
  label: String,
  tintColor: Color,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.padding(horizontal = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .size(48.dp)
        .clip(CircleShape)
        .background(tintColor.copy(alpha = 0.1f)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = tintColor,
        modifier = Modifier.size(24.dp)
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = value,
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    )
  }
}

@Composable
fun CategoryBar(
  label: String,
  count: Int,
  total: Int,
  color: Color
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(
        text = label,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Text(
        text = "$count dari $total",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
      )
    }

    Spacer(modifier = Modifier.height(4.dp))

    // Progress bar
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
      Box(
        modifier = Modifier
          .fillMaxHeight()
          .fillMaxWidth(if (total > 0) count.toFloat() / total else 0f)
          .clip(RoundedCornerShape(4.dp))
          .background(color)
      )
    }
  }
}

/**
 * Komponen untuk menampilkan rekomendasi perbaikan
 */
@Composable
fun RecommendationsCard(
  hasilKoreksi: List<PerSoal>,
  modifier: Modifier = Modifier
) {
  // Filter jawaban yang bermasalah
  val jawabanBermasalah = hasilKoreksi
    .filter { it.skor < 70 }
    .sortedBy { it.skor }

  if (jawabanBermasalah.isEmpty()) {
    return
  }

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Warning,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.error,
          modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
          text = "Fokus Perbaikan",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onErrorContainer
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Berdasarkan hasil evaluasi, berikut area yang perlu ditingkatkan:",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onErrorContainer
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Daftar rekomendasi
      jawabanBermasalah.take(3).forEachIndexed { index, perSoal ->
        Row(
          modifier = Modifier.padding(vertical = 6.dp),
          verticalAlignment = Alignment.Top
        ) {
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.error),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "${index + 1}",
              color = MaterialTheme.colorScheme.onError,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = "Soal ${perSoal.soal} (Nilai: ${perSoal.skor})",
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onErrorContainer
            )

            Spacer(modifier = Modifier.height(4.dp))

            val recommendation = perSoal.alasan
              .split(".")
              .filter { it.isNotBlank() }
              .joinToString(". ") { it.trim() }
              .plus(".")

            Text(
              text = recommendation,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
            )
          }
        }

        if (index < jawabanBermasalah.size - 1 && index < 2) {
          HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.2f)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Button(
        onClick = { /* Tampilkan semua rekomendasi */ },
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.error,
          contentColor = MaterialTheme.colorScheme.onError
        ),
        modifier = Modifier.align(Alignment.End)
      ) {
        Text("Lihat Semua")
      }
    }
  }
}

/**
 * Untuk menambahkan komponen ini ke UI utama, Anda dapat menambahkannya
 * ke bagian AnalisisJawabanTab di kode utama seperti berikut:
 */
@Composable
fun AnalisisJawabanTabModified(hasilKoreksi: List<PerSoal>) {
  Column(modifier = Modifier.fillMaxWidth()) {
    // Statistik
    StatisticsSummaryCard(hasilKoreksi = hasilKoreksi)

    Spacer(modifier = Modifier.height(16.dp))

    // Pie chart kartu yang sudah ada di kode sebelumnya
    // ...

    Spacer(modifier = Modifier.height(16.dp))

    // Rekomendasi perbaikan
    RecommendationsCard(hasilKoreksi = hasilKoreksi)

    Spacer(modifier = Modifier.height(16.dp))

    // Bar chart kartu yang sudah ada di kode sebelumnya
    // ...
  }
}