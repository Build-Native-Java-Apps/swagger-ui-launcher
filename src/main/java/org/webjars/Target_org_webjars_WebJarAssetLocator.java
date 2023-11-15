package org.webjars;

import static java.util.Objects.requireNonNull;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.vividcode.swaggeruilauncher.graal.WebJarInfoHolder;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.graalvm.collections.Pair;
import org.webjars.WebJarAssetLocator.WebJarInfo;

@TargetClass(WebJarAssetLocator.class)
public final class Target_org_webjars_WebJarAssetLocator {
  @Substitute
  private static Map<String, WebJarInfo> scanForWebJars(ClassGraph classGraph) {
    return WebJarInfoHolder.locator.webJars().entrySet().stream().map(
            new Function<Entry<String, io.vividcode.swaggeruilauncher.graal.WebJarInfo>, Pair<String, WebJarInfo>>() {
              @Override
              public Pair<String, WebJarInfo> apply(
                  Entry<String, io.vividcode.swaggeruilauncher.graal.WebJarInfo> entry) {
                  var value = entry.getValue();
                  var info = new WebJarInfo(value.getVersion(),
                      Optional.of(new MavenProperties(value.getGroupId(), value.getArtifactId(),
                          value.getVersion())),
                      value.getUri(), value.getContents());
                  return Pair.create(entry.getKey(), info);
              }
            }).collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
  }
}
