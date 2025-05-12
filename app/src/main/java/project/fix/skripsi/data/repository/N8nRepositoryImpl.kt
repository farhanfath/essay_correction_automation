package project.fix.skripsi.data.repository

import android.content.Context
import android.net.Uri
import project.fix.skripsi.data.mapper.toDomain
import project.fix.skripsi.data.remote.n8n.datasource.N8nDataSource
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.repository.N8nRepository
import project.fix.skripsi.domain.utils.ResultResponse
import javax.inject.Inject

class N8nRepositoryImpl @Inject constructor(
    private val n8nDataSource: N8nDataSource
) : N8nRepository {
    override suspend fun evaluateEssay(imageUri: Uri, context: Context): ResultResponse<HasilKoreksi> {
        return try {
            val result = n8nDataSource.evaluateEssay(imageUri, context)
            result.fold(
                onSuccess = { response ->
                    ResultResponse.Success(response.toDomain())
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