package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.i18n.appString
import com.example.model.CategoryId
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchLightSurface

/**
 * Returns the corresponding ImageVector icon for a given CategoryId.
 */
fun categoryIcon(category: CategoryId): ImageVector = when (category) {
  CategoryId.PLUMBER -> Icons.Default.Plumbing
  CategoryId.CLEANING -> Icons.Default.CleaningServices
  CategoryId.ELECTRIC -> Icons.Default.ElectricBolt
  CategoryId.HANDYMAN -> Icons.Default.Handyman
  CategoryId.COOK -> Icons.Default.Restaurant
  CategoryId.CARE -> Icons.Default.MedicalServices
}

/**
 * An individual pill item for category selection with icon, title, optional count badge, and ripple.
 */
@Composable
fun CategoryPill(
  title: String,
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: ImageVector? = null,
  countBadge: Int? = null,
  testTag: String = "category_pill_${title.lowercase()}"
) {
  val shape = RoundedCornerShape(24.dp)

  val animatedBgColor by animateColorAsState(
    targetValue = if (selected) SuyanchDeepGreen else Color(0xFFF3F6F3),
    animationSpec = tween(durationMillis = 200),
    label = "pill_bg"
  )

  val animatedTextColor by animateColorAsState(
    targetValue = if (selected) Color.White else SuyanchDeepGreen,
    animationSpec = tween(durationMillis = 200),
    label = "pill_text"
  )

  val animatedBorderColor by animateColorAsState(
    targetValue = if (selected) SuyanchDeepGreen else Color(0xFFE0E6E0),
    animationSpec = tween(durationMillis = 200),
    label = "pill_border"
  )

  Surface(
    modifier = modifier
      .clip(shape)
      .clickable(onClick = onClick)
      .testTag(testTag),
    shape = shape,
    color = animatedBgColor,
    border = BorderStroke(1.dp, animatedBorderColor),
    shadowElevation = if (selected) 2.dp else 0.dp
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      if (icon != null) {
        val iconBgColor = if (selected) Color(0xFF1E6B4A) else Color(0xFFE2EBE4)
        val iconTint = if (selected) Color.White else SuyanchDeepGreen

        Box(
          modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(iconBgColor),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(14.dp)
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
      }

      Text(
        text = title,
        fontFamily = ManropeFontFamily,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
        fontSize = 13.sp,
        color = animatedTextColor
      )

      if (countBadge != null && countBadge > 0) {
        Spacer(modifier = Modifier.width(6.dp))
        val badgeBg = if (selected) SuyanchGold else Color(0xFFDEE5DF)
        val badgeText = if (selected) SuyanchDeepGreen else Color(0xFF4A5A52)

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(badgeBg)
            .padding(horizontal = 6.dp, vertical = 1.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = countBadge.toString(),
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = badgeText
          )
        }
      }
    }
  }
}

/**
 * A horizontal scrolling category pill selector component to allow users to quickly filter services.
 * Features:
 * - "All" option (Hammasi)
 * - Individual categories with relevant icons (Plumbing, Cleaning, Electrician, Handyman, Cook, Care)
 * - Smooth horizontal scrolling with edge padding
 * - Active state styling with crisp contrast, rounded pill shape, and test tags.
 */
@Composable
fun CategoryPillSelector(
  selectedCategory: CategoryId?,
  onSelectCategory: (CategoryId?) -> Unit,
  modifier: Modifier = Modifier,
  categories: List<CategoryId> = CategoryId.values().toList(),
  showAllOption: Boolean = true,
  allLabel: String = "Hammasi",
  categoryCounts: Map<CategoryId, Int>? = null,
  contentPadding: PaddingValues = PaddingValues(horizontal = 0.dp),
  horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp)
) {
  LazyRow(
    modifier = modifier.testTag("category_pill_selector"),
    contentPadding = contentPadding,
    horizontalArrangement = horizontalArrangement,
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (showAllOption) {
      item(key = "all") {
        CategoryPill(
          title = allLabel,
          selected = selectedCategory == null,
          onClick = { onSelectCategory(null) },
          icon = Icons.Default.Tune,
          testTag = "category_pill_all"
        )
      }
    }

    items(
      items = categories,
      key = { it.id }
    ) { category ->
      val isSelected = selectedCategory == category
      val count = categoryCounts?.get(category)
      CategoryPill(
        title = appString(category.titleKey),
        selected = isSelected,
        onClick = {
          // Clicking active one toggles it back to null or select
          if (isSelected) onSelectCategory(null) else onSelectCategory(category)
        },
        icon = categoryIcon(category),
        countBadge = count,
        testTag = "category_pill_${category.id}"
      )
    }
  }
}
