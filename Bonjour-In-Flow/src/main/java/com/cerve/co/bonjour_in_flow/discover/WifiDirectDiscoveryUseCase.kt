package com.cerve.co.bonjour_in_flow.discover

import android.content.Context
import android.net.wifi.p2p.WifiP2pManager
import kotlinx.coroutines.flow.callbackFlow

class WifiDirectDiscoveryUseCase(
    context: Context,
    wifiP2pManager: WifiP2pManager
) {

//    constructor(context: Context) : this(
//        context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager,
//        context
//    )

//    operator fun invoke(type: String) = callbackFlow {
//        val buddies = mutableMapOf<String, String>()
//
//        val txtListener = WifiP2pManager.DnsSdTxtRecordListener { fullDomain, record, device ->
//            record["buddyname"]?.also {
//                buddies[device.deviceAddress] = it
//
//                trySendBlocking("$fullDomain, $record, $device")
//            }
//        }
//
//
//        val servListener =
//            WifiP2pManager.DnsSdServiceResponseListener { instanceName, registrationType, resourceType ->
//
//                // Update the device name with the human-friendly version from
//                // the DnsTxtRecord, assuming one arrived.
//                resourceType.deviceName = buddies[resourceType.deviceAddress] ?: resourceType.deviceName
//
//                // Add to the custom adapter defined specifically for showing
//                // wifi devices.
////            val fragment = fragmentManager
////                .findFragmentById(R.id.frag_peerlist) as WiFiDirectServicesList
////            (fragment.listAdapter as WiFiDevicesAdapter).apply {
////                add(resourceType)
////                notifyDataSetChanged()
////            }
//                trySendBlocking(registrationType)
//
////            Log.d(TAG, "onBonjourServiceAvailable $instanceName")
//            }
//        val channel = nsdManager.initialize(context, Looper.getMainLooper(), null)
//        nsdManager.setDnsSdResponseListeners(
//            channel,
//            servListener,
//            txtListener
//        )
//        val serviceRequest = WifiP2pDnsSdServiceRequest.newInstance()
//        nsdManager.addServiceRequest(
//            channel,
//            serviceRequest,
//            object : WifiP2pManager.ActionListener {
//                override fun onSuccess() {
//                }
//
//                override fun onFailure(p0: Int) {
//                    cancel("addServiceRequest $p0")
//                }
//
//            }
//        )
//        nsdManager.discoverServices(
//            channel,
//            object : WifiP2pManager.ActionListener {
//                override fun onSuccess() {
//
//                }
//
//                override fun onFailure(code: Int) {
//                    when (code) {
//                        WifiP2pManager.P2P_UNSUPPORTED -> {
//                            cancel("Wi-Fi Direct isn't supported on this device.")
//                        }
//                    }
//                }
//
//            }
//        )
//
//        awaitClose {
//
//        }
//
//    }
}