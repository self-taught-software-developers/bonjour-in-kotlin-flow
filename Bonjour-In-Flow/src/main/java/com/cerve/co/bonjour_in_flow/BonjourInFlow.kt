package com.cerve.co.bonjour_in_flow

import android.content.Context
import android.net.nsd.NsdServiceInfo
import android.util.Log
import com.cerve.co.bonjour_in_flow.discover.DiscoverConfiguration
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart

class BonjourInFlow(private val nsdMangerInFlow: NSDManagerInFlow) {
    constructor(context: Context) : this(NSDManagerInFlowImpl.fromContext(context))

    fun discoverAllServices() = nsdMangerInFlow.discoverService(
            DiscoverConfiguration(SERVICES_DOMAIN)
        ).onEmpty { Log.d("DiscoverEvent", "Started") }

    fun discoverByServiceType(serviceType: String) {
        nsdMangerInFlow.discoverService(
            DiscoverConfiguration(serviceType)
        )
    }

    fun discoverByServiceName(serviceName: String) {
        TODO("Not yet implemented")
    }

    private fun NsdServiceInfo.resolveService() {

    }

    companion object {
        private const val SERVICES_DOMAIN = "_services._dns-sd._udp"
//        private const val REALTEK_TYPE = "_Ayla_Device._tcp"
//        private const val MARVELL_TYPE = "_hap._tcp"
    }

}