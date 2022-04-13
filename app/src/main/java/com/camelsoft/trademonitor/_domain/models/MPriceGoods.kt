package com.camelsoft.trademonitor._domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MPriceGoods(
    val id: Long,                   // ID
    val id_coll: Long,              // ID сборки, к которой принадлежит товар
    val scancode: String,           // Сканкод
    val scancode_type: String,      // Тип сканкода
    val cena: Float,                // Цена
    val note: String,               // Примечания
    val name: String,               // Наименование
    val quantity: Float,            // Количество
    val ed_izm: String,             // Единицы измерения
    val status_code: Int,           // Состояния, ошибки для вывода на rv holder
    val holder_color: String        // Цвет holder`ов
): Parcelable