package project.fix.skripsi.data.remote.n8n.model

import com.google.gson.annotations.SerializedName

data class N8nResponse(
    val nama: String,
    @SerializedName("skor_akhir")
    val skorAkhir: Int,
    @SerializedName("penilaian_1")
    val penilaian1: String,
    @SerializedName("alasan_1")
    val alasan1: String,
    @SerializedName("skor_1")
    val skor1: Int,
    @SerializedName("penilaian_2")
    val penilaian2: String,
    @SerializedName("alasan_2")
    val alasan2: String,
    @SerializedName("skor_2")
    val skor2: Int
)