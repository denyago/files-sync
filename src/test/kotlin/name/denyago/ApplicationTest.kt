package name.denyago

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import name.denyago.plugins.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureTemplating()
        }
        client.get("/").apply {
            assertEquals(status, HttpStatusCode.OK)
            assertContains(bodyAsText(), "Welcome to the start page of our homeserver!")
        }
    }
}
