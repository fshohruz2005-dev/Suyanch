package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.example.core.i18n.StringKey
import com.example.core.i18n.appString
import com.example.model.Booking
import com.example.model.PaymentStatus
import com.example.model.ServiceCatalog
import com.example.model.Tier
import com.example.model.TimelineStepInfo
import com.example.model.TimelineStepState
import com.example.model.VisitStatus
import com.example.model.Worker
import com.example.repository.SuyanchRepository
import com.example.repository.VisitPhoto
import com.example.ui.components.ButtonVariant
import com.example.ui.components.PinCodeDisplay
import com.example.ui.components.StatusPill
import com.example.ui.components.StatusTone
import com.example.ui.components.SuyanchButton
import com.example.ui.components.SuyanchCard
import com.example.ui.components.SuyanchTextField
import com.example.ui.components.TierBadge
import com.example.ui.components.TimelineView
import com.example.ui.components.VisitStatusPill
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.IbmPlexMonoFontFamily
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchLightSurface
import com.example.ui.theme.SuyanchMonoStyles
import com.example.ui.theme.SuyanchStatusDangerBg
import com.example.ui.theme.SuyanchStatusDangerText
import com.example.ui.theme.SuyanchStatusOkBg
import com.example.ui.theme.SuyanchStatusOkText
import com.example.ui.theme.SuyanchStatusWarnBg

