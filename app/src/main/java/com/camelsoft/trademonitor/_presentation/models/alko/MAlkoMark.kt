package com.camelsoft.trademonitor._presentation.models.alko

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.String

@Parcelize
data class MAlkoMark(
    val id: Long,                   // ID
    val id_coll: Long,              // ID сборки, к которой принадлежит марка
    val marka: String,              // Акцизная марка
    val marka_type: String,         // Тип акцизной марки
    val scancode: String,           // Сканкод
    val scancode_type: String,      // Тип сканкода
    val cena: Float,                // Цена
    val note: String,               // Примечания
    val name: String,               // Наименование
    val quantity: Float,            // Количество
    val type: String,               // Тип чего-нибудь (резерв)
    val status_code: Int,           // Состояния, ошибки для вывода на rv holder
    val holder_color: String        // Цвет holder`ов
): Parcelable