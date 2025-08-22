package project.fix.skripsi.domain.exception

class CallException(
    val statusCode: Int,
    override val message: String,
    val status: String? = null
) : Throwable() {
    override fun getLocalizedMessage(): String = message

    companion object {
        fun fromApiError(statusCode: Int, errorMessage: String, status: String?) =
            CallException(statusCode, errorMessage, status)
    }
}