package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.core.i18n.appString
import com.example.model.VisitStatus
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchStatusDangerBg
import com.example.ui.theme.SuyanchStatusDangerText
import com.example.ui.theme.SuyanchStatusOkBg
import com.example.ui.theme.SuyanchStatusOkText
import com.example.ui.theme.SuyanchStatusWarnBg
import com.example.ui.theme.SuyanchStatusWarnText

enum class StatusTone {
  OK,
  WARNING,
  DANGER,
  NEUTRAL
}

@Composable
fun StatusPill(
  text: String,
  tone: StatusTone,
  modifier: Modifier = Modifier,
  showDot: Boolean = true
) {
  val (bgColor, textColor, borderColor) = when (tone) {
    StatusTone.OK -> Triple(SuyanchStatusOkBg, SuyanchStatusOkText, SuyanchStatusOkText.copy(alpha = 0.35f))
    StatusTone.WARNING -> Triple(SuyanchStatusWarnBg, SuyanchStatusWarnText, SuyanchStatusWarnText.copy(alpha = 0.35f))
    StatusTone.DANGER -> Triple(SuyanchStatusDangerBg, SuyanchStatusDangerText, SuyanchStatusDangerText.copy(alpha = 0.35f))
    StatusTone.NEUTRAL -> Triple(Color(0xFFECEFEA), Color(0xFF4B5D57), Color(0xFFD2D8CF))
  }

  Box(
    modifier = modifier
      .testTag("status_pill_${tone.name.lowercase()}")
      .clip(RoundedCornerShape(16.dp))
      .background(bgColor)
      .border(1.dp, borderColor, RoundedCornerShape(16.dp))
      .padding(horizontal = 10.dp, vertical = 4.dp),
    contentAlignment = Alignment.Center
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      if (showDot) {
        Box(
          modifier = Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(textColor)
        )
        Spacer(modifier = Modifier.width(6.dp))
      }
      Text(
        text = text,
        color = textColor,
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp
      )
    }
  }
}

@Composable
fun VisitStatusPill(
  status: VisitStatus,
  modifier: Modifier = Modifier
) {
  val tone = when (status) {
    VisitStatus.BOOKED -> StatusTone.NEUTRAL
    VisitStatus.CHECKED_IN -> StatusTone.WARNING
    VisitStatus.CHECKED_OUT -> StatusTone.WARNING
    VisitStatus.CONFIRMED -> StatusTone.OK
    VisitStatus.DISPUTED -> StatusTone.DANGER
  }
  StatusPill(
    text = appString(status.titleKey),
    tone = tone,
    modifier = modifier
  )
}
