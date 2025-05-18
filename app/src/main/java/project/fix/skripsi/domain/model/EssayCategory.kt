package project.fix.skripsi.domain.model

enum class EssayCategory(
    val id: String,
    val displayName: String,
    val description: String
) {
    QUESTION_ANSWER(
        id = "question_answer",
        displayName = "Essay Soal Jawab",
        description = "Format essay dengan pasangan soal dan jawaban yang disajikan secara terpisah"
    ),

    SEQUENTIAL_QUESTIONS_ANSWERS(
        id = "sequential_questions_answers",
        displayName = "Essay Urutan Soal dan Jawaban",
        description = "Format essay dengan urutan seluruh soal diikuti urutan seluruh jawaban"
    )
}