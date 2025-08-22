package project.fix.skripsi.data.remote.n8n

import okhttp3.MultipartBody
import project.fix.skripsi.BuildConfig
import project.fix.skripsi.data.base.model.BaseApiResponse
import project.fix.skripsi.data.remote.n8n.model.EssayEvaluationData
import project.fix.skripsi.data.remote.n8n.model.EssayEvaluationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface N8nApiService {
    @POST(BuildConfig.ENDPOINT)
    suspend fun evaluateEssayBase64(
        @Body request: EssayEvaluationRequest
    ): Response<BaseApiResponse<EssayEvaluationData>>

    @Multipart
    @POST(BuildConfig.ENDPOINT)
    suspend fun evaluateEssayFile(
        @Part image: MultipartBody.Part,
        @Part evaluationCategory: MultipartBody.Part,
        @Part answerKey: MultipartBody.Part
    ): Response<BaseApiResponse<EssayEvaluationData>>

    @Multipart
    @POST(BuildConfig.ENDPOINT)
    suspend fun evaluateEssayWithQuery(
        @Part image: MultipartBody.Part,
        @Query("evaluation_category") evaluationCategory: String,
        @Query("answer_key") answerKeyJson: String,
        @Query("source") source: String = "android"
    ): Response<BaseApiResponse<EssayEvaluationData>>
}