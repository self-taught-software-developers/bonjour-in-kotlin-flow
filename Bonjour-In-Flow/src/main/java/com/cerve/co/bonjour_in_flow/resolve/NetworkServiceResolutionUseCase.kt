package com.cerve.co.bonjour_in_flow.resolve

import android.content.Context
import android.content.Context.NSD_SERVICE
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.cerve.co.bonjour_in_flow.DiscoveryEvent
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class NetworkServiceResolutionUseCase(private val nsdManager: NsdManager) {
    constructor(context: Context) : this(context.getSystemService(NSD_SERVICE) as NsdManager)

    operator fun invoke(
        type: String,
        protocol: Int = NsdManager.PROTOCOL_DNS_SD
    ) = callbackFlow {
        val discoveryListener = object : NsdManager.DiscoveryListener {
            override fun onStartDiscoveryFailed(p0: String?, p1: Int) {
                cancel("onStartDiscoveryFailed $p0, $p1")
            }
            override fun onStopDiscoveryFailed(p0: String?, p1: Int) {
                cancel("onStopDiscoveryFailed $p0, $p1")
            }
            override fun onDiscoveryStarted(serviceType: String?) {
                trySendBlocking(DiscoveryEvent.Started(serviceType))
            }
            override fun onDiscoveryStopped(p0: String?) {
                trySendBlocking(DiscoveryEvent.Stopped(p0)).also {
                    cancel("onDiscoveryStopped $p0")
                }
            }

            override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
                serviceInfo?.let { trySendBlocking(DiscoveryEvent.Found(serviceInfo)) }
            }
            override fun onServiceLost(serviceInfo: NsdServiceInfo?) {
                serviceInfo?.let { trySendBlocking(DiscoveryEvent.Lost(serviceInfo)) }
            }

        }

        nsdManager.discoverServices(type, protocol, discoveryListener)

        awaitClose {
            nsdManager.stopServiceDiscovery(discoveryListener)
        }
    }.catch { error -> emit(DiscoveryEvent.Failed(error.message)) }.resolveService(nsdManager)

    companion object {
        private fun <T: DiscoveryEvent> Flow<T>.resolveService(
            nsdManager: NsdManager
        ) : Flow<DiscoveryEvent> {
            return map { event ->
                if(event is DiscoveryEvent.Found) {
                    callbackFlow {
                        nsdManager.resolveService(
                            event.service,
                            ResolutionListener(this)
                        )
                        awaitCancellation()
                    }.first()
                } else event
            }
        }
    }

}