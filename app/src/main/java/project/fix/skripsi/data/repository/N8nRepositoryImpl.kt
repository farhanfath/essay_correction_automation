package project.fix.skripsi.data.repository

import project.fix.skripsi.data.remote.n8n.datasource.N8nDataSource
import project.fix.skripsi.domain.exception.CallException
import project.fix.skripsi.domain.model.AnswerKeyItem
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.repository.N8nRepository
import project.fix.skripsi.domain.utils.ResultResponse
import project.fix.skripsi.presentation.utils.helper.ErrorMessageHelper
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

            when {
                result.isSuccess -> {
                    val hasilKoreksi = result.getOrNull()
                    if (hasilKoreksi != null) {
                        ResultResponse.Success(hasilKoreksi)
                    } else {
                        ResultResponse.Error("Data hasil evaluasi tidak valid")
                    }
                }
                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    val errorMessage = if (exception is CallException) {
                        ErrorMessageHelper.getDetailedErrorMessage(exception.message, exception)
                    } else {
                        exception?.message ?: "Terjadi kesalahan tidak diketahui"
                    }
                    ResultResponse.Error(errorMessage)
                }
                else -> {
                    ResultResponse.Error("Terjadi kesalahan tidak diketahui")
                }
            }
        } catch (e: Exception) {
            ResultResponse.Error("Kesalahan: ${e.message}")
        }
    }
}
