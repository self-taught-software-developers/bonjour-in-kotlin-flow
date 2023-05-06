package com.cerve.co.bonjour_in_flow.api

import android.content.Context
import com.cerve.co.bonjour_in_flow.DiscoveryEvent
import com.cerve.co.bonjour_in_flow.discover.NetworkServiceDiscoveryUseCase
import com.cerve.co.bonjour_in_flow.resolve.NetworkServiceResolutionUseCase
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class BonjourInFlow (
    private val discoveryUseCase: NetworkServiceDiscoveryUseCase,
    private val resolutionUseCase: NetworkServiceResolutionUseCase
) {
    constructor(context: Context) : this(
        NetworkServiceDiscoveryUseCase(context),
        NetworkServiceResolutionUseCase(context)
    )

    fun onWithTimeout(
        timeout: Duration,
        retries: Long = 0,
        retryDelay: Duration = 1.seconds,
        action: () -> Flow<DiscoveryEvent>
    ) : Flow<DiscoveryEvent> = flow {
        withTimeout(timeout) {
            action().collect { event -> emit(event) }
        }
    }.retryWhen { cause, attempt ->
        (attempt < retries).also {
            if (cause !is TimeoutCancellationException) {
                (delay(retryDelay))
            }
        }
    }.catch { emit(DiscoveryEvent.Stopped(it.message)) }

    fun onResolveWithTimeout(
        type: String,
        timeout: Duration,
        retries: Long = 0,
        retryDelay: Duration = 1.seconds,
    ) = onWithTimeout(timeout, retries, retryDelay) {
        resolutionUseCase.invoke(type)
    }

    fun onDiscoverWithTimeout(
        type: String,
        timeout: Duration,
        retries: Long = 0,
        retryDelay: Duration = 1.seconds,
    ) = onWithTimeout(timeout, retries, retryDelay) {
        discoveryUseCase.invoke(type)
    }

    companion object {
        const val TAG = "BonjourInFlow"
    }

}