package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko_mark

import com.camelsoft.trademonitor._domain.models.MAlkoColl
import com.camelsoft.trademonitor._domain.models.MAlkoMark
import com.camelsoft.trademonitor._presentation.models.MMark

sealed class EventVmAlkoMark {
    data class OnInsertOrUpdateAlkoMark(val parentAlkoColl: MAlkoColl, val mark: MMark): EventVmAlkoMark()
    data class OnInsertOrUpdateAlkoMarks(val parentAlkoColl: MAlkoColl, val markList: ArrayList<MMark>): EventVmAlkoMark()
    data class OnDeleteAlkoMark(val parentAlkoColl: MAlkoColl, val pos: Int): EventVmAlkoMark()
    data class OnUpdateAlkoMark(val parentAlkoColl: MAlkoColl, val alkoMark: MAlkoMark): EventVmAlkoMark()
    data class OnGetAlkoMarks(val parentAlkoColl: MAlkoColl): EventVmAlkoMark()
}