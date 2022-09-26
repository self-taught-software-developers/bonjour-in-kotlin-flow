package com.cerve.co.sample

import android.content.Context
import android.net.nsd.NsdServiceInfo
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerve.co.bonjour_in_flow.BonjourInFlow
import com.cerve.co.bonjour_in_flow.BonjourInFlow.Companion.MARVELL_TYPE
import com.cerve.co.bonjour_in_flow.BonjourInFlow.Companion.toDiscoveryType
import com.cerve.co.bonjour_in_flow.discover.DiscoverEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    private val bonjourInFlow : BonjourInFlow by lazy { BonjourInFlow(context) }

    private val _sampleUi = MutableStateFlow(SampleUiState())
    val sampleUi = _sampleUi.asStateFlow()

    init {
        viewModelScope.launch {

            bonjourInFlow.discoverServicesWithState()
                .collect { event ->

                    when(event) {
                        is DiscoverEvent.ServiceFound,
                        is DiscoverEvent.ServiceUnResolved,
                        is DiscoverEvent.ServiceResolved -> _sampleUi.update {
                            it.copy(nsdItems = it.add(event.serviceInfo()))
                        }
                        is DiscoverEvent.ServiceLost -> _sampleUi.update {
                            it.copy(nsdItems = it.remove(event.service))
                        }
                        is DiscoverEvent.DiscoveryStarted -> _sampleUi.update { it.copy(nsdState = UiDiscoveryState.DISCOVERING) }
                        else -> _sampleUi.update { it.copy(nsdState = UiDiscoveryState.IDLE) }
                    }

            }

        }
    }

}

data class SampleUiState(
    val nsdState: UiDiscoveryState = UiDiscoveryState.IDLE,
    val nsdItems: List<NsdServiceInfo> = listOf()
) {

    fun add(item: NsdServiceInfo?): List<NsdServiceInfo> {
        return item?.let { nonNullItem ->
            nsdItems + (nonNullItem)
        } ?: nsdItems
    }

    fun remove(item: NsdServiceInfo?): List<NsdServiceInfo> {
        return item?.let { nonNullItem ->
            nsdItems.drop(nsdItems.indexOf(nonNullItem)).toMutableList()
        } ?: nsdItems
    }
}

enum class UiDiscoveryState {
    DISCOVERING,
    IDLE
}