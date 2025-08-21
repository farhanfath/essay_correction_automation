package project.fix.skripsi.data.remote.n8n.model

import project.fix.skripsi.domain.model.AnswerKeyItem

data class EssayEvaluationRequest(
    val source: String,
    val evaluationCategory: String,
    val answerKey: List<AnswerKeyItem>,
    val imageBase64: String
)