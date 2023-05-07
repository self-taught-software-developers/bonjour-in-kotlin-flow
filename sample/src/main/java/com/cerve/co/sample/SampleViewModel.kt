package com.cerve.co.sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerve.co.bonjour_in_flow.DiscoveryEvent
import com.cerve.co.bonjour_in_flow.TimedBonjourInFlow
import com.cerve.co.bonjour_in_flow.api.BonjourInFlow
import com.cerve.co.bonjour_in_flow.discover.NetworkServiceDiscoveryUseCase
import com.cerve.co.bonjour_in_flow.resolve.NetworkServiceResolutionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SampleViewModel @Inject constructor(
    private val bonjourInFlow: BonjourInFlow,
) : ViewModel() {

    private val _sampleUiState = MutableStateFlow(SampleUiState())
    val sampleUiState = _sampleUiState.asStateFlow()

    init {
        viewModelScope.launch {

            launch {
                bonjourInFlow.onResolveWithTimeout(
                    type = ALL_SERVICES,
                    timeout = 1.seconds,
                    retries = 0
                ).collect { event ->
                    when(event) {
                        is DiscoveryEvent.Started -> _sampleUiState.update { it.copy(UiDiscoveryState.DISCOVERING) }
                        is DiscoveryEvent.Stopped -> _sampleUiState.update { it.copy(UiDiscoveryState.IDLE) }
                        is DiscoveryEvent.Found -> _sampleUiState.update { it.copy(nsdItems = it.nsdItems + event.service.serviceName) }
                        is DiscoveryEvent.Resolved -> _sampleUiState.update { it.copy(nsdItems = it.nsdItems + event.service.serviceName) }
                        is DiscoveryEvent.Lost -> _sampleUiState.update { it.copy(nsdItems = it.nsdItems - event.service.serviceName) }
                        else -> Unit
                    }
                    event.logIt()
                }
            }

        }
    }

    companion object {
        const val AYLA_SERVICES = "_Ayla_Device._tcp"
        const val HAP_SERVICES = "_hap._tcp"
        const val HTTP_SERVICES = "_http._tcp."
        const val ALL_SERVICES = "_services._dns-sd._udp"

        private const val TAG = "SampleViewModel"
    }

}


data class SampleUiState(
    val nsdState: UiDiscoveryState = UiDiscoveryState.IDLE,
    val nsdItems: List<String> = emptyList()
)

enum class UiDiscoveryState {
    DISCOVERING,
    IDLE
}