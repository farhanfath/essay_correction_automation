package project.fix.skripsi.presentation.utils

import androidx.compose.runtime.Composable
import project.fix.skripsi.domain.utils.ResultResponse

@Composable
fun <T> HandlerResponseCompose(
  response: ResultResponse<T>,
  onInitiate: @Composable () -> Unit = {},
  onLoading: @Composable () -> Unit,
  onSuccess: @Composable (data: T) -> Unit,
  onError: @Composable (error: ResultResponse.Error) -> Unit
) {
  when (response) {
    is ResultResponse.Initiate -> onInitiate()
    is ResultResponse.Loading -> onLoading()
    is ResultResponse.Success -> onSuccess(response.data)
    is ResultResponse.Error -> onError(response)
  }
}

fun <T> customResponseHandler(
  response: ResultResponse<T>,
  onInitiate: () -> Unit ={},
  onLoading: () -> Unit = {},
  onSuccess: (data: T) -> Unit = {},
  onError: (error: ResultResponse.Error) -> Unit = {}
) {
  when (response) {
    is ResultResponse.Initiate -> onInitiate()
    is ResultResponse.Loading -> onLoading()
    is ResultResponse.Success -> onSuccess(response.data)
    is ResultResponse.Error -> onError(response)
  }
}