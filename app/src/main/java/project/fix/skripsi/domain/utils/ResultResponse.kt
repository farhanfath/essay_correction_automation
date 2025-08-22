package project.fix.skripsi.domain.utils

sealed class ResultResponse<out T> {
    data class Success<out T>(val data: T) : ResultResponse<T>()
    data class Error(
        val message: String,
        val throwable: Throwable? = null
    ) : ResultResponse<Nothing>()
    data object Loading : ResultResponse<Nothing>()
}