package project.fix.skripsi.data.remote.n8n.model

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    @SerializedName("status_code")
    val statusCode: Int? = null,

    @SerializedName("error")
    val error: String? = null,

    @SerializedName("status")
    val status: String? = null
)
