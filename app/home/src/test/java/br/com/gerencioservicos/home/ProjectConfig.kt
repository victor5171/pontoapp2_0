package br.com.gerencioservicos.home

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.extensions.robolectric.RobolectricExtension

internal class ProjectConfig : AbstractProjectConfig() {

    override fun extensions() = super.extensions() + RobolectricExtension()
}
