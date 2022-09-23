package com.cerve.co.bonjourinkotlinflow.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cerve.co.bonjourinkotlinflow.ui.presenter.BonjourInFlowViewModel

@Composable
fun SampleScreen(sampleViewModel: BonjourInFlowViewModel = viewModel()) {

    Scaffold { bounds ->

        SampleScreeList(
            modifier = Modifier.padding(bounds).fillMaxWidth()
        )

    }

}

@Composable
fun SampleScreeList(
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier,
        color = Color.White
    ) {



    }

}