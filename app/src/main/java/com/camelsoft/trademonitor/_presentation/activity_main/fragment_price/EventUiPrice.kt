package com.camelsoft.trademonitor._presentation.activity_main.fragment_price

import java.io.File

sealed class EventUiPrice {
    data class ShowErrorUi(val message: String): EventUiPrice()
    data class ShowInfoUi(val message: String): EventUiPrice()
    data class ScrollToPos(val position: Int): EventUiPrice()
    data class ShareFile(val file: File, val sign: String): EventUiPrice()
    data class ConfirmSouthUpload(val south: String, val pos: Int): EventUiPrice()
    data class ShowProgress(val show: Boolean, val textTop: String, val textBottom: String): EventUiPrice()
}
