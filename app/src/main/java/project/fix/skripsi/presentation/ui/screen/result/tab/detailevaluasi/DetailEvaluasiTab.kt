package project.fix.skripsi.presentation.ui.screen.result.tab.detailevaluasi

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import project.fix.skripsi.domain.model.AnswerKeyItem
import project.fix.skripsi.domain.model.CorrectionType
import project.fix.skripsi.domain.model.PerSoal
import project.fix.skripsi.presentation.ui.screen.result.tab.detailevaluasi.components.ResultItemCard

@Composable
fun DetailEvaluasiTab(
  hasilKoreksi: List<PerSoal>,
  bobotNilai: List<AnswerKeyItem>,
  tipeEvaluasi: CorrectionType
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    hasilKoreksi.forEachIndexed { index, perSoal ->
      key(perSoal.soal) {
        AnimatedVisibility(
          visible = true,
          enter = fadeIn() + expandVertically()
        ) {

          val answerKeyItem = bobotNilai.find { it.number == index + 1 }
          val scoreWeight = answerKeyItem?.scoreWeight ?: 0
          val kunciJawaban = answerKeyItem?.answer

          ResultItemCard(
            perSoal = perSoal,
            nomorSoal = index + 1,
            maxBobot = scoreWeight,
            kunciJawaban = kunciJawaban,
            tipeEvaluasi = tipeEvaluasi,
          )
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}