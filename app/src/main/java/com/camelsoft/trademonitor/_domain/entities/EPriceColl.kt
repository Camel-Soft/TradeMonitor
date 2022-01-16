package com.camelsoft.trademonitor._domain.entities

data class EPriceColl(
    val id_coll: Long,
    val created: Long,
    val changed: Long,
    val total: Int,
    val note: String
)