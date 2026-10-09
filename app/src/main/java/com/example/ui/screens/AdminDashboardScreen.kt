package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.VisitStatus
import com.example.repository.SuyanchRepository
import com.example.repository.UserRole
import com.example.repository.VerificationQueueEntry
import com.example.ui.components.ButtonVariant
import com.example.ui.components.SuyanchButton
import com.example.ui.components.SuyanchCard
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchStatusDangerBg
import com.example.ui.theme.SuyanchStatusDangerText
import com.example.ui.theme.SuyanchStatusOkText
import com.example.ui.theme.SuyanchStatusWarnBg

@Composable
fun AdminDashboardScreen(
  repository: SuyanchRepository,
  modifier: Modifier = Modifier
) {
  val sosAlert by repository.sosAlertMessage.collectAsState()
  val queue by repository.verificationQueue.collectAsState()
  val bookings by repository.bookings.collectAsState()
  val disputedBookings = remember(bookings) {
    bookings.filter { it.status == VisitStatus.DISPUTED }
  }

  var callModalText by remember { mutableStateOf<String?>(null) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF6F8F5))
      .verticalScroll(rememberScrollState())
      .padding(bottom = 80.dp)
  ) {
    // Header Banner
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(SuyanchDeepGreen)
        .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Suyanch Operator Paneli",
            fontFamily = FrauncesFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White
          )
          Spacer(modifier = Modifier.height(3.dp))
          Text(
            text = "Navbatchi dispetcher xizmati",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color(0xFFC7D7D2)
          )
        }

        // Quick Switcher back to Client/Worker
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF274D42))
            .border(1.dp, SuyanchGold, RoundedCornerShape(12.dp))
            .clickable { repository.switchRole(UserRole.CLIENT) }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = SuyanchGold, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Mijozga qaytish", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
          }
        }
      }
    }

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
      // Realtime SOS Feed Alert
    if (sosAlert != null) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(Color(0xFFFFECEB))
          .border(1.5.dp, Color(0xFFC02626), RoundedCornerShape(14.dp))
          .padding(14.dp)
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFC02626), modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("FAVQULODDA SOS XABARI", fontFamily = FrauncesFontFamily, fontWeight = FontWeight.Bold, color = Color(0xFFC02626), fontSize = 15.sp)
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(sosAlert ?: "", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF4A1E1A))
          Spacer(modifier = Modifier.height(10.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SuyanchButton(
              text = "Mijozga qo'ng'iroq",
              onClick = { callModalText = "Mijoz bilan aloqa o'rnatilmoqda (+998 90 123 45 67)... Dispetcher liniyasi faol." },
              variant = ButtonVariant.DANGER,
              modifier = Modifier.weight(1f)
            )
            SuyanchButton(
              text = "Ko'rib chiqildi",
              onClick = { repository.dismissSos() },
              variant = ButtonVariant.OUTLINED,
              modifier = Modifier.weight(0.7f)
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(14.dp))
    }

    // Verification Queue Card
    SuyanchCard(containerColor = Color.White) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Shield, contentDescription = null, tint = SuyanchDeepGreen, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Tekshiruv navbati (Verifications Queue)", fontFamily = FrauncesFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SuyanchDeepGreen)
        }
        Spacer(modifier = Modifier.height(10.dp))

        queue.forEachIndexed { index, item ->
          VerificationQueueItemView(
            entry = item,
            onApprove = { repository.approveVerificationQueueEntry(item.id, true) },
            onReject = { repository.approveVerificationQueueEntry(item.id, false) }
          )
          if (index < queue.size - 1) {
            HorizontalDivider(color = SuyanchLightLine, modifier = Modifier.padding(vertical = 8.dp))
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Active Disputes Card
    SuyanchCard(containerColor = Color.White) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.ReportProblem, contentDescription = null, tint = SuyanchDeepGreen, modifier = Modifier.size(20.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Nizolar va to'lov muzlatishlari", fontFamily = FrauncesFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SuyanchDeepGreen)
        }
        Spacer(modifier = Modifier.height(10.dp))

        if (disputedBookings.isNotEmpty()) {
          disputedBookings.forEach { booking ->
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(SuyanchStatusWarnBg.copy(alpha = 0.5f))
                .border(1.dp, SuyanchLightLine, RoundedCornerShape(10.dp))
                .padding(10.dp)
            ) {
              Column {
                Text(
                  text = "Buyurtma #${booking.id} (${booking.workerName})",
                  fontFamily = ManropeFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = SuyanchDeepGreen
                )
                Text(
                  text = "Mijoz da'vosi: '${booking.disputeReason ?: "Ish chala bajarildi"}'",
                  fontFamily = ManropeFontFamily,
                  fontWeight = FontWeight.Medium,
                  fontSize = 12.sp,
                  color = Color(0xFF263C35)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                  SuyanchButton(
                    text = "Mijozga qaytarish (Refund)",
                    onClick = { repository.resolveDispute(booking.id, refundToClient = true) },
                    variant = ButtonVariant.PRIMARY,
                    modifier = Modifier.weight(1f)
                  )
                  SuyanchButton(
                    text = "Ustaga o'tkazish",
                    onClick = { repository.resolveDispute(booking.id, refundToClient = false) },
                    variant = ButtonVariant.OUTLINED,
                    modifier = Modifier.weight(1f)
                  )
                }
              }
            }
          }
        } else {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0xFFEAF1EC))
              .padding(12.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF1E6B4A), modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text("Hozirda barcha nizolar hal qilingan.", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF1E6B4A))
            }
          }
        }
      }
    }
  }
}

  // Call simulation dialog
  if (callModalText != null) {
    AlertDialog(
      onDismissRequest = { callModalText = null },
      title = {
        Text("Aloqa o'rnatildi", fontFamily = FrauncesFontFamily, fontWeight = FontWeight.Bold, color = SuyanchDeepGreen)
      },
      text = {
        Text(callModalText ?: "", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Medium, fontSize = 13.sp, color = Color(0xFF263C35))
      },
      confirmButton = {
        SuyanchButton(
          text = "Yakunlash",
          onClick = { callModalText = null },
          variant = ButtonVariant.PRIMARY,
          fullWidth = false
        )
      }
    )
  }
}

@Composable
fun VerificationQueueItemView(
  entry: VerificationQueueEntry,
  onApprove: () -> Unit,
  onReject: () -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = "${entry.workerName} (${entry.profession})",
        fontFamily = FrauncesFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = SuyanchDeepGreen
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = entry.checkType,
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = Color(0xFF142923)
      )
      Text(
        text = entry.submittedTime,
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        color = Color(0xFF263C35)
      )
    }

    if (entry.status == "PENDING") {
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0xFFE5EDE8))
            .clickable(onClick = onApprove),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Check, contentDescription = "Tasdiqlash", tint = Color(0xFF1E6B4A), modifier = Modifier.size(20.dp))
        }

        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color(0xFFFFECEB))
            .clickable(onClick = onReject),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Close, contentDescription = "Rad etish", tint = Color(0xFFC02626), modifier = Modifier.size(20.dp))
        }
      }
    } else {
      Text(
        text = if (entry.status == "APPROVED") "✓ Tasdiqlandi" else "✕ Rad etildi",
        fontFamily = ManropeFontFamily,
        fontSize = 13.sp,
        color = if (entry.status == "APPROVED") Color(0xFF1E6B4A) else Color(0xFFC02626),
        fontWeight = FontWeight.ExtraBold
      )
    }
  }
}
