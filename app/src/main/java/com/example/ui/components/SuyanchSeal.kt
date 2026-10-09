package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.model.Tier
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchGreen

/**
 * Suyanch Brand Seal:
 * Two concentric circles (outer solid, inner dashed) with:
 * - A key in the center for Gold ("Kalit") tier
 * - A checkmark in the center for Silver / Basic tiers
 */
@Composable
fun SuyanchSeal(
  tier: Tier,
  modifier: Modifier = Modifier,
  size: Dp = 48.dp,
  primaryColor: Color = if (tier == Tier.GOLD) SuyanchGold else SuyanchDeepGreen,
  ringColor: Color = if (tier == Tier.GOLD) SuyanchGold else SuyanchGreen
) {
  val desc = if (tier == Tier.GOLD) "Kalit kafolat muhri" else "Suyanch tasdiqlangan mutaxassis muhri"

  Box(
    modifier = modifier
      .size(size)
      .semantics { contentDescription = desc },
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.size(size)) {
      val canvasWidth = this.size.width
      val canvasHeight = this.size.height
      val center = Offset(canvasWidth / 2f, canvasHeight / 2f)
      val radius = (canvasWidth.coerceAtMost(canvasHeight) / 2f) - 2.dp.toPx()

      // 1. Outer Solid Circle
      drawCircle(
        color = ringColor,
        radius = radius,
        center = center,
        style = Stroke(width = 2.dp.toPx())
      )

      // 2. Inner Dashed Circle
      val innerRadius = radius * 0.78f
      val dashInterval = floatArrayOf(6.dp.toPx(), 4.dp.toPx())
      drawCircle(
        color = ringColor.copy(alpha = 0.85f),
        radius = innerRadius,
        center = center,
        style = Stroke(
          width = 1.2.dp.toPx(),
          pathEffect = PathEffect.dashPathEffect(dashInterval, 0f)
        )
      )

      // 3. Center Icon
      if (tier == Tier.GOLD) {
        // Draw Key Symbol
        val keyScale = radius * 0.45f
        val keyStroke = 2.dp.toPx()

        // Key head (round ring)
        val headCenter = Offset(center.x - keyScale * 0.4f, center.y - keyScale * 0.4f)
        val headRadius = keyScale * 0.45f
        drawCircle(
          color = primaryColor,
          radius = headRadius,
          center = headCenter,
          style = Stroke(width = keyStroke)
        )

        // Key shaft
        val shaftStart = Offset(headCenter.x + headRadius * 0.7f, headCenter.y + headRadius * 0.7f)
        val shaftEnd = Offset(center.x + keyScale * 0.75f, center.y + keyScale * 0.75f)
        drawLine(
          color = primaryColor,
          start = shaftStart,
          end = shaftEnd,
          strokeWidth = keyStroke,
          cap = StrokeCap.Round
        )

        // Key teeth (2 bits)
        val tooth1Start = Offset(shaftEnd.x - keyScale * 0.25f, shaftEnd.y - keyScale * 0.25f)
        val tooth1End = Offset(tooth1Start.x + keyScale * 0.22f, tooth1Start.y - keyScale * 0.22f)
        drawLine(
          color = primaryColor,
          start = tooth1Start,
          end = tooth1End,
          strokeWidth = keyStroke,
          cap = StrokeCap.Round
        )

        val tooth2End = Offset(shaftEnd.x + keyScale * 0.22f, shaftEnd.y - keyScale * 0.22f)
        drawLine(
          color = primaryColor,
          start = shaftEnd,
          end = tooth2End,
          strokeWidth = keyStroke,
          cap = StrokeCap.Round
        )
      } else {
        // Draw Checkmark
        val checkScale = radius * 0.4f
        val checkPath = Path().apply {
          moveTo(center.x - checkScale * 0.6f, center.y)
          lineTo(center.x - checkScale * 0.1f, center.y + checkScale * 0.5f)
          lineTo(center.x + checkScale * 0.7f, center.y - checkScale * 0.5f)
        }
        drawPath(
          path = checkPath,
          color = primaryColor,
          style = Stroke(
            width = 2.2.dp.toPx(),
            cap = StrokeCap.Round
          )
        )
      }
    }
  }
}
