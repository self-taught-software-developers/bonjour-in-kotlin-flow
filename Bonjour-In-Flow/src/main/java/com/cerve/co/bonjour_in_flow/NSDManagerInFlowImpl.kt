package com.cerve.co.bonjour_in_flow

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.cerve.co.bonjour_in_flow.discover.DiscoverConfiguration
import com.cerve.co.bonjour_in_flow.discover.DiscoverEvent
import com.cerve.co.bonjour_in_flow.discover.DiscoverEventListener
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NSDManagerInFlowImpl(private val nsdManager: NsdManager) : NSDManagerInFlow {

    override fun discoverService(
        configuration: DiscoverConfiguration
    ): Flow<DiscoverEvent> = callbackFlow {

        nsdManager.discoverServices(
            configuration.type,
            configuration.protocol,
            DiscoverEventListener(this)
        )

        awaitClose {
            // also stop discovery?
        }
    }

    override fun resolveService() {
        TODO("Not yet implemented")
    }

    companion object {
        fun fromContext(
            context: Context
        ) = NSDManagerInFlowImpl(context.getSystemService(Context.NSD_SERVICE) as NsdManager)
    }

}