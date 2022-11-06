package com.camelsoft.trademonitor._domain.use_cases.use_cases_export

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._presentation.models.price.MPriceColl
import com.camelsoft.trademonitor._domain.libs.ExportJsonGoodes
import com.camelsoft.trademonitor.common.App
import com.camelsoft.trademonitor.common.events.EventsSync
import java.io.File
import javax.inject.Inject

class UseCaseExportJsonGoodes @Inject constructor(
    private val iRoom: IRoom,
    private val exportJsonGoodes: ExportJsonGoodes
) {
    suspend fun execute(priceColl: MPriceColl): EventsSync<File> {
        try {
            exportJsonGoodes.open()
            val list = iRoom.getRoomGoodes(id_coll = priceColl.id_coll)
            list.forEach {
                exportJsonGoodes.addToArrayGoodes(exportJsonGoodes.createGoods(it))
            }
            exportJsonGoodes.createFinalJsonGoods(priceColl = priceColl)
            exportJsonGoodes.close()
            return EventsSync.Success(exportJsonGoodes.getFile())
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsSync.Error(
                App.getAppContext().resources.getString(R.string.error_in)+
                        " UseCaseExportJsonGoodes.execute: "+e.message
            )
        }
    }
}