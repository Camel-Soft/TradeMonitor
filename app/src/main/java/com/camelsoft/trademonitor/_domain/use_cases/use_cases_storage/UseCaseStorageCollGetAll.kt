package com.camelsoft.trademonitor._domain.use_cases.use_cases_storage

import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._domain.models.MPriceColl
import javax.inject.Inject

class UseCaseStorageCollGetAll @Inject constructor(
    private val iRoom: IRoom
) {
    suspend fun execute(): List<MPriceColl> {
        return iRoom.getRoomCollAll()
    }
}