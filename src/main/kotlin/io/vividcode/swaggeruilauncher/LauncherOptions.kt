package io.vividcode.swaggeruilauncher

import java.io.File

sealed interface ApiSpecSource {
    fun name(): String
}

data class LocalFileSource(val file: File) : ApiSpecSource {
    override fun name(): String {
        return file.path
    }
}

data class UrlSource(val url: String) : ApiSpecSource {
    override fun name(): String {
        return url
    }
}

data class LauncherOptions(
    val sources: List<ApiSpecSource>
)