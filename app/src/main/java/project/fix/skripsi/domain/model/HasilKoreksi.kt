package project.fix.skripsi.domain.model

import project.fix.skripsi.domain.model.constants.CorrectionType

data class HasilKoreksi (
    val evaluationType: CorrectionType,
    val resultData: List<SiswaData>,
    val listAnswerKey: List<AnswerKeyItem>
)