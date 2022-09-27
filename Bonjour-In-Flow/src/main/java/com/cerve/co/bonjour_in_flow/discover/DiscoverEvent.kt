package com.cerve.co.bonjour_in_flow.discover

import android.net.nsd.NsdServiceInfo
import android.util.Log
import timber.log.Timber

sealed class DiscoverEvent {

    data class ServiceFound(val service: NsdServiceInfo?) : DiscoverEvent()
    data class ServiceLost(val service: NsdServiceInfo?) : DiscoverEvent()

    data class ServiceResolved(val service: NsdServiceInfo?) : DiscoverEvent()
    data class ServiceUnResolved(val service: NsdServiceInfo?, val errorCode: Int) : DiscoverEvent()

    data class DiscoveryStarted(val serviceType: String?): DiscoverEvent()
    data class DiscoveryStopped(val serviceType: String?) : DiscoverEvent()

    data class DiscoveryStartedFailed(val serviceType: String?, val errorCode: Int)
        : Throwable("onStartDiscovery failed for $serviceType with error $errorCode")

    data class DiscoveryStopFailed(val serviceType: String?, val errorCode: Int)
        : Throwable("onStopDiscovery failed for $serviceType with error $errorCode")

    data class DiscoveryStopSuccess(val serviceType: String?)
        : Throwable("onDiscoveryStopped success for $serviceType")

    fun logIt(msg: String = "") { Log.d("DiscoverEvent","$msg ${toString()}") }

    fun isService() : Boolean {
        return this is ServiceFound || this is ServiceLost || this is ServiceResolved || this is ServiceUnResolved
    }

    fun serviceInfo() : NsdServiceInfo? {
        return when(this) {
            is ServiceFound -> this.service
            is ServiceLost -> this.service
            is ServiceResolved -> this.service
            is ServiceUnResolved -> this.service
            else -> null
        }
//            .also {
//            Log.d("DiscoverEvent", "${toString()} service: ${it?.serviceName}")
//        }
    }
}