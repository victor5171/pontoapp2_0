import org.gradle.api.JavaVersion

object SdkConfig {
    const val COMPILE_SDK_VERSION = 30
    const val MIN_SDK_VERSION = 22
    const val TARGET_SDK_VERSION = 30
    const val BUILD_TOOLS_VERSION = "30.0.2"
    const val ANDROID_TOOLS_VERSION = "4.0.1"
    val JAVA_VERSION = JavaVersion.VERSION_1_8
}
