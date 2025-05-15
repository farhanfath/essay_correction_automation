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
import project.fix.skripsi.domain.model.PerSoal
import project.fix.skripsi.presentation.ui.screen.result.tab.detailevaluasi.components.ResultItemCard

@Composable
fun DetailEvaluasiTab(hasilKoreksi: List<PerSoal>) {
  Column(modifier = Modifier.fillMaxWidth()) {
    hasilKoreksi.forEachIndexed { index, perSoal ->
      key(perSoal.soal) {
        AnimatedVisibility(
          visible = true,
          enter = fadeIn() + expandVertically()
        ) {
          ResultItemCard(
            perSoal = perSoal,
            nomorSoal = index + 1
          )
        }

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}