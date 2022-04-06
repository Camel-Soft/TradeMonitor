package com.camelsoft.trademonitor._domain.use_cases.use_cases_storage

import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._domain.models.MPriceGoods
import javax.inject.Inject

class UseCaseStorageGoodsUpdate @Inject constructor(
    private val iRoom: IRoom
) {
    suspend fun execute(priceGoods: MPriceGoods) {
        iRoom.updateRoomGoods(priceGoods = priceGoods)
    }
}