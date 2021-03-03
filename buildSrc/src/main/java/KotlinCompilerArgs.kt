object KotlinCompilerArgs {
    const val allowResultReturnType = "-Xallow-result-return-type"
    const val allowExperimentalCoroutinesApi = "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi"
    const val allowFlowPreview = "-Xuse-experimental=kotlinx.coroutines.FlowPreview"
    const val allowExperimentalStdlibApi = "-Xopt-in=kotlin.ExperimentalStdlibApi"
    const val explicitApi = "-Xexplicit-api=strict"
    const val jvmDefault = "-Xjvm-default=all"
}
