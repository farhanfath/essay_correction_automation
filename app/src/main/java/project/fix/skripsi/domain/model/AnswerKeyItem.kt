package project.fix.skripsi.domain.model

data class AnswerKeyItem(
    val number: Int,
    val answer: String,
    val scoreWeight: Int = 10
)