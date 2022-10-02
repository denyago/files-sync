package name.denyago // ktlint-disable filename

import com.lordcodes.turtle.ShellScript
import io.ktor.server.application.* // ktlint-disable no-wildcard-imports
import io.ktor.server.engine.* // ktlint-disable no-wildcard-imports
import io.ktor.server.netty.* // ktlint-disable no-wildcard-imports
import io.ktor.server.sessions.* // ktlint-disable no-wildcard-imports
import name.denyago.volumes.MontedVolumes
import name.denyago.volumes.http.volumesApi
import name.denyago.web.HttpSession
import name.denyago.web.startPage
import name.denyago.web.staticResources
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun main() {
    val di = DI {
        bind<ShellScript>() with singleton { ShellScript() }
        bind<MontedVolumes>() with singleton { MontedVolumes(instance()) }
    }
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(Sessions) {
            cookie<HttpSession>("the_session")
        }

        staticResources()
        startPage(di)
        volumesApi(di)
    }.start(wait = true)
}
