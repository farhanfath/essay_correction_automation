package project.fix.skripsi.presentation.ui.screen.home

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import project.fix.skripsi.presentation.ui.screen.home.components.previewimages.ImagePreviewSection
import project.fix.skripsi.presentation.ui.screen.home.sample.PreviewMergedImage
import project.fix.skripsi.presentation.utils.common.base.state.UiState
import project.fix.skripsi.presentation.utils.helper.mergeImagesVertically
import project.fix.skripsi.presentation.utils.helper.rememberMediaHelper
import project.fix.skripsi.presentation.utils.helper.uriToBitmap
import project.fix.skripsi.presentation.viewmodel.EssayViewModel

@Composable
fun HomeScreen(
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

    Scaffold(
        topBar = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
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
                HorizontalDivider(modifier = Modifier.padding(10.dp))
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            if (imageUris.isNotEmpty()) {
                item {
                    ImagePreviewSection(
                        uris = imageUris,
                        context = context
                    )
                }
            }

            item {
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
            }
        }
    }
}