package project.fix.skripsi.presentation.ui.screen.result.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
fun ErrorView(
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