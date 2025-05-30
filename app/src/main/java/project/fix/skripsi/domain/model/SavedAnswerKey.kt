package project.fix.skripsi.domain.model

// Data class untuk template kunci jawaban yang tersimpan
data class SavedAnswerKey(
  val id: Int,
  val title: String,
  val createdAt: Long,
  val answerKeys: List<AnswerKeyItem>
)