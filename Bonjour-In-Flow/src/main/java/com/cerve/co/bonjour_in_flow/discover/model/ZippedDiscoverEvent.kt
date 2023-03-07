package com.cerve.co.bonjour_in_flow.discover.model

import com.cerve.co.bonjour_in_flow.discover.DiscoverEvent.Companion.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import java.net.InetAddress

data class ZippedDiscoverEvent(
    val name1 : String,
    val name2 : String,
    val type : String,
    val host : InetAddress?,
    val port : Int
) {
    fun logIt() {
        Timber.tag(TAG).d(toString())
    };
    companion object {
        fun Flow<ZippedDiscoverEvent?>.logZippedDiscoverEmission() = onEach { it?.logIt() }

    }
}
