package project.fix.skripsi.data.remote.n8n.datasource

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import project.fix.skripsi.data.remote.n8n.N8nApiService
import project.fix.skripsi.data.remote.n8n.model.WebhookResponse
import java.io.File
import javax.inject.Inject

class N8nDataSourceImpl @Inject constructor(
    private val n8nApiService: N8nApiService
) : N8nDataSource {

    override suspend fun evaluateEssay(
        imageFile: File,
        quizType: String,
        evaluationCategory: String,
        answerKey: List<String>
    ): Result<WebhookResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // image data
                val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

                // essayType as JSON string
                val quizCategoryJsonString = """{"source":"android","quizType":"$quizType"}"""
                val quizCategoryRequestBody = quizCategoryJsonString.toRequestBody("text/plain".toMediaTypeOrNull())
                val quizTypePart = MultipartBody.Part.create(
                    Headers.headersOf("Content-Disposition", "form-data; name=\"type\""),
                    quizCategoryRequestBody
                )

                // evaluation_category
                val evaluationCategoryJsonString = """{"source":"android","evaluationCategory":"$evaluationCategory"}"""
                val evaluationCategoryRequestBody = evaluationCategoryJsonString.toRequestBody("text/plain".toMediaTypeOrNull())
                val evaluationCategoryPart = MultipartBody.Part.create(
                    Headers.headersOf("Content-Disposition", "form-data; name=\"evaluation_category\""),
                    evaluationCategoryRequestBody
                )

                // answer_key
                val answerKeyJson = Gson().toJson(answerKey)
                val answerKeyBody = answerKeyJson.toRequestBody("text/plain".toMediaTypeOrNull())
                val answerKeyPart = MultipartBody.Part.create(
                    Headers.headersOf("Content-Disposition", "form-data; name=\"answer_key\""),
                    answerKeyBody
                )

                val response = n8nApiService.evaluateEssay(imagePart, quizTypePart, evaluationCategoryPart, answerKeyPart)

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("API call failed: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
