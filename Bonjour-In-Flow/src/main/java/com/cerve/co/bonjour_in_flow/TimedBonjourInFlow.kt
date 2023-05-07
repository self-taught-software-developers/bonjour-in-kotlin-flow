package com.cerve.co.bonjour_in_flow

import android.content.Context
import android.net.nsd.NsdServiceInfo
import com.cerve.co.bonjour_in_flow.discover.DiscoverEvent
import com.cerve.co.bonjour_in_flow.discover.model.ZippedDiscoverEvent
import com.cerve.co.bonjour_in_flow.discover.model.ZippedDiscoverEvent.Companion.logZippedDiscoverEmission
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Deprecated("unstable", replaceWith = ReplaceWith("BonjourInFlow"))
class TimedBonjourInFlow (private val manager: NSDManagerInFlow) {
    constructor(context: Context) : this(NSDManagerInFlowImpl.fromContext(context))

    private fun discoverZipped(
        types: Pair<String, String>
    ) : Flow<ZippedDiscoverEvent> {
        return (manager.discoverServiceByType(types.first))
            .zip((manager.discoverServiceByType(types.second))) { p1, p2 ->
                p2.resolve()?.let { r1 ->
                    p1.serviceInfo()?.let { f1 ->

                        ZippedDiscoverEvent(
                            name1 = f1.serviceName,
                            name2 = r1.serviceName,
                            type = r1.serviceType,
                            host = try {
                                r1.host
                            } catch (_: Exception) {
                                null
                            },
                            port = r1.port
                        )
                    }
                } }
            .logThreadLifecycle()
            .logZippedDiscoverEmission()
            .filterNotNull()
            .flowOn(IO)
    }

    suspend fun discoverByType(
        types: Pair<String, String>,
        timeout: Duration = 10.seconds
    ) : List<ZippedDiscoverEvent> = mutableListOf<ZippedDiscoverEvent>()
        .apply {
            withTimeoutOrNull(timeout) {
                discoverZipped(types)
                    .collectLatest { event ->
                        this@apply.add(event)
                    }
            }
        }

    suspend fun discoverByName(
        name: String,
        types: Pair<String, String>,
        timeout: Duration = 30.seconds
    ) : ZippedDiscoverEvent? = withTimeoutOrNull(timeout) {
        discoverZipped(types).first { it.name1 == name || it.name2 == name }
    }

    private suspend fun DiscoverEvent.resolve() : NsdServiceInfo? {
        return manager.resolveService(this).first().serviceInfo()
    }

    companion object {
        private const val SERVICES_DOMAIN = "_services._dns-sd._udp"

        fun String.toDiscoveryType() : String {
            return "$this._tcp"
        }

        fun <T> Flow<T>.logThreadLifecycle() : Flow<T> {
            return this
                .onStart {
                    Timber.tag(DiscoverEvent.TAG)
                        .d("startedIn context: ${currentCoroutineContext()}")
                }
                .onCompletion {
                    Timber.tag(DiscoverEvent.TAG)
                        .d("completedIn context: ${currentCoroutineContext()}")
                }
        }

        fun String.logIt(header: String = "") {
            Timber.tag(DiscoverEvent.TAG)
                .d("$header | $this")
        }

    }
}