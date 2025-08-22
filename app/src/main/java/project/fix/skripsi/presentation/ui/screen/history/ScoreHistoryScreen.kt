package project.fix.skripsi.presentation.ui.screen.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import project.fix.skripsi.presentation.ui.components.CustomTopHeader
import project.fix.skripsi.presentation.ui.screen.history.components.EmptyStateView
import project.fix.skripsi.presentation.ui.screen.history.components.ScoreHistoryCard
import project.fix.skripsi.presentation.viewmodel.SavedScoreHistoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ScoreHistoryScreen(
  scoreHistoryViewModel: SavedScoreHistoryViewModel,
  onNavigateToDetailHistory: (Long) -> Unit,
  onBackClick: () -> Unit
) {
  val scoreHistoryList by scoreHistoryViewModel.savedScoreHistoryList.collectAsState()

  Scaffold(
    topBar = {
      CustomTopHeader(
        statusBarColor = MaterialTheme.colorScheme.primary
      ) {
        // Header
        Surface(
          modifier = Modifier.fillMaxWidth(),
          color = MaterialTheme.colorScheme.primary
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 24.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            IconButton(
              onClick = onBackClick
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "back button"
              )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column {
              Row(
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Riwayat Penilaian",
                  style = MaterialTheme.typography.headlineSmall,
                  color = MaterialTheme.colorScheme.onPrimary,
                  fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                  imageVector = Icons.Default.History,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onPrimary,
                  modifier = Modifier.size(20.dp)
                )
              }
              Text(
                text = "${scoreHistoryList.size} riwayat tersimpan",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
              )
            }
          }
        }
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .padding(innerPadding)
        .fillMaxSize()
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(
              MaterialTheme.colorScheme.surface,
              MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
            )
          )
        )
    ) {


      // Content
      if (scoreHistoryList.isEmpty()) {
        EmptyStateView()
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(
            items = scoreHistoryList,
            key = { it.id }
          ) { history ->
            ScoreHistoryCard(
              history = history,
              onClick = {
                onNavigateToDetailHistory(history.id)
              },
              onDelete = {
                scoreHistoryViewModel.deleteSavedScoreHistoryById(history.id)
              }
            )
          }
        }
      }
    }
  }
}

// Helper Functions
fun formatDate(timestamp: Long): String {
  val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
  return sdf.format(Date(timestamp))
}

fun getScoreColor(score: Double): Color {
  return when {
    score >= 80 -> Color(0xFF4CAF50) // Green
    score >= 60 -> Color(0xFFFF9800) // Orange
    else -> Color(0xFFFF5722) // Red
  }
}