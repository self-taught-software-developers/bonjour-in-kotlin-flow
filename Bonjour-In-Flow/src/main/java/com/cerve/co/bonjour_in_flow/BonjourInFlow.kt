package com.cerve.co.bonjour_in_flow

import android.content.Context
import android.net.nsd.NsdServiceInfo
import com.cerve.co.bonjour_in_flow.discover.DiscoverConfiguration
import kotlinx.coroutines.flow.*

class BonjourInFlow(private val nsdMangerInFlow: NSDManagerInFlow) {
    constructor(context: Context) : this(NSDManagerInFlowImpl.fromContext(context))

    fun discoverServices() = nsdMangerInFlow.discoverService(
            DiscoverConfiguration(SERVICES_DOMAIN)
        ).filter { it.isService() }.onEach { it.logIt() }

    fun discoverServicesWithState() = nsdMangerInFlow.discoverService(
        DiscoverConfiguration(SERVICES_DOMAIN)
    ).onEach { it.logIt() }

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