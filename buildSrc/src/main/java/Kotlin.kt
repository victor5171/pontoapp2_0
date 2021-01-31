@file:Suppress("MemberVisibilityCanBePrivate")

object Kotlin {

    const val VERSION = "1.4.21"

    private object Versions {
        const val coroutinesVersion = "1.4.0"
    }

    const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:$VERSION"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesVersion}"
    const val testJunit = "org.jetbrains.kotlin:kotlin-test-junit:$VERSION"
}
