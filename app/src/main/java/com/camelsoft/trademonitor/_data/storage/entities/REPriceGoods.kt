package com.camelsoft.trademonitor._data.storage.entities

import androidx.room.*
import com.camelsoft.trademonitor._domain.entities.EPriceGoods

@Entity(
    tableName = "price_goods",
    foreignKeys = [
        ForeignKey(
            entity = REPriceColl::class,
            parentColumns = ["id_coll"],
            childColumns = ["id_coll"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("id_coll", unique = false)
    ]
)
data class REPriceGoods(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "id_coll") val id_coll: Long,
    @ColumnInfo(name = "scancode") val scancode: String,
    @ColumnInfo(name = "scancode_type") val scancode_type: String,
    @ColumnInfo(name = "cena") val cena: Float,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "name") val name: String
) {

    fun toEPriceGoods(): EPriceGoods = EPriceGoods(
        id = id,
        id_coll = id_coll,
        scancode = scancode,
        scancode_type = scancode_type,
        cena = cena,
        note = note,
        name = name
    )

    companion object {
        fun toREPriceGoods(goods: EPriceGoods) = REPriceGoods(
            id = goods.id, // need 0
            id_coll = goods.id_coll,
            scancode = goods.scancode,
            scancode_type = goods.scancode_type,
            cena = goods.cena,
            note = goods.note,
            name = goods.name
        )
    }
}