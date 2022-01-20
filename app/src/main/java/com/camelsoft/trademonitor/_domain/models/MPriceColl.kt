package com.camelsoft.trademonitor._domain.models

data class MPriceColl(
    val id_coll: Long,
    val created: Long,
    val changed: Long,
    val total: Int,
    val note: String
)