package io.vividcode.swaggeruilauncher

import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.vividcode.swaggeruilauncher.plugins.configureRouting
import picocli.CommandLine
import java.io.File
import java.nio.file.Paths
import java.util.concurrent.Callable

@CommandLine.Command(
    name = "swagger-ui-launcher", mixinStandardHelpOptions = true, version = ["1.0.0"],
    description = ["Launch Swagger UI"]
)
class Application : Callable<Void> {

    @CommandLine.ArgGroup(exclusive = false, multiplicity = "1", heading = "Source of OpenAPI spec")
    lateinit var apiSpecSources: ApiSpecSources

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
        embeddedServer(CIO, port = 0, host = "localhost") {
            configureRouting(
                LauncherOptions(
                    sources = sources()
                )
            )
        }.start(wait = true)
        return null
    }
}

fun main(args: Array<String>) {
    CommandLine(Application()).execute(*args)
}
