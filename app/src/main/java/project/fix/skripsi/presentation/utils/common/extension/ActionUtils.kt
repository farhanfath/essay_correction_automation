package project.fix.skripsi.presentation.utils.common.extension

import android.content.Context
import android.widget.Toast

object ActionUtils {
    fun showToast(
        context: Context,
        message: String,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        Toast.makeText(context, message, duration).show()
    }
}