@Composable
fun MyVisitsScreen(
  repository: SuyanchRepository,
  onOpenChatWithWorker: ((Worker) -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val bookings by repository.bookings.collectAsState()
  val visitPhotos by repository.visitPhotos.collectAsState()
  val activeBooking = bookings.firstOrNull { it.status != VisitStatus.CONFIRMED && it.status != VisitStatus.DISPUTED } ?: bookings.firstOrNull()

  var showReportDialog by remember { mutableStateOf(false) }
  var disputeReason by remember { mutableStateOf("") }
  var showSosDialog by remember { mutableStateOf(false) }
  var showPhotosGallery by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF6F8F5))
  ) {
    // Header Banner
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(SuyanchDeepGreen)
        .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
      Column {
        Text(
          text = "Tashriflar va buyurtmalar",
          fontFamily = FrauncesFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 22.sp,
          color = Color.White
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
          text = "Xavfsiz tashrif mexanizmi va buyurtmalar monitoringi",
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = Color(0xFFC7D7D2)
        )
      }
    }

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      if (activeBooking != null) {
      val assignedWorker = remember(activeBooking.workerId) {
        repository.getWorkerById(activeBooking.workerId)
      }

      val steps = remember(activeBooking.status, activeBooking.beforePhotosCount, activeBooking.afterPhotosCount) {
        listOf(
          TimelineStepInfo(
            stepNumber = 1,
            titleKey = StringKey.TIMELINE_CODE_ISSUED,
            subtitle = "Xavfsizlik PIN kodi berildi (#${activeBooking.pinCode})",
            timestamp = "09:30",
            state = TimelineStepState.COMPLETED
          ),
          TimelineStepInfo(
            stepNumber = 2,
            titleKey = StringKey.TIMELINE_ARRIVED,
            subtitle = if (activeBooking.checkInTime != null) "Jonli selfie tasdiqlandi" else "Usta kelishi kutilmoqda",
            timestamp = activeBooking.checkInTime,
            state = when (activeBooking.status) {
              VisitStatus.BOOKED -> TimelineStepState.PENDING
              VisitStatus.CHECKED_IN -> TimelineStepState.IN_PROGRESS
              else -> TimelineStepState.COMPLETED
            }
          ),
          TimelineStepInfo(
            stepNumber = 3,
            titleKey = StringKey.TIMELINE_BEFORE_PHOTOS,
            subtitle = if (activeBooking.beforePhotosCount > 0) "${activeBooking.beforePhotosCount} ta surat yuklandi" else "Ish oldidan suratlar",
            timestamp = if (activeBooking.beforePhotosCount > 0) "10:05" else null,
            state = if (activeBooking.beforePhotosCount > 0) TimelineStepState.COMPLETED else TimelineStepState.PENDING
          ),
          TimelineStepInfo(
            stepNumber = 4,
            titleKey = StringKey.TIMELINE_LEFT,
            subtitle = if (activeBooking.status == VisitStatus.CHECKED_OUT || activeBooking.status == VisitStatus.CONFIRMED) "Chiqish kodi tasdiqlandi" else "Chiqish kodi va yakuniy suratlar",
            timestamp = activeBooking.checkOutTime,
            state = when (activeBooking.status) {
              VisitStatus.CHECKED_OUT -> TimelineStepState.IN_PROGRESS
              VisitStatus.CONFIRMED, VisitStatus.DISPUTED -> TimelineStepState.COMPLETED
              else -> TimelineStepState.PENDING
            }
          ),
          TimelineStepInfo(
            stepNumber = 5,
            titleKey = StringKey.TIMELINE_CONFIRMED,
            subtitle = if (activeBooking.status == VisitStatus.CONFIRMED) "To'lov o'tkazildi" else "Mijoz tasdiqlashi",
            timestamp = if (activeBooking.status == VisitStatus.CONFIRMED) "13:50" else null,
            state = if (activeBooking.status == VisitStatus.CONFIRMED) TimelineStepState.COMPLETED else TimelineStepState.PENDING
          )
        )
      }

      SuyanchCard(
        borderColor = if (activeBooking.status == VisitStatus.CHECKED_IN) SuyanchGold else SuyanchLightLine,
        containerColor = Color.White
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "Faol tashrif",
                  fontFamily = FrauncesFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 17.sp,
                  color = SuyanchDeepGreen
                )
                Spacer(modifier = Modifier.width(8.dp))
                VisitStatusPill(status = activeBooking.status)
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "${activeBooking.workerName} • ${activeBooking.date}",
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF142923)
              )
            }

            TierBadge(tier = activeBooking.workerTier, sealSize = 18.dp)
          }

          Spacer(modifier = Modifier.height(10.dp))

          // PIN code info box
          if (activeBooking.status == VisitStatus.BOOKED || activeBooking.status == VisitStatus.CHECKED_IN) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(SuyanchStatusWarnBg)
                .border(1.2.dp, SuyanchGold, RoundedCornerShape(12.dp))
                .padding(12.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = "Tashrif PIN kodi:",
                    fontFamily = ManropeFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.5.sp,
                    color = SuyanchDeepGreen
                  )
                  Text(
                    text = activeBooking.pinCode,
                    style = SuyanchMonoStyles.pinCode,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = SuyanchDeepGreen
                  )
                }
                Text(
                  text = "Ustaga faqat kelganda bering",
                  fontFamily = ManropeFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.5.sp,
                  color = Color(0xFF1B382F),
                  modifier = Modifier.width(130.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(14.dp))
          }

          // Timeline View
          TimelineView(steps = steps)

          Spacer(modifier = Modifier.height(12.dp))

          // Visit Photos Preview Button / Toggle
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(Color(0xFFF3F6F3))
              .border(1.dp, SuyanchLightLine, RoundedCornerShape(10.dp))
              .clickable { showPhotosGallery = !showPhotosGallery }
              .padding(10.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = SuyanchDeepGreen, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Tashrif suratlari (${visitPhotos.size} ta)",
                  fontFamily = ManropeFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = SuyanchDeepGreen
                )
              }
              Text(
                text = if (showPhotosGallery) "Yopish ▲" else "Ko'rish ▼",
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color(0xFF1E6B4A)
              )
            }
          }

          // Expandable Photos Gallery
          if (showPhotosGallery) {
            Spacer(modifier = Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              visitPhotos.forEach { photo ->
                VisitPhotoItem(photo = photo)
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          HorizontalDivider(color = SuyanchLightLine)
          Spacer(modifier = Modifier.height(14.dp))

          // Action Buttons: Chat vs Everything fine vs Report problem vs SOS
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Direct chat with worker button
            if (assignedWorker != null && onOpenChatWithWorker != null) {
              SuyanchButton(
                text = "Usta bilan bog'lanish (Chat)",
                onClick = { onOpenChatWithWorker(assignedWorker) },
                variant = ButtonVariant.OUTLINED,
                leadingIcon = {
                  Icon(Icons.Default.Chat, contentDescription = null, tint = SuyanchDeepGreen, modifier = Modifier.size(18.dp))
                }
              )
            }

            if (activeBooking.status == VisitStatus.CHECKED_OUT || activeBooking.status == VisitStatus.CHECKED_IN) {
              SuyanchButton(
                text = "Hammasi a'lo, to'lovni o'tkazish",
                onClick = { repository.confirmEverythingFine(activeBooking.id) },
                variant = ButtonVariant.PRIMARY
              )

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                SuyanchButton(
                  text = "Muammo haqida xabar",
                  onClick = { showReportDialog = true },
                  variant = ButtonVariant.OUTLINED,
                  modifier = Modifier.weight(1f)
                )

                SuyanchButton(
                  text = "SOS",
                  onClick = {
                    repository.triggerSos(activeBooking.id, activeBooking.clientId)
                    showSosDialog = true
                  },
                  variant = ButtonVariant.SOS,
                  modifier = Modifier.weight(0.7f)
                )
              }
            } else if (activeBooking.status == VisitStatus.CONFIRMED) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Check, contentDescription = null, tint = SuyanchStatusOkText, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Tashrif muvaffaqiyatli yakunlandi va to'lov o'tkazildi.",
                  fontFamily = ManropeFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = SuyanchStatusOkText
                )
              }
            } else if (activeBooking.status == VisitStatus.DISPUTED) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = SuyanchStatusDangerText, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Nizo ochilgan. To'lov muzlatildi va operator tekshirmoqda.",
                  fontFamily = ManropeFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = SuyanchStatusDangerText
                )
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Barcha buyurtmalar tarixi",
      fontFamily = FrauncesFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 16.sp,
      color = SuyanchDeepGreen
    )

    Spacer(modifier = Modifier.height(8.dp))

    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(bottom = 80.dp)
    ) {
      items(bookings) { b ->
        PastBookingCard(booking = b)
      }
    }
  }
}

  // Report Problem Dialog
  if (showReportDialog && activeBooking != null) {
    AlertDialog(
      onDismissRequest = { showReportDialog = false },
      title = {
        Text("Muammo haqida xabar berish", fontFamily = FrauncesFontFamily, fontWeight = FontWeight.Bold, color = SuyanchDeepGreen)
      },
      text = {
        Column {
          Text(
            text = "To'lov darhol muzlatiladi (u usta hisobiga o'tmaydi) va xodim qidiruvdan yashiriladi. Holatni operator tekshiradi.",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = Color(0xFF263C35)
          )
          Spacer(modifier = Modifier.height(10.dp))
          SuyanchTextField(
            value = disputeReason,
            onValueChange = { disputeReason = it },
            placeholder = "Nima muammo bo'ldi? (Masalan: ish chala qilindi)",
            label = "Sababni yozing"
          )
        }
      },
      confirmButton = {
        SuyanchButton(
          text = "Nizo ochish",
          onClick = {
            repository.reportProblem(activeBooking.id, disputeReason)
            showReportDialog = false
          },
          variant = ButtonVariant.DANGER,
          fullWidth = false
        )
      },
      dismissButton = {
        SuyanchButton(
          text = "Bekor qilish",
          onClick = { showReportDialog = false },
          variant = ButtonVariant.OUTLINED,
          fullWidth = false
        )
      }
    )
  }

  // SOS Confirmation Dialog
  if (showSosDialog) {
    AlertDialog(
      onDismissRequest = { showSosDialog = false },
      title = {
        Text("SOS signali yuborildi!", fontFamily = FrauncesFontFamily, fontWeight = FontWeight.Bold, color = Color(0xFFC02626))
      },
      text = {
        Text(
          text = "Xavfsizlik dispetcheri xabardor qilindi. 2 daqiqa ichida siz bilan va hududiy xavfsizlik guruhi bilan bog'laniladi.",
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.Medium,
          fontSize = 13.sp,
          color = Color(0xFF142923)
        )
      },
      confirmButton = {
        SuyanchButton(
          text = "Tushunarli",
          onClick = { showSosDialog = false },
          variant = ButtonVariant.SOS,
          fullWidth = false
        )
      }
    )
  }
}

