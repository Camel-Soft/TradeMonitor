package com.camelsoft.trademonitor._presentation.models

import com.google.gson.annotations.SerializedName

data class MGoodsBig (
    @SerializedName("cod")
    var cod: String = "",               // Код товара
    @SerializedName("scancod")
    var scancod: String = "",           // Сканкод товара
    @SerializedName("scancod_type")
    var scancod_type: String = "",      // Тип сканкода товара
    @SerializedName("scancod_is_find")
    var scancod_is_find: String = "",   // Сканкод найденный в базе
    @SerializedName("marka")
    var marka: String = "",             // Марка (акцизная) товара
    @SerializedName("marka_type")
    var marka_type: String = "",        // Тип марки (акцизной) товара
    @SerializedName("sklad")
    var sklad: String = "",             // Склад
    @SerializedName("name")
    var name: String = "",              // Наименование товара
    @SerializedName("name_full")
    var name_full: String = "",         // Полное наименование товара
    @SerializedName("cena1")
    var cena1: String = "",             // Цена товара 1
    @SerializedName("cena2")
    var cena2: String = "",             // Цена товара 2
    @SerializedName("cena3")
    var cena3: String = "",             // Цена товара 3
    @SerializedName("cena_spec")
    var cena_spec: String = "",         // Цена товара (для упаковки)
    @SerializedName("ed_izm")
    var ed_izm: String = "",            // Единицы измерения товара
    @SerializedName("articul")
    var articul: String = "",           // Артикул товара в South
    @SerializedName("grt")
    var grt: String = "",               // Группа товара
    @SerializedName("sgrt")
    var sgrt: String = "",              // Подгруппа товара
    @SerializedName("made")
    var made: String = "",              // Производитель товара
    @SerializedName("day_save")
    var day_save: String = "",          // Дней хранения товара
    @SerializedName("in_place")
    var in_place: String = "",          // Кол-во товара в упаковке
    @SerializedName("litera")
    var litera: String = "",            // Литера товара
    @SerializedName("prc_type")
    var prc_type: String = "",          // Прайс - тип
    @SerializedName("prc_number")
    var prc_number: String = "",        // Прайс - номер
    @SerializedName("prc_date")
    var prc_date: String = "",          // Прайс - дата создания
    @SerializedName("prc_time")
    var prc_time: String = "",          // Прайс - время создания
    @SerializedName("nsp")
    var nsp: String = "",               // Промо-Акция (в числах)
    @SerializedName("modelline")
    var modelline: String = "",         // Промо-Акция (boolean)
    @SerializedName("nds")
    var nds: String = "",               // НДС товара
    @SerializedName("mrc")
    var mrc: String = "",               // МРЦ (сигареты и алкоголь)
    @SerializedName("cod_vvp")
    var cod_vvp: String = "",           // Код Алкогольной продукции
    @SerializedName("capacity")
    var capacity: String = "",          // Объем единицы товара
    @SerializedName("ostatok")
    var ostatok: String = "",           // Остаток товара
    @SerializedName("prih_last_date")
    var prih_last_date: String = "",    // Дата последнего прихода
    @SerializedName("notes")
    var notes: String = "",             // Примечания
    @SerializedName("id")
    var id: String = "",                // ID (from mobile)
    @SerializedName("id_coll")
    var id_coll: String = "",           // ID сборки, к которой принадлежит товар (from mobile)
    @SerializedName("quantity")
    var quantity: String = "",          // Количество (from mobile)
    @SerializedName("summa")
    var summa: String = "",             // Сумма
    @SerializedName("status_code")
    var status_code: String = "",       // Состояния, ошибки для вывода на rv holder (from mobile)
    @SerializedName("holder_color")
    var holder_color: String = "",      // Цвет holder`ов (from mobile)
    @SerializedName("rezerv1")
    var rezerv1: String = "",           //
    @SerializedName("rezerv2")
    var rezerv2: String = "",           //
    @SerializedName("rezerv3")
    var rezerv3: String = ""            //
)
