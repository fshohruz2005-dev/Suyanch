package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchLightSurface

@Composable
fun SuyanchCard(
  modifier: Modifier = Modifier,
  onClick: (() -> Unit)? = null,
  borderColor: Color = SuyanchLightLine,
  containerColor: Color = SuyanchLightSurface,
  cornerRadius: Dp = 16.dp,
  elevation: Dp = 1.dp,
  testTag: String = "suyanch_card",
  content: @Composable BoxScope.() -> Unit
) {
  val shape = RoundedCornerShape(cornerRadius)

  Card(
    modifier = modifier
      .testTag(testTag)
      .then(
        if (onClick != null) {
          Modifier.clip(shape).clickable(onClick = onClick)
        } else {
          Modifier
        }
      ),
    shape = shape,
    colors = CardDefaults.cardColors(
      containerColor = containerColor
    ),
    border = BorderStroke(1.dp, borderColor),
    elevation = CardDefaults.cardElevation(defaultElevation = elevation)
  ) {
    Box(
      modifier = Modifier.padding(16.dp),
      content = content
    )
  }
}
