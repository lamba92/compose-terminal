package com.github.lamba92.ui

import com.github.lamba92.ui.model.ViewModel
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.serialization.json.Json

val LocalViewModel =
    staticCompositionLocalOf<ViewModel> { error("No ViewModel provided") }
val LocalJson =
    staticCompositionLocalOf<Json> { error("No Json provided") }