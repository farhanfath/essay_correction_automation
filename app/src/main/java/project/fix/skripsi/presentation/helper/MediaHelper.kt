package project.fix.skripsi.presentation.helper

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import java.io.File

class MediaHelper(
    val openGallery: () -> Unit,
    val openCamera: () -> Unit
)

@Composable
fun rememberMediaHelper(
    context: Context,
    setImageUri: (Uri) -> Unit
): MediaHelper {
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { setImageUri(it) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempImageUri?.let { setImageUri(it) }
        }
    }

    return remember {
        MediaHelper(
            openGallery = {
                galleryLauncher.launch("image/*")
            },
            openCamera = {
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
        )
    }
}
