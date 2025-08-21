package project.fix.skripsi.data.remote.n8n

import okhttp3.MultipartBody
import project.fix.skripsi.BuildConfig
import project.fix.skripsi.data.remote.n8n.model.EssayEvaluationRequest
import project.fix.skripsi.data.remote.n8n.model.WebhookResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface N8nApiService {
    @POST(BuildConfig.ENDPOINT)
    suspend fun evaluateEssayBase64(
        @Body request: EssayEvaluationRequest
    ): Response<WebhookResponse>

    @Multipart
    @POST(BuildConfig.ENDPOINT)
    suspend fun evaluateEssayFile(
        @Part image: MultipartBody.Part,
        @Part evaluationCategory: MultipartBody.Part,
        @Part answerKey: MultipartBody.Part
    ): Response<WebhookResponse>
}