package com.camelsoft.trademonitor._domain.use_cases.use_cases_export

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._domain.models.MPriceColl
import com.camelsoft.trademonitor._domain.utils.SouthRevision
import com.camelsoft.trademonitor.common.App
import com.camelsoft.trademonitor.common.events.EventsSync
import java.io.File
import javax.inject.Inject

class UseCaseExportSouthRevision @Inject constructor(
    private val iRoom: IRoom,
    private val southRevision: SouthRevision
) {
    suspend fun execute(priceColl: MPriceColl): EventsSync<File> {
        try {
            southRevision.open()

            // Заполение данными
            val list = iRoom.getRoomGoodes(id_coll = priceColl.id_coll)
            list.forEach { priceGoods ->
                southRevision.add(scancode = priceGoods.scancode, quantity = priceGoods.quantity)
            }

            southRevision.close()
            return EventsSync.Success(southRevision.getFile())
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsSync.Error(
                App.getAppContext().resources.getString(R.string.error_in)+
                        " UseCaseExportSouthRevision.execute: "+e.message
            )
        }
    }
}