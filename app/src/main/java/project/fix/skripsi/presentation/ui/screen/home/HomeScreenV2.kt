package project.fix.skripsi.presentation.ui.screen.home

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min
import android.graphics.Bitmap
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import project.fix.skripsi.presentation.ui.components.EnhancedPreviewCard
import project.fix.skripsi.presentation.ui.components.LoadingAnimation
import project.fix.skripsi.presentation.utils.common.base.state.UiState
import project.fix.skripsi.presentation.utils.helper.mergeImagesVertically
import project.fix.skripsi.presentation.utils.helper.rememberMediaHelper
import project.fix.skripsi.presentation.utils.helper.uriToBitmap
import project.fix.skripsi.presentation.viewmodel.EssayViewModel

@Composable
fun HomeScreenV2(
  navController: NavController,
  viewModel: EssayViewModel
) {
  val context = LocalContext.current
  val resultState by viewModel.result.collectAsState()

  // Collect image uris from viewModel
  val imageUris by viewModel.selectedImageUris.collectAsState()

  val mediaHelper = rememberMediaHelper(
    context = context,
    addImageUri = { uri ->
      viewModel.addSingleImage(uri)
    },
    addImageUris = { uris ->
      viewModel.addSelectedImages(uris)
    }
  )

  // For drag and drop functionality
  val dragDropState = rememberDragDropState(
    onDragEnd = { fromIndex, toIndex ->
      viewModel.reorderImages(fromIndex, toIndex)
    }
  )

  // Animation for the button
  val buttonScale = remember { Animatable(1f) }
  LaunchedEffect(imageUris.isNotEmpty()) {
    if (imageUris.isNotEmpty()) {
      buttonScale.animateTo(
        targetValue = 1.05f,
        animationSpec = spring(
          dampingRatio = Spring.DampingRatioMediumBouncy,
          stiffness = Spring.StiffnessMedium
        )
      )
      buttonScale.animateTo(
        targetValue = 1f,
        animationSpec = spring(
          dampingRatio = Spring.DampingRatioLowBouncy,
          stiffness = Spring.StiffnessLow
        )
      )
    }
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .systemBarsPadding(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // App Title with Icon
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 16.dp)
      ) {
        Icon(
          imageVector = Icons.Outlined.DocumentScanner,
          contentDescription = "Essay Icon",
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
          text = "Evaluasi Essay Otomatis",
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Image Caption
      AnimatedVisibility(
        visible = true,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
      ) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
          ),
          shape = RoundedCornerShape(12.dp)
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = "Info",
              tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = if (imageUris.isEmpty())
                "Tambahkan gambar essay untuk dievaluasi"
              else
                "Geser dan lepas gambar untuk mengubah urutan",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSecondaryContainer,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      // Image Preview Grid with Drag and Drop
      if (imageUris.isNotEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
          DraggableImageGrid(
            imageUris = imageUris,
            context = context,
            dragDropState = dragDropState,
            onDelete = { index -> viewModel.removeImage(index) }
          )
        }
      } else {
        // Empty state with animation
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(
                  MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                  MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          EmptyImagesAnimation()
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Image Source Options with improved design
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        ElevatedButton(
          onClick = { mediaHelper.openGallery() },
          modifier = Modifier
            .weight(1f)
            .height(56.dp),
          colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
          ),
          shape = RoundedCornerShape(12.dp),
          elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
          )
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Rounded.PhotoLibrary,
              contentDescription = "Gallery",
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Galeri",
              fontWeight = FontWeight.SemiBold,
              fontSize = 16.sp
            )
          }
        }

        Spacer(modifier = Modifier.width(16.dp))

        ElevatedButton(
          onClick = { mediaHelper.openCamera() },
          modifier = Modifier
            .weight(1f)
            .height(56.dp),
          colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
          ),
          shape = RoundedCornerShape(12.dp),
          elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
          )
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Rounded.CameraAlt,
              contentDescription = "Camera",
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Kamera",
              fontWeight = FontWeight.SemiBold,
              fontSize = 16.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Action Buttons at the bottom
      Column(
        modifier = Modifier.fillMaxWidth()
      ) {
        // Clear All Button (only show if there are images)
        AnimatedVisibility(
          visible = imageUris.isNotEmpty(),
          enter = fadeIn() + expandVertically(),
          exit = fadeOut() + shrinkVertically()
        ) {
          OutlinedButton(
            onClick = { viewModel.clearSelectedImages() },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              contentColor = MaterialTheme.colorScheme.error
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
            shape = RoundedCornerShape(12.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Rounded.DeleteSweep,
                contentDescription = "Clear All",
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Hapus Semua Gambar",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
        }

        // Evaluate Button with animation
        Button(
          onClick = {
            viewModel.evaluateEssay(context)
            navController.navigate("result")
          },
          enabled = imageUris.isNotEmpty() && resultState !is UiState.Loading,
          modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .scale(if (imageUris.isNotEmpty()) buttonScale.value else 1f),
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
          ),
          shape = RoundedCornerShape(12.dp),
          elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp,
            disabledElevation = 0.dp
          )
        ) {
          if (resultState is UiState.Loading) {
            LoadingAnimation()
          } else {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Rounded.RateReview,
                contentDescription = "Evaluate",
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Nilai Essay",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      // Error message if any
      when (val state = resultState) {
        is UiState.Error -> {
          Spacer(modifier = Modifier.height(16.dp))
          Card(
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            shape = RoundedCornerShape(8.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Rounded.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = state.message,
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
        else -> {}
      }
    }
  }
}

/**
 * Custom drag drop state to handle reordering
 */
@Composable
fun rememberDragDropState(
  onDragEnd: (Int, Int) -> Unit
): DragDropState {
  return remember {
    DragDropState(onDragEnd)
  }
}

class DragDropState(
  val onDragEnd: (Int, Int) -> Unit
) {
  var isDragging by mutableStateOf(false)
    private set

  var draggedItemIndex by mutableStateOf<Int?>(null)
    private set

  var currentOverItemIndex by mutableStateOf<Int?>(null)
    private set

  fun onDragStart(index: Int) {
    isDragging = true
    draggedItemIndex = index
  }

  fun onDragOver(index: Int) {
    currentOverItemIndex = index
  }

  fun onDragEnd() {
    val fromIndex = draggedItemIndex
    val toIndex = currentOverItemIndex

    if (fromIndex != null && toIndex != null && fromIndex != toIndex) {
      onDragEnd(fromIndex, toIndex)
    }

    isDragging = false
    draggedItemIndex = null
    currentOverItemIndex = null
  }
}

@Composable
fun DraggableImageGrid(
  imageUris: List<Uri>,
  context: Context,
  dragDropState: DragDropState,
  onDelete: (Int) -> Unit
) {
  val isColumnLayout = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

  BoxWithConstraints(
    modifier = Modifier.fillMaxSize()
  ) {
    val height = maxHeight
    val itemHeight = if (isColumnLayout) {
      (height / min(3f, (imageUris.size / 2f + 0.5f))).coerceAtMost(180.dp)
    } else {
      height.coerceAtMost(180.dp)
    }

    LazyVerticalGrid(
      columns = GridCells.Fixed(if (isColumnLayout) 2 else 3),
      contentPadding = PaddingValues(8.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.fillMaxSize()
    ) {
      itemsIndexed(
        items = imageUris,
        key = { index, _ -> index }
      ) { index, uri ->
        val isDragged = dragDropState.draggedItemIndex == index
        val elevation = if (isDragged) 8.dp else 2.dp

        DraggableImageItem(
          uri = uri,
          context = context,
          index = index,
          isDragged = isDragged,
          elevation = elevation,
          itemHeight = itemHeight,
          onDelete = { onDelete(index) },
          onDragStart = { dragDropState.onDragStart(index) },
          onDragEnd = { dragDropState.onDragEnd() },
          onDragOver = { dragDropState.onDragOver(index) }
        )
      }
    }
  }
}

@Composable
fun DraggableImageItem(
  uri: Uri,
  context: Context,
  index: Int,
  isDragged: Boolean,
  elevation: Dp,
  itemHeight: Dp,
  onDelete: () -> Unit,
  onDragStart: () -> Unit,
  onDragEnd: () -> Unit,
  onDragOver: () -> Unit
) {
  var longPressed by remember { mutableStateOf(false) }
  val scale by animateFloatAsState(
    targetValue = if (isDragged) 1.1f else 1.0f,
    label = "scale"
  )
  val rotationZ by animateFloatAsState(
    targetValue = if (isDragged) 5f else 0f,
    label = "rotation"
  )

  val dragModifier = Modifier
    .pointerInput(Unit) {
      detectDragGesturesAfterLongPress(
        onDragStart = {
          longPressed = true
          onDragStart()
        },
        onDragEnd = {
          longPressed = false
          onDragEnd()
        },
        onDragCancel = {
          longPressed = false
          onDragEnd()
        },
        onDrag = { change, _ ->
          change.consume()
        }
      )
    }
    .pointerInput(Unit) {
      detectDragGestures { change, _ ->
        change.consume()
        onDragOver()
      }
    }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .height(itemHeight)
      .graphicsLayer {
        scaleX = scale
        scaleY = scale
        this.rotationZ = rotationZ
        alpha = if (isDragged) 0.9f else 1f
      }
      .then(dragModifier),
    elevation = CardDefaults.cardElevation(defaultElevation = elevation),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    )
  ) {
    Box(
      modifier = Modifier.fillMaxSize()
    ) {
      // Image
      AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
          .data(uri)
          .crossfade(true)
          .build(),
        contentDescription = "Selected image $index",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )

      // Overlay gradient for better visibility of controls
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            brush = Brush.verticalGradient(
              colors = listOf(
                Color.Black.copy(alpha = 0.6f),
                Color.Transparent,
                Color.Transparent,
                Color.Black.copy(alpha = 0.6f),
              )
            )
          )
      )

      // Index indicator with drag handle
      Row(
        modifier = Modifier
          .align(Alignment.TopStart)
          .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .background(
              color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
              shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${index + 1}",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
              imageVector = Icons.Filled.DragIndicator,
              contentDescription = "Drag to reorder",
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      // Delete button
      IconButton(
        onClick = onDelete,
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(4.dp)
          .size(36.dp)
          .background(
            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
            shape = CircleShape
          )
      ) {
        Icon(
          imageVector = Icons.Filled.Delete,
          contentDescription = "Delete",
          tint = MaterialTheme.colorScheme.onErrorContainer
        )
      }

      // Hint text for long press when not in drag mode
      if (!longPressed && !isDragged) {
        Text(
          text = "Tekan lama untuk reorder",
          color = Color.White.copy(alpha = 0.9f),
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.4f))
            .padding(vertical = 6.dp)
        )
      }
    }
  }
}

