package project.fix.skripsi.presentation.ui.screen.home.components.previewimages

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ImagePreviewItem(
  imageItem: ImageItem,
  modifier: Modifier = Modifier,
  isSelected: Boolean = false,
  onClick: () -> Unit = {},
  additionalContent: @Composable () -> Unit = {}
) {
  Box(
    modifier = modifier
      .padding(4.dp)
      .clip(RoundedCornerShape(8.dp))
      .clickable(onClick = onClick)
      .border(
        width = if (isSelected) 2.dp else 0.dp,
        color = if (isSelected) MaterialTheme.colorScheme.onSurfaceVariant else Color.Transparent,
        shape = RoundedCornerShape(8.dp)
      )
  ) {
    Image(
      painter = painterResource(id = imageItem.resourceId),
      contentDescription = imageItem.description,
      modifier = Modifier.matchParentSize(),
      contentScale = ContentScale.Crop
    )

    Box(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .padding(8.dp)
    ) {
      additionalContent()
    }
  }
}