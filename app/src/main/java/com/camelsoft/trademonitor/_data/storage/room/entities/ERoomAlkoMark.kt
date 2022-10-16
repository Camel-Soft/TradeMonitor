package com.camelsoft.trademonitor._data.storage.room.entities

import androidx.room.*
import com.camelsoft.trademonitor._presentation.models.MAlkoMark

@Entity(
    tableName = "room_mark_alko",
    foreignKeys = [
        ForeignKey(
            entity = ERoomAlkoColl::class,
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

data class ERoomAlkoMark(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "id_coll") val id_coll: Long,
    @ColumnInfo(name = "marka") val marka: String,
    @ColumnInfo(name = "marka_type") val marka_type: String,
    @ColumnInfo(name = "scancode") val scancode: String,
    @ColumnInfo(name = "scancode_type") val scancode_type: String,
    @ColumnInfo(name = "cena") val cena: Float,
    @ColumnInfo(name = "note") val note: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "quantity") val quantity: Float,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "status_code") val status_code: Int,
    @ColumnInfo(name = "holder_color") val holder_color: String
) {
    fun toMAlkoMark(): MAlkoMark = MAlkoMark(
        id = id,
        id_coll = id_coll,
        marka = marka,
        marka_type = marka_type,
        scancode = scancode,
        scancode_type = scancode_type,
        cena = cena,
        note = note,
        name = name,
        quantity = quantity,
        type = type,
        status_code = status_code,
        holder_color = holder_color
    )

    companion object {
        fun toERoomAlkoMark(alkoMark: MAlkoMark) = ERoomAlkoMark(
            id = alkoMark.id,
            id_coll = alkoMark.id_coll,
            marka = alkoMark.marka,
            marka_type = alkoMark.marka_type,
            scancode = alkoMark.scancode,
            scancode_type = alkoMark.scancode_type,
            cena = alkoMark.cena,
            note = alkoMark.note,
            name = alkoMark.name,
            quantity = alkoMark.quantity,
            type = alkoMark.type,
            status_code = alkoMark.status_code,
            holder_color = alkoMark.holder_color
        )
    }
}