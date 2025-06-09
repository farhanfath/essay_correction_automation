package project.fix.skripsi.data.repository

import project.fix.skripsi.data.remote.n8n.datasource.N8nDataSource
import project.fix.skripsi.data.remote.n8n.model.WebhookResponse
import project.fix.skripsi.domain.model.AnswerKeyItem
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.repository.N8nRepository
import project.fix.skripsi.domain.utils.ResultResponse
import java.io.File
import javax.inject.Inject

class N8nRepositoryImpl @Inject constructor(
    private val n8nDataSource: N8nDataSource
) : N8nRepository {
    override suspend fun evaluateEssay(
        imageFile: File,
        evaluationCategory: String,
        answerKey: List<AnswerKeyItem>
    ): ResultResponse<HasilKoreksi> {
        return try {
            val result = n8nDataSource.evaluateEssay(imageFile, evaluationCategory, answerKey)
            result.fold(
                onSuccess = {
                    val response = WebhookResponse.transform(it)
                    ResultResponse.Success(response)
                },
                onFailure = { e ->
                    ResultResponse.Error(e.message ?: "Unexpected error")
                }
            )
        } catch (e: Exception) {
            ResultResponse.Error(e.message ?: "Unexpected error")
        }
    }
}
