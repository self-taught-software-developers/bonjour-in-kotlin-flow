package com.cerve.co.bonjour_in_flow

import com.cerve.co.bonjour_in_flow.discover.DiscoverConfiguration
import com.cerve.co.bonjour_in_flow.discover.DiscoverEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.internal.ChannelFlow
import java.lang.reflect.Type

interface NSDManagerInFlow {

    fun discoverService(configuration: DiscoverConfiguration) : Flow<DiscoverEvent>
    fun discoverServiceByType(type: String) : Flow<DiscoverEvent>

    fun resolveService(event: DiscoverEvent) : Flow<DiscoverEvent>

}