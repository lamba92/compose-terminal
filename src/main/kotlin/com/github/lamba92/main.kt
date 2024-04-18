package com.github.lamba92

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.awaitApplication
import com.github.lamba92.ui.AppProviders
import com.github.lamba92.ui.RefreshTimeButton
import com.github.lamba92.ui.WorldTimeText
import com.github.lamba92.ui.model.ViewModel
import com.jthemedetecor.OsThemeDetector
import kotlinx.serialization.json.Json

suspend fun main() {

    val json = Json {
        prettyPrint = true
    }

    val viewModel = ViewModel()

    awaitApplication {
        Window(
            onCloseRequest = {
                viewModel.close()
                exitApplication()
            },
            content = {
                val isDark by isDarkFlow.collectAsState(OsThemeDetector.getDetector().isDark)
                MaterialTheme(
                    colors = if (isDark) darkColors() else lightColors()
                ) {
                    AppProviders(viewModel, json) {
                        Surface(modifier = Modifier.fillMaxSize()) {
                            Column(
                                modifier = Modifier.padding(8.dp)
                            ) {
                                RefreshTimeButton()
                                WorldTimeText()
                            }
                        }
                    }
                }
            }
        )
    }
}
