package com.camelsoft.trademonitor._data.storage.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.camelsoft.trademonitor._data.storage.entities.REPriceColl
import com.camelsoft.trademonitor._data.storage.entities.REPriceGoods

data class RElPriceCollWithGoods(
    @Embedded val collection: REPriceColl,
    @Relation(
        parentColumn = "id_coll",
        entityColumn = "id_coll"
    )
    val goods: List<REPriceGoods>
)