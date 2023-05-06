package com.cerve.co.bonjour_in_flow.resolve

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.cerve.co.bonjour_in_flow.DiscoveryEvent
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.trySendBlocking
import timber.log.Timber

data class ResolutionListener(
    private val scope: ProducerScope<DiscoveryEvent>
) : NsdManager.ResolveListener {
    override fun onResolveFailed(serviceInfo: NsdServiceInfo?, code: Int) {
        Timber.tag(TAG).d("$code")
        serviceInfo?.let { scope.trySendBlocking(DiscoveryEvent.Found(serviceInfo)) }
    }

    override fun onServiceResolved(serviceInfo: NsdServiceInfo?) {
        serviceInfo?.let { scope.trySendBlocking(DiscoveryEvent.Resolved(serviceInfo)) }
    }

    companion object {
        private const val TAG = "ResolutionListener"
    }
}

