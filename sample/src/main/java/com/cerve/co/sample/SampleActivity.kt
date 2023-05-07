package com.cerve.co.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cerve.co.sample.ui.component.SampleScreen
import com.cerve.co.sample.ui.theme.BonjourInKotlinFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BonjourInKotlinFlowTheme {

                val vm : SampleViewModel = viewModel()
                val sampleUiState by vm.sampleUiState.collectAsState()

                SampleScreen(
                    discoveryState = sampleUiState.nsdState,
                    discoveredList = sampleUiState.nsdItems
                )
            }
        }
    }
}
