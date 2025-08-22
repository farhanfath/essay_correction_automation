package project.fix.skripsi.data.utils

import com.google.gson.Gson
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.nio.charset.StandardCharsets

fun File.toImagePart(partName: String): MultipartBody.Part {
  val requestFile = asRequestBody("image/*".toMediaTypeOrNull())
  return MultipartBody.Part.createFormData(partName, name, requestFile)
}

fun createJsonPart(partName: String, content: Any): MultipartBody.Part {
  val gson = Gson()
  val json = gson.toJson(content)

  val jsonBytes = json.toByteArray(StandardCharsets.UTF_8)

  val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

  val filename = "${partName}.json"

  // Create with filename extension
  val part = MultipartBody.Part.createFormData(partName, filename, requestBody)

  return part
}