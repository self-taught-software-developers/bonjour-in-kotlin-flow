package com.cerve.co.sample

import android.net.nsd.NsdServiceInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerve.co.bonjour_in_flow.BonjourInFlow
import com.cerve.co.bonjour_in_flow.discover.DiscoverEvent

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor(
    private val bonjourInFlow: BonjourInFlow
) : ViewModel() {

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