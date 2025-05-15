package project.fix.skripsi.presentation.ui.components

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoSizeSelectActual
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.ZoomOutMap
import androidx.compose.material.icons.outlined.ZoomIn
import androidx.compose.material.icons.outlined.ZoomOut
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import project.fix.skripsi.presentation.state.EssayState
import kotlin.math.max
import kotlin.math.min

@Composable
fun EnhancedPreviewCard(
  bitmap: Bitmap,
  uiState: EssayState = EssayState.Idle
) {
  // State for animated elements
  val infiniteTransition = rememberInfiniteTransition(label = "analyzeTransition")
  val scanLinePosition by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(2500, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ), label = "scanLine"
  )

  // State for image zoom/pan
  var scale by remember { mutableStateOf(1f) }
  var offset by remember { mutableStateOf(Offset.Zero) }
  var showFullscreen by remember { mutableStateOf(false) }

  // Animated values
  val cardElevation by animateFloatAsState(
    targetValue = if (uiState == EssayState.Loading) 12f else 4f,
    label = "cardElevation"
  )

  val previewBackground = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
  val primaryColor = MaterialTheme.colorScheme.primary
  val backgroundColor = MaterialTheme.colorScheme.surface

  Box(
    modifier = Modifier.fillMaxWidth(),
    contentAlignment = Alignment.Center
  ) {
    // Card with Preview Image
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      elevation = CardDefaults.cardElevation(defaultElevation = cardElevation.dp),
      shape = RoundedCornerShape(24.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            brush = Brush.verticalGradient(
              colors = listOf(
                backgroundColor,
                previewBackground
              )
            )
          )
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          // Title with icon
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Image,
              contentDescription = null,
              tint = primaryColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Preview Gambar",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
          }

          // Zoom controls
          Row {
            IconButton(
              onClick = { scale = max(0.5f, scale - 0.25f) },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.Outlined.ZoomOut,
                contentDescription = "Zoom Out",
                tint = MaterialTheme.colorScheme.primary
              )
            }

            IconButton(
              onClick = { scale = min(3f, scale + 0.25f) },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = Icons.Outlined.ZoomIn,
                contentDescription = "Zoom In",
                tint = MaterialTheme.colorScheme.primary
              )
            }

            IconButton(
              onClick = { showFullscreen = !showFullscreen },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = if (showFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                contentDescription = "Fullscreen Toggle",
                tint = MaterialTheme.colorScheme.primary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
        // Image preview with effects
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // This makes it a square container
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black.copy(alpha = 0.1f))
            .border(
              width = 2.dp,
              brush = Brush.linearGradient(
                colors = listOf(primaryColor, primaryColor.copy(alpha = 0.3f), primaryColor)
              ),
              shape = RoundedCornerShape(16.dp)
            )
            .pointerInput(Unit) {
              detectTransformGestures { _, pan, zoom, _ ->
                scale = (scale * zoom).coerceIn(0.5f, 3f)
                offset = Offset(
                  offset.x + pan.x,
                  offset.y + pan.y
                )
              }
            },
          contentAlignment = Alignment.Center
        ) {
          // Background pattern
          Canvas(
            modifier = Modifier.matchParentSize()
          ) {
            val gridSize = 20.dp.toPx()
            val gridColor = Color.Gray.copy(alpha = 0.1f)

            for (x in 0..size.width.toInt() step gridSize.toInt()) {
              drawLine(
                color = gridColor,
                start = Offset(x.toFloat(), 0f),
                end = Offset(x.toFloat(), size.height),
                strokeWidth = 1f
              )
            }

            for (y in 0..size.height.toInt() step gridSize.toInt()) {
              drawLine(
                color = gridColor,
                start = Offset(0f, y.toFloat()),
                end = Offset(size.width, y.toFloat()),
                strokeWidth = 1f
              )
            }
          }

          // Actual image
          Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Preview Gambar",
            modifier = Modifier
              .matchParentSize()
              .scale(scale)
              .offset { IntOffset(offset.x.toInt(), offset.y.toInt()) },
            contentScale = if (showFullscreen) ContentScale.Fit else ContentScale.Crop
          )

          // Scanning animation when analyzing
          if (uiState == EssayState.Loading) {
            Box(
              modifier = Modifier
                .matchParentSize()
                .drawBehind {
                  val scanLineY = scanLinePosition * size.height

                  // Draw scan line
                  drawRect(
                    brush = Brush.verticalGradient(
                      colors = listOf(
                        primaryColor.copy(alpha = 0f),
                        primaryColor.copy(alpha = 0.7f),
                        primaryColor.copy(alpha = 0f)
                      ),
                      startY = scanLineY - 30f,
                      endY = scanLineY + 30f
                    ),
                    topLeft = Offset(0f, scanLineY - 30f),
                    size = Size(size.width, 60f)
                  )

                  // Draw scanner rect
                  drawRect(
                    color = primaryColor,
                    style = Stroke(width = 2f),
                    size = Size(size.width, size.height)
                  )
                }
            )

            // Scanning text overlay
            Box(
              modifier = Modifier
                .matchParentSize()
                .padding(16.dp),
              contentAlignment = Alignment.BottomCenter
            ) {
              Text(
                text = "Menganalisis gambar...",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                  .background(
                    Color.Black.copy(alpha = 0.6f),
                    RoundedCornerShape(50)
                  )
                  .padding(horizontal = 16.dp, vertical = 8.dp)
              )
            }
          }

          // Reset zoom/pan button
          Column {
            AnimatedVisibility(
              visible = scale > 1.05f || offset != Offset.Zero,
              modifier = Modifier
//                .align(Alignment.TopEnd)
                .padding(8.dp),
              enter = fadeIn(),
              exit = fadeOut()
            ) {
              IconButton(
                onClick = {
                  scale = 1f
                  offset = Offset.Zero
                },
                modifier = Modifier
                  .size(36.dp)
                  .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    CircleShape
                  )
              ) {
                Icon(
                  imageVector = Icons.Default.RestartAlt,
                  contentDescription = "Reset Zoom",
                  tint = Color.White
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Image Resolution Info
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.PhotoSizeSelectActual,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "${bitmap.width} × ${bitmap.height} px",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.width(16.dp))

          Icon(
            imageVector = Icons.Default.ZoomOutMap,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Skala: ${(scale * 100).toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}