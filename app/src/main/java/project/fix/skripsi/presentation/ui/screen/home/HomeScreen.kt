package project.fix.skripsi.presentation.ui.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import project.fix.skripsi.domain.model.CorrectionType
import project.fix.skripsi.presentation.ui.screen.home.components.answerkey.AnswerKeyDialog
import project.fix.skripsi.presentation.ui.screen.home.components.answerkey.CorrectionTypeDialog
import project.fix.skripsi.presentation.ui.screen.home.components.answerkey.EssayInfoSummary
import project.fix.skripsi.presentation.ui.screen.home.components.previewimages.AdaptiveImagePreviewSection
import project.fix.skripsi.presentation.ui.screen.home.components.previewimages.ImageSourceChooserSection
import project.fix.skripsi.presentation.utils.helper.rememberMediaHelper
import project.fix.skripsi.presentation.viewmodel.EssayViewModel

@Composable
fun HomeScreen(
    onNavigateToResult: () -> Unit,
    viewModel: EssayViewModel
) {
    val context = LocalContext.current

    val essayData by viewModel.essayData.collectAsState()

    val mediaHelper = rememberMediaHelper(
        context = context,
        addImageUri = { uri ->
            viewModel.addSingleImage(uri)
        },
        addImageUris = { uris ->
            viewModel.addSelectedImages(uris)
        }
    )

    var showPreviewRow by remember { mutableStateOf(false) }
    var showMediaOptions by remember { mutableStateOf(false) }
    var showAnswerKeyDialog by remember { mutableStateOf(false) }
    var showCorrectionTypeDialog by remember { mutableStateOf(false) }
    var showDetailEvaluation by remember { mutableStateOf(false) }

    val mediaOptionsVisibility by animateFloatAsState(
        targetValue = if (showMediaOptions) 1f else 0f,
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing
        ),
        label = "mediaOptionsVisibility"
    )

    if (showAnswerKeyDialog) {
        AnswerKeyDialog(
            answerKeyItems = essayData.answerKeyItems,
            onDismiss = { showAnswerKeyDialog = false },
            onAnswerKeySaved = { newAnswerKeyItems ->
                viewModel.updateAnswerKeyItems(newAnswerKeyItems)
                showAnswerKeyDialog = false
            }
        )
    }

    if (showCorrectionTypeDialog) {
        CorrectionTypeDialog(
            currentType = essayData.correctionType,
            onDismiss = { showCorrectionTypeDialog = false },
            onTypeSelected = { type ->
                viewModel.setCorrectionType(type)
                showCorrectionTypeDialog = false
            }
        )
    }

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
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // custom bottomSheet for media picker
                ImageSourceChooserSection(
                    onGalleryClick = {
                        mediaHelper.openGallery()
                        showMediaOptions = false
                    },
                    onCameraClick = {
                        mediaHelper.openCamera()
                        showMediaOptions = false
                    },
                    mediaOptionsVisibility = mediaOptionsVisibility
                )

                // Main bottom bar buttons
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Add Image Button
                    FloatingActionButton(
                        onClick = { showMediaOptions = !showMediaOptions },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        shape = CircleShape,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = if (showMediaOptions) Icons.Rounded.Close else Icons.Rounded.AddPhotoAlternate,
                            contentDescription = "Tambah Gambar",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Answer Key button
                    FloatingActionButton(
                        onClick = { showAnswerKeyDialog = true },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        shape = CircleShape,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.List,
                            contentDescription = "Kunci Jawaban",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Correction Type button
                    FloatingActionButton(
                        onClick = { showCorrectionTypeDialog = true },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        shape = CircleShape,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = if (essayData.correctionType == CorrectionType.AI)
                                Icons.Rounded.Psychology else Icons.Rounded.Key,
                            contentDescription = "Tipe Koreksi",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Mulai Evaluasi Button
                    Button(
                        onClick = {
                            viewModel.evaluateEssay(context)
                            onNavigateToResult()
                        },
                        enabled = essayData.selectedImageUris.isNotEmpty() &&
                                (essayData.correctionType == CorrectionType.AI || essayData.answerKeyItems.isNotEmpty()),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mulai Evaluasi",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            Column {
                if (essayData.selectedImageUris.isNotEmpty()) {
                    AdaptiveImagePreviewSection(
                        uris = essayData.selectedImageUris,
                        showPreviewRow = showPreviewRow,
                        showMediaOptions = showMediaOptions,
                        onTogglePreviewRow = {
                            showPreviewRow = it
                        },
                        onDeleteImage = {
                            viewModel.removeImage(it)
                        },
                        onReorderImages = { newOrderedUris ->
                            viewModel.updateImagesOrder(newOrderedUris)
                        }
                    )
                } else {
                    // Empty state
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Pilih gambar atau foto dokumen essay",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding(),
                start = 40.dp,
                end = 40.dp
            )
        ) {
            // pill button
            AnimatedVisibility(
                visible = essayData.selectedImageUris.isNotEmpty() && !showPreviewRow && !showMediaOptions,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Box(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    AnimatedVisibility(
                        visible = !showDetailEvaluation,
                        enter = fadeIn() + slideInHorizontally(),
                        exit = fadeOut() + slideOutHorizontally()
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f))
                                .clickable { showDetailEvaluation = !showDetailEvaluation }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Detail Evaluasi",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Cek Status Evaluasi",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            // Overlay EssayInfoSummary
            AnimatedVisibility(
                visible = showDetailEvaluation && !showPreviewRow && !showMediaOptions,
                enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutHorizontally(),
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                EssayInfoSummary(
                    imageUris = essayData.selectedImageUris,
                    answerKeyItems = essayData.answerKeyItems,
                    correctionType = essayData.correctionType,
                    onAnswerKeyClick = { showAnswerKeyDialog = true },
                    onCorrectionTypeClick = { showCorrectionTypeDialog = true },
                    onCloseDialog = { showDetailEvaluation = false }
                )
            }
        }
    }
}