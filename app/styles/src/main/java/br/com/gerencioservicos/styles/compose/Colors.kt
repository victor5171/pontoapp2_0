package br.com.gerencioservicos.styles.compose

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

private val PRIMARY = Color(0xff0d2a47)
private val PRIMARY_LIGHT = Color(0xff3b5273)
private val PRIMARY_DARK = Color(0xff000020)
private val SECONDARY = Color(0xff00ffb0)
private val SECONDARY_LIGHT = Color(0xff6cffe2)
private val SECONDARY_DARK = Color(0xff00ca80)
private val PRIMARY_TEXT_COLOR = Color(0xffffffff)
private val SECONDARY_TEXT_COLOR = Color(0xff000000)
private val SURFACE = Color(0xffffffff)
private val BACKGROUND = Color(0xfff5f5f6)

object Colors {
    val lightColors = lightColors(
        primary = PRIMARY,
        primaryVariant = PRIMARY_LIGHT,
        secondary = SECONDARY,
        secondaryVariant = SECONDARY_LIGHT,
        surface = SURFACE,
        background = BACKGROUND,
        onPrimary = PRIMARY_TEXT_COLOR,
        onSecondary = SECONDARY_TEXT_COLOR
    )

    val darkColors = darkColors(
        primary = PRIMARY,
        primaryVariant = PRIMARY_DARK,
        secondary = SECONDARY,
        secondaryVariant = SECONDARY_DARK,
        surface = SURFACE,
        background = BACKGROUND,
        onPrimary = PRIMARY_TEXT_COLOR,
        onSecondary = SECONDARY_TEXT_COLOR
    )

    internal val dividerColor = Color(0xff999999)

    val transparentWhite = Color(0xBFFFFFFF)
}
