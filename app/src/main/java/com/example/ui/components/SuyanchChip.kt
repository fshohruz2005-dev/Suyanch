package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ui.theme.SuyanchStatusWarnBg

@Composable
fun SuyanchChip(
  label: String,
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  isGoldHighlighted: Boolean = false,
  leadingIcon: (@Composable () -> Unit)? = null,
  badgeText: String? = null
) {
  val shape = RoundedCornerShape(20.dp)

  val (bgColor, textColor, borderColor) = when {
    selected && isGoldHighlighted -> Triple(SuyanchStatusWarnBg, SuyanchDeepGreen, SuyanchGold)
    selected -> Triple(SuyanchDeepGreen, SuyanchLightSurface, SuyanchDeepGreen)
    isGoldHighlighted -> Triple(SuyanchStatusWarnBg.copy(alpha = 0.5f), SuyanchDeepGreen, SuyanchGold.copy(alpha = 0.6f))
    else -> Triple(SuyanchLightSurface, SuyanchDeepGreen, SuyanchLightLine)
  }

  Surface(
    modifier = modifier
      .clip(shape)
      .clickable(onClick = onClick)
      .testTag("suyanch_chip_${label.lowercase()}"),
    shape = shape,
    color = bgColor,
    border = BorderStroke(1.dp, borderColor)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (leadingIcon != null) {
        leadingIcon()
        Spacer(modifier = Modifier.width(6.dp))
      }
      Text(
        text = label,
        color = textColor,
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        lineHeight = 18.sp
      )
      if (badgeText != null) {
        Spacer(modifier = Modifier.width(6.dp))
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) SuyanchGold else SuyanchLightLine.copy(alpha = 0.5f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text(
            text = badgeText,
            color = if (selected) SuyanchDeepGreen else Color(0xFF4B5D57),
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          )
        }
      }
    }
  }
}
