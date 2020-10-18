object Android {

    private object Versions {
        const val appCompat = "1.2.0"
        const val constraintLayout = "2.0.1"
        const val core = "1.3.2"
        const val lifecycle = "2.2.0"
        const val arch = "2.1.0"
        const val recyclerView = "1.1.0"
        const val material = "1.2.1"
        const val activity = "1.2.0-beta01"
        const val fragment = "1.3.0-beta01"
    }

    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.core}"
    const val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleLiveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val coreTesting = "androidx.arch.core:core-testing:${Versions.arch}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val activity = "androidx.activity:activity-ktx:${Versions.activity}"
    const val fragment = "androidx.fragment:fragment:${Versions.fragment}"
}
