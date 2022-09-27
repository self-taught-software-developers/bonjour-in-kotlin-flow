package com.cerve.co.bonjour_in_flow.discover

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.trySendBlocking

data class DiscoverEventListener(
    private val scope: ProducerScope<DiscoverEvent>
) : NsdManager.DiscoveryListener {

    override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
        scope.channel.close(DiscoverEvent.DiscoveryStartedFailed(serviceType, errorCode))
    }

    override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
        scope.channel.close(DiscoverEvent.DiscoveryStopFailed(serviceType, errorCode))
    }

    override fun onDiscoveryStopped(serviceType: String?) {
        scope.channel.close(DiscoverEvent.DiscoveryStopSuccess(serviceType))
    }

    override fun onDiscoveryStarted(serviceType: String?) {
        scope.trySendBlocking(DiscoverEvent.DiscoveryStarted(serviceType = serviceType))
    }

    override fun onServiceFound(service: NsdServiceInfo?) {
        scope.channel.trySendBlocking(DiscoverEvent.ServiceFound(service))
    }

    override fun onServiceLost(service: NsdServiceInfo?) {
        scope.channel.trySendBlocking(DiscoverEvent.ServiceLost(service))
    }

}

data class ScopedDiscoverEventListener(
    private val scope: ProducerScope<Pair<DiscoverEvent, CoroutineScope>>
) : NsdManager.DiscoveryListener {

    override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
        scope.channel.close(DiscoverEvent.DiscoveryStartedFailed(serviceType, errorCode))
    }

    override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
        scope.channel.close(DiscoverEvent.DiscoveryStopFailed(serviceType, errorCode))
    }

    override fun onDiscoveryStopped(serviceType: String?) {
        scope.channel.close(DiscoverEvent.DiscoveryStopSuccess(serviceType))
    }

    override fun onDiscoveryStarted(serviceType: String?) {
        scope.trySendBlocking(
            Pair(DiscoverEvent.DiscoveryStarted(serviceType = serviceType), scope)
        )
    }

    override fun onServiceFound(service: NsdServiceInfo?) {
        scope.channel.trySendBlocking(
            Pair(DiscoverEvent.ServiceFound(service), scope)
        )
    }

    override fun onServiceLost(service: NsdServiceInfo?) {
        scope.channel.trySendBlocking(
            Pair(DiscoverEvent.ServiceLost(service), scope)
        )
    }

}
