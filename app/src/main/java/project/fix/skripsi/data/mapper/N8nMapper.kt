package project.fix.skripsi.data.mapper

import project.fix.skripsi.data.remote.n8n.model.HasilKoreksiItem
import project.fix.skripsi.data.remote.n8n.model.WebhookResponse
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.model.PerSoal

fun WebhookResponse.toDomain() : HasilKoreksi {
    return HasilKoreksi(
        nama = this.nama ?: "",
        skorAkhir = this.skorAkhir ?: 0,
        hasilKoreksi = this.hasilKoreksi
            ?.filterNotNull()
            ?.map { it.toDomain() }
            ?: emptyList()
    )
}

fun HasilKoreksiItem.toDomain(): PerSoal {
    return PerSoal(
        penilaian = this.penilaian ?: "",
        soal = this.soal ?: "",
        jawaban = this.jawaban ?: "",
        skor = this.skor ?: 0,
        alasan = this.alasan ?: ""
    )
}