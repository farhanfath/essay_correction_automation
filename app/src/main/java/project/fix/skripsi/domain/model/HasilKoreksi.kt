package project.fix.skripsi.domain.model

data class HasilKoreksi (
    val evaluationType: CorrectionType,
    val resultData: List<SiswaData>,
    val listAnswerKey: List<AnswerKeyItem>
)

data class SiswaData (
    val nama: String,
    val skorAkhir: Double,
    val hasilKoreksi: List<PerSoal>
)

data class PerSoal(
    val penilaian: String,
    val soal: String,
    val jawaban: String,
    val skor: Int,
    val alasan: String
)