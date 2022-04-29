package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko

import java.io.File

sealed class EventUiAlkoColl {
    data class ShowErrorUi(val message: String): EventUiAlkoColl()
    data class ShowInfoUi(val message: String): EventUiAlkoColl()
    data class ScrollToPos(val position: Int): EventUiAlkoColl()
    data class ShareFile(val file: File, val sign: String): EventUiAlkoColl()
}