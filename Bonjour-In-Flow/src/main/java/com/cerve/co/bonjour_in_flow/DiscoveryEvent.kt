package com.cerve.co.bonjour_in_flow

import android.net.nsd.NsdServiceInfo
import timber.log.Timber
import kotlin.jvm.internal.Intrinsics.Kotlin

sealed interface DiscoveryEvent {

    data class Found(val service: NsdServiceInfo) : DiscoveryEvent
    data class Lost(val service: NsdServiceInfo) : DiscoveryEvent
    data class Resolved(val service: NsdServiceInfo) : DiscoveryEvent

    data class List(val services : kotlin.collections.List<DiscoveryEvent>) : DiscoveryEvent

    data class Started(val serviceType: String?) : DiscoveryEvent
    data class Stopped(val message: String?) : DiscoveryEvent

    data class Failed(val message: String?) : DiscoveryEvent

    fun logIt(id: String? = null) {
        val prefix = id?.let {"$id | "} ?: ""
        Timber.tag(TAG).d("$prefix${this@DiscoveryEvent}")
    }

    companion object {
        private const val TAG = "DiscoveryEvent"
    }

}