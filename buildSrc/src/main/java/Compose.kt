object Compose {

    const val VERSION = "1.0.0-beta01"

    private object Versions {
        const val constraintLayout = "1.0.0-alpha03"
    }

    const val ui = "androidx.compose.ui:ui:$VERSION"
    const val uiTooling = "androidx.compose.ui:ui-tooling:$VERSION"
    const val uiTest = "androidx.compose.ui:ui-test-junit4:$VERSION"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:${Versions.constraintLayout}"
    const val foundation = "androidx.compose.foundation:foundation:$VERSION"
    const val material = "androidx.compose.material:material:$VERSION"
    const val materialIcons = "androidx.compose.material:material-icons-core:$VERSION"
    const val materialIconsExtended = "androidx.compose.material:material-icons-extended:$VERSION"
    const val runtimeLiveData = "androidx.compose.runtime:runtime-livedata:$VERSION"
}
