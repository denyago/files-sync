package name.denyago.plugins

import com.github.mustachejava.DefaultMustacheFactory
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.mustache.Mustache
import io.ktor.server.mustache.MustacheContent
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import name.denyago.pages.DriveState
import name.denyago.pages.HddState
import name.denyago.pages.IndexPage

fun Application.configureTemplating() {
    install(Mustache) {
        mustacheFactory = DefaultMustacheFactory("templates")
    }

    routing {
        get("/") {
            val page = IndexPage(
                hddState = HddState(busy = true),
                driveStates = listOf(
                    DriveState("/mnt/Photos", connected = true),
                    DriveState("/mnt/Docs", connected = false),
                    DriveState("/mnt/Movies", connected = true)
                ),
                lastDriveError = "Non-root can't mount volumes."
            )
            call.respond(MustacheContent("index.hbs", page))
        }
    }
}