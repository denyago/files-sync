package name.denyago // ktlint-disable filename

import com.lordcodes.turtle.ShellScript
import io.ktor.server.engine.* // ktlint-disable no-wildcard-imports
import io.ktor.server.netty.* // ktlint-disable no-wildcard-imports
import name.denyago.volumes.MontedVolumes
import name.denyago.web.startPage
import name.denyago.web.staticResources
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        staticResources()
        startPage(
            DI {
                bind<ShellScript>() with singleton { ShellScript() }
                bind<MontedVolumes>() with singleton { MontedVolumes(instance()) }
            }
        )
    }.start(wait = true)
}
