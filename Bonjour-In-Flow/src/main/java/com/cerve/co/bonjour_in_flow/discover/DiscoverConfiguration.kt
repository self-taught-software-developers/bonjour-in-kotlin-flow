package com.cerve.co.bonjour_in_flow.discover

import android.net.nsd.NsdManager
import androidx.annotation.IntDef

data class DiscoverConfiguration(
    val type: String,
    @ProtocolType val protocol: Int = NsdManager.PROTOCOL_DNS_SD
) {
    @Retention
    @IntDef(NsdManager.PROTOCOL_DNS_SD)
    annotation class ProtocolType
}