@Composable
fun VisitPhotoItem(photo: VisitPhoto) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(Color(0xFFF9FAF8))
      .border(1.dp, SuyanchLightLine, RoundedCornerShape(8.dp))
      .padding(10.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(34.dp)
          .clip(CircleShape)
          .background(if (photo.phase == "before") Color(0xFFEAF1EC) else Color(0xFFFFF7ED)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.CameraAlt,
          contentDescription = null,
          tint = if (photo.phase == "before") SuyanchDeepGreen else Color(0xFFC79A45),
          modifier = Modifier.size(16.dp)
        )
      }
      Spacer(modifier = Modifier.width(10.dp))
      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = if (photo.phase == "before") "Oldingi holat" else "Keyingi holat (Natija)",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = if (photo.phase == "before") Color(0xFF1E6B4A) else Color(0xFF8A6B1F)
          )
          Text(
            text = photo.timestamp,
            fontFamily = IbmPlexMonoFontFamily,
            fontSize = 10.sp,
            color = Color(0xFF6B7B75)
          )
        }
        Text(
          text = photo.description,
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.Medium,
          fontSize = 12.sp,
          color = Color(0xFF1D332B)
        )
      }
    }
  }
}

@Composable
fun PastBookingCard(booking: Booking) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .background(Color.White)
      .border(1.dp, SuyanchLightLine, RoundedCornerShape(14.dp))
      .padding(14.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = booking.workerName,
          fontFamily = FrauncesFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 17.sp,
          color = SuyanchDeepGreen
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "${booking.date} • ${booking.address}",
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 13.5.sp,
          color = Color(0xFF142923)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
          text = "${ServiceCatalog.formatPrice(booking.amount, com.example.model.PriceFormat.VISIT)} so'm",
          style = SuyanchMonoStyles.price,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 17.sp,
          color = SuyanchGold
        )
      }

      VisitStatusPill(status = booking.status)
    }
  }
}
