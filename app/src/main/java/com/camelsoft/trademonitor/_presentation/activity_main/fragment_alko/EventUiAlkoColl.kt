package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko

import com.camelsoft.trademonitor._presentation.models.MStringString
import java.io.File

sealed class EventUiAlkoColl {
    data class ShowErrorUi(val message: String): EventUiAlkoColl()
    data class ShowInfoUi(val message: String): EventUiAlkoColl()
    data class ScrollToPos(val position: Int): EventUiAlkoColl()
    data class ShareFile(val file: File, val sign: String): EventUiAlkoColl()
    data class SpecifyChZnUi(val position: Int, val itemsInn: ArrayList<MStringString>): EventUiAlkoColl()
}