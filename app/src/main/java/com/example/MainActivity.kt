package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.core.i18n.LocaleManager
import com.example.core.i18n.ProvideLanguage
import com.example.repository.SuyanchRepository
import com.example.ui.navigation.SuyanchApp
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      val isDarkTheme = isSystemInDarkTheme()
      val currentLanguage by LocaleManager.currentLanguage.collectAsState()
      val repository = remember { SuyanchRepository() }

      ProvideLanguage(language = currentLanguage) {
        MyApplicationTheme(darkTheme = isDarkTheme) {
          SuyanchApp(repository = repository)
        }
      }
    }
  }
}
