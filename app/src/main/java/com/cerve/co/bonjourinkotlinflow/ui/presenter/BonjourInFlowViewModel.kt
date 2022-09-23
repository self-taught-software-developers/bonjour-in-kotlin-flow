package com.cerve.co.bonjourinkotlinflow.ui.presenter

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerve.co.bonjour_in_flow.BonjourInFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BonjourInFlowViewModel @Inject constructor(
    @ApplicationContext context: Context,
) : ViewModel() {

    private val bonjourInFlow: BonjourInFlow by lazy { BonjourInFlow(context) }

    fun startDis() {
        viewModelScope.launch {

            println("bonjourInFlow $bonjourInFlow")
//            bonjourInFlow.discoverAllServices().collect {
//                //TODO add service to our list
//            }
        }
    }

}