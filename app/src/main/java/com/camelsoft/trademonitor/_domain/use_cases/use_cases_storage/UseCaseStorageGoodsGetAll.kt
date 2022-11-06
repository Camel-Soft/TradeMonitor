package com.camelsoft.trademonitor._domain.use_cases.use_cases_storage

import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._presentation.models.price.MPriceGoods
import javax.inject.Inject

class UseCaseStorageGoodsGetAll @Inject constructor(
    private val iRoom: IRoom
) {
    suspend fun execute(id_coll: Long): List<MPriceGoods> {
        return iRoom.getRoomGoodes(id_coll = id_coll)
    }
}