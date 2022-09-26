package com.cerve.co.sample.ui.component

import android.graphics.drawable.Icon
import android.net.nsd.NsdServiceInfo
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cerve.co.bonjour_in_flow.discover.DiscoverEvent

@Composable
fun SampleScreen(
    bonjourDiscoveryList: List<NsdServiceInfo>
) {

    Scaffold(
        floatingActionButton = {

        }
    ) { bounds ->

        HunterNDSScreen(
            modifier = Modifier.padding(bounds),
            bonjourListItems = bonjourDiscoveryList
        )

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HunterNDSScreen(
    modifier: Modifier = Modifier,
    bonjourListItems: List<NsdServiceInfo>
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(12.dp)
    ) {

        itemsIndexed(bonjourListItems, key = { index, _ -> index }) { index, item ->
            Column(
                modifier = Modifier.animateItemPlacement()
            ) {
                Text(text = item.serviceName)
//                Text(text = item.serviceDSN)
//                item.serviceIp?.let {
//                    Text(text = "$it : ${item.servicePort}")
//                }
            }

            if (index != bonjourListItems.lastIndex) {
                Divider(modifier = Modifier.padding(vertical = 12.dp))
            }
        }
    }
}