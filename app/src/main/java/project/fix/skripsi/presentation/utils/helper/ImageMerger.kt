package project.fix.skripsi.presentation.utils.helper

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.graphics.createBitmap

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
