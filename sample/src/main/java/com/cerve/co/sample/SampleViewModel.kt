package com.cerve.co.sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerve.co.bonjour_in_flow.DiscoveryEvent
import com.cerve.co.bonjour_in_flow.TimedBonjourInFlow
import com.cerve.co.bonjour_in_flow.discover.NetworkServiceDiscoveryUseCase
import com.cerve.co.bonjour_in_flow.resolve.NetworkServiceResolutionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SampleViewModel @Inject constructor(
    private val discoveryUseCase: NetworkServiceDiscoveryUseCase,
    private val resolutionUseCase: NetworkServiceResolutionUseCase,
    private val bonjourInFlow: com.cerve.co.bonjour_in_flow.api.BonjourInFlow,
    private val timedBonjourInFlow: TimedBonjourInFlow
) : ViewModel() {

    init {
        viewModelScope.launch {
            launch {
                bonjourInFlow.onWithTimeout(timeout = 2.seconds) {
                    (resolutionUseCase.invoke(HAP_SERVICES)).zip(
                        resolutionUseCase.invoke(AYLA_SERVICES)
                    ) { hap, ayla ->
                        DiscoveryEvent.List(listOf(hap, ayla))
                    }
                }.collect { event ->
                    event.logIt()
                }
            }

//            launch {
//                bonjourInFlow.onResolveWithTimeout(
//                    type = HAP_SERVICES,
//                    timeout = 1.seconds,
////                    retries = 0
//                ).collect { event ->
//                    event.logIt()
//                }
//            }

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