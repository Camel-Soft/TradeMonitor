package com.camelsoft.trademonitor._data.storage.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.camelsoft.trademonitor._presentation.models.alko.MAlkoColl

@Entity(
    tableName = "room_collections_alko"
)
data class ERoomAlkoColl(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_coll") val id_coll: Long,
    @ColumnInfo(name = "created") val created: Long,
    @ColumnInfo(name = "changed") val changed: Long,
    @ColumnInfo(name = "total") val total: Int,
    @ColumnInfo(name = "note") val note: String
) {
    fun toMAlkoColl(): MAlkoColl = MAlkoColl(
        id_coll = id_coll,
        created = created,
        changed = changed,
        total = total,
        note = note
    )

    companion object {
        fun toERoomAlkoColl(alkoColl: MAlkoColl) = ERoomAlkoColl(
            id_coll = alkoColl.id_coll, // need 0
            created = alkoColl.created,
            changed = alkoColl.changed,
            total = alkoColl.total,
            note = alkoColl.note
        )
    }
}