package com.cerve.co.bonjour_in_flow.resolve

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.cerve.co.bonjour_in_flow.discover.DiscoverEvent
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.trySendBlocking

data class ResolveListener(
    private val scope: ProducerScope<DiscoverEvent>
) : NsdManager.ResolveListener {

    override fun onResolveFailed(service: NsdServiceInfo, errorCode: Int) {
        scope.trySendBlocking(DiscoverEvent.ServiceUnResolved(service, errorCode))
    }

    override fun onServiceResolved(service: NsdServiceInfo) {
        scope.trySendBlocking(DiscoverEvent.ServiceResolved(service))
    }
}