package project.fix.skripsi.domain.usecase

import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.repository.N8nRepository
import project.fix.skripsi.domain.utils.ResultResponse
import java.io.File
import javax.inject.Inject

class EvaluateEssayUseCase @Inject constructor(
  private val n8nRepository: N8nRepository
) {
  suspend operator fun invoke(imageFile: File): ResultResponse<HasilKoreksi> {
    return n8nRepository.evaluateEssay(imageFile)
  }
}