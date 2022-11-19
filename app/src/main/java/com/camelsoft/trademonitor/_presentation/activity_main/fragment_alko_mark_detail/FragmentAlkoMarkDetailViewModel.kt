package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko_mark_detail

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
class FragmentAlkoMarkDetailViewModel @Inject constructor(
    private val iGoods: IGoods
): ViewModel() {

    private val _eventsUi = Channel<EventsUiAlkoMarkDetail>()
    val eventsUi = _eventsUi.receiveAsFlow()

    fun eventsVm(events: EventsVmAlkoMarkDetail) {
        try {
            when (events) {
                is EventsVmAlkoMarkDetail.SendRequestGoods -> {
                    viewModelScope.launch {
                        when (val result = iGoods.getGoodsBig(mGoodsBig = events.mGoodsBig)) {
                            is EventsGoods.Success -> { sendEventUi(EventsUiAlkoMarkDetail.Success(result.data)) }
                            is EventsGoods.UnSuccess -> { sendEventUi(EventsUiAlkoMarkDetail.UnSuccess(result.message)) }
                            is EventsGoods.Update -> { sendEventUi(EventsUiAlkoMarkDetail.Update(result.message)) }
                            is EventsGoods.Info -> { sendEventUi(EventsUiAlkoMarkDetail.ShowInfo(result.message)) }
                            is EventsGoods.Error -> { sendEventUi(EventsUiAlkoMarkDetail.ShowError(result.message)) }
                        }
                        sendEventUi(EventsUiAlkoMarkDetail.Progress(show = false))
                    }
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            sendEventUi(EventsUiAlkoMarkDetail.Progress(show = false))
            sendEventUi(EventsUiAlkoMarkDetail.ShowError("[FragmentAlkoMarkDetailViewModel.eventsVm] ${e.localizedMessage}"))
        }
    }

    private fun sendEventUi(event: EventsUiAlkoMarkDetail) {
        viewModelScope.launch {
            _eventsUi.send(event)
        }
    }
}
