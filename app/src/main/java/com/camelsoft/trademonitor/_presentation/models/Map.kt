package com.camelsoft.trademonitor._presentation.models

import com.camelsoft.trademonitor._presentation.models.alko.MAlkoMark
import com.camelsoft.trademonitor._presentation.models.price.MPriceGoods
import com.camelsoft.trademonitor._presentation.utils.getErrCode

fun mapPriceGoods(mPriceGoods: MPriceGoods, mGoodsBig: MGoodsBig): MPriceGoods {
    try {
        var cena = 0F
        try { cena = mGoodsBig.cena1.toFloat()/1000 } catch (_: Exception) {}

        return MPriceGoods(
            id = mPriceGoods.id,
            id_coll = mPriceGoods.id_coll,
            scancode = mPriceGoods.scancode,
            scancode_type = mPriceGoods.scancode_type,
            cena = if (mPriceGoods.cena != 0F) mPriceGoods.cena else cena,
            note = mPriceGoods.note,
            name = if (mPriceGoods.name.isNotEmpty()) mPriceGoods.name else mGoodsBig.name,
            quantity = mPriceGoods.quantity,
            ed_izm = if (mGoodsBig.ed_izm.isNotEmpty()) mGoodsBig.ed_izm else mPriceGoods.ed_izm,
            status_code = 300 + getErrCode(mPriceGoods.status_code),
            holder_color = mPriceGoods.holder_color
        )
    }
    catch (e: Exception) {
        e.printStackTrace()
        throw Exception("[Map.mapPriceGoods] ${e.localizedMessage}")
    }
}

fun mapAlkoMark(mAlkoMark: MAlkoMark, mGoodsBig: MGoodsBig): MAlkoMark {
    try {
        var cena = 0F
        try { cena = mGoodsBig.cena1.toFloat()/1000 } catch (_: Exception) {}

        return MAlkoMark(
            id = mAlkoMark.id,
            id_coll = mAlkoMark.id_coll,
            marka = mAlkoMark.marka,
            marka_type = mAlkoMark.marka_type,
            scancode = mAlkoMark.scancode,
            scancode_type = mAlkoMark.scancode_type,
            cena = if (mAlkoMark.cena != 0F) mAlkoMark.cena else cena,
            note = mAlkoMark.note,
            name = if (mAlkoMark.name.isNotEmpty()) mAlkoMark.name else mGoodsBig.name,
            quantity = mAlkoMark.quantity,
            type = mAlkoMark.type,
            status_code = 300 + getErrCode(mAlkoMark.status_code),
            holder_color = "1"  // Бежевый
        )
    }
    catch (e: Exception) {
        e.printStackTrace()
        throw Exception("[Map.mapAlkoMark] ${e.localizedMessage}")
    }
}
