package io.vividcode.swaggeruilauncher.graal

import java.net.URI

data class WebJarInfo(
    val version: String,
    val groupId: String,
    val artifactId: String,
    val uri: URI,
    val contents: List<String>
)