package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.model.TimelineStepInfo
import com.example.model.TimelineStepState
import com.example.ui.theme.IbmPlexMonoFontFamily
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchLightSurface
import com.example.ui.theme.SuyanchStatusOkBg
import com.example.ui.theme.SuyanchStatusOkText
import com.example.ui.theme.SuyanchStatusWarnBg

@Composable
fun TimelineView(
  steps: List<TimelineStepInfo>,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .testTag("timeline_view")
  ) {
    steps.forEachIndexed { index, step ->
      TimelineStepRow(
        step = step,
        isLast = index == steps.size - 1
      )
    }
  }
}

@Composable
fun TimelineStepRow(
  step: TimelineStepInfo,
  isLast: Boolean,
  modifier: Modifier = Modifier
) {
  val stepIcon: ImageVector = when (step.stepNumber) {
    1 -> Icons.Default.Lock
    2 -> Icons.Default.Schedule
    3 -> Icons.Default.PhotoCamera
    4 -> Icons.Default.PhotoCamera
    5 -> Icons.Default.Verified
    else -> Icons.Default.Check
  }

  val circleBgColor by animateColorAsState(
    targetValue = when (step.state) {
      TimelineStepState.COMPLETED -> SuyanchStatusOkBg
      TimelineStepState.IN_PROGRESS -> SuyanchStatusWarnBg
      TimelineStepState.PENDING -> Color(0xFFF1F3EF)
    },
    label = "circle_bg"
  )

  val circleIconColor by animateColorAsState(
    targetValue = when (step.state) {
      TimelineStepState.COMPLETED -> SuyanchStatusOkText
      TimelineStepState.IN_PROGRESS -> SuyanchDeepGreen
      TimelineStepState.PENDING -> Color(0xFF9EABA5)
    },
    label = "circle_icon"
  )

  val borderColor = when (step.state) {
    TimelineStepState.COMPLETED -> SuyanchStatusOkText
    TimelineStepState.IN_PROGRESS -> SuyanchGold
    TimelineStepState.PENDING -> SuyanchLightLine
  }

  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.Top
  ) {
    // Left column: Icon + Connector line
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.width(36.dp)
    ) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(circleBgColor)
          .border(1.5.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        if (step.state == TimelineStepState.COMPLETED) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Bajarildi",
            tint = SuyanchStatusOkText,
            modifier = Modifier.size(18.dp)
          )
        } else {
          Icon(
            imageVector = stepIcon,
            contentDescription = null,
            tint = circleIconColor,
            modifier = Modifier.size(16.dp)
          )
        }
      }

      if (!isLast) {
        val lineColor = if (step.state == TimelineStepState.COMPLETED) SuyanchStatusOkText else SuyanchLightLine
        Box(
          modifier = Modifier
            .width(2.dp)
            .height(36.dp)
            .background(lineColor)
        )
      }
    }

    Spacer(modifier = Modifier.width(12.dp))

    // Right column: Content
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = if (isLast) 0.dp else 16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = appString(step.titleKey),
          color = if (step.state == TimelineStepState.PENDING) Color(0xFF72847E) else SuyanchDeepGreen,
          fontFamily = ManropeFontFamily,
          fontWeight = if (step.state == TimelineStepState.IN_PROGRESS) FontWeight.Bold else FontWeight.SemiBold,
          fontSize = 14.sp
        )
        if (step.timestamp != null) {
          Spacer(modifier = Modifier.weight(1f))
          Text(
            text = step.timestamp,
            color = Color(0xFF1D332B),
            fontFamily = IbmPlexMonoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
          )
        }
      }

      if (step.subtitle.isNotBlank()) {
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = step.subtitle,
          color = if (step.state == TimelineStepState.PENDING) Color(0xFF4C6159) else Color(0xFF1D332B),
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 12.sp,
          lineHeight = 16.sp
        )
      }
    }
  }
}
