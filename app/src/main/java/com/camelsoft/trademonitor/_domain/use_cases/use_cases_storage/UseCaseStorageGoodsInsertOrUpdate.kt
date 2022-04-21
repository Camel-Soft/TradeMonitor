package com.camelsoft.trademonitor._domain.use_cases.use_cases_storage

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._domain.models.MPriceGoods
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

                // Получаем вес
                var newVes = 0F
                newVes = if (newPriceGoods.scancode.length >= 12)
                    try {newPriceGoods.scancode.substring(7,12).toFloat()/1000} catch (e: Exception) {newPriceGoods.quantity}
                else
                    newPriceGoods.quantity

                // Получаем единицы измерения
                


                // Запускаем поиск по базе
                val dbPriceGoodsVes = iRoom.getRoomRightGoods(id_coll = newPriceGoods.id_coll, scancode = newPriceGoods.scancode.substring(0,7))
                if (dbPriceGoodsVes.isEmpty()) {
                    // Insert
                    val priceGoods = MPriceGoods(
                        id = newPriceGoods.id,
                        id_coll = newPriceGoods.id_coll,
                        scancode = newPriceGoods.scancode.substring(0,7),
                        scancode_type = newPriceGoods.scancode_type,
                        cena = newPriceGoods.cena,
                        note = newPriceGoods.note,
                        name = newPriceGoods.name,
                        quantity = newVes,
                        ed_izm = getAppContext().resources.getString(R.string.ed_kg),
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
                        scancode_type = newPriceGoods.scancode_type,
                        cena = dbPriceGoodsVes[0].cena,
                        note = dbPriceGoodsVes[0].note,
                        name = dbPriceGoodsVes[0].name,
                        quantity = (dbPriceGoodsVes[0].quantity)+(newPriceGoods.scancode.substring(7,12).toFloat()/1000),
                        ed_izm = dbPriceGoodsVes[0].ed_izm,
                        status_code = 200 + getErrCode(dbPriceGoodsVes[0].status_code),
                        holder_color = dbPriceGoodsVes[0].holder_color
                    )
                    iRoom.updateRoomGoods(priceGoods = priceGoods)
                    return priceGoods
                }






            }










        }




            // Странный код... принимаем как ШТУЧНЫЙ
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
                    ed_izm = newPriceGoods.ed_izm,
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
                    scancode_type = newPriceGoods.scancode_type,
                    cena = dbPriceGoods[0].cena,
                    note = dbPriceGoods[0].note,
                    name = dbPriceGoods[0].name,
                    quantity = (dbPriceGoods[0].quantity)+(newPriceGoods.quantity),
                    ed_izm = dbPriceGoods[0].ed_izm,
                    status_code = 200 + getErrCode(dbPriceGoods[0].status_code),
                    holder_color = dbPriceGoods[0].holder_color
                )
                iRoom.updateRoomGoods(priceGoods = priceGoods)
                return priceGoods
            }









        val prefix = settings.getPrefix()
        if (prefix == newPriceGoods.scancode.substring(0,2)) {
            // Весовой
            val dbPriceGoodsVes = iRoom.getRoomRightGoods(id_coll = newPriceGoods.id_coll, scancode = newPriceGoods.scancode.substring(0,7))
            if (dbPriceGoodsVes.isEmpty()) {
                // Insert
                val priceGoods = MPriceGoods(
                    id = newPriceGoods.id,
                    id_coll = newPriceGoods.id_coll,
                    scancode = newPriceGoods.scancode.substring(0,7),
                    scancode_type = newPriceGoods.scancode_type,
                    cena = newPriceGoods.cena,
                    note = newPriceGoods.note,
                    name = newPriceGoods.name,
                    quantity = newPriceGoods.scancode.substring(7,12).toFloat()/1000,
                    ed_izm = getAppContext().resources.getString(R.string.ed_kg),
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
                    scancode_type = newPriceGoods.scancode_type,
                    cena = dbPriceGoodsVes[0].cena,
                    note = dbPriceGoodsVes[0].note,
                    name = dbPriceGoodsVes[0].name,
                    quantity = (dbPriceGoodsVes[0].quantity)+(newPriceGoods.scancode.substring(7,12).toFloat()/1000),
                    ed_izm = dbPriceGoodsVes[0].ed_izm,
                    status_code = 200 + getErrCode(dbPriceGoodsVes[0].status_code),
                    holder_color = dbPriceGoodsVes[0].holder_color
                )
                iRoom.updateRoomGoods(priceGoods = priceGoods)
                return priceGoods
            }
        }
        else {
            // Штучный
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
                    ed_izm = newPriceGoods.ed_izm,
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
                    scancode_type = newPriceGoods.scancode_type,
                    cena = dbPriceGoods[0].cena,
                    note = dbPriceGoods[0].note,
                    name = dbPriceGoods[0].name,
                    quantity = (dbPriceGoods[0].quantity)+(newPriceGoods.quantity),
                    ed_izm = dbPriceGoods[0].ed_izm,
                    status_code = 200 + getErrCode(dbPriceGoods[0].status_code),
                    holder_color = dbPriceGoods[0].holder_color
                )
                iRoom.updateRoomGoods(priceGoods = priceGoods)
                return priceGoods
            }
        }














    }
}