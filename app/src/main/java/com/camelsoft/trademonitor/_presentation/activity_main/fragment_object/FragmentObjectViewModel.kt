package com.camelsoft.trademonitor._presentation.activity_main.fragment_object

import androidx.lifecycle.ViewModel
import com.camelsoft.trademonitor._presentation.api.repo.IHello
import com.camelsoft.trademonitor._presentation.api.repo.IObject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentObjectViewModel @Inject constructor(
    private val iHello: IHello,
    private val iObject: IObject
): ViewModel() {


}
