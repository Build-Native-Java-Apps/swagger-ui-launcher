val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val graalvm_version: String by project
val picocli_version: String by project

plugins {
    application
    kotlin("jvm") version "1.9.20"
    kotlin("kapt") version "1.9.20"
    id("io.ktor.plugin") version "2.2.4"
    id("org.graalvm.buildtools.native") version "0.9.16"
}

group = "io.vividcode.swaggeruilauncher"
version = "1.3.0"
application {
    mainClass.set("io.vividcode.swaggeruilauncher.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-webjars-jvm:$ktor_version")
    implementation("org.webjars:swagger-ui:4.14.2")
    implementation("info.picocli:picocli:$picocli_version")
    implementation("io.ktor:ktor-server-cio-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    kapt("info.picocli:picocli-codegen:$picocli_version")
    implementation("org.graalvm.sdk:graal-sdk:$graalvm_version")
    compileOnly("org.graalvm.nativeimage:svm:$graalvm_version")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

kotlin {
    jvmToolchain(17)
}

kapt {
    arguments {
        arg("project", "${project.group}/${project.name}")
    }
}

graalvmNative {
    binaries {
        named("main") {

            javaLauncher.set(javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(17))
            })

            fallback.set(false)
            verbose.set(true)

            buildArgs.add("--initialize-at-build-time=io.ktor,kotlin,org.webjars,io.github.classgraph,nonapi.io.github.classgraph,org.slf4j,ch.qos.logback")

            buildArgs.add("-H:+InstallExitHandlers")
            buildArgs.add("-H:+ReportUnsupportedElementsAtRuntime")
            buildArgs.add("-H:+ReportExceptionStackTraces")
            buildArgs.add("-H:+AllowDeprecatedBuilderClassesOnImageClasspath")

            buildArgs.add("--features=io.vividcode.swaggeruilauncher.graal.WebJarResourceFeature")

            jvmArgs.add("--add-exports=org.graalvm.nativeimage.builder/com.oracle.svm.core.configure=ALL-UNNAMED")
            jvmArgs.add("--add-exports=org.graalvm.sdk/org.graalvm.nativeimage.impl=ALL-UNNAMED")
//            jvmArgs.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005")

            imageName.set("swagger-ui-launcher")
        }
    }
}