package project.fix.skripsi.presentation.ui.screen.home.sample

import android.content.Context
import android.net.Uri
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.DocumentScanner
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import project.fix.skripsi.presentation.ui.components.LoadingAnimation
import project.fix.skripsi.presentation.utils.common.base.state.UiState
import project.fix.skripsi.presentation.utils.helper.rememberMediaHelper
import project.fix.skripsi.presentation.utils.helper.uriToBitmap
import project.fix.skripsi.presentation.viewmodel.EssayViewModel

@Composable
fun EnhancedHomeScreen(
  navController: NavController,
  viewModel: EssayViewModel
) {
  val context = LocalContext.current
  val resultState by viewModel.result.collectAsState()

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

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // App Title
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

    // Image Preview Grid
    if (imageUris.isNotEmpty()) {
      ImagePreviewGrid(
        imageUris = imageUris,
        context = context,
        onReorder = { from, to -> viewModel.reorderImages(from, to) },
        onDelete = { index -> viewModel.removeImage(index) }
      )
    } else {
      // Empty state
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
          .background(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(12.dp)
          ),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Icon(
            imageVector = Icons.Default.Image,
            contentDescription = "No Images",
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Belum ada gambar",
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Image Source Options
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      ElevatedButton(
        onClick = {
          mediaHelper.openGallery()
        },
        modifier = Modifier.weight(1f),
        colors = ButtonDefaults.elevatedButtonColors(
          containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.Image,
            contentDescription = "Gallery"
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text("Galeri")
        }
      }

      Spacer(modifier = Modifier.width(16.dp))

      ElevatedButton(
        onClick = {
          mediaHelper.openCamera()
        },
        modifier = Modifier.weight(1f),
        colors = ButtonDefaults.elevatedButtonColors(
          containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.CameraAlt,
            contentDescription = "Camera"
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text("Kamera")
        }
      }
    }

    Spacer(modifier = Modifier.height(32.dp))

    // Clear All Button (only show if there are images)
    if (imageUris.isNotEmpty()) {
      OutlinedButton(
        onClick = {
          viewModel.clearSelectedImages()
        },
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
            imageVector = Icons.Default.Delete,
            contentDescription = "Clear All"
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Hapus Semua Gambar",
            fontSize = 14.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }

    // Evaluate Button
    Button(
      onClick = {
        viewModel.evaluateEssay(context)
        navController.navigate("result")
      },
      enabled = imageUris.isNotEmpty() && resultState !is UiState.Loading,
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
      ),
      shape = RoundedCornerShape(12.dp)
    ) {
      if (resultState is UiState.Loading) {
        LoadingAnimation()
      } else {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Evaluate"
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

    // Error message if any
    when (val state = resultState) {
      is UiState.Error -> {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = state.message,
          color = MaterialTheme.colorScheme.error,
          textAlign = TextAlign.Center
        )
      }
      else -> {}
    }
  }
}

@Composable
fun ImagePreviewGrid(
  imageUris: List<Uri>,
  context: Context,
  onReorder: (Int, Int) -> Unit,
  onDelete: (Int) -> Unit
) {
  val gridItemsCount = (imageUris.size + 1) / 2 // Calculate number of rows needed

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp)
  ) {
    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      contentPadding = PaddingValues(4.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height((gridItemsCount * 180).dp.coerceAtMost(350.dp))
    ) {
      itemsIndexed(imageUris) { index, uri ->
        ImagePreviewItem(
          uri = uri,
          context = context,
          index = index,
          onMoveUp = { if (index > 0) onReorder(index, index - 1) },
          onMoveDown = { if (index < imageUris.size - 1) onReorder(index, index + 1) },
          onDelete = { onDelete(index) }
        )
      }
    }
  }
}

@Composable
fun ImagePreviewItem(
  uri: Uri,
  context: Context,
  index: Int,
  onMoveUp: () -> Unit,
  onMoveDown: () -> Unit,
  onDelete: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(1f)
      .clip(RoundedCornerShape(12.dp))
      .border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
        shape = RoundedCornerShape(12.dp)
      )
  ) {
    // Image
    Image(
      bitmap = uriToBitmap(context, uri).asImageBitmap(),
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

    // Index indicator
    Text(
      text = "${index + 1}",
      color = Color.White,
      fontWeight = FontWeight.Bold,
      fontSize = 16.sp,
      modifier = Modifier
        .align(Alignment.TopStart)
        .padding(8.dp)
        .background(
          color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
          shape = CircleShape
        )
        .padding(horizontal = 10.dp, vertical = 4.dp)
    )

    // Control buttons
    Row(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .padding(6.dp),
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      // Move Up Button
      IconButton(
        onClick = onMoveUp,
        modifier = Modifier
          .size(36.dp)
          .background(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
            shape = CircleShape
          )
      ) {
        Icon(
          imageVector = Icons.Default.KeyboardArrowUp,
          contentDescription = "Move Up",
          tint = MaterialTheme.colorScheme.onSurface
        )
      }

      // Delete Button
      IconButton(
        onClick = onDelete,
        modifier = Modifier
          .size(36.dp)
          .background(
            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f),
            shape = CircleShape
          )
      ) {
        Icon(
          imageVector = Icons.Default.Delete,
          contentDescription = "Delete",
          tint = MaterialTheme.colorScheme.onErrorContainer
        )
      }

      // Move Down Button
      IconButton(
        onClick = onMoveDown,
        modifier = Modifier
          .size(36.dp)
          .background(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
            shape = CircleShape
          )
      ) {
        Icon(
          imageVector = Icons.Default.KeyboardArrowDown,
          contentDescription = "Move Down",
          tint = MaterialTheme.colorScheme.onSurface
        )
      }
    }
  }
}