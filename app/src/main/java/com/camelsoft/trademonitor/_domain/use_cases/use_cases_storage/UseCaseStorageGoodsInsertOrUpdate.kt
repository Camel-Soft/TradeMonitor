package com.camelsoft.trademonitor._domain.use_cases.use_cases_storage

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._presentation.models.MPriceGoods
import com.camelsoft.trademonitor._presentation.utils.getErrCode
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.Settings
import javax.inject.Inject

class UseCaseStorageGoodsInsertOrUpdate @Inject constructor(
    private val iRoom: IRoom,
    private val settings: Settings
) {
    suspend fun execute(newPriceGoods: MPriceGoods): MPriceGoods {  // return for scroll
        // Проверяем на пустоту сканкода
        if (newPriceGoods.scancode.isEmpty()) return newPriceGoods

        // Отсеиваем ВЕСОВОЙ, если весовой, то обрабатываем и выходим
        if (newPriceGoods.scancode.length >= 7) {
            val prefix = settings.getPrefix()
            if (prefix == newPriceGoods.scancode.substring(0,2)) {

                // Получаем вес (количество)
                var newVes = 0F
                newVes = if (newPriceGoods.scancode.length >= 12)
                    try {newPriceGoods.scancode.substring(7,12).toFloat()/1000} catch (e: Exception) {newPriceGoods.quantity}
                else
                    newPriceGoods.quantity

                // Получаем единицы измерения
                var newEdizm = getAppContext().resources.getString(R.string.ed_kg)
                if (newPriceGoods.ed_izm.isNotEmpty()) newEdizm = newPriceGoods.ed_izm

                // Запускаем поиск по базе
                val dbPriceGoodsVes = iRoom.getRoomRightGoods(id_coll = newPriceGoods.id_coll, scancode = newPriceGoods.scancode.substring(0,7))
                if (dbPriceGoodsVes.isEmpty()) {
                    // Insert
                    val priceGoods = MPriceGoods(
                        id = newPriceGoods.id,
                        id_coll = newPriceGoods.id_coll,
                        scancode = newPriceGoods.scancode.substring(0,7),
                        scancode_type = "EAN_13_WEIGHT",
                        cena = newPriceGoods.cena,
                        note = newPriceGoods.note,
                        name = newPriceGoods.name,
                        quantity = newVes,
                        ed_izm = newEdizm,
                        status_code = 100 + getErrCode(newPriceGoods.status_code),
                        holder_color = newPriceGoods.holder_color
                    )
                    iRoom.insertRoomGoods(priceGoods = priceGoods)
                    return priceGoods
                }
                else {
                    // Update
                    val priceGoods = MPriceGoods(
                        id = dbPriceGoodsVes[0].id,
                        id_coll = dbPriceGoodsVes[0].id_coll,
                        scancode = dbPriceGoodsVes[0].scancode,
                        scancode_type = dbPriceGoodsVes[0].scancode_type,
                        cena = if (dbPriceGoodsVes[0].cena == 0F) newPriceGoods.cena else dbPriceGoodsVes[0].cena,
                        note = if (dbPriceGoodsVes[0].note.isEmpty()) newPriceGoods.note else dbPriceGoodsVes[0].note,
                        name = if (dbPriceGoodsVes[0].name.isEmpty()) newPriceGoods.name else dbPriceGoodsVes[0].name,
                        quantity = dbPriceGoodsVes[0].quantity+newVes,
                        ed_izm = if (dbPriceGoodsVes[0].ed_izm.isEmpty()) newPriceGoods.ed_izm else dbPriceGoodsVes[0].ed_izm,
                        status_code = 200 + getErrCode(dbPriceGoodsVes[0].status_code),
                        holder_color = dbPriceGoodsVes[0].holder_color
                    )
                    iRoom.updateRoomGoods(priceGoods = priceGoods)
                    return priceGoods
                }
            }
        }

        // ШТУЧНЫЙ
        // Получаем единицы измерения
        var newEdizm = getAppContext().resources.getString(R.string.ed_sht)
        if (newPriceGoods.ed_izm.isNotEmpty()) newEdizm = newPriceGoods.ed_izm

        // Запускаем поиск по базе
        val dbPriceGoods = iRoom.getRoomRightGoods(id_coll = newPriceGoods.id_coll, scancode = newPriceGoods.scancode)
        if (dbPriceGoods.isEmpty()) {
            // Insert
            val priceGoods = MPriceGoods(
                id = newPriceGoods.id,
                id_coll = newPriceGoods.id_coll,
                scancode = newPriceGoods.scancode,
                scancode_type = newPriceGoods.scancode_type,
                cena = newPriceGoods.cena,
                note = newPriceGoods.note,
                name = newPriceGoods.name,
                quantity = newPriceGoods.quantity,
                ed_izm = newEdizm,
                status_code = 100 + getErrCode(newPriceGoods.status_code),
                holder_color = newPriceGoods.holder_color
            )
            iRoom.insertRoomGoods(priceGoods = priceGoods)
            return priceGoods
        }
        else {
            // Update
            val priceGoods = MPriceGoods(
                id = dbPriceGoods[0].id,
                id_coll = dbPriceGoods[0].id_coll,
                scancode = dbPriceGoods[0].scancode,
                scancode_type = dbPriceGoods[0].scancode_type,
                cena = if (dbPriceGoods[0].cena == 0F) newPriceGoods.cena else dbPriceGoods[0].cena,
                note = if (dbPriceGoods[0].note.isEmpty()) newPriceGoods.note else dbPriceGoods[0].note,
                name = if (dbPriceGoods[0].name.isEmpty()) newPriceGoods.name else dbPriceGoods[0].name,
                quantity = (dbPriceGoods[0].quantity)+(newPriceGoods.quantity),
                ed_izm = if (dbPriceGoods[0].ed_izm.isEmpty()) newPriceGoods.ed_izm else dbPriceGoods[0].ed_izm,
                status_code = 200 + getErrCode(dbPriceGoods[0].status_code),
                holder_color = dbPriceGoods[0].holder_color
            )
            iRoom.updateRoomGoods(priceGoods = priceGoods)
            return priceGoods
        }
    }
}