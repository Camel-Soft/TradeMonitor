package com.camelsoft.trademonitor._data.storage.room.entities

import androidx.room.*
import com.camelsoft.trademonitor._presentation.models.MPriceGoods

@Entity(
    tableName = "room_goods",
    foreignKeys = [
        ForeignKey(
            entity = ERoomColl::class,
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
data class ERoomGoods(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "id_coll") val id_coll: Long,
    @ColumnInfo(name = "scancode") val scancode: String,
    @ColumnInfo(name = "scancode_type") val scancode_type: String,
    @ColumnInfo(name = "cena") val cena: Float,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "quantity") val quantity: Float,
    @ColumnInfo(name = "ed_izm") val ed_izm: String,
    @ColumnInfo(name = "status_code") val status_code: Int,
    @ColumnInfo(name = "holder_color") val holder_color: String
) {

    fun toMPriceGoods(): MPriceGoods = MPriceGoods(
        id = id,
        id_coll = id_coll,
        scancode = scancode,
        scancode_type = scancode_type,
        cena = cena,
        note = note,
        name = name,
        quantity = quantity,
        ed_izm = ed_izm,
        status_code = status_code,
        holder_color = holder_color
    )

    companion object {
        fun toERoomGoods(goods: MPriceGoods) = ERoomGoods(
            id = goods.id, // need 0
            id_coll = goods.id_coll,
            scancode = goods.scancode,
            scancode_type = goods.scancode_type,
            cena = goods.cena,
            note = goods.note,
            name = goods.name,
            quantity = goods.quantity,
            ed_izm = goods.ed_izm,
            status_code = goods.status_code,
            holder_color = goods.holder_color
        )
    }
}