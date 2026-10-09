package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import java.util.Locale

/**
 * 5-Star Rating Bar rendering filled, half-filled, and outlined stars.
 */
@Composable
fun StarRatingBar(
  rating: Float,
  modifier: Modifier = Modifier,
  maxStars: Int = 5,
  starSize: Dp = 14.dp,
  starSpacing: Dp = 2.dp,
  filledColor: Color = SuyanchGold,
  emptyColor: Color = Color(0xFFD4DDD6)
) {
  Row(
    modifier = modifier.testTag("star_rating_bar"),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(starSpacing)
  ) {
    for (i in 1..maxStars) {
      val diff = rating - (i - 1)
      val (icon, tint) = when {
        diff >= 0.75f -> Pair(Icons.Default.Star, filledColor)
        diff >= 0.25f -> Pair(Icons.AutoMirrored.Filled.StarHalf, filledColor)
        else -> Pair(Icons.Outlined.StarOutline, emptyColor)
      }

      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(starSize)
      )
    }
  }
}

/**
 * Complete Service Provider Star Rating Badge component for service cards.
 * Displays:
 * - Visual 5-star rating bar (filled, half, outline)
 * - Exact numerical score (e.g., 4.95)
 * - Total reviews count (e.g., "(48 baho)" or "(48)")
 * - Optional confidence label (e.g., "A'lo daraja", "Ishonchli usta")
 * - Crisp background pill for enhanced contrast and visual appeal.
 */
@Composable
fun StarRatingBadge(
  rating: Float,
  reviewsCount: Int,
  modifier: Modifier = Modifier,
  starSize: Dp = 14.dp,
  fontSize: TextUnit = 13.sp,
  showStars: Boolean = true,
  showReviewCount: Boolean = true,
  showConfidenceLabel: Boolean = false,
  compact: Boolean = false,
  tintColor: Color = SuyanchDeepGreen
) {
  val formattedRating = String.format(Locale.US, "%.1f", rating)

  // Confidence text based on rating threshold
  val confidenceLabel = when {
    rating >= 4.9f -> "A'lo"
    rating >= 4.7f -> "Ishonchli"
    rating >= 4.5f -> "Yaxshi"
    else -> null
  }

  if (compact) {
    // Compact pill style (ideal for dense grid cards)
    Row(
      modifier = modifier
        .clip(RoundedCornerShape(8.dp))
        .background(Color(0xFFFFF9EC))
        .padding(horizontal = 7.dp, vertical = 3.dp)
        .testTag("star_rating_badge_compact"),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.Default.Star,
        contentDescription = null,
        tint = SuyanchGold,
        modifier = Modifier.size(starSize)
      )
      Spacer(modifier = Modifier.width(3.dp))
      Text(
        text = formattedRating,
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = fontSize,
        color = Color(0xFF6B4700)
      )
      if (showReviewCount) {
        Spacer(modifier = Modifier.width(3.dp))
        Text(
          text = "($reviewsCount)",
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = (fontSize.value - 2).sp,
          color = Color(0xFF8C7352)
        )
      }
    }
  } else {
    // Rich rating system with 5 stars and optional confidence pill
    Row(
      modifier = modifier.testTag("star_rating_badge"),
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (showStars) {
        StarRatingBar(
          rating = rating,
          starSize = starSize,
          starSpacing = 2.dp
        )
        Spacer(modifier = Modifier.width(5.dp))
      }

      Text(
        text = formattedRating,
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = fontSize,
        color = tintColor
      )

      if (showReviewCount) {
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "($reviewsCount baho)",
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = (fontSize.value - 1.5).sp,
          color = Color(0xFF5A6E65)
        )
      }

      if (showConfidenceLabel && confidenceLabel != null) {
        Spacer(modifier = Modifier.width(6.dp))
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFFE8F3EE))
            .padding(horizontal = 6.dp, vertical = 1.dp)
        ) {
          Text(
            text = confidenceLabel,
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = SuyanchDeepGreen
          )
        }
      }
    }
  }
}
