import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.plugins.ide.eclipse.model.EclipseModel
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
        google()
        maven(url = "https://central.sonatype.com/repository/maven-snapshots/")
    }
    dependencies {
        val kotlinVersion: String by project
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    apply(plugin = "eclipse")
    apply(plugin = "idea")

    extensions.configure<IdeaModel>("idea") {
        module {
            outputDir = file("build/classes/java/main")
            testOutputDir = file("build/classes/java/test")
        }
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    // Java 17 compatibility
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // Kotlin JVM target 17
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }

    // Assets list generation
    tasks.register("generateAssetList") {
        inputs.dir("${rootDir}/assets/")

        doLast {
            val assetsFolder = file("${rootDir}/assets/")
            val assetsFile = file("${assetsFolder.path}/assets.txt")
            assetsFile.delete()

            fileTree(assetsFolder)
                .files
                .map { it.relativeTo(assetsFolder).invariantSeparatorsPath }
                .sorted()
                .forEach { assetsFile.appendText("$it\n") }
        }
    }

    tasks.named("processResources") {
        dependsOn("generateAssetList")
    }

    tasks.withType<JavaCompile>().configureEach {
        options.isIncremental = true
    }

    // version + appName
    val projectVersion: String by project
    version = projectVersion
    extra["appName"] = "KotlinRogue"

    repositories {
        mavenCentral()
        mavenLocal()
        maven(url = "https://central.sonatype.com/repository/maven-snapshots/")
        maven(url = "https://jitpack.io")
    }
}

extensions.configure<EclipseModel>("eclipse") {
    project {
        name = "KotlinRogue-parent"
    }
}
