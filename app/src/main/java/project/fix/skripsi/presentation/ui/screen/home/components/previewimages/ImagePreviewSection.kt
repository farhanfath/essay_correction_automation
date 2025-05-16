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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import project.fix.skripsi.R

@Preview
@Composable
fun ImagePreviewSection() {

  /**
   * todo: change with list uri image
   */
  val sampleImages = List(10) { index ->
    ImageItem(
      id = index,
      // Using same resource for all items in this example
      resourceId = R.drawable.ic_launcher_background
    )
  }

  var showPreviewRow by remember { mutableStateOf(false) }
  val lazyRowState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()

  val pagerState = rememberPagerState(
    initialPage = 0,
    pageCount = { sampleImages.size }
  )

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }.collect { page ->
      if (showPreviewRow) {
        lazyRowState.animateScrollToItem(page)
      }
    }
  }

  Column(
    modifier = Modifier
      .fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    HorizontalPager(
      state = pagerState,
      modifier = Modifier
        .width(300.dp)
        .height(450.dp)
    ) { page ->
      Box(
        modifier = Modifier.fillMaxWidth()
      ) {
        Image(
          painter = painterResource(sampleImages[page].resourceId),
          contentDescription = sampleImages[page].description,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )
      }
    }

    /**
     * navigation controller for preview image
     */
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
          if (pagerState.currentPage > 0) {
            coroutineScope.launch {
              pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
          }
        },
        enabled = pagerState.currentPage > 0
      )

      AssistChip(
        onClick = {
          showPreviewRow = !showPreviewRow
        },
        label = {
          Text(
            text = "Halaman ${pagerState.currentPage + 1} dari ${sampleImages.size}",
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
        icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
        onClick = {
          if (pagerState.currentPage < sampleImages.size - 1) {
            coroutineScope.launch {
              pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
          }
        },
        enabled = pagerState.currentPage < sampleImages.size - 1
      )
    }

    AnimatedVisibility(
      visible = showPreviewRow,
      enter = fadeIn() + expandVertically(),
      exit = fadeOut() + slideOutVertically()
    ) {
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp),
        state = lazyRowState
      ) {
        items(sampleImages) { imageItem ->
          ImagePreviewItem(
            imageItem = imageItem,
            modifier = Modifier
              .width(100.dp)
              .height(150.dp),
            isSelected = pagerState.currentPage == imageItem.id,
            onClick = {
              coroutineScope.launch {
                pagerState.animateScrollToPage(imageItem.id)
              }
            },
            additionalContent = {
              Box(
                modifier = Modifier
                  .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(4.dp)
                  )
                  .padding(vertical = 4.dp, horizontal = 10.dp)
              ) {
                Text(
                  text = "${imageItem.id + 1}",
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
  icon: ImageVector,
  onClick: () -> Unit,
  enabled: Boolean = true
) {
  IconButton(
    onClick = onClick,
    enabled = enabled,
    modifier = Modifier
//      .background(
//        color = backgroundColor,
//        shape = CircleShape
//      )
      .border(
        width = 1.dp,
        color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        shape = CircleShape
      )
      .size(32.dp),
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    )
  }
}
