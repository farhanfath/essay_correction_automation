package project.fix.skripsi.presentation.ui.screen.home.components.previewimages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import project.fix.skripsi.R

@Preview(showBackground = true)
@Composable
fun PreviewImageRow(
  modifier: Modifier = Modifier
) {

}

@Preview
@Composable
fun ImagePreviewItem(
  modifier: Modifier = Modifier,
  additionalContent: @Composable BoxScope.() -> Unit = {}
) {
  Box(
    modifier = modifier
      .width(300.dp)
      .height(450.dp)
      .padding(4.dp)
  ) {
    Image(
      painter = painterResource(R.drawable.ic_launcher_background),
      contentDescription = "",
      modifier = Modifier.fillMaxSize(),
      contentScale = ContentScale.Crop
    )
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(2.dp),
      contentAlignment = Alignment.BottomCenter
    ) {
      additionalContent()
    }
  }
}