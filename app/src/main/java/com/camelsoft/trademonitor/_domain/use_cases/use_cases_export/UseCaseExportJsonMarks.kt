package com.camelsoft.trademonitor._domain.use_cases.use_cases_export

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._presentation.models.MAlkoColl
import com.camelsoft.trademonitor._domain.libs.ExportJsonMarks
import com.camelsoft.trademonitor.common.App
import com.camelsoft.trademonitor.common.events.EventsSync
import java.io.File
import javax.inject.Inject

class UseCaseExportJsonMarks @Inject constructor(
    private val iRoom: IRoom,
    private val exportJsonMarks: ExportJsonMarks
) {
    suspend fun execute(alkoColl: MAlkoColl): EventsSync<File> {
        try {
            exportJsonMarks.open()
            val list = iRoom.getRoomAlkoMarks(id_coll = alkoColl.id_coll)
            list.forEach {
                exportJsonMarks.addToArrayMarks(exportJsonMarks.createMark(it))
            }
            exportJsonMarks.createFinalJsonMark(alkoColl = alkoColl)
            exportJsonMarks.close()
            return EventsSync.Success(exportJsonMarks.getFile())
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsSync.Error(App.getAppContext().resources.getString(R.string.error_in)+
                        " UseCaseExportJsonMarks.execute: "+e.message
            )
        }
    }
}