package project.fix.skripsi.presentation.utils.helper

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.io.File
import java.io.FileOutputStream

class MediaHelper(
    val openGallery: () -> Unit,
    val openCamera: () -> Unit
)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberMediaHelper(
    context: Context,
    addImageUri: (Uri) -> Unit,
    addImageUris: (List<Uri>) -> Unit,
): MediaHelper {
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var shouldLaunchCamera by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) addImageUris(uris)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempImageUri?.let { addImageUri(it) }
        }
    }

    fun launchCamera() {
        val photoFile = File.createTempFile(
            "camera_photo_${System.currentTimeMillis()}",
            ".jpg",
            context.cacheDir
        )
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
        tempImageUri = uri
        cameraLauncher.launch(uri)
    }

    LaunchedEffect(cameraPermissionState.status) {
        if (cameraPermissionState.status.isGranted && shouldLaunchCamera) {
            launchCamera()
            shouldLaunchCamera = false
        } else if (!cameraPermissionState.status.isGranted && !cameraPermissionState.status.shouldShowRationale && shouldLaunchCamera) {
            Toast.makeText(context, "Izin kamera diperlukan untuk mengambil foto", Toast.LENGTH_LONG).show()
            shouldLaunchCamera = false
        }
    }


    return remember {
        MediaHelper(
            openGallery = {
                galleryLauncher.launch("image/*")
            },
            openCamera = {
                if (cameraPermissionState.status.isGranted) {
                    launchCamera()
                } else {
                    shouldLaunchCamera = true
                    cameraPermissionState.launchPermissionRequest()
                }
            }
        )
    }
}

fun uriToBitmap(
    context: Context,
    image: Uri
) : Bitmap {
    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val source = ImageDecoder.createSource(context.contentResolver, image)
        ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            decoder.isMutableRequired = true
        }
    } else {
        @Suppress("DEPRECATION")
        MediaStore.Images.Media.getBitmap(context.contentResolver, image)
    }
    return bitmap
}

fun uriToTempFile(
    context: Context,
    uri: Uri
): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val tempFile = File.createTempFile("essay_image", ".jpg", context.cacheDir)
    inputStream?.use { input ->
        FileOutputStream(tempFile).use { output ->
            input.copyTo(output)
        }
    }
    return tempFile
}

fun bitmapToTempFile(context: Context, bitmap: Bitmap): File {
    val file = File.createTempFile(
        "merged_essay_${System.currentTimeMillis()}",
        ".jpg",
        context.cacheDir
    )

    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }

    return file
}