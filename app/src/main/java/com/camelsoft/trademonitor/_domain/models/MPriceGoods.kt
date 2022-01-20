package com.camelsoft.trademonitor._domain.models

data class MPriceGoods(
    val id: Long,
    val id_coll: Long,
    val scancode: String,
    val scancode_type: String,
    val cena: Float,
    val note: String,
    val name: String
)