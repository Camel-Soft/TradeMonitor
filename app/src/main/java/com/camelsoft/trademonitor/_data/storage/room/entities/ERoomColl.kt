package com.camelsoft.trademonitor._data.storage.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.camelsoft.trademonitor._presentation.models.price.MPriceColl

@Entity(
    tableName = "room_collections"
)
data class ERoomColl(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_coll") val id_coll: Long,
    @ColumnInfo(name = "created") val created: Long,
    @ColumnInfo(name = "changed") val changed: Long,
    @ColumnInfo(name = "total") val total: Int,
    @ColumnInfo(name = "note") val note: String
) {

    fun toMPriceColl(): MPriceColl = MPriceColl(
        id_coll = id_coll,
        created = created,
        changed = changed,
        total = total,
        note = note
    )

    companion object {
        fun toERoomColl(collection: MPriceColl) = ERoomColl(
            id_coll = collection.id_coll, // need 0
            created = collection.created,
            changed = collection.changed,
            total = collection.total,
            note = collection.note
        )
    }
}