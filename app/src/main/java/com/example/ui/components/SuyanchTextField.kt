package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IbmPlexMonoFontFamily
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchLightSurface
import com.example.ui.theme.SuyanchStatusWarnBg

@Composable
fun SuyanchTextField(
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  label: String? = null,
  placeholder: String? = null,
  leadingIcon: (@Composable () -> Unit)? = null,
  trailingIcon: (@Composable () -> Unit)? = null,
  isError: Boolean = false,
  errorMessage: String? = null,
  visualTransformation: VisualTransformation = VisualTransformation.None,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  singleLine: Boolean = true,
  testTag: String = "suyanch_text_field"
) {
  Column(modifier = modifier.fillMaxWidth()) {
    OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      modifier = Modifier
        .fillMaxWidth()
        .testTag(testTag),
      label = label?.let { { Text(it, fontFamily = ManropeFontFamily) } },
      placeholder = placeholder?.let { { Text(it, fontFamily = ManropeFontFamily, color = Color(0xFF7A8D86)) } },
      leadingIcon = leadingIcon,
      trailingIcon = trailingIcon,
      isError = isError,
      visualTransformation = visualTransformation,
      keyboardOptions = keyboardOptions,
      keyboardActions = keyboardActions,
      singleLine = singleLine,
      shape = RoundedCornerShape(12.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = SuyanchDeepGreen,
        unfocusedBorderColor = SuyanchLightLine,
        focusedContainerColor = SuyanchLightSurface,
        unfocusedContainerColor = SuyanchLightSurface,
        cursorColor = SuyanchDeepGreen
      )
    )

    if (isError && errorMessage != null) {
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = errorMessage,
        color = Color(0xFFB5533D),
        fontFamily = ManropeFontFamily,
        fontSize = 12.sp,
        modifier = Modifier.padding(start = 8.dp)
      )
    }
  }
}

/**
 * 4-digit PIN Code display / verification component
 * Uses IBM Plex Mono for clean cryptographic styling
 */
@Composable
fun PinCodeDisplay(
  pin: String,
  modifier: Modifier = Modifier,
  isHighlighted: Boolean = true
) {
  val padded = pin.padEnd(4, '—')

  Row(
    modifier = modifier
      .testTag("pin_code_display")
      .padding(vertical = 8.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    for (i in 0 until 4) {
      val char = padded.getOrElse(i) { '—' }
      val isFilled = char != '—'

      Box(
        modifier = Modifier
          .size(52.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(if (isHighlighted) SuyanchStatusWarnBg.copy(alpha = 0.5f) else SuyanchLightSurface)
          .border(
            width = 1.5.dp,
            color = if (isFilled) SuyanchGold else SuyanchLightLine,
            shape = RoundedCornerShape(12.dp)
          ),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = char.toString(),
          fontFamily = IbmPlexMonoFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 24.sp,
          color = if (isFilled) SuyanchDeepGreen else Color(0xFFA1AEA8)
        )
      }
    }
  }
}
