package project.fix.skripsi.presentation.ui.screen.history.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EmptyStateView() {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Icon(
      imageVector = Icons.Default.History,
      contentDescription = null,
      modifier = Modifier.size(80.dp),
      tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      text = "Belum Ada Riwayat",
      style = MaterialTheme.typography.headlineSmall,
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
      fontWeight = FontWeight.Medium
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = "Riwayat penilaian akan muncul di sini setelah Anda melakukan penilaian",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
      textAlign = TextAlign.Center
    )
  }
}