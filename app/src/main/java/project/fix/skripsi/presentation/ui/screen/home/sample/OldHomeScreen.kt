package project.fix.skripsi.presentation.ui.screen.home.sample

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun PreviewMergedImage(mergedBitmap: Bitmap) {
    Image(
        bitmap = mergedBitmap.asImageBitmap(),
        contentDescription = "Merged Image",
        modifier = Modifier.fillMaxWidth()
    )
}

//@Composable
//fun OldHomeScreen(
//    navController: NavController,
//    viewModel: EssayViewModel
//) {
//    val context = LocalContext.current
//    val imageUris = remember { mutableStateListOf<Uri>() }
//
//    val resultState by viewModel.result.collectAsState()
//
//    val mediaHelper = rememberMediaHelper(
//        context = context,
//        addImageUri = { uri ->
//            imageUris.add(uri)
//            viewModel.setSelectedImage(uri)
//        },
//        addImageUris = { uris ->
//            imageUris.addAll(uris)
//            viewModel.addSelectedImages(uris)
//        }
//    )
//
//    /**
//     * todo: deleted soon
//     */
//    var showSampleMergeDialog by remember { mutableStateOf(false) }
//    var mergedBitmap by remember { mutableStateOf<Bitmap?>(null) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // App Title
//        Text(
//            text = "Evaluasi Essay Otomatis",
//            style = MaterialTheme.typography.headlineMedium,
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.padding(vertical = 16.dp)
//        )
//
//        // Image Preview
////        viewModel.selectedImageUri?.let { uri ->
////            EnhancedPreviewCard(
////                bitmap = uriToBitmap(context, uri),
////                uiState = resultState
////            )
////        }
//
//        LazyRow(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            items(imageUris) { uri ->
//                Image(
//                    bitmap = uriToBitmap(context, uri).asImageBitmap(),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(120.dp)
//                        .padding(8.dp)
//                        .clip(RoundedCornerShape(12.dp))
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Image Source Options
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            ElevatedButton(
//                onClick = {
//                    mediaHelper.openGallery()
//                },
//                modifier = Modifier.weight(1f),
//                colors = ButtonDefaults.elevatedButtonColors(
//                    containerColor = MaterialTheme.colorScheme.secondaryContainer
//                )
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Image,
//                        contentDescription = "Gallery"
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Galeri")
//                }
//            }
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            ElevatedButton(
//                onClick = {
//                    mediaHelper.openCamera()
//                },
//                modifier = Modifier.weight(1f),
//                colors = ButtonDefaults.elevatedButtonColors(
//                    containerColor = MaterialTheme.colorScheme.secondaryContainer
//                )
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.CameraAlt,
//                        contentDescription = "Camera"
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Kamera")
//                }
//            }
//        }
//
//        /**
//         * todo: delete soon
//         */
//        Row(
//            modifier = Modifier
//                .padding(vertical = 1.dp)
//                .fillMaxWidth()
//        ) {
//            Button(
//                onClick = {
//                    val bitmaps = imageUris.map { uri ->
//                        uriToBitmap(context, uri)
//                    }
//                    mergedBitmap = mergeImagesVertically(bitmaps)
//                },
//                enabled = resultState !is UiState.Loading,
//                modifier = Modifier
//                    .weight(1f)
//                    .height(56.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.primary
//                ),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Text(
//                    text = "gabungkan gambar"
//                )
//            }
//            Button(
//                onClick = {
//                    showSampleMergeDialog = true
//                },
//                enabled = resultState !is UiState.Loading,
//                modifier = Modifier
//                    .weight(1f)
//                    .height(56.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.primary
//                ),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Text(
//                    text = "tampilkan preview gabungan gambar"
//                )
//            }
//        }
//        Spacer(modifier = Modifier.height(32.dp))
//
//        // Evaluate Button
//        Button(
//            onClick = {
//                viewModel.evaluateEssay(context)
//                navController.navigate("result")
//            },
//            enabled = resultState !is UiState.Loading,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(56.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.primary
//            ),
//            shape = RoundedCornerShape(12.dp)
//        ) {
//            if (resultState is UiState.Loading) {
//                LoadingAnimation()
//            } else {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Check,
//                        contentDescription = "Evaluate"
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = "Nilai Essay",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//        }
//
//        // Error message if any
//        when (val state = resultState) {
//            is UiState.Error -> {
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(
//                    text = state.message,
//                    color = MaterialTheme.colorScheme.error,
//                    textAlign = TextAlign.Center
//                )
//            }
//            else -> {}
//        }
//
//        if (showSampleMergeDialog) {
//            Dialog(
//                onDismissRequest = {
//                    showSampleMergeDialog = false
//                }
//            ) {
//                if (mergedBitmap != null) {
//                    mergedBitmap?.let {
//                        PreviewMergedImage(mergedBitmap = it)
//                    }
//                }
//            }
//        }
//    }
//}