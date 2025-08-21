package project.fix.skripsi.data.utils

import com.google.gson.Gson
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

fun File.toImagePart(partName: String): MultipartBody.Part {
  val requestFile = asRequestBody("image/*".toMediaTypeOrNull())
  return MultipartBody.Part.createFormData(partName, name, requestFile)
}

fun createJsonPart(partName: String, content: Any): MultipartBody.Part {
  val json = Gson().toJson(content)
  val requestBody = json.toRequestBody("json/plain".toMediaTypeOrNull())
  return MultipartBody.Part.create(
    Headers.headersOf("Content-Disposition", "form-data; name=\"$partName\""),
    requestBody
  )
}