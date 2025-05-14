package project.fix.skripsi.data.remote.n8n.model

import com.google.gson.annotations.SerializedName
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.model.PerSoal

data class WebhookResponse(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("total_skor")
	val skorAkhir: Int? = null,

	@field:SerializedName("hasil_koreksi")
	val hasilKoreksi: List<HasilKoreksiItem?>? = null
) {
	companion object {
		fun transform(response: WebhookResponse) : HasilKoreksi {
			return HasilKoreksi(
				nama = response.nama ?: "",
				skorAkhir = response.skorAkhir ?: 0,
				hasilKoreksi = response.hasilKoreksi
					?.filterNotNull()
					?.map {
						val subResponse = HasilKoreksiItem.transform(it)
						subResponse
					}
					?: emptyList()
			)
		}
	}
}

data class HasilKoreksiItem(

	@field:SerializedName("penilaian")
	val penilaian: String? = null,

	@field:SerializedName("soal")
	val soal: String? = null,

	@field:SerializedName("jawaban")
	val jawaban: String? = null,

	@field:SerializedName("skor")
	val skor: Int? = null,

	@field:SerializedName("alasan")
	val alasan: String? = null
) {
	companion object {
		fun transform(response: HasilKoreksiItem) : PerSoal {
			return PerSoal(
				penilaian = response.penilaian ?: "",
				soal = response.soal ?: "",
				jawaban = response.jawaban ?: "",
				skor = response.skor ?: 0,
				alasan = response.alasan ?: ""
			)
		}
	}
}
