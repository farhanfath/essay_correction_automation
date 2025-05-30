package project.fix.skripsi.data.local.answerkey.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import project.fix.skripsi.domain.model.AnswerKeyItem

class SavedAnswerKeyConverter {
  @TypeConverter
  fun fromAnswerKeyItemList(value: List<AnswerKeyItem>): String {
    val gson = Gson()
    return gson.toJson(value)
  }

  @TypeConverter
  fun toAnswerKeyItemList(value: String): List<AnswerKeyItem> {
    val gson = Gson()
    val type = object : TypeToken<List<AnswerKeyItem>>() {}.type
    return gson.fromJson(value, type)
  }
}