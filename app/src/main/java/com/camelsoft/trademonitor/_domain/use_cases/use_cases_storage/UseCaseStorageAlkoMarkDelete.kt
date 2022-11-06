package com.camelsoft.trademonitor._domain.use_cases.use_cases_storage

import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._presentation.models.alko.MAlkoMark
import javax.inject.Inject

class UseCaseStorageAlkoMarkDelete @Inject constructor(
    private val iRoom: IRoom
) {
    suspend fun execute(alkoMark: MAlkoMark) {
        iRoom.deleteRoomAlkoMark(alkoMark = alkoMark)
    }
}