package name.denyago

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import name.denyago.plugins.*
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import java.util.*
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

    @Test
    fun testProvideFakeRandom() {
        val log = arrayListOf<String>()
        testApplication {
            /** Calls the [myKodeinApp] module with a [Random] class that always return 7. */
            application {
                myKodeinApp(
                    DI {
                        bind<Random>() with singleton {
                            object : Random() {
                                override fun next(bits: Int): Int = 7.also { log += "Random.next" }
                            }
                        }
                    }
                )
            }

            /**
             * Checks that the single route, returns a constant value '7' from the mock,
             * and that the [Random.next] has been called just once
             */
            val response = client.get("/random")
            assertEquals("Random number in 0..99: 7", response.bodyAsText())
            assertEquals(listOf("Random.next"), log)
        }
    }
}
