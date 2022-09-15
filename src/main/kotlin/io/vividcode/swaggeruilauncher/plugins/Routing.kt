package io.vividcode.swaggeruilauncher.plugins

import io.vividcode.swaggeruilauncher.LauncherOptions
import io.vividcode.swaggeruilauncher.LocalFileSource
import io.vividcode.swaggeruilauncher.UrlSource
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.webjars.*
import java.time.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import java.io.File
import java.lang.IllegalArgumentException
import java.nio.file.Path

data class SwaggerUIUrl(
    val url: String,
    val name: String
)

data class RewriteSwaggerJsPluginOptions(
    var urls: List<SwaggerUIUrl> = mutableListOf()
)

val RewriteSwaggerJsPlugin = createApplicationPlugin(name ="RewriteSwaggerJs", createConfiguration = ::RewriteSwaggerJsPluginOptions) {
    val urls = pluginConfig.urls.joinToString(", ") {
        """{url: "${it.url}", name: "${it.name}"}"""
    }
    pluginConfig.apply {
        onCallRespond { call ->
            transformBody { body ->
                if (call.request.uri == "/webjars/swagger-ui/swagger-initializer.js") {
                    """
                window.onload = function() {
                  window.ui = SwaggerUIBundle({
                    urls: [$urls],
                    dom_id: '#swagger-ui',
                    deepLinking: true,
                    presets: [
                      SwaggerUIBundle.presets.apis,
                      SwaggerUIStandalonePreset
                    ],
                    plugins: [
                      SwaggerUIBundle.plugins.DownloadUrl
                    ],
                    layout: "StandaloneLayout"
                  });
                };
            """
                } else {
                    body
                }
            }
        }
    }
}

fun Application.configureRouting(options: LauncherOptions) {
    install(Webjars) {
        path = "/webjars"
    }

    install(RewriteSwaggerJsPlugin) {
        urls = options.sources.mapIndexed { index, source -> SwaggerUIUrl(
            "/openapi/$index",
            source.name()
        ) }
    }

    routing {
        get("/") {
            call.respondRedirect("/webjars/swagger-ui/index.html")
        }

        get("/openapi/{index}") {
            val source = call.parameters["index"]?.toInt()?.run {
                options.sources[this]
            } ?: throw IllegalArgumentException("Invalid API spec")
            when (source) {
                is LocalFileSource -> call.respondFile(source.file)
                is UrlSource -> call.respondRedirect(source.url)
            }

        }
    }
}
