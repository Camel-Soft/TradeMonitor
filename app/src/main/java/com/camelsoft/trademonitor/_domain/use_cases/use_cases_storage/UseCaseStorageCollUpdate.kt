package com.camelsoft.trademonitor._domain.use_cases.use_cases_storage

import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._presentation.models.price.MPriceColl
import javax.inject.Inject

class UseCaseStorageCollUpdate @Inject constructor(
    private val iRoom: IRoom
) {
    suspend fun execute(priceColl: MPriceColl) {
        iRoom.updateRoomColl(priceColl = priceColl)
    }
}