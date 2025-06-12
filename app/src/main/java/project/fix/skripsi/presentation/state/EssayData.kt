package project.fix.skripsi.presentation.state

import android.net.Uri
import project.fix.skripsi.domain.model.AnswerKeyItem
import project.fix.skripsi.domain.model.constants.CorrectionType

data class EssayData(
  val selectedImageUris: List<Uri> = emptyList(),
  val answerKeyItems: List<AnswerKeyItem> = emptyList(),
  val correctionType: CorrectionType = CorrectionType.AI
)