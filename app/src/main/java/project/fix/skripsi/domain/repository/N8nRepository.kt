package project.fix.skripsi.domain.repository

import android.content.Context
import android.net.Uri
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.utils.ResultResponse

interface N8nRepository {
    suspend fun evaluateEssay(imageUri: Uri, context: Context): ResultResponse<HasilKoreksi>
}