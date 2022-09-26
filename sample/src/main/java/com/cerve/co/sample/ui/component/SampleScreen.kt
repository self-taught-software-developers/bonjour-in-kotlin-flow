package com.cerve.co.sample.ui.component

import android.net.nsd.NsdServiceInfo
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cerve.co.sample.UiDiscoveryState

@Composable
fun SampleScreen(
    discoveryState : UiDiscoveryState,
    discoveredList: List<NsdServiceInfo>
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = discoveryState.toString()) },
                backgroundColor = MaterialTheme.colors.primaryVariant,
                contentColor = Color.White
            )
        }
    ) { bounds ->

        HunterNDSScreen(
            modifier = Modifier.padding(bounds),
            bonjourListItems = discoveredList
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