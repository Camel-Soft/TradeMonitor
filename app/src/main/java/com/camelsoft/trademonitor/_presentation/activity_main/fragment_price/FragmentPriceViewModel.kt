package com.camelsoft.trademonitor._presentation.activity_main.fragment_price

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.camelsoft.trademonitor._data.storage.IPrice
import com.camelsoft.trademonitor._domain.models.MPriceColl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentPriceViewModel @Inject constructor(
    private val iPrice: IPrice
) : ViewModel() {

    private val _eventUi =  Channel<EventUi>()
    val eventUi = _eventUi.receiveAsFlow()

    private var deletedPriceColl: MPriceColl? = null

    fun onEvent(eventVm: EventVm) {
        when(eventVm) {
            is EventVm.OnAddCollClick -> {
                sendEventUi(EventUi.ShowSnackbar("просто тестируем вот", "кнопочка"))
            }
            is EventVm.OnUpdateCollClick -> {}
            is EventVm.OnDeleteCollClick -> {}
            is EventVm.OnUndoDeleteCollClick -> {}
        }
    }







    private fun sendEventUi(eventUi: EventUi) {
        viewModelScope.launch {
            _eventUi.send(eventUi)
        }
    }
}