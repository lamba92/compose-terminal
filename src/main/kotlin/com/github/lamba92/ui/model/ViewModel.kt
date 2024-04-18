package com.github.lamba92.ui.model

import com.github.lamba92.WorldTimeApiBerlinUrl
import com.github.lamba92.data.WorldTimeApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import java.io.Closeable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry

class ViewModel : Closeable {
    private val scope = CoroutineScope(SupervisorJob())
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }

    private val mutableResourceFlow: MutableStateFlow<ResourceStatus> = MutableStateFlow(ResourceStatus.NotLoaded)
    val resourceFlow = mutableResourceFlow.asStateFlow()
    private val fetchTimeChannel = Channel<Unit>()

    init {
        fetchTimeChannel
            .consumeAsFlow()
            .map { httpClient.get(WorldTimeApiBerlinUrl).body<WorldTimeApiResponse>() }
            .onEach { mutableResourceFlow.emit(ResourceStatus.Success(it)) }
            .retry {
                mutableResourceFlow.emit(ResourceStatus.Error(it.message ?: "Unknown error"))
                true
            }
            .launchIn(scope)
    }

    fun fetchTime() {
        fetchTimeChannel.trySend(Unit)
    }

    override fun close() {
        scope.cancel()
    }
}