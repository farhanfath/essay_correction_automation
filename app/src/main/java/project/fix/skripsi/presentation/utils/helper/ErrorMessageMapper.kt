package project.fix.skripsi.presentation.utils.helper

import project.fix.skripsi.domain.exception.CallException

object ErrorMessageHelper {

    fun getDetailedErrorMessage(errorMessage: String?, throwable: Throwable? = null): String {
        if (throwable is CallException) {
            return getDetailedErrorFromCallException(throwable)
        }

        return getDetailedErrorFromMessage(errorMessage)
    }

    private fun getDetailedErrorFromCallException(exception: CallException): String {
        return when (exception.status) {
            "invalid_content" -> {
                "📷 Gambar Tidak Valid\n\n" +
                        "${exception.message}\n\n" +
                        "Tips:\n" +
                        "• Pastikan foto berisi lembar jawaban essay\n" +
                        "• Nama siswa harus terlihat jelas\n" +
                        "• Soal dan jawaban harus terbaca\n" +
                        "• Ambil foto dengan pencahayaan yang baik"
            }

            "invalid_file" -> {
                "📁 File Tidak Valid\n\n" +
                        "${exception.message}\n\n" +
                        "Pastikan file yang diupload adalah gambar (JPG, PNG, dsb)"
            }

            "file_too_large" -> {
                "📏 File Terlalu Besar\n\n" +
                        "${exception.message}\n\n" +
                        "Coba kompres gambar atau gunakan resolusi yang lebih kecil"
            }

            "server_error" -> {
                "🔧 Server Bermasalah\n\n" +
                        "${exception.message}\n\n" +
                        "Tim teknis sedang memperbaiki masalah ini. Coba lagi dalam beberapa menit."
            }

            "empty_data" -> {
                "📄 Data Kosong\n\n" +
                        "${exception.message}\n\n" +
                        "Tips:\n" +
                        "• Pastikan foto jelas dan tidak blur\n" +
                        "• Nama siswa dan jawaban harus terbaca\n" +
                        "• Coba ambil foto ulang dengan angle yang lebih baik"
            }

            "http_error" -> {
                when (exception.statusCode) {
                    400 -> "📝 Data Tidak Valid\n\n${exception.message}\n\nPeriksa gambar dan coba lagi"
                    401, 403 -> "🔒 Akses Ditolak\n\n${exception.message}"
                    404 -> "🔍 Layanan Tidak Ditemukan\n\n${exception.message}"
                    500 -> "🔧 Server Error\n\n${exception.message}\n\nCoba lagi nanti"
                    else -> "❌ Error ${exception.statusCode}\n\n${exception.message}"
                }
            }

            "unexpected_error" -> {
                "❌ Kesalahan Tidak Terduga\n\n" +
                        "${exception.message}\n\n" +
                        "Coba restart aplikasi atau hubungi support jika masalah berlanjut"
            }

            else -> {
                // Default berdasarkan status code
                when (exception.statusCode) {
                    400 -> "📝 Data Tidak Valid\n\n${exception.message}"
                    500 -> "🔧 Server Bermasalah\n\n${exception.message}"
                    else -> "❌ Error\n\n${exception.message}"
                }
            }
        }
    }

    private fun getDetailedErrorFromMessage(errorMessage: String?): String {
        if (errorMessage == null) return "Terjadi kesalahan yang tidak diketahui"

        return when {
            errorMessage.contains("bukan lembar jawaban") -> {
                "📷 Gambar Tidak Valid\n\n" +
                        "$errorMessage\n\n" +
                        "Pastikan foto berisi lembar jawaban essay yang jelas"
            }
            errorMessage.contains("kosong") -> {
                "📄 Data Kosong\n\n$errorMessage"
            }
            errorMessage.contains("Network error") -> {
                "🌐 Masalah Jaringan\n\n$errorMessage"
            }
            else -> "❌ Kesalahan\n\n$errorMessage"
        }
    }

    fun getShortErrorMessage(errorMessage: String?, throwable: Throwable? = null): String {
        if (throwable is CallException) {
            return when (throwable.status) {
                "invalid_content" -> "Gambar tidak valid"
                "invalid_file" -> "File tidak valid"
                "file_too_large" -> "File terlalu besar"
                "server_error" -> "Server bermasalah"
                "empty_data" -> "Data kosong"
                else -> "Error ${throwable.statusCode}"
            }
        }

        return errorMessage?.let {
            when {
                it.contains("bukan lembar jawaban") -> "Gambar tidak valid"
                it.contains("kosong") -> "Data kosong"
                it.contains("Network") -> "Masalah jaringan"
                else -> "Kesalahan"
            }
        } ?: "Kesalahan tidak diketahui"
    }
}