package project.fix.skripsi.data.remote.n8n.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import project.fix.skripsi.data.remote.n8n.N8nApiService
import project.fix.skripsi.data.remote.n8n.model.WebhookResponse
import project.fix.skripsi.data.utils.createJsonPart
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
                    content = answerKey
                )

                val response = n8nApiService.evaluateEssay(imagePart, evaluationCategoryPart, answerKeyPart)

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
