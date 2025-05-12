package project.fix.skripsi.domain.utils

sealed class ResultResponse<out T> {
    data object Initiate: ResultResponse<Nothing>()
    data object Loading : ResultResponse<Nothing>()
    data class Success<T>(val data: T) : ResultResponse<T>()
    data class Error(val message: String) : ResultResponse<Nothing>()
}