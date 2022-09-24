package com.cerve.co.sample

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerve.co.bonjour_in_flow.BonjourInFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    private val bonjourInFlow : BonjourInFlow by lazy { BonjourInFlow(context) }

    init {
        viewModelScope.launch {
            bonjourInFlow.discoverAllServices().collect { event ->
                Log.d("SampleViewModel", "DiscoverEvent : $event")
            }
        }
    }

}
