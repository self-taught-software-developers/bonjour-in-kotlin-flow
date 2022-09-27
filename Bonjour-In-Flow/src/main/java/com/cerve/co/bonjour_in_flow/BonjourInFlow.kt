package com.cerve.co.bonjour_in_flow

import android.content.Context
import android.net.nsd.NsdServiceInfo
import android.util.Log
import com.cerve.co.bonjour_in_flow.discover.DiscoverConfiguration
import com.cerve.co.bonjour_in_flow.discover.DiscoverEvent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.InetAddress

class BonjourInFlow(private val manager: NSDManagerInFlow) {
    constructor(context: Context) : this(NSDManagerInFlowImpl.fromContext(context))

    /**
     *
     */
    fun discoverServices() = channelFlow {
        manager.discoverService(DiscoverConfiguration(SERVICES_DOMAIN)).collect { event ->

            launch {

                event.serviceInfo()?.let { service ->
                    manager.discoverServiceByType(service.serviceName.toDiscoveryType())
                        .filter { it.isService() }
                        .mapResolve()
                        .collect { send(it) }
                }

            }
        }
    }.flowOn(IO)

    fun discoverServicesWithState() = channelFlow {
        manager.discoverService(DiscoverConfiguration(SERVICES_DOMAIN)).collect { event ->

            launch {

                event.serviceInfo()?.let { service ->
                    manager.discoverServiceByType(service.serviceName.toDiscoveryType())
                        .mapResolve()
                        .collect { send(it) }
                } ?: send(event)

            }
        }
    }.flowOn(IO)


    private fun <T: DiscoverEvent> Flow<T>.mapResolve() : Flow<DiscoverEvent> {
        return this.map { event ->
            manager.resolveService(event).first()
        }
    }

    companion object {
        private const val SERVICES_DOMAIN = "_services._dns-sd._udp"
        const val REALTEK_TYPE = "_Ayla_Device._tcp"
        const val MARVELL_TYPE = "_hap._tcp"

        fun String.toDiscoveryType() : String {
            return "$this._tcp"
        }

        fun <T> Flow<T>.logThreadLifecycle() : Flow<T> {
            return this
                .onStart { Log.d("DiscoverEvent", "start thread activity ${currentCoroutineContext()}") }
                .onCompletion { Log.d("DiscoverEvent", "completion thread activity ${currentCoroutineContext()}") }
        }

    }

}

data class ZippedDiscoverEvent(
    val name1 : String,
    val name2 : String,
    val type : String,
    val host : InetAddress,
    val port : Int
) {
    fun logIt() {
        Log.d("DiscoverEvent", "${toString()}")
    }
}