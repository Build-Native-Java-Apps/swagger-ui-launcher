package io.vividcode.swaggeruilauncher.graal

import com.oracle.svm.core.annotate.AutomaticFeature
import com.oracle.svm.core.configure.ResourcesRegistry
import org.graalvm.nativeimage.ImageSingletons
import org.graalvm.nativeimage.hosted.Feature
import org.graalvm.nativeimage.impl.ConfigurationCondition
import org.webjars.OpenWebJarAssetLocator
import org.webjars.WebJarAssetLocator
import java.net.JarURLConnection
import java.net.URL
import java.util.*


@AutomaticFeature
class WebJarResourceFeature : Feature {

    override fun beforeAnalysis(access: Feature.BeforeAnalysisAccess) {
        WebJarInfoHolder.locator = OpenWebJarAssetLocator(WebJarAssetLocator())
        val registry: ResourcesRegistry = ImageSingletons.lookup(ResourcesRegistry::class.java)
        WebJarInfoHolder.locator.webJars().values.flatMap { it.contents }.forEach {
            registry.addResources(ConfigurationCondition.alwaysTrue(), it)
        }

    }
}