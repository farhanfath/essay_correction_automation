package project.fix.skripsi.data.repository

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import project.fix.skripsi.data.remote.n8n.model.N8nResponse
import java.io.File
import java.io.FileOutputStream

//class EssayRepository {
//
//    suspend fun evaluateEssay(imageUri: Uri, context: Context): Result<List<N8nResponse>> {
//        return withContext(Dispatchers.IO) {
//            try {
//                // Convert Uri to File
//                val file = uriToFile(imageUri, context)
//
//                // Create MultipartBody.Part
//                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//                val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
//
//                // Make API call
//                val response = RetrofitClient.api.evaluateEssay(imagePart)
//
//                if (response.isSuccessful && response.body() != null) {
//                    Result.success(response.body()!!)
//                } else {
//                    Result.failure(Exception("API call failed: ${response.code()} ${response.message()}"))
//                }
//            } catch (e: Exception) {
//                Result.failure(e)
//            }
//        }
//    }
//
//    private fun uriToFile(uri: Uri, context: Context): File {
//        val inputStream = context.contentResolver.openInputStream(uri)
//        val tempFile = File.createTempFile("image", ".jpg", context.cacheDir)
//        val outputStream = FileOutputStream(tempFile)
//
//        inputStream?.use { input ->
//            outputStream.use { output ->
//                input.copyTo(output)
//            }
//        }
//
//        return tempFile
//    }
//}