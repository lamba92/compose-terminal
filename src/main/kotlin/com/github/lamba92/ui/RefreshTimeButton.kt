package com.github.lamba92.ui

import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.lamba92.ui.model.ResourceStatus

@Composable
fun RefreshTimeButton() {
    val viewModel = LocalViewModel.current
    val resourceStatus by viewModel.resourceFlow.collectAsState()
    Button(
        modifier = Modifier.width(200.dp),
        onClick = { viewModel.fetchTime() },
        content = {
            when (resourceStatus) {
                is ResourceStatus.Success -> Text("Refresh time")
                else -> Text("Get time")
            }
        },
        enabled = resourceStatus !is ResourceStatus.Loading
    )
}