package project.fix.skripsi.domain.utils

sealed class ResultResponse<out T> {
    data class Success<out T>(val data: T) : ResultResponse<T>()
    data class Error(val message: String) : ResultResponse<Nothing>()
    data object Loading : ResultResponse<Nothing>()
}