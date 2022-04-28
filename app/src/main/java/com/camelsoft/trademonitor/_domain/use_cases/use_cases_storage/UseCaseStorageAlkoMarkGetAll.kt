package com.camelsoft.trademonitor._domain.use_cases.use_cases_storage

import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._domain.models.MAlkoMark
import javax.inject.Inject

class UseCaseStorageAlkoMarkGetAll @Inject constructor(
    private val iRoom: IRoom
) {
    suspend fun execute(id_coll: Long): List<MAlkoMark> {
        return iRoom.getRoomAlkoMarks(id_coll = id_coll)
    }
}