@file:Suppress("MemberVisibilityCanBePrivate")

object Kotlin {

    const val VERSION = "1.4.30"

    private object Versions {
        const val coroutinesVersion = "1.4.2"
    }

    const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:$VERSION"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesVersion}"
    const val playServicesCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutinesVersion}"
    const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$VERSION"
    const val testJunit = "org.jetbrains.kotlin:kotlin-test-junit:$VERSION"
}
