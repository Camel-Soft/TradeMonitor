package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods
import com.camelsoft.trademonitor._presentation.api.IGoods
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentPriceGoodsDetailViewModel @Inject constructor(
    private val iGoods: IGoods
): ViewModel() {

    private val _eventsUi = Channel<EventsUiPriceGoodsDetail>()
    val eventsUi = _eventsUi.receiveAsFlow()

    fun eventsVm(events: EventsVmPriceGoodsDetail) {
        try {
            when (events) {
                is EventsVmPriceGoodsDetail.SendRequestGoods -> {
                    viewModelScope.launch {
                        when (val result = iGoods.getGoodsBig(mGoodsBig = events.mGoodsBig)) {
                            is EventsGoods.Success -> { sendEventUi(EventsUiPriceGoodsDetail.Success(result.data)) }
                            is EventsGoods.UnSuccess -> { sendEventUi(EventsUiPriceGoodsDetail.UnSuccess(result.message)) }
                            is EventsGoods.Update -> { sendEventUi(EventsUiPriceGoodsDetail.Update(result.message)) }
                            is EventsGoods.Info -> { sendEventUi(EventsUiPriceGoodsDetail.ShowInfo(result.message)) }
                            is EventsGoods.Error -> { sendEventUi(EventsUiPriceGoodsDetail.ShowError(result.message)) }
                        }
                        sendEventUi(EventsUiPriceGoodsDetail.Progress(show = false))
                    }
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            sendEventUi(EventsUiPriceGoodsDetail.Progress(show = false))
            sendEventUi(EventsUiPriceGoodsDetail.ShowError("[FragmentPriceGoodsDetailViewModel.eventsVm] ${e.localizedMessage}"))
        }
    }

    private fun sendEventUi(event: EventsUiPriceGoodsDetail) {
        viewModelScope.launch {
            _eventsUi.send(event)
        }
    }
}
