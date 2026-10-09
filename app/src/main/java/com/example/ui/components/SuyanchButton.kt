package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchLightSurface
import com.example.ui.theme.SuyanchStatusDangerBg
import com.example.ui.theme.SuyanchStatusDangerText

enum class ButtonVariant {
  PRIMARY,       // Deep Green #1D3D34 with high contrast
  GOLD,          // Vibrant Gold #C79A45
  OUTLINED,      // Solid bordered with soft fill
  DANGER,        // High-contrast warning Red
  SOS            // Urgent red warning
}

@Composable
fun SuyanchButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  variant: ButtonVariant = ButtonVariant.PRIMARY,
  enabled: Boolean = true,
  isLoading: Boolean = false,
  leadingIcon: (@Composable () -> Unit)? = null,
  fullWidth: Boolean = true
) {
  val widthModifier = if (fullWidth) modifier.fillMaxWidth() else modifier
  val shape = RoundedCornerShape(16.dp)
  val minHeight = 52.dp

  val content: @Composable () -> Unit = {
    Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (isLoading) {
        val indicatorColor = when (variant) {
          ButtonVariant.PRIMARY, ButtonVariant.SOS, ButtonVariant.DANGER -> Color.White
          ButtonVariant.GOLD -> SuyanchDeepGreen
          ButtonVariant.OUTLINED -> SuyanchDeepGreen
        }
        CircularProgressIndicator(
          modifier = Modifier.size(22.dp),
          color = indicatorColor,
          strokeWidth = 2.5.dp
        )
        Spacer(modifier = Modifier.width(10.dp))
      } else if (leadingIcon != null) {
        leadingIcon()
        Spacer(modifier = Modifier.width(10.dp))
      }

      val textColor = when (variant) {
        ButtonVariant.PRIMARY -> Color.White
        ButtonVariant.GOLD -> Color(0xFF0F2620)
        ButtonVariant.OUTLINED -> SuyanchDeepGreen
        ButtonVariant.DANGER -> Color.White
        ButtonVariant.SOS -> Color.White
      }

      Text(
        text = text,
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        letterSpacing = 0.3.sp,
        color = if (enabled) textColor else textColor.copy(alpha = 0.5f)
      )
    }
  }

  when (variant) {
    ButtonVariant.PRIMARY -> {
      Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = widthModifier
          .heightIn(min = minHeight)
          .testTag("suyanch_button_primary"),
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(
          defaultElevation = 4.dp,
          pressedElevation = 1.dp,
          focusedElevation = 6.dp
        ),
        colors = ButtonDefaults.buttonColors(
          containerColor = SuyanchDeepGreen,
          contentColor = Color.White,
          disabledContainerColor = SuyanchDeepGreen.copy(alpha = 0.45f),
          disabledContentColor = Color.White.copy(alpha = 0.7f)
        ),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp)
      ) {
        content()
      }
    }

    ButtonVariant.GOLD -> {
      Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = widthModifier
          .heightIn(min = minHeight)
          .testTag("suyanch_button_gold"),
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(
          defaultElevation = 5.dp,
          pressedElevation = 1.dp,
          focusedElevation = 7.dp
        ),
        border = BorderStroke(1.5.dp, Color(0xFFE8BD65)),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFFD4A346),
          contentColor = Color(0xFF0F2620),
          disabledContainerColor = Color(0xFFD4A346).copy(alpha = 0.45f),
          disabledContentColor = Color(0xFF0F2620).copy(alpha = 0.6f)
        ),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp)
      ) {
        content()
      }
    }

    ButtonVariant.OUTLINED -> {
      OutlinedButton(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = widthModifier
          .heightIn(min = minHeight)
          .testTag("suyanch_button_outlined"),
        shape = shape,
        border = BorderStroke(2.dp, if (enabled) SuyanchDeepGreen else Color(0xFFADC0B8)),
        colors = ButtonDefaults.outlinedButtonColors(
          containerColor = Color(0xFFF3F7F5),
          contentColor = SuyanchDeepGreen
        ),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp)
      ) {
        content()
      }
    }

    ButtonVariant.DANGER -> {
      Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = widthModifier
          .heightIn(min = minHeight)
          .testTag("suyanch_button_danger"),
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(
          defaultElevation = 4.dp,
          pressedElevation = 1.dp
        ),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFFC53030),
          contentColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp)
      ) {
        content()
      }
    }

    ButtonVariant.SOS -> {
      Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = widthModifier
          .heightIn(min = minHeight)
          .testTag("suyanch_button_sos"),
        shape = shape,
        elevation = ButtonDefaults.buttonElevation(
          defaultElevation = 6.dp,
          pressedElevation = 2.dp
        ),
        border = BorderStroke(2.dp, Color(0xFFFF8B8B)),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFFDC2626),
          contentColor = Color.White
        ),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp)
      ) {
        content()
      }
    }
  }
}
