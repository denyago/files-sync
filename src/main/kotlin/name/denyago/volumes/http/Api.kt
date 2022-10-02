package name.denyago.volumes.http // ktlint-disable filename

import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import io.ktor.server.request.* // ktlint-disable no-wildcard-imports
import io.ktor.server.response.* // ktlint-disable no-wildcard-imports
import io.ktor.server.routing.* // ktlint-disable no-wildcard-imports
import io.ktor.server.sessions.* // ktlint-disable no-wildcard-imports
import name.denyago.volumes.MontedVolumes
import name.denyago.web.HttpSession
import org.kodein.di.DI
import org.kodein.di.instance

fun Application.volumesApi(kodein: DI) {
    routing {
        val mountedVolumes by kodein.instance<MontedVolumes>()

        post("/mount") {
            val mountRequest = call.receive<MountRequest>()
            val result = mountedVolumes.mount(mountRequest.mountPoint)
            if (result.isFailure) {
                call.sessions.set(HttpSession(result.exceptionOrNull().toString()))
            }
            call.respondRedirect("/")
        }
    }
}
