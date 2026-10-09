package com.example.core.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppLanguage(val code: String, val displayName: String, val flagText: String) {
  UZBEK("uz", "O'zbekcha", "UZ"),
  RUSSIAN("ru", "Русский", "RU");

  companion object {
    val DEFAULT = UZBEK
  }
}

object LocaleManager {
  private val _currentLanguage = MutableStateFlow(AppLanguage.DEFAULT)
  val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

  fun setLanguage(lang: AppLanguage) {
    _currentLanguage.value = lang
  }

  fun toggle() {
    _currentLanguage.value = if (_currentLanguage.value == AppLanguage.UZBEK) AppLanguage.RUSSIAN else AppLanguage.UZBEK
  }
}

val LocalAppLanguage = staticCompositionLocalOf { AppLanguage.DEFAULT }

@Composable
fun ProvideLanguage(
  language: AppLanguage = AppLanguage.DEFAULT,
  content: @Composable () -> Unit
) {
  CompositionLocalProvider(LocalAppLanguage provides language) {
    content()
  }
}

@Composable
@ReadOnlyComposable
fun currentLanguage(): AppLanguage = LocalAppLanguage.current
