package project.fix.skripsi.data.remote.n8n.datasource

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import project.fix.skripsi.data.base.model.BaseApiResponse
import project.fix.skripsi.data.remote.n8n.N8nApiService
import project.fix.skripsi.data.remote.n8n.model.ApiErrorResponse
import project.fix.skripsi.data.remote.n8n.model.EssayEvaluationData
import project.fix.skripsi.data.utils.toImagePart
import project.fix.skripsi.domain.exception.CallException
import project.fix.skripsi.domain.model.AnswerKeyItem
import project.fix.skripsi.domain.model.HasilKoreksi
import java.io.File
import javax.inject.Inject

class N8nDataSourceImpl @Inject constructor(
    private val n8nApiService: N8nApiService
) : N8nDataSource {

    private val gson = Gson()

    override suspend fun evaluateEssay(
        imageFile: File,
        evaluationCategory: String,
        answerKey: List<AnswerKeyItem>
    ): Result<HasilKoreksi> {
        return withContext(Dispatchers.IO) {
            try {
                val imagePart = imageFile.toImagePart("image")
                val answerKeyJson = gson.toJson(answerKey)

                val response = n8nApiService.evaluateEssayWithQuery(
                    image = imagePart,
                    evaluationCategory = evaluationCategory,
                    answerKeyJson = answerKeyJson
                )

                handleApiResponse(response.body()!!)
            } catch (e: Exception) {
                Result.failure(
                    CallException.fromApiError(
                        statusCode = -1,
                        errorMessage = "Kesalahan tidak terduga: ${e.message}",
                        status = "unexpected_error"
                    )
                )
            }
        }
    }

    private fun handleApiResponse(apiResponse: BaseApiResponse<EssayEvaluationData>): Result<HasilKoreksi> {
        return when {
            apiResponse.isSuccess() && apiResponse.data != null -> {
                // Success case
                val evaluationData = apiResponse.data
                if (isValidEvaluationData(evaluationData)) {
                    val hasilKoreksi = EssayEvaluationData.transform(evaluationData)
                    Result.success(hasilKoreksi)
                } else {
                    Result.failure(
                        CallException.fromApiError(
                            statusCode = 200,
                            errorMessage = "Data hasil evaluasi kosong atau tidak valid",
                            status = "empty_data"
                        )
                    )
                }
            }
            apiResponse.isError() -> {
                // Error case
                Result.failure(
                    CallException.fromApiError(
                        statusCode = apiResponse.statusCode,
                        errorMessage = apiResponse.error ?: "Unknown error occurred",
                        status = apiResponse.status
                    )
                )
            }
            else -> {
                // Unexpected case
                Result.failure(
                    CallException.fromApiError(
                        statusCode = apiResponse.statusCode,
                        errorMessage = "Response tidak dalam format yang diharapkan",
                        status = "invalid_response"
                    )
                )
            }
        }
    }

    private fun isValidEvaluationData(data: EssayEvaluationData): Boolean {
        return try {
            !data.resultData.isNullOrEmpty() &&
                    data.evaluationType != null
        } catch (e: Exception) {
            false
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
