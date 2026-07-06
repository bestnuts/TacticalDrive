plugins {
    `java-library`
    alias(libs.plugins.shadow)
    alias(libs.plugins.resourcefactory)
}

allprojects {
    subprojects {
        plugins.apply("java")
        layout.buildDirectory.set(rootProject.layout.buildDirectory.dir(name))

        repositories {
            mavenCentral()
            maven (url = "https://repo.papermc.io/repository/maven-public/")
        }

        tasks.withType<JavaCompile>().configureEach {
            options.release.set(21)
            options.encoding = Charsets.UTF_8.name()
            options.isFork = true
            options.forkOptions.memoryMaximumSize = "512m"
        }
    }
}

dependencies {
    implementation(project(":plugin"))
}

bukkitPluginYaml {
    val versionProperty = findProperty("version") as? String
        ?: throw IllegalArgumentException("version was null")

    main = "me.bestnuts.plugin.TacticalDrive"
    name = rootProject.name
    version = versionProperty
    apiVersion = "1.21"

    authors = listOf("BestNuts")
}