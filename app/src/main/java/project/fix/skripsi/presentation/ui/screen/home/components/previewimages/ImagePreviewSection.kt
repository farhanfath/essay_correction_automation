package project.fix.skripsi.presentation.ui.screen.home.components.previewimages

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdaptiveImagePreviewSection(
  uris: List<Uri>,
  showPreviewRow: Boolean,
  showMediaOptions: Boolean,
  onTogglePreviewRow: (Boolean) -> Unit,
  onDeleteImage: (Int) -> Unit,
  onReorderImages: (List<Uri>) -> Unit
) {
  val lazyRowState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()
  var isReorderMode by remember { mutableStateOf(false) }
  var reorderedUris by remember { mutableStateOf(uris) }

  var showPreviewDialog by remember { mutableStateOf(false) }
  var selectedPreviewIndex by remember { mutableIntStateOf(0) }

  var showDeleteDialog by remember { mutableStateOf(false) }
  var selectedDeleteIndex by remember { mutableIntStateOf(0) }

  val pagerState = rememberPagerState(
    initialPage = 0,
    pageCount = { uris.size }
  )

  LaunchedEffect(pagerState) {
    snapshotFlow { pagerState.currentPage }.collect { page ->
      if (showPreviewRow) {
        lazyRowState.animateScrollToItem(page)
      }
    }
  }

  // Reset reorderedUris saat uris berubah
  LaunchedEffect(uris) {
    reorderedUris = uris
  }

  val imageHeightPx by animateDpAsState(
    targetValue = when {
      showMediaOptions && showPreviewRow -> 200.dp
      showPreviewRow -> 350.dp
      showMediaOptions -> 380.dp
      else -> 450.dp
    },
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    ),
    label = "imageHeight"
  )

  val imageWidthPx by animateDpAsState(
    targetValue = when {
      showMediaOptions && showPreviewRow -> 150.dp
      showPreviewRow -> 200.dp
      showMediaOptions -> 230.dp
      else -> 300.dp
    },
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    ),
    label = "imageWidth"
  )

  Column(
    modifier = Modifier
      .fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .width(imageWidthPx)
        .height(imageHeightPx)
    ) {
      HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
      ) { page ->
        AsyncImage(
          model = uris[page],
          contentDescription = "",
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
        .padding(vertical = 16.dp, horizontal = 10.dp)
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

      LazyRow(
        modifier = Modifier.width(200.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        item {
          AssistChip(
            onClick = {
              onTogglePreviewRow(!showPreviewRow)
            },
            label = {
              Text(
                text = "Halaman ${pagerState.currentPage + 1} dari ${uris.size}",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = MaterialTheme.colorScheme.onSurface
                )
              )
            },
            shape = CircleShape,
            trailingIcon = {
              val rotation by animateFloatAsState(
                targetValue = if (showPreviewRow) 180f else 0f,
                animationSpec = tween(durationMillis = 300),
                label = "iconRotation"
              )

              Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.graphicsLayer {
                  rotationX = rotation
                }
              )
            },
            border = BorderStroke(
              width = 1.dp,
              color = if (showPreviewRow) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            )
          )

          Spacer(modifier = Modifier.width(8.dp))
        }
        item {
          AnimatedVisibility(
            visible = showPreviewRow,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally()
          ) {
            AssistChip(
              onClick = {
                isReorderMode = !isReorderMode
                if (!isReorderMode) {
                  // Apply reordering when exiting reorder mode
                  onReorderImages(reorderedUris)
                }
              },
              label = {
                Text(
                  text = if (isReorderMode) "Selesai" else "Atur Urutan",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface
                  )
                )
              },
              leadingIcon = {
                Icon(
                  imageVector = if (isReorderMode) Icons.Default.Check else Icons.Default.SwapVert,
                  contentDescription = if (isReorderMode) "Selesai Mengatur" else "Atur Urutan",
                  tint = if (isReorderMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(16.dp)
                )
              },
              shape = CircleShape,
              border = BorderStroke(
                width = 1.dp,
                color = if (isReorderMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
              )
            )
          }
        }
      }

      CustomCircleButton(
        icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
        onClick = {
          if (pagerState.currentPage < uris.size - 1) {
            coroutineScope.launch {
              pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
          }
        },
        enabled = pagerState.currentPage < uris.size - 1
      )
    }

    // preview images on a row
    AnimatedVisibility(
      visible = showPreviewRow,
      enter = fadeIn(
        animationSpec = tween(300)
      ) + expandVertically(
        animationSpec = spring(
          dampingRatio = Spring.DampingRatioMediumBouncy,
          stiffness = Spring.StiffnessLow
        )
      ),
      exit = fadeOut(
        animationSpec = tween(200)
      ) + shrinkVertically(
        animationSpec = spring(
          dampingRatio = Spring.DampingRatioMediumBouncy,
          stiffness = Spring.StiffnessLow
        )
      )
    ) {
      LazyRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp),
        state = lazyRowState
      ) {
        itemsIndexed(if (isReorderMode) reorderedUris else uris) { index, uri ->
          val borderWidth by animateDpAsState(
            targetValue = if (pagerState.currentPage == index && !isReorderMode) 2.dp else 0.dp,
            animationSpec = tween(durationMillis = 150),
            label = "borderWidth"
          )

          Box(
            modifier = Modifier
              .width(100.dp)
              .height(150.dp)
              .padding(4.dp)
              .clip(RoundedCornerShape(8.dp))
              .combinedClickable(
                enabled = !isReorderMode,
                onClick = {
                  coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                  }
                },
                onLongClick = {
                  selectedPreviewIndex = index
                  showPreviewDialog = true
                }
              )
              .border(
                width = borderWidth,
                color = if (pagerState.currentPage == index && !isReorderMode)
                  MaterialTheme.colorScheme.primary
                else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
              )
          ) {
            AsyncImage(
              model = uri,
              contentDescription = "",
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop
            )

            // Overlay untuk status reordering
            if (isReorderMode) {
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
              )
            }

            if (isReorderMode) {
              // Tombol reordering
              Row(
                modifier = Modifier
                  .align(Alignment.BottomCenter)
                  .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                    RoundedCornerShape(8.dp)
                  )
                  .fillMaxWidth()
                  .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                // Tombol geser ke kiri
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(enabled = index > 0) {
                      // Pindahkan item ke posisi sebelumnya
                      if (index > 0) {
                        val newList = reorderedUris.toMutableList()
                        val temp = newList[index]
                        newList[index] = newList[index - 1]
                        newList[index - 1] = temp
                        reorderedUris = newList
                      }
                    },
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = "Pindah ke Kiri",
                    tint = if (index > 0)
                      MaterialTheme.colorScheme.primary
                    else
                      MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    modifier = Modifier.size(24.dp)
                  )
                }

                // Tombol geser ke kanan
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(enabled = index < reorderedUris.size - 1) {
                      // Pindahkan item ke posisi selanjutnya
                      if (index < reorderedUris.size - 1) {
                        val newList = reorderedUris.toMutableList()
                        val temp = newList[index]
                        newList[index] = newList[index + 1]
                        newList[index + 1] = temp
                        reorderedUris = newList
                      }
                    },
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = "Pindah ke Kanan",
                    tint = if (index < reorderedUris.size - 1)
                      MaterialTheme.colorScheme.primary
                    else
                      MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    modifier = Modifier.size(24.dp)
                  )
                }
              }
            } else {
              // Tombol delete hanya muncul ketika tidak dalam mode reorder
              Box(
                modifier = Modifier
                  .align(Alignment.TopEnd)
                  .size(32.dp)
                  .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(bottomStart = 8.dp)
                  )
                  .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false, color = MaterialTheme.colorScheme.error),
                    onClick = {
                      showDeleteDialog = true
                      selectedDeleteIndex = index
                    }
                  ),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Delete,
                  contentDescription = "Delete Image",
                  tint = MaterialTheme.colorScheme.error,
                  modifier = Modifier.size(16.dp)
                )
              }

              // numbering di preview image
              Box(
                modifier = Modifier
                  .align(Alignment.BottomCenter)
                  .size(24.dp)
                  .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp)
                  ),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "${index + 1}",
                  style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                )
              }
            }
          }
        }
      }
    }

    // zoomable preview image dialog
    if (showPreviewDialog && (if (isReorderMode) reorderedUris else uris).isNotEmpty()) {
      val currentUri = if (isReorderMode) reorderedUris[selectedPreviewIndex] else uris[selectedPreviewIndex]

      // State untuk transformasi gambar
      var scale by remember { mutableFloatStateOf(1f) }
      var offset by remember { mutableStateOf(Offset.Zero) }

      // Gesture untuk zoom dan pan
      val transformableState = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(0.5f, 3f)
        offset += offsetChange
      }

      ZoomableDialog(
        currentUri = currentUri,
        scale = scale,
        offset = offset,
        transformableState = transformableState,
        onDismiss = { showPreviewDialog = false },
        onResetSize = {
          scale = 1f
          offset = Offset.Zero
        }
      )
    }

    // delete image dialog
    if (showDeleteDialog) {
      AlertDialog(
        onDismissRequest = {
          showDeleteDialog = false
        },
        title = {
          Text("Konfirmasi")
        },
        text = {
          Text("Yakin ingin menghapus gambar ini?")
        },
        confirmButton = {
          Button(
            onClick = {
              onDeleteImage(selectedDeleteIndex)
              showDeleteDialog = false
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.error
            )
          ) {
            Text("Hapus")
          }
        },
        dismissButton = {
          Button(
            onClick = { showDeleteDialog = false },
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant,
              contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
          ) {
            Text("Batal")
          }
        }
      )
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
