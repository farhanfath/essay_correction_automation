package project.fix.skripsi.presentation.ui.screen.result.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SaveDataButton(
  onSaveClick: (String) -> Unit,
  modifier: Modifier = Modifier,
  isEnabled: Boolean = true
) {
  var showDialog by remember { mutableStateOf(false) }
  var isLoading by remember { mutableStateOf(false) }
  var showSuccess by remember { mutableStateOf(false) }

  // Animasi untuk button state
  val buttonScale by animateFloatAsState(
    targetValue = if (isLoading) 0.95f else 1f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessHigh
    )
  )

  val buttonColor by animateColorAsState(
    targetValue = when {
      showSuccess -> Color(0xFF4CAF50)
      isLoading -> MaterialTheme.colorScheme.secondary
      else -> MaterialTheme.colorScheme.primary
    },
    animationSpec = tween(300)
  )

  val iconRotation by animateFloatAsState(
    targetValue = if (isLoading) 360f else 0f,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    )
  )

  Box(modifier = modifier) {
    Button(
      onClick = {
        if (!isLoading && !showSuccess) {
          showDialog = true
        }
      },
      enabled = isEnabled && !isLoading,
      colors = ButtonDefaults.buttonColors(
        containerColor = buttonColor,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
      ),
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .graphicsLayer {
          scaleX = buttonScale
          scaleY = buttonScale
        },
      shape = RoundedCornerShape(16.dp),
      elevation = ButtonDefaults.buttonElevation(
        defaultElevation = 8.dp,
        pressedElevation = 12.dp,
        disabledElevation = 0.dp
      )
    ) {
      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
      ) {
        when {
          showSuccess -> {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Tersimpan!",
              color = Color.White,
              fontSize = 16.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
          isLoading -> {
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier
                .size(24.dp)
                .graphicsLayer { rotationZ = iconRotation }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Menyimpan...",
              color = Color.White,
              fontSize = 16.sp,
              fontWeight = FontWeight.Medium
            )
          }
          else -> {
            Icon(
              imageVector = Icons.Default.Save,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Simpan Hasil",
              color = Color.White,
              fontSize = 16.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }

    // Ripple effect overlay
    if (isLoading) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Color.White.copy(alpha = 0.1f),
            RoundedCornerShape(16.dp)
          )
      )
    }
  }

  // Dialog untuk input nama
  if (showDialog) {
    SaveDataDialog(
      onDismiss = { showDialog = false },
      onSave = { name ->
        showDialog = false
        isLoading = true

        // Simulate save process
        onSaveClick(name)

        isLoading = false
        showSuccess = true

        showSuccess = false
      }
    )
  }
}

@Composable
fun SaveDataDialog(
  onDismiss: () -> Unit,
  onSave: (String) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var isError by remember { mutableStateOf(false) }
  val focusRequester = remember { FocusRequester() }

  // Animasi untuk dialog
  val scale by animateFloatAsState(
    targetValue = 1f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessMedium
    )
  )

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .background(
              MaterialTheme.colorScheme.primaryContainer,
              CircleShape
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Save,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
          )
        }
        Text(
          text = "Simpan Hasil Evaluasi",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        Text(
          text = "Berikan nama untuk hasil evaluasi ini agar mudah ditemukan nanti.",
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 14.sp,
          lineHeight = 20.sp
        )

        OutlinedTextField(
          value = name,
          onValueChange = {
            name = it
            isError = false
          },
          label = { Text("Nama Evaluasi") },
          placeholder = { Text("Contoh: Essay Kelas 4 IPS") },
          isError = isError,
          supportingText = if (isError) {
            { Text("Nama tidak boleh kosong", color = MaterialTheme.colorScheme.error) }
          } else null,
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Edit,
              contentDescription = null,
              tint = if (isError) MaterialTheme.colorScheme.error
              else MaterialTheme.colorScheme.onSurfaceVariant
            )
          },
          modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            errorBorderColor = MaterialTheme.colorScheme.error
          ),
          shape = RoundedCornerShape(12.dp),
          singleLine = true
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (name.trim().isNotEmpty()) {
            onSave(name.trim())
          } else {
            isError = true
          }
        },
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.height(48.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Save,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Simpan",
          fontWeight = FontWeight.SemiBold
        )
      }
    },
    dismissButton = {
      TextButton(
        onClick = onDismiss,
        colors = ButtonDefaults.textButtonColors(
          contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.height(48.dp)
      ) {
        Text(
          text = "Batal",
          fontWeight = FontWeight.Medium
        )
      }
    },
    modifier = Modifier.graphicsLayer {
      scaleX = scale
      scaleY = scale
    },
    shape = RoundedCornerShape(24.dp),
    containerColor = MaterialTheme.colorScheme.surface,
    tonalElevation = 6.dp
  )

  // Auto focus pada text field
  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }
}

// Komponen untuk floating save button (alternatif)
@Composable
fun FloatingSaveButton(
  onSaveClick: (String) -> Unit,
  modifier: Modifier = Modifier,
  isVisible: Boolean = true
) {
  var showDialog by remember { mutableStateOf(false) }

  val scale by animateFloatAsState(
    targetValue = if (isVisible) 1f else 0f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessMedium
    )
  )

  val translationY by animateFloatAsState(
    targetValue = if (isVisible) 0f else 100f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioNoBouncy,
      stiffness = Spring.StiffnessMedium
    )
  )

  Box(
    modifier = modifier,
    contentAlignment = Alignment.BottomStart
  ) {
    ExtendedFloatingActionButton(
      onClick = { showDialog = true },
      modifier = Modifier
        .graphicsLayer {
          scaleX = scale
          scaleY = scale
          this.translationY = translationY
        }
        .shadow(
          elevation = 8.dp,
          shape = RoundedCornerShape(16.dp)
        ),
      icon = {
        Icon(
          Icons.Default.Save,
          contentDescription = null,
          modifier = Modifier.size(20.dp)
        )
      },
      text = {
        Text(
          "Simpan Hasil",
          fontWeight = FontWeight.SemiBold
        )
      },
      containerColor = MaterialTheme.colorScheme.secondary,
      contentColor = MaterialTheme.colorScheme.onSecondary
    )
  }

  if (showDialog) {
    SaveDataDialog(
      onDismiss = { showDialog = false },
      onSave = { name ->
        showDialog = false
        onSaveClick(name)
      }
    )
  }
}