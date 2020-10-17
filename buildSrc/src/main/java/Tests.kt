object Tests {

    private object Versions {

        const val jUnit4 = "4.13"
        const val instrumentedJUnit = "1.1.2"
        const val espresso = "3.3.0"
        const val robolectric = "4.4"
        const val testXCore = "1.3.0"
        const val mockk = "1.10.2"
    }

    const val jUnit4 = "junit:junit:${Versions.jUnit4}"
    const val instrumentedJUnit = "androidx.test.ext:junit:${Versions.instrumentedJUnit}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    const val testXCore = "androidx.test:core:${Versions.testXCore}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
}
