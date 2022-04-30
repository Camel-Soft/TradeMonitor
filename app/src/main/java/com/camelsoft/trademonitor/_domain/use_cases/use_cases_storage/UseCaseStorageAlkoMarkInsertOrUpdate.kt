package com.camelsoft.trademonitor._domain.use_cases.use_cases_storage

import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._domain.models.MAlkoMark
import com.camelsoft.trademonitor._presentation.utils.getErrCode
import javax.inject.Inject

class UseCaseStorageAlkoMarkInsertOrUpdate @Inject constructor(
    private val iRoom: IRoom
) {
    suspend fun execute(newAlkoMark: MAlkoMark): MAlkoMark {  // return for scroll
        // Проверяем на пустоту акцизки
        if (newAlkoMark.marka.isEmpty()) return newAlkoMark

        // Запускаем поиск по базе
        val dbAlkoMark = iRoom.getRoomAlkoMarkMark(id_coll = newAlkoMark.id_coll, marka = newAlkoMark.marka)
        if (dbAlkoMark.isEmpty()) {
            // Insert
            val alkoMark = MAlkoMark (
                id = newAlkoMark.id,
                id_coll = newAlkoMark.id_coll,
                marka = newAlkoMark.marka,
                marka_type = newAlkoMark.marka_type,
                scancode = newAlkoMark.scancode,
                scancode_type = newAlkoMark.scancode_type,
                cena = newAlkoMark.cena,
                note = newAlkoMark.note,
                name = newAlkoMark.name,
                quantity = newAlkoMark.quantity,
                type = newAlkoMark.type,
                status_code = 100 + getErrCode(newAlkoMark.status_code),
                holder_color = newAlkoMark.holder_color
            )
            iRoom.insertRoomAlkoMark(alkoMark = alkoMark)
            return alkoMark
        }
        else {
            // Update
            val alkoMark = MAlkoMark (
                id = dbAlkoMark[0].id,
                id_coll = dbAlkoMark[0].id_coll,
                marka = dbAlkoMark[0].marka,
                marka_type = dbAlkoMark[0].marka_type,
                scancode = if (dbAlkoMark[0].scancode.isEmpty()) newAlkoMark.scancode else dbAlkoMark[0].scancode,
                scancode_type = if (dbAlkoMark[0].scancode_type.isEmpty()) newAlkoMark.scancode_type else dbAlkoMark[0].scancode_type,
                cena = if (dbAlkoMark[0].cena == 0F) newAlkoMark.cena else dbAlkoMark[0].cena,
                note = if (dbAlkoMark[0].note.isEmpty()) newAlkoMark.note else dbAlkoMark[0].note,
                name = if (dbAlkoMark[0].name.isEmpty()) newAlkoMark.name else dbAlkoMark[0].name,
                quantity = (dbAlkoMark[0].quantity)+(newAlkoMark.quantity),
                type = if (dbAlkoMark[0].type.isEmpty()) newAlkoMark.type else dbAlkoMark[0].type,
                status_code = 200 + getErrCode(dbAlkoMark[0].status_code),
                holder_color = "7"  // Пометить красным, чтобы обратить внимание
            )
            iRoom.updateRoomAlkoMark(alkoMark)
            return alkoMark
        }
    }
}