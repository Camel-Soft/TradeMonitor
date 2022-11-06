package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko_mark

import com.camelsoft.trademonitor._presentation.models.alko.MAlkoColl
import com.camelsoft.trademonitor._presentation.models.alko.MAlkoMark
import com.camelsoft.trademonitor._presentation.models.MScan

sealed class EventVmAlkoMark {
    data class OnInsertOrUpdateAlkoMark(val parentAlkoColl: MAlkoColl, val scan: MScan): EventVmAlkoMark()
    data class OnInsertOrUpdateAlkoMarks(val parentAlkoColl: MAlkoColl, val scanList: ArrayList<MScan>): EventVmAlkoMark()
    data class OnDeleteAlkoMark(val parentAlkoColl: MAlkoColl, val pos: Int): EventVmAlkoMark()
    data class OnUpdateAlkoMark(val parentAlkoColl: MAlkoColl, val alkoMark: MAlkoMark): EventVmAlkoMark()
    data class OnGetAlkoMarks(val parentAlkoColl: MAlkoColl): EventVmAlkoMark()
}