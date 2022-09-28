package com.cerve.co.bonjour_in_flow

import android.content.Context
import android.net.nsd.NsdServiceInfo
import com.cerve.co.bonjour_in_flow.BonjourInFlow.Companion.logThreadLifecycle
import com.cerve.co.bonjour_in_flow.discover.DiscoverEvent
import com.cerve.co.bonjour_in_flow.discover.model.ZippedDiscoverEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeoutOrNull

class TimedBonjourInFlow (private val manager: NSDManagerInFlow) {
    constructor(context: Context) : this(NSDManagerInFlowImpl.fromContext(context))

    suspend fun searchByNameWithTimeout(
        name: String,
        timeMillis: Long = 60_000L,
        types: Pair<String, String>
    ) : ZippedDiscoverEvent? {

        return withTimeoutOrNull(timeMillis) {
            (manager.discoverServiceByType(types.first))
                .zip((manager.discoverServiceByType(types.second))) { p1, p2 ->

                    p2.resolve()?.let { r1 ->
                        p1.serviceInfo()?.let { f1 ->

                            ZippedDiscoverEvent(
                                name1 = f1.serviceName,
                                name2 = r1.serviceName,
                                type = r1.serviceType,
                                host = r1.host,
                                port = r1.port
                            )
                        }
                    }
                }.logThreadLifecycle()
                .filterNotNull()
                .onEach { it.logIt() }
                .flowOn(Dispatchers.IO)
                .first { it.name1 == name || it.name2 == name }
        }

    }

    private suspend fun DiscoverEvent.resolve() : NsdServiceInfo? {
        return manager.resolveService(this).first().serviceInfo()
    }

}