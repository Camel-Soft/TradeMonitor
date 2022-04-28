package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko

import androidx.lifecycle.ViewModel
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageAlkoCollDelete
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageAlkoCollGetAll
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageAlkoCollInsert
import com.camelsoft.trademonitor._domain.use_cases.use_cases_storage.UseCaseStorageAlkoCollUpdate
import javax.inject.Inject

class FragmentAlkoViewModel @Inject constructor(
    private val useCaseStorageAlkoCollDelete: UseCaseStorageAlkoCollDelete,
    private val useCaseStorageAlkoCollGetAll: UseCaseStorageAlkoCollGetAll,
    private val useCaseStorageAlkoCollInsert: UseCaseStorageAlkoCollInsert,
    private val useCaseStorageAlkoCollUpdate: UseCaseStorageAlkoCollUpdate
): ViewModel() {
}