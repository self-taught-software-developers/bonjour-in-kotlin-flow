package com.cerve.co.bonjour_in_flow

import android.net.nsd.NsdManager
import android.net.nsd.NsdManager.DiscoveryListener
import android.net.nsd.NsdManager.ResolveListener
import android.net.nsd.NsdServiceInfo
import com.cerve.co.bonjour_in_flow.discover.DiscoverConfiguration

class BonjourInFlowImpl(private val nsdMangerInFlow: NSDManagerInFlow) : BonjourInFlow {

    override fun discoverAllServices() {
        nsdMangerInFlow.discoverService(
            DiscoverConfiguration(SERVICES_DOMAIN)
        )
    }

    override fun discoverByServiceType(serviceType: String) {
        nsdMangerInFlow.discoverService(
            DiscoverConfiguration(serviceType)
        )
    }

    override fun discoverByServiceName(serviceName: String) {
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