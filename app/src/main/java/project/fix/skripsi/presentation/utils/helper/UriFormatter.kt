package project.fix.skripsi.presentation.utils.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream

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