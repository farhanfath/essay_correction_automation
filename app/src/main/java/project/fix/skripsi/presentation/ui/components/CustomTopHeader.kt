package project.fix.skripsi.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CustomTopHeader(
  statusBarColor: Color = MaterialTheme.colorScheme.surface,
  content: @Composable () -> Unit = {}) {
  Column {
    Box(
      modifier = Modifier
        .height(35.dp)
        .fillMaxWidth()
        .background(color = statusBarColor)
    )
    content()
  }
}

@Composable
fun CustomDetailTopBar(
  onBackClick: () -> Unit,
  title: String
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 10.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp),
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
    Text(
      text = title,
      style = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Medium
      )
    )
  }
}