@Composable
fun EmptyImagesAnimation() {
  var isVisible by remember { mutableStateOf(true) }
  val alpha by animateFloatAsState(
    targetValue = if (isVisible) 1f else 0.6f,
    animationSpec = tween(durationMillis = 1000),
    label = "alpha"
  )

  LaunchedEffect(key1 = true) {
    while (true) {
      delay(1500)
      isVisible = !isVisible
    }
  }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.alpha(alpha)
  ) {
    Icon(
      imageVector = Icons.Outlined.Image,
      contentDescription = "No Images",
      modifier = Modifier.size(80.dp),
      tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      text = "Belum ada gambar essay",
      style = MaterialTheme.typography.titleMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      fontWeight = FontWeight.Medium
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = "Tambahkan dari galeri atau kamera",
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
      textAlign = TextAlign.Center
    )
  }
}
//
//@Composable
//fun LoadingAnimation() {
//  Row(
//    verticalAlignment = Alignment.CenterVertically,
//    horizontalArrangement = Arrangement.Center
//  ) {
//    val infiniteTransition = rememberInfiniteTransition(label = "loading")
//    val dots = listOf(
//      remember { Animatable(0f) },
//      remember { Animatable(0f) },
//      remember { Animatable(0f) }
//    )
//
//    dots.forEachIndexed { index, animatable ->
//      LaunchedEffect(animatable) {
//        delay(index * 150L)
//        while (true) {
//          animatable.animateTo(
//            targetValue = 1f,
//            animationSpec = tween(
//              durationMillis = 300,
//              easing = FastOutSlowInEasing
//            )
//          )
//          animatable.animateTo(
//            targetValue = 0f,
//            animationSpec = tween(
//              durationMillis = 300,
//              easing = FastOutSlowInEasing
//            )
//          )
//          delay(600)
//        }
//      }
//
//      Box(
//        modifier = Modifier
//          .padding(horizontal = 2.dp)
//          .size(8.dp)
//          .graphicsLayer {
//            scaleX = 1f + animatable.value * 0.5f
//            scaleY = 1f + animatable.value * 0.5f
//            alpha = 0.6f + animatable.value * 0.4f
//          }
//          .background(
//            color = MaterialTheme.colorScheme.onPrimary,
//            shape = CircleShape
//          )
//      )
//    }
//
//    Spacer(modifier = Modifier.width(8.dp))
//    Text(
//      text = "Menilai...",
//      color = MaterialTheme.colorScheme.onPrimary,
//      fontSize = 16.sp,
//      fontWeight = FontWeight.SemiBold
//    )
//  }
//}