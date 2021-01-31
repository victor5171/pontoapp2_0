object Tests {

    private object Versions {

        const val jUnit4 = "4.13"
        const val instrumentedJUnit = "1.1.2"
        const val espresso = "3.3.0"
        const val robolectric = "4.4"
        const val testXCore = "1.3.0"
        const val mockk = "1.10.2"
        const val kotest = "4.3.2"
    }

    const val jUnit4 = "junit:junit:${Versions.jUnit4}"
    const val instrumentedJUnit = "androidx.test.ext:junit:${Versions.instrumentedJUnit}"
    const val kotestRunner = "io.kotest:kotest-runner-junit5:${Versions.kotest}"
    const val kotestFrameworkJvm = "io.kotest:kotest-framework-api-jvm:${Versions.kotest}"
    const val kotestRobolectric = "io.kotest:kotest-extensions-robolectric:${Versions.kotest}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val testXCore = "androidx.test:core:${Versions.testXCore}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
}
