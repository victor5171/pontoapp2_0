object Camera {
    private object Versions {
        const val cameraX = "1.0.0-rc03"
        const val cameraView = "1.0.0-alpha22"
    }

    const val core = "androidx.camera:camera-core:${Versions.cameraX}"
    const val camera2 = "androidx.camera:camera-camera2:${Versions.cameraX}"
    const val lifecycle = "androidx.camera:camera-lifecycle:${Versions.cameraX}"
    const val view = "androidx.camera:camera-view:${Versions.cameraView}"
    const val extensions = "androidx.camera:camera-extensions:${Versions.cameraView}"
}
