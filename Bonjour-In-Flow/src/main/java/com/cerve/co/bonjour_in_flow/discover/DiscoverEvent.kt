package com.cerve.co.bonjour_in_flow.discover

import android.net.nsd.NsdServiceInfo
import android.util.Log
import timber.log.Timber

sealed class DiscoverEvent {

    data class ServiceFound(val service: NsdServiceInfo?) : DiscoverEvent()
    data class ServiceLost(val service: NsdServiceInfo?) : DiscoverEvent()

    data class DiscoveryStarted(val serviceType: String?): DiscoverEvent()
    data class DiscoveryStopped(val serviceType: String?) : DiscoverEvent()

    data class DiscoveryStartedFailed(val serviceType: String?, val errorCode: Int)
        : Throwable("onStartDiscovery failed for $serviceType with error $errorCode")

    data class DiscoveryStopFailed(val serviceType: String?, val errorCode: Int)
        : Throwable("onStopDiscovery failed for $serviceType with error $errorCode")

    data class DiscoveryStopSuccess(val serviceType: String?)
        : Throwable("onDiscoveryStopped success for $serviceType")

    fun logIt() { Log.d("DiscoverEvent", toString()) }

    fun isService() : Boolean {
        return this is ServiceFound || this is ServiceLost
    }
}