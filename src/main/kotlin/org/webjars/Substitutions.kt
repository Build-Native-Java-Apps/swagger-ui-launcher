package org.webjars

import com.oracle.svm.core.annotate.Substitute
import com.oracle.svm.core.annotate.TargetClass
import io.github.classgraph.ClassGraph
import io.vividcode.swaggeruilauncher.graal.WebJarInfoHolder
import org.webjars.WebJarAssetLocator.WebJarInfo

@TargetClass(WebJarAssetLocator::class)
class Target_org_webjars_WebJarAssetLocator {
    @Substitute
    private fun scanForWebJars(classGraph: ClassGraph): Map<String, WebJarInfo> {
        return WebJarInfoHolder.locator.webJars().mapValues {
            val (version, groupId, uri, contents) = it.value
            WebJarInfo(
                version, groupId, uri, contents
            )
        }
    }
}