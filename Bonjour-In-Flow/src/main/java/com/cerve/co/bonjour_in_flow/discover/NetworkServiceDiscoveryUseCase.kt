package com.cerve.co.bonjour_in_flow.discover

import android.content.Context
import android.content.Context.NSD_SERVICE
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.cerve.co.bonjour_in_flow.DiscoveryEvent
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch

/**
 * @[NetworkServiceDiscoveryUseCase] begins a nsd discovery nested within a [callbackFlow]
 * This allow for easy integration with StateFlows or Channel flows for complex operation.
 *
 * NOTE: the emitted services are unresolved.
 * To resolve any services use @[NetworkServiceResolutionUseCase]
 *
 */
class NetworkServiceDiscoveryUseCase(private val nsdManager: NsdManager) {

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
    }.catch { error -> emit(DiscoveryEvent.Failed(error.message)) }

}