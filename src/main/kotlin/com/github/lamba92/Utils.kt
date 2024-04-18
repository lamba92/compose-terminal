package com.github.lamba92

import com.jthemedetecor.OsThemeDetector
import java.util.function.Consumer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val WorldTimeApiBerlinUrl = "http://worldtimeapi.org/api/timezone/Europe/Berlin"

val isDarkFlow: Flow<Boolean>
    get() = callbackFlow {
        val detector = OsThemeDetector.getDetector()
        val listener = Consumer<Boolean> {
            trySend(it)
        }
        detector.registerListener(listener)
        awaitClose { detector.removeListener(listener) }
    }