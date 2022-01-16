package com.camelsoft.trademonitor._data.storage.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.camelsoft.trademonitor._domain.entities.EPriceColl

@Entity(
    tableName = "price_collections"
)
data class REPriceColl(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_coll") val id_coll: Long,
    @ColumnInfo(name = "created") val created: Long,
    @ColumnInfo(name = "changed") val changed: Long,
    @ColumnInfo(name = "total") val total: Int,
    @ColumnInfo(name = "note") val note: String
) {

    fun toEPriceColl(): EPriceColl = EPriceColl(
        id_coll = id_coll,
        created = created,
        changed = changed,
        total = total,
        note = note
    )

    companion object {
        fun toREPriceColl(collection: EPriceColl) = REPriceColl(
            id_coll = collection.id_coll, // need 0
            created = collection.created,
            changed = collection.changed,
            total = collection.total,
            note = collection.note
        )
    }
}