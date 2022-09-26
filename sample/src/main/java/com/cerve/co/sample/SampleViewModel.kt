package com.cerve.co.sample

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerve.co.bonjour_in_flow.BonjourInFlow
import com.cerve.co.bonjour_in_flow.BonjourInFlow.Companion.MARVELL_TYPE
import com.cerve.co.bonjour_in_flow.BonjourInFlow.Companion.toDiscoveryType
import com.cerve.co.bonjour_in_flow.discover.DiscoverEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    private val bonjourInFlow : BonjourInFlow by lazy { BonjourInFlow(context) }

    init {
        viewModelScope.launch {

            bonjourInFlow.discoverServicesWithState().collect { event ->
                //TODO add or remove from the list.
            }

        }
    }

}
