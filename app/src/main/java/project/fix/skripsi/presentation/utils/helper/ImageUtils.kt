package project.fix.skripsi.presentation.utils.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.core.graphics.createBitmap
import java.io.ByteArrayOutputStream

fun mergeImagesVertically(bitmaps: List<Bitmap>): Bitmap {
  val width = bitmaps.maxOf { it.width }
  val totalHeight = bitmaps.sumOf { it.height }

  val result = createBitmap(width, totalHeight)
  val canvas = Canvas(result)
  var currentHeight = 0

  for (bitmap in bitmaps) {
    canvas.drawBitmap(bitmap, 0f, currentHeight.toFloat(), null)
    currentHeight += bitmap.height
  }

  return result
}

fun optimizeForOCR(bitmap: Bitmap): Bitmap {
    val fileSize = bitmap.byteCount

    return when {
        fileSize < 5_000_000 -> bitmap // <5MB: Keep original
        fileSize < 15_000_000 -> compressBitmapProcess(bitmap, 90) // 5-15MB: Light compression
        else -> compressBitmapProcess(bitmap, 75) // >15MB: Moderate compression
    }
}

private fun compressBitmapProcess(bitmap: Bitmap, quality: Int): Bitmap {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
    return BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size())
}