package com.cerve.co.sample

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerve.co.bonjour_in_flow.BonjourInFlow
import com.cerve.co.bonjour_in_flow.BonjourInFlow.Companion.logIt
import com.cerve.co.bonjour_in_flow.TimedBonjourInFlow
import com.cerve.co.bonjour_in_flow.discover.DiscoverEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor(
    private val bonjourInFlow: BonjourInFlow,
    private val timedBonjourInFlow: TimedBonjourInFlow
) : ViewModel() {

    private val _sampleUi = MutableStateFlow(SampleUiState())
    val sampleUi = _sampleUi.asStateFlow()

    init {
        viewModelScope.launch {

            timedBonjourInFlow
                .discoverByType(
                    types = Pair(REALTEK_TYPE,MARVELL_TYPE)
                ).map { it.name1 }.also { "$it".logIt("timedBonjourInFlow") }


//            bonjourInFlow.discoverServicesWithState()
//                .collect { event ->
//
//                    when(event) {
//                        is DiscoverEvent.ServiceFound,
//                        is DiscoverEvent.ServiceUnResolved,
//                        is DiscoverEvent.ServiceResolved -> {
//                            event.logIt("ServiceResolved")
////                            _sampleUi.update {
////                                it.copy(nsdItems = it.add(event.serviceInfo()))
////                            }
//                        }
//                        is DiscoverEvent.ServiceLost -> {
//                            event.logIt("ServiceLost")
////                            _sampleUi.update {
////                                it.copy(nsdItems = it.remove(event.service))
////                            }
//                        }
//                        is DiscoverEvent.DiscoveryStarted -> {
//                            event.logIt("DiscoveryStarted")
////                            _sampleUi.update {
////                                it.copy(nsdState = UiDiscoveryState.DISCOVERING)
////                            }
//                        }
//                        else -> {
//                            event.logIt("DiscoveryStarted")
////                            _sampleUi.update {
////                                it.copy(nsdState = UiDiscoveryState.IDLE)
////                            }
//                        }
//                    }
//
//                }

        }
    }

    companion object {
        const val REALTEK_TYPE = "_Ayla_Device._tcp"
        const val MARVELL_TYPE = "_hap._tcp"
    }

}

data class SampleUiState(val nsdState: UiDiscoveryState = UiDiscoveryState.IDLE) {

    private val _nsdItems = mutableStateListOf<String>()
    val nsdItems = _nsdItems.toList()

    fun add(items: List<String?>) {
        _nsdItems.addAll(items.filterNotNull())
    }
    fun add(item: String?) = item?.let { nonNullItem ->
        _nsdItems.add(nonNullItem)
    }

    fun remove(item: String?) = item?.let {
        _nsdItems.remove(item)
    }
}

enum class UiDiscoveryState {
    DISCOVERING,
    IDLE
}