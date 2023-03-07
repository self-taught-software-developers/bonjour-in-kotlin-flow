package com.cerve.co.bonjour_in_flow

import android.content.Context
import com.cerve.co.bonjour_in_flow.discover.DiscoverConfiguration
import com.cerve.co.bonjour_in_flow.discover.DiscoverEvent
import com.cerve.co.bonjour_in_flow.discover.DiscoverEvent.Companion.TAG
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class BonjourInFlow(private val manager: NSDManagerInFlow) {
    constructor(context: Context) : this(NSDManagerInFlowImpl.fromContext(context))

    fun discoverServices() = channelFlow {
        manager.discoverService(DiscoverConfiguration(SERVICES_DOMAIN)).collect { event ->

            launch {

                event.serviceInfo()?.let { service ->
                    manager.discoverServiceByType(service.serviceName.toDiscoveryType())
                        .filter { it.isService() }
                        .mapResolve()
                        .collect { send(it) }
                }

            }
        }
    }.flowOn(IO)

    fun discoverServicesWithState() = channelFlow {
        manager.discoverService(DiscoverConfiguration(SERVICES_DOMAIN)).collect { event ->

            launch {

                event.serviceInfo()?.let { service ->
                    manager.discoverServiceByType(service.serviceName.toDiscoveryType())
                        .mapResolve()
                        .collect { send(it) }
                } ?: send(event)

            }
        }
    }.flowOn(IO)


    private fun <T: DiscoverEvent> Flow<T>.mapResolve() : Flow<DiscoverEvent> {
        return this.map { event ->
            manager.resolveService(event).first()
        }
    }

    companion object {
        private const val SERVICES_DOMAIN = "_services._dns-sd._udp"

        fun String.toDiscoveryType() : String {
            return "$this._tcp"
        }

        fun <T> Flow<T>.logThreadLifecycle() : Flow<T> {
            return this
                .onStart {
                    Timber.tag(TAG)
                        .d("startedIn context: ${currentCoroutineContext()}")
                }
                .onCompletion {
                    Timber.tag(TAG)
                        .d("completedIn context: ${currentCoroutineContext()}")
                }
        }

        fun String.logIt(header: String = "") {
            Timber.tag(TAG)
                .d("$header | $this")
        }

    }

}