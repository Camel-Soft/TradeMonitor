package com.camelsoft.trademonitor._domain.use_cases.use_cases_storage

import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._presentation.models.MAlkoMark
import javax.inject.Inject

class UseCaseStorageAlkoMarkUpdate @Inject constructor(
    private val iRoom: IRoom
) {
    suspend fun execute(alkoMark: MAlkoMark) {
        iRoom.updateRoomAlkoMark(alkoMark = alkoMark)
    }
}