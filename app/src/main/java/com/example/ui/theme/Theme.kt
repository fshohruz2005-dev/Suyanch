package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LightColorScheme = lightColorScheme(
  primary = SuyanchDeepGreen,
  onPrimary = SuyanchLightSurface,
  primaryContainer = SuyanchLightSurfaceVariant,
  onPrimaryContainer = SuyanchLightInk,
  secondary = SuyanchGreen,
  onSecondary = SuyanchLightSurface,
  tertiary = SuyanchGold,
  onTertiary = SuyanchLightInk,
  background = SuyanchLightBackground,
  onBackground = SuyanchLightInk,
  surface = SuyanchLightSurface,
  onSurface = SuyanchLightInk,
  surfaceVariant = SuyanchLightSurfaceVariant,
  onSurfaceVariant = SuyanchLightMuted,
  outline = SuyanchLightLine,
  outlineVariant = SuyanchLightLine,
  error = SuyanchStatusDangerText,
  onError = SuyanchLightSurface
)

private val DarkColorScheme = darkColorScheme(
  primary = SuyanchDarkGold,
  onPrimary = SuyanchDarkBackground,
  primaryContainer = SuyanchDarkSurfaceVariant,
  onPrimaryContainer = SuyanchDarkText,
  secondary = SuyanchGreen,
  onSecondary = SuyanchDarkText,
  tertiary = SuyanchDarkGold,
  onTertiary = SuyanchDarkBackground,
  background = SuyanchDarkBackground,
  onBackground = SuyanchDarkText,
  surface = SuyanchDarkSurface,
  onSurface = SuyanchDarkText,
  surfaceVariant = SuyanchDarkSurfaceVariant,
  onSurfaceVariant = SuyanchDarkMuted,
  outline = SuyanchDarkLine,
  outlineVariant = SuyanchDarkLine,
  error = SuyanchDarkStatusDangerText,
  onError = SuyanchDarkBackground
)

private val LightExtraColors = SuyanchExtraColors(
  deepGreen = SuyanchDeepGreen,
  brandGreen = SuyanchGreen,
  gold = SuyanchGold,
  goldLight = SuyanchGoldLight,
  line = SuyanchLightLine,
  muted = SuyanchLightMuted,
  statusOkText = SuyanchStatusOkText,
  statusOkBg = SuyanchStatusOkBg,
  statusWarnText = SuyanchStatusWarnText,
  statusWarnBg = SuyanchStatusWarnBg,
  statusDangerText = SuyanchStatusDangerText,
  statusDangerBg = SuyanchStatusDangerBg,
  surfaceVariant = SuyanchLightSurfaceVariant
)

private val DarkExtraColors = SuyanchExtraColors(
  deepGreen = SuyanchDeepGreen,
  brandGreen = SuyanchGreen,
  gold = SuyanchDarkGold,
  goldLight = SuyanchGoldLight,
  line = SuyanchDarkLine,
  muted = SuyanchDarkMuted,
  statusOkText = SuyanchDarkStatusOkText,
  statusOkBg = SuyanchDarkStatusOkBg,
  statusWarnText = SuyanchDarkStatusWarnText,
  statusWarnBg = SuyanchDarkStatusWarnBg,
  statusDangerText = SuyanchDarkStatusDangerText,
  statusDangerBg = SuyanchDarkStatusDangerBg,
  surfaceVariant = SuyanchDarkSurfaceVariant
)

val LocalSuyanchColors = staticCompositionLocalOf { LightExtraColors }

object SuyanchThemeExtended {
  val colors: SuyanchExtraColors
    @Composable
    @ReadOnlyComposable
    get() = LocalSuyanchColors.current
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  val extraColors = if (darkTheme) DarkExtraColors else LightExtraColors

  CompositionLocalProvider(LocalSuyanchColors provides extraColors) {
    MaterialTheme(
      colorScheme = colorScheme,
      typography = SuyanchTypography,
      content = content
    )
  }
}
