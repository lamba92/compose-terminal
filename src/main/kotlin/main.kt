import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlin.time.Duration.Companion.minutes
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.event.Level
import io.ktor.client.engine.cio.CIO as CIOClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ContentNegotiationClient
import io.ktor.server.cio.CIO as CIOServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ContentNegotiationServer

const val URL = "http://worldtimeapi.org/api/timezone/Europe/Berlin"

sealed interface State {
    data object NotLoaded : State
    data object Loading : State
    data class Success(val data: WorldTimeApiResponse) : State
    data class Error(val message: String) : State
}

fun main() {

    val httpClient = HttpClient(CIOClient) {
        install(ContentNegotiationClient) {
            json()
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }
    val channel = Channel<String>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    embeddedServer(CIOServer, port = 8080) {
        install(ContentNegotiationServer) {
            json()
        }
        install(CallLogging) {
            level = Level.INFO
        }
        install(RateLimit) {
            global {
                rateLimiter(100, 15.minutes, 10)
            }
        }
        routing {
            get("/time") {
                channel.send(call.request.local.remoteAddress)
                call.respond(httpClient.get(URL).body<WorldTimeApiResponse>())
            }
        }
    }.start(false)

    val json = Json {
        prettyPrint = true
    }

    singleWindowApplication {
        MaterialTheme {
            Column {
                var state: State by remember { mutableStateOf(State.NotLoaded) }
                val scope = rememberCoroutineScope()
                val ips = remember { mutableStateListOf<String>() }
                LaunchedEffect(Unit) {
                    channel.consumeAsFlow()
                        .collect { ips.add(it) }
                }
                Button(
                    modifier = Modifier.width(200.dp),
                    onClick = {
                        scope.launch {
                            state = State.Loading
                            state = try {
                                State.Success(httpClient.get(URL).body())
                            } catch (e: Exception) {
                                State.Error(e.message ?: "Unknown error")
                            }
                        }
                    },
                    content = {
                        when (state) {
                            is State.Success -> Text("Refresh time")
                            else -> Text("Get time")
                        }
                    },
                    enabled = state !is State.Loading
                )

                when (val currentState = state) {
                    is State.NotLoaded -> Text("Press the button to get the time")
                    is State.Loading -> CircularProgressIndicator()
                    is State.Success -> Text("Current time: \n ${json.encodeToString(currentState.data)}")
                    is State.Error -> Text("Error: ${currentState.message}")
                }

                Text("IPs: ${ips.joinToString()}")
            }
        }
    }
}

@Serializable
data class WorldTimeApiResponse(
    @SerialName("abbreviation")
    val abbreviation: String,

    @SerialName("client_ip")
    val clientIp: String,

    @SerialName("datetime")
    val dateTime: String,

    @SerialName("day_of_week")
    val dayOfWeek: Int,

    @SerialName("day_of_year")
    val dayOfYear: Int,

    @SerialName("dst")
    val dst: Boolean,

    @SerialName("dst_from")
    val dstFrom: String,

    @SerialName("dst_offset")
    val dstOffset: Int,

    @SerialName("dst_until")
    val dstUntil: String,

    @SerialName("raw_offset")
    val rawOffset: Int,

    @SerialName("timezone")
    val timezone: String,

    @SerialName("unixtime")
    val unixTime: Long,

    @SerialName("utc_datetime")
    val utcDateTime: String,

    @SerialName("utc_offset")
    val utcOffset: String,

    @SerialName("week_number")
    val weekNumber: Int
)

