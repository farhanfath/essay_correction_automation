package project.fix.skripsi.data.remote.n8n.model

import com.google.gson.annotations.SerializedName
import project.fix.skripsi.domain.model.AnswerKeyItem
import project.fix.skripsi.domain.model.constants.CorrectionType
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.model.PerSoal
import project.fix.skripsi.domain.model.SiswaData

data class WebhookResponse(
	@SerializedName("tipe_evaluasi")
	val evaluationType: String? = null,

	@SerializedName("daftar_hasil_siswa")
	val resultData: List<SiswaDataResponse>? = null,

	@SerializedName("daftar_kunci_jawaban")
	val listAnswerKey: List<AnswerKeyItemResponse>? = null
) {
	companion object {
		fun transform(response: WebhookResponse): HasilKoreksi {
			return HasilKoreksi(
				evaluationType = if (response.evaluationType == "AI") {
					CorrectionType.AI
				} else {
					CorrectionType.ANSWER_KEY
				},
				resultData = response.resultData
					?.map { SiswaDataResponse.transform(it) }
					?: emptyList(),
				listAnswerKey = response.listAnswerKey
					?.map { AnswerKeyItemResponse.transform(it) }
					?: emptyList()
			)
		}
	}
}

data class SiswaDataResponse(
	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("skor_akhir")
	val skorAkhir: Double? = null,

	@field:SerializedName("hasil_koreksi")
	val hasilKoreksi: List<PerSoalResponse?>? = null
) {
	companion object {
		fun transform(response: SiswaDataResponse) : SiswaData {
			return SiswaData(
				nama = response.nama ?: "",
				skorAkhir = response.skorAkhir ?: 0.0,
				hasilKoreksi = response.hasilKoreksi
					?.filterNotNull()
					?.map {
						val subResponse = PerSoalResponse.transform(it)
						subResponse
					}
					?: emptyList()
			)
		}
	}
}

data class PerSoalResponse(

	@field:SerializedName("soal")
	val soal: String? = null,

	@field:SerializedName("jawaban")
	val jawaban: String? = null,

	@field:SerializedName("penilaian")
	val penilaian: String? = null,

	@field:SerializedName("alasan")
	val alasan: String? = null,

	@field:SerializedName("skor")
val skor: Int? = null
) {
	companion object {
		fun transform(response: PerSoalResponse) : PerSoal {
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

data class AnswerKeyItemResponse(

	@field:SerializedName("number")
	val number: Int? = null,

	@field:SerializedName("answer")
	val answer: String? = null,

	@field:SerializedName("scoreWeight")
	val scoreWeight: Int? = null
) {
	companion object {
		fun transform(response: AnswerKeyItemResponse) : AnswerKeyItem {
			return AnswerKeyItem(
				number = response.number ?: 0,
				answer = response.answer ?: "",
				scoreWeight = response.scoreWeight ?: 0
			)
		}
	}
}
