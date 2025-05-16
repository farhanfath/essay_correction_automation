package project.fix.skripsi.presentation.ui.screen.home.components.previewimages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import project.fix.skripsi.R

@Preview
@Composable
fun ImagePreviewSection() {
  var showPreviewRow by remember { mutableStateOf(false) }
  Column(
    modifier = Modifier
      .fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Image(
      painter = painterResource(R.drawable.ic_launcher_background),
      contentDescription = "",
      modifier = Modifier
        .width(300.dp)
        .height(450.dp),
      contentScale = ContentScale.Crop
    )

    Row(
      modifier = Modifier
        .padding(vertical = 24.dp, horizontal = 10.dp)
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      CustomCircleButton(
        icon = Icons.AutoMirrored.Default.KeyboardArrowLeft,
        onClick = {

        }
      )

      AssistChip(
        onClick = {
          showPreviewRow = !showPreviewRow
        },
        label = {
          Text(
            text = "Halaman 1 dari 2",
            style = MaterialTheme.typography.labelSmall.copy(
              color = MaterialTheme.colorScheme.onSurface
            )
          )
        },
        shape = CircleShape,
        trailingIcon = {
          Icon(
            imageVector = if (showPreviewRow) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface
          )
        }
      )
      
      CustomCircleButton(
        icon = Icons.AutoMirrored.Default.KeyboardArrowRight,
        onClick = {

        }
      )
    }

    AnimatedVisibility(
      visible = showPreviewRow,
      enter = fadeIn() + expandVertically(),
      exit = fadeOut() + slideOutVertically()
    ) {
      LazyRow(
        modifier = Modifier.fillMaxWidth()
      ) {
        items(10) {
          ImagePreviewItem(
            modifier = Modifier
              .width(100.dp)
              .height(150.dp),
            additionalContent = {
              Box(
                modifier = Modifier
                  .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(0.5f),
                    shape = RoundedCornerShape(4.dp)
                  )
                  .padding(vertical = 4.dp, horizontal = 10.dp)
              ) {
                Text(
                  text = "$it",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                )
              }
            }
          )
        }
      }
    }
  }
}

@Composable
fun CustomCircleButton(
  modifier: Modifier = Modifier,
  icon: ImageVector,
  onClick: () -> Unit,
  iconColor: Color = MaterialTheme.colorScheme.onSurface,
  borderColor: Color = MaterialTheme.colorScheme.surfaceVariant,
  backgroundColor: Color = MaterialTheme.colorScheme.surface,
) {
  IconButton(
    modifier = Modifier
      .background(
        color = backgroundColor,
        shape = CircleShape
      )
      .border(
        width = 2.dp,
        color = borderColor,
        shape = CircleShape
      )
      .size(32.dp),
    onClick = onClick
  ) {
    Icon(
      imageVector = icon,
      contentDescription = "Back",
      modifier = modifier,
      tint = iconColor
    )
  }
}
