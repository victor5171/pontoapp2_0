object Tests {

    private object Versions {

        const val jUnit4 = "4.13"
        const val instrumentedJUnit = "1.1.2"
        const val espresso = "3.3.0"
    }

    const val jUnit4 = "junit:junit:${Versions.jUnit4}"
    const val instrumentedJUnit = "androidx.test.ext:junit:${Versions.instrumentedJUnit}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
}
