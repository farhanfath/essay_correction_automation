package project.fix.skripsi.data.remote.n8n.datasource

import android.content.Context
import android.net.Uri
import project.fix.skripsi.data.remote.n8n.model.WebhookResponse

interface N8nDataSource {
    suspend fun evaluateEssay(imageUri: Uri, context: Context): Result<WebhookResponse>
}