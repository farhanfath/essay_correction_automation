package project.fix.skripsi.data.remote.n8n.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import project.fix.skripsi.data.remote.n8n.N8nApiService
import project.fix.skripsi.data.remote.n8n.model.WebhookResponse
import project.fix.skripsi.data.utils.createJsonPart
import project.fix.skripsi.data.utils.extractErrorMessage
import project.fix.skripsi.data.utils.parseApiResponse
import project.fix.skripsi.data.utils.toImagePart
import project.fix.skripsi.domain.model.AnswerKeyItem
import java.io.File
import javax.inject.Inject

class N8nDataSourceImpl @Inject constructor(
    private val n8nApiService: N8nApiService
) : N8nDataSource {

    override suspend fun evaluateEssay(
        imageFile: File,
        evaluationCategory: String,
        answerKey: List<AnswerKeyItem>
    ): Result<WebhookResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val imagePart = imageFile.toImagePart("image")

                val evaluationCategoryPart = createJsonPart(
                    partName = "evaluation_category",
                    content = mapOf("source" to "android", "evaluationCategory" to evaluationCategory)
                )

                val answerKeyPart = createJsonPart(
                    partName = "answer_key",
                    content = answerKey,
                )

                val response = n8nApiService.evaluateEssayFile(imagePart, evaluationCategoryPart, answerKeyPart)

                if (response.isSuccessful) {
                    val responseBodyString = response.body()?.toString() ?: ""

                    val apiResponse = parseApiResponse(responseBodyString)

                    when (apiResponse.statusCode) {
                        200 -> {
                            response.body()?.let {
                                Result.success(it)
                            } ?: Result.failure(Exception("Response body kosong"))
                        }
                        else -> {
                            val errorMessage = extractErrorMessage(apiResponse)
                            Result.failure(Exception(errorMessage))
                        }
                    }
                } else {
                    Result.failure(Exception("Network error: ${response.code()} ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

// sample with base64

//    override suspend fun evaluateEssay(
//        imageFile: File,
//        evaluationCategory: String,
//        answerKey: List<AnswerKeyItem>
//    ): Result<WebhookResponse> {
//        return withContext(Dispatchers.IO) {
//            try {
//                val imageBase64 = Base64.encodeToString(imageFile.readBytes(), Base64.NO_WRAP)
//
//                val requestBody = EssayEvaluationRequest(
//                    source = "android",
//                    evaluationCategory = evaluationCategory,
//                    answerKey = answerKey,
//                    imageBase64 = imageBase64
//                )
//
//                val response = n8nApiService.evaluateEssay(requestBody)
//
//                if (response.isSuccessful) {
//                    val responseBodyString = response.body()?.toString() ?: ""
//
//                    // Parse response body untuk cek status_code
//                    val apiResponse = parseApiResponse(responseBodyString)
//
//                    when (apiResponse.statusCode) {
//                        200 -> {
//                            // Success - return original response body
//                            response.body()?.let {
//                                Result.success(it)
//                            } ?: Result.failure(Exception("Response body kosong"))
//                        }
//                        else -> {
//                            // Error - extract error message
//                            val errorMessage = extractErrorMessage(apiResponse)
//                            Result.failure(Exception(errorMessage))
//                        }
//                    }
//                } else {
//                    Result.failure(Exception("Network error: ${response.code()} ${response.message()}"))
//                }
//            } catch (e: Exception) {
//                Result.failure(e)
//            }
//        }
//    }
}
