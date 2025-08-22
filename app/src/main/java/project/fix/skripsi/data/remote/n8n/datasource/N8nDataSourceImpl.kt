package project.fix.skripsi.data.remote.n8n.datasource

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import project.fix.skripsi.data.remote.n8n.N8nApiService
import project.fix.skripsi.data.remote.n8n.model.ApiErrorResponse
import project.fix.skripsi.data.remote.n8n.model.WebhookResponse
import project.fix.skripsi.data.utils.createJsonPart
import project.fix.skripsi.data.utils.toImagePart
import project.fix.skripsi.domain.exception.CallException
import project.fix.skripsi.domain.model.AnswerKeyItem
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
    ): Result<WebhookResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val imagePart = imageFile.toImagePart("image")

                val gson = Gson()
                val answerKeyJson = gson.toJson(answerKey)

                val response = n8nApiService.evaluateEssayWithQuery(
                    image = imagePart,
                    evaluationCategory = evaluationCategory,
                    answerKeyJson = answerKeyJson
                )

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!

                    val responseString = gson.toJson(responseBody)

                    if (isErrorResponse(responseString)) {
                        val errorResponse = parseErrorResponse(responseString)
                        Result.failure(
                            CallException.fromApiError(
                                statusCode = errorResponse.statusCode ?: 400,
                                errorMessage = errorResponse.error ?: "Unknown error",
                                status = errorResponse.status
                            )
                        )
                    } else {
                        if (isValidSuccessResponse(responseBody)) {
                            Result.success(responseBody)
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
                } else {
                    // Handle HTTP error codes
                    val errorMessage = when (response.code()) {
                        400 -> "Data yang dikirim tidak valid"
                        401 -> "Tidak memiliki akses ke layanan"
                        403 -> "Akses ditolak"
                        404 -> "Layanan tidak ditemukan"
                        500 -> "Server mengalami masalah"
                        else -> "Network error: ${response.code()} ${response.message()}"
                    }
                    Result.failure(
                        CallException.fromApiError(
                            statusCode = response.code(),
                            errorMessage = errorMessage,
                            status = "http_error"
                        )
                    )
                }
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

    private fun isErrorResponse(responseString: String): Boolean {
        return try {
            responseString.contains("status_code") && responseString.contains("error")
        } catch (e: Exception) {
            false
        }
    }

    private fun parseErrorResponse(responseString: String): ApiErrorResponse {
        return try {
            val jsonArray = gson.fromJson(responseString, Array<ApiErrorResponse>::class.java)
            jsonArray.firstOrNull() ?: ApiErrorResponse()
        } catch (e: JsonSyntaxException) {
            ApiErrorResponse(
                statusCode = 400,
                error = "Format response tidak valid",
                status = "parse_error"
            )
        }
    }

    private fun isValidSuccessResponse(response: WebhookResponse): Boolean {
        return try {
            // Cek apakah response memiliki data yang valid
            response.resultData?.isNotEmpty() == true
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
