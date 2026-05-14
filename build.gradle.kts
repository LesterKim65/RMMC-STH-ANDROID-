buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.6.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.24")
        classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:1.9.24-1.0.20")
    }

    configurations.classpath {
        resolutionStrategy.eachDependency {
            when {
                requested.group == "org.checkerframework" && requested.name == "checker-qual" -> useVersion("3.43.0")
                requested.group == "commons-codec" && requested.name == "commons-codec" -> useVersion("1.17.1")
                requested.group == "com.google.guava" && requested.name == "failureaccess" -> useVersion("1.0.2")
                requested.group == "org.jetbrains.kotlinx" && requested.name == "kotlinx-coroutines-core-jvm" -> useVersion("1.9.0")
            }
        }
    }
}
