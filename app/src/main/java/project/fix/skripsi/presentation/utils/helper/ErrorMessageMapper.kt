package project.fix.skripsi.presentation.utils.helper

object ErrorMessageMapper {

    fun mapErrorToUserFriendlyMessage(status: String?, defaultError: String): Pair<String, String> {
        return when (status) {
            "invalid_file" -> Pair(
                "File Tidak Valid",
                "Pastikan Anda memilih file gambar yang valid"
            )
            "file_too_large" -> Pair(
                "File Terlalu Besar",
                "Kompres gambar atau pilih gambar dengan ukuran lebih kecil"
            )
            "invalid_format" -> Pair(
                "Format Tidak Didukung",
                "Gunakan format gambar JPG, PNG, atau JPEG"
            )
            "poor_quality" -> Pair(
                "Kualitas Gambar Kurang",
                "Ambil foto dengan pencahayaan yang baik dan fokus yang jelas"
            )
            "invalid_content" -> Pair(
                "Konten Tidak Sesuai",
                "Pastikan gambar berisi lembar jawaban essay siswa"
            )
            "no_text_content" -> Pair(
                "Teks Tidak Terdeteksi",
                "Pastikan gambar mengandung tulisan yang jelas dan terbaca"
            )
            "extraction_failed" -> Pair(
                "Gagal Memproses",
                "Coba ambil foto ulang dengan kualitas yang lebih baik"
            )
            else -> Pair(
                "Terjadi Kesalahan",
                defaultError
            )
        }
    }

    fun getActionableAdvice(status: String?): List<String> {
        return when (status) {
            "poor_quality" -> listOf(
                "✓ Gunakan pencahayaan yang cukup",
                "✓ Pastikan kamera fokus",
                "✓ Hindari bayangan pada kertas",
                "✓ Ambil foto dari jarak yang tepat"
            )
            "no_text_content" -> listOf(
                "✓ Pastikan ada tulisan tangan yang jelas",
                "✓ Periksa kontras antara tinta dan kertas",
                "✓ Ambil foto seluruh halaman jawaban",
                "✓ Hindari refleksi cahaya"
            )
            "invalid_content" -> listOf(
                "✓ Pastikan ada nama siswa",
                "✓ Pastikan ada nomor soal (1., 2., 3.)",
                "✓ Pastikan ada jawaban essay",
                "✓ Gunakan lembar jawaban yang benar"
            )
            "file_too_large" -> listOf(
                "✓ Kompres gambar terlebih dahulu",
                "✓ Kurangi resolusi kamera",
                "✓ Crop gambar jika perlu",
                "✓ Gunakan aplikasi kompres gambar"
            )
            else -> listOf(
                "✓ Coba ambil foto ulang",
                "✓ Periksa koneksi internet",
                "✓ Restart aplikasi jika perlu"
            )
        }
    }
}