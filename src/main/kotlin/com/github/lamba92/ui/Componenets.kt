package com.github.lamba92.ui

import com.github.lamba92.ui.model.ResourceStatus
import com.github.lamba92.ui.model.ViewModel
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun WorldTimeText() {
    val json = LocalJson.current
    when (val currentState = LocalViewModel.current.resourceFlow.collectAsState().value) {
        is ResourceStatus.NotLoaded -> Text("Press the button to get the time")
        is ResourceStatus.Loading -> CircularProgressIndicator()
        is ResourceStatus.Success -> Text("Current time: \n ${json.encodeToString(currentState.data)}")
        is ResourceStatus.Error -> Text("Error: ${currentState.message}")
    }
}

@Composable
fun AppProviders(viewModel: ViewModel, json: Json, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalViewModel provides viewModel,
        LocalJson provides json
    ) {
        content()
    }
}