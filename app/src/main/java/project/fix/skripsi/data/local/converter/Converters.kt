package project.fix.skripsi.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import project.fix.skripsi.domain.model.AnswerKeyItem
import project.fix.skripsi.domain.model.HasilKoreksi
import project.fix.skripsi.domain.model.SiswaData
import project.fix.skripsi.domain.model.constants.CorrectionType

class Converters {
  private val gson = Gson()

  @TypeConverter
  fun fromHasilKoreksiList(value: List<HasilKoreksi>): String {
    return gson.toJson(value)
  }

  @TypeConverter
  fun toHasilKoreksiList(value: String): List<HasilKoreksi> {
    val listType = object : TypeToken<List<HasilKoreksi>>() {}.type
    return gson.fromJson(value, listType)
  }

  @TypeConverter
  fun fromCorrectionType(value: CorrectionType): String = value.name

  @TypeConverter
  fun toCorrectionType(value: String): CorrectionType = CorrectionType.valueOf(value)

  @TypeConverter
  fun fromSiswaDataList(value: List<SiswaData>): String {
    return gson.toJson(value)
  }

  @TypeConverter
  fun toSiswaDataList(value: String): List<SiswaData> {
    val listType = object : TypeToken<List<SiswaData>>() {}.type
    return gson.fromJson(value, listType)
  }

  @TypeConverter
  fun fromAnswerKeyItemList(value: List<AnswerKeyItem>): String {
    return gson.toJson(value)
  }

  @TypeConverter
  fun toAnswerKeyItemList(value: String): List<AnswerKeyItem> {
    val type = object : TypeToken<List<AnswerKeyItem>>() {}.type
    return gson.fromJson(value, type)
  }
}