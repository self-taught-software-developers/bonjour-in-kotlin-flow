package com.cerve.co.bonjour_in_flow

import com.cerve.co.bonjour_in_flow.discover.DiscoverConfiguration
import com.cerve.co.bonjour_in_flow.discover.DiscoverEvent
import kotlinx.coroutines.flow.Flow

interface NSDManagerInFlow {

    fun discoverService(configuration: DiscoverConfiguration) : Flow<DiscoverEvent>
    fun resolveService()

}