package project.fix.skripsi.domain.repository

import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.utils.ResultResponse
import java.io.File

interface N8nRepository {
    suspend fun evaluateEssay(
        imageFile: File,
        quizType: String,
        evaluationCategory: String,
        answerKey: List<String>
    ): ResultResponse<HasilKoreksi>
}
