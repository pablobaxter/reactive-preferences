plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.7.10"
}

repositories {
    mavenCentral()
    google()
    maven("https://plugins.gradle.org/m2/")
}

dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation("com.android.tools.build:gradle:7.3.0")
    implementation("com.android.tools.build:gradle-api:7.3.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.7.10")
    implementation("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.2")
    implementation("com.google.dagger:hilt-android-gradle-plugin:2.43.2")
}

repositories {
    google()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("frybitsAppPlugin") {
            id = "frybits-application"
            implementationClass = "com.frybits.gradle.FrybitsApplicationPlugin"
        }

        create("frybitsLibraryPlugin") {
            id = "frybits-library"
            implementationClass = "com.frybits.gradle.FrybitsLibraryPlugin"
        }

        create("frybitsTestPlugin") {
            id = "frybits-test"
            implementationClass = "com.frybits.gradle.FrybitsTestPlugin"
        }
    }
}
