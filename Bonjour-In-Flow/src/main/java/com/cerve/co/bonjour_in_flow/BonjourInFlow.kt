package com.cerve.co.bonjour_in_flow

interface BonjourInFlow {

    fun discoverAllServices()

    fun discoverByServiceType(serviceType: String)

    fun discoverByServiceName(serviceName: String)

}