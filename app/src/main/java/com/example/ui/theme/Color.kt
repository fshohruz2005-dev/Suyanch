package com.example.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Suyanch Brand Colors (Web Prototype Matching Tokens)
val SuyanchDeepGreen = Color(0xFF1D3D34)      // Primary buttons, main headers, seal border
val SuyanchGreen = Color(0xFF2F5D50)          // Secondary deep green
val SuyanchGold = Color(0xFFB58014)           // Rich high-contrast Gold (clearly visible on white)
val SuyanchGoldLight = Color(0xFFF0D8A8)      // Light gold tint
val SuyanchGoldDark = Color(0xFF8C620A)       // Deep gold outline

// Light Mode Tokens
val SuyanchLightBackground = Color(0xFFF2F4EF)
val SuyanchLightSurface = Color(0xFFFFFFFF)
val SuyanchLightSurfaceVariant = Color(0xFFE8EDE5)
val SuyanchLightInk = Color(0xFF142923)
val SuyanchLightMuted = Color(0xFF2C433C)
val SuyanchLightLine = Color(0xFFCCD4C9)

// Status Tokens - Light
val SuyanchStatusOkText = Color(0xFF1E6B4A)
val SuyanchStatusOkBg = Color(0xFFD9EAE1)
val SuyanchStatusWarnText = Color(0xFF8A6B1F)
val SuyanchStatusWarnBg = Color(0xFFF3E7CD)
val SuyanchStatusDangerText = Color(0xFFB5533D)
val SuyanchStatusDangerBg = Color(0xFFF1DCD6)

// Dark Mode Tokens
val SuyanchDarkBackground = Color(0xFF0E1714)
val SuyanchDarkSurface = Color(0xFF16231F)
val SuyanchDarkSurfaceVariant = Color(0xFF1C2C27)
val SuyanchDarkText = Color(0xFFE4ECE7)
val SuyanchDarkMuted = Color(0xFF8B9E96)
val SuyanchDarkLine = Color(0xFF263B34)
val SuyanchDarkGold = Color(0xFFD2A658)

// Status Tokens - Dark
val SuyanchDarkStatusOkText = Color(0xFF4ADE80)
val SuyanchDarkStatusOkBg = Color(0xFF143525)
val SuyanchDarkStatusWarnText = Color(0xFFFACC15)
val SuyanchDarkStatusWarnBg = Color(0xFF382F13)
val SuyanchDarkStatusDangerText = Color(0xFFF87171)
val SuyanchDarkStatusDangerBg = Color(0xFF3B1B15)

@Immutable
data class SuyanchExtraColors(
  val deepGreen: Color,
  val brandGreen: Color,
  val gold: Color,
  val goldLight: Color,
  val line: Color,
  val muted: Color,
  val statusOkText: Color,
  val statusOkBg: Color,
  val statusWarnText: Color,
  val statusWarnBg: Color,
  val statusDangerText: Color,
  val statusDangerBg: Color,
  val surfaceVariant: Color
)
