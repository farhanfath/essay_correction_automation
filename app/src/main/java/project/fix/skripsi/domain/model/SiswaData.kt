package project.fix.skripsi.domain.model

data class SiswaData (
  val nama: String,
  val skorAkhir: Double,
  val hasilKoreksi: List<PerSoal>
)