package project.fix.skripsi.data.utils

import com.google.gson.Gson
import project.fix.skripsi.data.remote.n8n.model.ApiErrorResponse
import project.fix.skripsi.data.remote.n8n.model.ApiResponse
import project.fix.skripsi.data.remote.n8n.model.WebhookResponse

fun parseApiResponse(responseString: String): ApiResponse {
    return try {
        val gson = Gson()
        val apiResponse = gson.fromJson(responseString, ApiResponse::class.java)
        apiResponse
    } catch (e: Exception) {
        try {
            val gson = Gson()
            val jsonObject = gson.fromJson(responseString, Map::class.java)

            ApiResponse(
                statusCode = (jsonObject["status_code"] as? Double)?.toInt(),
                status = jsonObject["status"] as? String,
                error = jsonObject["error"] as? String,
                result = jsonObject["result"]
            )
        } catch (e2: Exception) {
            ApiResponse(statusCode = 200, status = "success")
        }
    }
}

fun extractErrorMessage(apiResponse: ApiResponse): String {
    val statusCode = apiResponse.statusCode ?: 400
    val status = apiResponse.status
    val error = apiResponse.error

    // Handle berdasarkan status field
    return when (status) {
        "invalid_file" -> "❌ ${error ?: "File tidak valid"}"
        "file_too_large" -> "📂 ${error ?: "File terlalu besar"}"
        "invalid_format" -> "🖼️ ${error ?: "Format gambar tidak didukung"}"
        "poor_quality" -> "📷 ${error ?: "Kualitas gambar kurang baik"}"
        "invalid_content" -> "📝 ${error ?: "Konten gambar tidak valid"}"
        "no_text_content" -> "🔍 ${error ?: "Tidak ada teks yang dapat dibaca"}"
        "extraction_failed" -> "⚠️ ${error ?: "Gagal mengekstrak teks"}"
        "server_error" -> "🔧 ${error ?: "Server error"}"
        "invalid_base64" -> "🔢 ${error ?: "Format base64 tidak valid"}"
        "empty_data" -> "📭 ${error ?: "Data gambar kosong"}"
        else -> "❌ ${error ?: "Gambar tidak dapat diproses (Status: $statusCode)"}"
    }
}