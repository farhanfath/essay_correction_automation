package project.fix.skripsi.domain.model.constants

enum class CorrectionType(
    val badge: String,
    val title: String,
    val desc: String,
) {
    AI(
        badge = "AI",
        title = "Correction By AI",
        desc = "Penilaian generatif oleh AI Agent"
    ),
    ANSWER_KEY(
        badge = "KJ",
        title = "Correction By Answer Key",
        desc = "Penilaian dengan berdasarkan kunci jawaban"
    )
}