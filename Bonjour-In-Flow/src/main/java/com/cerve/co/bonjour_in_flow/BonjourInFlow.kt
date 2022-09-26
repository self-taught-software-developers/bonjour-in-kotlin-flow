package com.cerve.co.bonjour_in_flow

import android.content.Context
import com.cerve.co.bonjour_in_flow.BonjourInFlow.Companion.toDiscoveryType
import com.cerve.co.bonjour_in_flow.discover.DiscoverConfiguration
import com.cerve.co.bonjour_in_flow.discover.DiscoverEvent
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BonjourInFlow(private val nsdMangerInFlow: NSDManagerInFlow) {
    constructor(context: Context) : this(NSDManagerInFlowImpl.fromContext(context))

    fun discoverServices() = channelFlow {
        nsdMangerInFlow.discoverService(
            DiscoverConfiguration(SERVICES_DOMAIN)
        ).collect { event ->

            launch {

                event.serviceInfo()?.let { service ->

                    discoverByServiceType(service.serviceName.toDiscoveryType())
                        .filter { it.isService() }
                        .collect { send(it) }

                }

            }
        }
    }
    fun discoverServicesWithState() = channelFlow {
        nsdMangerInFlow.discoverService(
            DiscoverConfiguration(SERVICES_DOMAIN)
        ).collect { event ->

            launch {

                event.serviceInfo()?.let { service ->

                    discoverByServiceType(service.serviceName.toDiscoveryType())
                        .collect { send(it) }

                } ?: send(event)

            }
        }
    }

    private fun discoverByServiceType(serviceType: String) = nsdMangerInFlow.discoverService(
        DiscoverConfiguration(serviceType)
    )

    //TODO unresolved isn't being emitted
    fun <T: DiscoverEvent> Flow<T>.resolveIt() : Flow<DiscoverEvent> {
        return this.map { event ->
            nsdMangerInFlow.resolveService(event).first()
        }
    }

    companion object {
        private const val SERVICES_DOMAIN = "_services._dns-sd._udp"
        const val REALTEK_TYPE = "_Ayla_Device._tcp"
        const val MARVELL_TYPE = "_hap._tcp"

        fun String.toDiscoveryType() : String {
            return "$this._tcp"
        }
    }

}