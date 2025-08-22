package project.fix.skripsi.data.base.model

import com.google.gson.annotations.SerializedName

data class BaseApiResponse<T>(
    @SerializedName("status_code")
    val statusCode: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("data")
    val data: T? = null,

    @SerializedName("error")
    val error: String? = null
) {
    fun isSuccess(): Boolean = statusCode == 200 && status == "success"
    fun isError(): Boolean = !isSuccess()
}