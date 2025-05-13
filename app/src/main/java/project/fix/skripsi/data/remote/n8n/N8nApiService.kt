package project.fix.skripsi.data.remote.n8n

import okhttp3.MultipartBody
import project.fix.skripsi.data.remote.n8n.model.WebhookResponse
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import project.fix.skripsi.BuildConfig
import retrofit2.http.Part

interface N8nApiService {
    @Multipart
    @POST(BuildConfig.ENDPOINT)
    suspend fun evaluateEssay(
        @Part image: MultipartBody.Part
    ): Response<WebhookResponse>
}