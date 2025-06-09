package project.fix.skripsi.data.remote.n8n.datasource

import project.fix.skripsi.data.remote.n8n.model.WebhookResponse
import project.fix.skripsi.domain.model.AnswerKeyItem
import java.io.File

interface N8nDataSource {
    suspend fun evaluateEssay(
        imageFile: File,
        evaluationCategory: String,
        answerKey: List<AnswerKeyItem>
    ): Result<WebhookResponse>
}
