package name.denyago.web

import com.github.mustachejava.DefaultMustacheFactory
import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import io.ktor.server.mustache.* // ktlint-disable no-wildcard-imports
import io.ktor.server.response.* // ktlint-disable no-wildcard-imports
import io.ktor.server.routing.* // ktlint-disable no-wildcard-imports
import io.ktor.server.sessions.* // ktlint-disable no-wildcard-imports
import name.denyago.volumes.DriveState
import name.denyago.volumes.MontedVolumes
import name.denyago.volumes.MountPoint
import org.kodein.di.DI
import org.kodein.di.instance

fun Application.startPage(kodein: DI) {
    val mountedVolumes by kodein.instance<MontedVolumes>()

    install(Mustache) {
        mustacheFactory = DefaultMustacheFactory("templates")
    }

    routing {
        get("/") {
            val driveStates = mountedVolumes.statuses(
                listOf("/mnt/Photos", "/mnt/Docs", "/mnt/Movies").map { MountPoint(it) }
            )
            val page = IndexPage(
                hddState = HddState(driveStates.any { it.connected }),
                driveStates = driveStates,
                lastDriveError = call.sessions.get<HttpSession>()?.error
            )
            call.sessions.clear<HttpSession>()
            call.respond(MustacheContent("index.hbs", page))
        }
    }
}

data class HddState(val busy: Boolean)
data class IndexPage(val hddState: HddState, val driveStates: List<DriveState>, val lastDriveError: String?)
