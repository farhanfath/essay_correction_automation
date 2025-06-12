package project.fix.skripsi.domain.model

data class SavedScoreHistory(
  val id: Long = 0,
  val title: String,
  val hasilKoreksi: List<HasilKoreksi>,
  val createdAt: Long = System.currentTimeMillis()
)
