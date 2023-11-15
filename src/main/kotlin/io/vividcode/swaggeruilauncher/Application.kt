package io.vividcode.swaggeruilauncher

import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.vividcode.swaggeruilauncher.plugins.configureRouting
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import picocli.CommandLine
import java.nio.file.Paths
import java.util.concurrent.Callable

@CommandLine.Command(
    name = "swagger-ui-launcher", mixinStandardHelpOptions = true, version = ["1.3.0"],
    description = ["Launch Swagger UI"]
)
class Application : Callable<Void> {

    @CommandLine.ArgGroup(exclusive = false, multiplicity = "1", heading = "Source of OpenAPI spec")
    lateinit var apiSpecSources: ApiSpecSources

    @CommandLine.Option(names = ["--no-open-browser"], description = ["Open browser"], negatable = true)
    var openBrowser = true

    class ApiSpecSources {
        @CommandLine.Option(names = ["-f", "--file"], description = ["Local file"])
        lateinit var fileSources: List<String>

        @CommandLine.Option(names = ["-u", "--url"], description = ["URL"])
        lateinit var urlSources: List<String>

        fun sources(): List<ApiSpecSource> {
            return (if (this::fileSources.isInitialized) fileSources.map { LocalFileSource(Paths.get(it).toFile()) } else listOf()) +
                    (if (this::urlSources.isInitialized) urlSources.map { UrlSource(it) } else listOf())
        }
    }

    private fun sources(): List<ApiSpecSource> {
        return if (this::apiSpecSources.isInitialized) apiSpecSources.sources() else listOf()
    }

    override fun call(): Void? {
        val logger = LoggerFactory.getLogger(Application::class.java)
        val server = embeddedServer(CIO, port = 0, host = "localhost") {
            configureRouting(
                LauncherOptions(
                    sources = sources()
                )
            )
        }.start()
        val exitChannel = Channel<Boolean>(1)
        Runtime.getRuntime().addShutdownHook(Thread {
            runBlocking {
                logger.info("Shutting down the server")
                exitChannel.send(true)
            }
        })
        runBlocking {
            val port = server.resolvedConnectors()[0].port
            logger.info("Server started at port $port")
            if (openBrowser) {
                BrowserOpener.openUrl("http://localhost:$port")
            }
            exitChannel.receive()
        }
        return null
    }
}

fun main(args: Array<String>) {
    CommandLine(Application()).execute(*args)
}
