package name.denyago.web

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import io.ktor.server.http.content.* // ktlint-disable no-wildcard-imports
import io.ktor.server.routing.* // ktlint-disable no-wildcard-imports

fun Application.staticResources() {
    routing {
        static("/static") {
            resources("static")
        }
    }
}
