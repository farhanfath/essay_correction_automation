package project.fix.skripsi.data.remote.n8n

import okhttp3.MultipartBody
import project.fix.skripsi.data.remote.n8n.model.N8nResponse
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface N8nApi {
    @Multipart
    @POST("ENDPOINT") // Replace with your N8N webhook endpoint
    suspend fun evaluateEssay(
        @Part image: MultipartBody.Part
    ): Response<List<N8nResponse>>
}