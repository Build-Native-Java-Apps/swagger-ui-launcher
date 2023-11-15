package org.webjars

class OpenWebJarAssetLocator(private val delegate: WebJarAssetLocator) : WebJarAssetLocator() {

    fun webJars() = delegate.allWebJars.mapValues {
        val webJarInfo = it.value
        io.vividcode.swaggeruilauncher.graal.WebJarInfo(
            webJarInfo.version,
            webJarInfo.groupId,
            webJarInfo.artifactId,
            webJarInfo.uri,
            webJarInfo.contents.toList()
        )
    }
}

