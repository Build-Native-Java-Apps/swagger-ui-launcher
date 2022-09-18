val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val graalvm_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.10"
    kotlin("kapt") version "1.7.10"
    id("io.ktor.plugin") version "2.1.0"
    id("org.graalvm.buildtools.native") version "0.9.13"
}

group = "io.vividcode.swaggeruilauncher"
version = "1.0.0"
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
    implementation("org.webjars:swagger-ui:4.14.0")
    implementation("info.picocli:picocli:4.6.3")
    implementation("io.ktor:ktor-server-cio-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    kapt("info.picocli:picocli-codegen:4.6.3")
    implementation("org.graalvm.sdk:graal-sdk:$graalvm_version")
    compileOnly("org.graalvm.nativeimage:svm:$graalvm_version")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

kapt {
    arguments {
        arg("project", "${project.group}/${project.name}")
    }
}


graalvmNative {
    binaries {
        named("main") {

            fallback.set(false)
            verbose.set(true)

            buildArgs.add("--initialize-at-build-time=io.ktor,kotlin,org.webjars,io.github.classgraph,nonapi.io.github.classgraph")

            buildArgs.add("-H:+InstallExitHandlers")
            buildArgs.add("-H:+ReportUnsupportedElementsAtRuntime")
            buildArgs.add("-H:+ReportExceptionStackTraces")

            jvmArgs.add("--add-exports=org.graalvm.nativeimage.builder/com.oracle.svm.core.configure=ALL-UNNAMED")
            jvmArgs.add("--add-exports=org.graalvm.sdk/org.graalvm.nativeimage.impl=ALL-UNNAMED")
//            jvmArgs.add("-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005")

            imageName.set("swagger-ui-launcher")
        }
    }
}