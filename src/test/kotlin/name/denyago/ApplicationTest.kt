package name.denyago

import com.lordcodes.turtle.ShellScript
import io.ktor.client.request.* // ktlint-disable no-wildcard-imports
import io.ktor.client.statement.* // ktlint-disable no-wildcard-imports
import io.ktor.http.* // ktlint-disable no-wildcard-imports
import io.ktor.server.testing.* // ktlint-disable no-wildcard-imports
import io.mockk.every
import io.mockk.mockk
import it.skrape.core.htmlDocument
import it.skrape.selects.html5.body
import it.skrape.selects.text
import name.denyago.volumes.MontedVolumes
import name.denyago.web.startPage
import org.junit.Test
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        val mockShellScript = mockk<ShellScript>()
        every { mockShellScript.command("mount") } returns """
/dev/disk3s1s1 on / (apfs, sealed, local, read-only, journaled)
/dev/disk3s5 on /System/Volumes/Data (apfs, local, journaled, nobrowse, protect)
/dev/whatever on /mnt/Photos (ext4, and, other, stuff)
map auto_home on /System/Volumes/Data/home (autofs, automounted, nobrowse)"""

        application {
            startPage(
                DI {
                    bind<ShellScript>() with instance(mockShellScript)
                    bind<MontedVolumes>() with singleton { MontedVolumes(instance()) }
                }
            )
        }
        client.get("/").apply {
            assertEquals(status, HttpStatusCode.OK)
            htmlDocument(bodyAsText()) {
                body {
                    findFirst("p#welcome-message") {
                        assertContains(text, "Welcome to the start page of our homeserver!")
                    }

                    findAll("table#connected-drive-states tr") {
                        assertContains(text, "/mnt/Photos connected")
                        assertContains(text, "/mnt/Docs not connected")
                        assertContains(text, "/mnt/Movies not connected")
                    }
                }
            }
        }
    }
}
