package com.cerve.co.bonjour_in_flow.discover

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.trySendBlocking

@Deprecated("")
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