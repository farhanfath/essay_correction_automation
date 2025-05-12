package project.fix.skripsi.data.remote.n8n.model

import com.google.gson.annotations.SerializedName

data class WebhookResponse(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("skor_akhir")
	val skorAkhir: Int? = null,

	@field:SerializedName("hasil_koreksi")
	val hasilKoreksi: List<HasilKoreksiItem?>? = null
)

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
)
