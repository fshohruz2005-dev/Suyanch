package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.i18n.appString
import com.example.model.Tier
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchGoldDark
import com.example.ui.theme.SuyanchGoldLight
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchStatusWarnBg

@Composable
fun TierBadge(
  tier: Tier,
  modifier: Modifier = Modifier,
  showFullLabel: Boolean = false,
  sealSize: Dp = 20.dp
) {
  val title = appString(tier.nameKey)

  when (tier) {
    Tier.GOLD -> {
      Box(
        modifier = modifier
          .testTag("tier_badge_gold")
          .clip(RoundedCornerShape(20.dp))
          .background(SuyanchStatusWarnBg.copy(alpha = 0.75f))
          .border(1.dp, SuyanchGold.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
          .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          SuyanchSeal(
            tier = Tier.GOLD,
            size = sealSize,
            primaryColor = SuyanchGoldDark,
            ringColor = SuyanchGold
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (showFullLabel) title else "Kalit",
            color = SuyanchDeepGreen,
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            letterSpacing = 0.3.sp
          )
        }
      }
    }

    Tier.SILVER -> {
      Box(
        modifier = modifier
          .testTag("tier_badge_silver")
          .clip(RoundedCornerShape(20.dp))
          .background(Color(0xFFE8EEEC))
          .border(1.dp, Color(0xFFBDC9C5), RoundedCornerShape(20.dp))
          .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          SuyanchSeal(
            tier = Tier.SILVER,
            size = sealSize,
            primaryColor = Color(0xFF4A6860),
            ringColor = Color(0xFF738E86)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = title,
            color = Color(0xFF28443D),
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
          )
        }
      }
    }

    Tier.BASIC -> {
      Box(
        modifier = modifier
          .testTag("tier_badge_basic")
          .clip(RoundedCornerShape(20.dp))
          .background(Color(0xFFF1F3EF))
          .border(1.dp, SuyanchLightLine, RoundedCornerShape(20.dp))
          .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          SuyanchSeal(
            tier = Tier.BASIC,
            size = sealSize,
            primaryColor = Color(0xFF6B7B75),
            ringColor = Color(0xFFA5B2AC)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = title,
            color = Color(0xFF4B5D57),
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
          )
        }
      }
    }
  }
}
