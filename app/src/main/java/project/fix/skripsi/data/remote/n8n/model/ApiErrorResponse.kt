package project.fix.skripsi.data.remote.n8n.model

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    @SerializedName("status_code")
    val statusCode: Int? = null,
    val error: String? = null,
    val status: String? = null,
    val result: Any? = null
)
