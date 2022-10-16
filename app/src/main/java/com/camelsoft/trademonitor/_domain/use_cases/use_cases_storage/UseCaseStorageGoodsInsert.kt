package com.camelsoft.trademonitor._domain.use_cases.use_cases_storage

import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._presentation.models.MPriceGoods
import javax.inject.Inject

class UseCaseStorageGoodsInsert @Inject constructor(
    private val iRoom: IRoom
) {
    suspend fun execute(priceGoods: MPriceGoods) {
        iRoom.insertRoomGoods(priceGoods = priceGoods)
    }
}