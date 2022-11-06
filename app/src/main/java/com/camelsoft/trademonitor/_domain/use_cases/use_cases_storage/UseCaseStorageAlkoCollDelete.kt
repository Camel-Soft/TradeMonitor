package com.camelsoft.trademonitor._domain.use_cases.use_cases_storage

import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._presentation.models.alko.MAlkoColl
import javax.inject.Inject

class UseCaseStorageAlkoCollDelete @Inject constructor(
    private val iRoom: IRoom
) {
    suspend fun execute(alkoColl: MAlkoColl) {
        iRoom.deleteRoomAlkoColl(alkoColl = alkoColl)
    }
}