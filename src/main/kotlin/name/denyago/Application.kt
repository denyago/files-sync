package name.denyago

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import name.denyago.plugins.*
import org.kodein.di.*
import java.security.SecureRandom
import java.util.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        myKodeinApp(
            DI {
                bind<Random>() with singleton { SecureRandom() }

                configureTemplating()
            }
        )
    }.start(wait = true)
}

fun Application.myKodeinApp(kodein: DI) {
    val random by kodein.instance<Random>()

    routing {
        get("/random") {
            val range = 0 until 100
            call.respondText("Random number in $range: ${random[range]}")
        }
    }
}

/**
 * Convenience [Random] extension operator method to get a random integral value inside the specified [range].
 */
private operator fun Random.get(range: IntRange) = range.first + this.nextInt(range.last - range.first)
