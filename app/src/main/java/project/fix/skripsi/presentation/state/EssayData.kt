package project.fix.skripsi.presentation.state

import android.net.Uri
import project.fix.skripsi.domain.model.AnswerKeyItem
import project.fix.skripsi.domain.model.CorrectionType
import project.fix.skripsi.domain.model.EssayCategory

data class EssayData(
  val selectedImageUris: List<Uri> = emptyList(),
  val selectedCategory: EssayCategory? = null,
  val answerKeyItems: List<AnswerKeyItem> = emptyList(),
  val correctionType: CorrectionType = CorrectionType.AI
)