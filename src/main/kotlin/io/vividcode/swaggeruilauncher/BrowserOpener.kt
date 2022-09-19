package io.vividcode.swaggeruilauncher

import java.util.*

/**
 * Code adopted from https://stackoverflow.com/a/28807079
 */
object BrowserOpener {
    fun openUrl(url:String) {
        val os = System.getProperty("os.name").lowercase(Locale.getDefault())
        when {
            os.indexOf("win") >= 0 -> windows(url)
            os.indexOf("mac") >= 0 -> mac(url)
            else -> linux(url)
        }
    }

    private fun windows(url: String) {
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler $url")
    }

    private fun mac(url: String) {
        Runtime.getRuntime().exec("open $url")
    }

    private fun linux(url: String) {
        val browsers = listOf("google-chrome", "firefox", "mozilla", "epiphany", "konqueror",
            "netscape", "opera", "links", "lynx")
        val cmd = browsers.joinToString(" || ") {
            """$it "$url"""
        }
        Runtime.getRuntime().exec(arrayOf("sh", "-c", cmd))
    }
}