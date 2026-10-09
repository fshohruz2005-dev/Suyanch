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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.model.Tier
import com.example.model.VisitStatus
import com.example.model.Worker
import com.example.repository.SuyanchRepository
import com.example.ui.components.ButtonVariant
import com.example.ui.components.PinCodeDisplay
import com.example.ui.components.SuyanchButton
import com.example.ui.components.SuyanchCard
import com.example.ui.components.SuyanchSeal
import com.example.ui.components.SuyanchTextField
import com.example.ui.components.TierBadge
import com.example.ui.components.VisitStatusPill
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.IbmPlexMonoFontFamily
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchMonoStyles
import com.example.ui.theme.SuyanchStatusDangerText
import com.example.ui.theme.SuyanchStatusOkText
import com.example.ui.theme.SuyanchStatusWarnBg

@Composable
fun WorkerDashboardScreen(
  repository: SuyanchRepository,
  onOpenClientChat: ((Worker) -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  val bookings by repository.bookings.collectAsState()
  val visitPhotos by repository.visitPhotos.collectAsState()
  val activeBooking = bookings.firstOrNull { it.status != VisitStatus.CONFIRMED && it.status != VisitStatus.DISPUTED }

  var workerPinInput by remember { mutableStateOf("") }
  var pinErrorMsg by remember { mutableStateOf<String?>(null) }
  var showSelfieDialog by remember { mutableStateOf(false) }
  var showAddPhotoDialog by remember { mutableStateOf(false) }
  var photoPhase by remember { mutableStateOf("after") }
  var photoDescription by remember { mutableStateOf("Yangi montaj yakunlandi, suv bosimi sinovdan o'tdi") }

  val myWorker = remember {
    repository.getWorkerById("w8") ?: repository.workers.value.first()
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF6F8F5))
      .verticalScroll(rememberScrollState())
      .padding(bottom = 80.dp)
  ) {
    // Worker Profile Header Banner
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
            text = "Usta ish kabineti",
            fontFamily = FrauncesFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color.White
          )
          Spacer(modifier = Modifier.height(3.dp))
          Text(
            text = "${myWorker.fullName} (${myWorker.city.displayNameUz})",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color(0xFFC7D7D2)
          )
        }
        TierBadge(tier = Tier.GOLD, sealSize = 18.dp)
      }
    }

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
      // Active Visit Card
    if (activeBooking != null) {
      SuyanchCard(borderColor = SuyanchGold, containerColor = Color.White) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Bugungi tashrif",
              fontFamily = FrauncesFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = SuyanchDeepGreen
            )
            VisitStatusPill(status = activeBooking.status)
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Manzil: ${activeBooking.address}",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.5.sp,
            color = Color(0xFF142923)
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Topshiriq: ${activeBooking.comment}",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = Color(0xFF263C35)
          )

          // Owner Away Tag
          Spacer(modifier = Modifier.height(8.dp))
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(if (!activeBooking.ownerPresent) SuyanchStatusWarnBg else Color(0xFFE5EDE8))
              .border(
                width = 1.dp,
                color = if (!activeBooking.ownerPresent) SuyanchGold.copy(alpha = 0.5f) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
              )
              .padding(horizontal = 10.dp, vertical = 5.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Key,
                contentDescription = null,
                tint = SuyanchDeepGreen,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (!activeBooking.ownerPresent) "Uy egasi yo'q (Kalit tashrifi)" else "Egasi uyda bo'ladi",
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = SuyanchDeepGreen
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Client Chat Button
          if (onOpenClientChat != null) {
            SuyanchButton(
              text = "Mijoz bilan yozishish (Chat)",
              onClick = { onOpenClientChat(myWorker) },
              variant = ButtonVariant.OUTLINED,
              leadingIcon = {
                Icon(Icons.Default.Chat, contentDescription = null, tint = SuyanchDeepGreen, modifier = Modifier.size(18.dp))
              }
            )
            Spacer(modifier = Modifier.height(10.dp))
          }

          HorizontalDivider(color = SuyanchLightLine)
          Spacer(modifier = Modifier.height(12.dp))

          // Check-in or Check-out Actions
          if (activeBooking.status == VisitStatus.BOOKED) {
            Text(
              text = "1-Qadam: Yetib kelish (Check-in)",
              fontFamily = FrauncesFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = SuyanchDeepGreen
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Mijozdan 4 xonali PIN kodni so'rab kiriting va jonli selfie tushing:",
              fontFamily = ManropeFontFamily,
              fontWeight = FontWeight.Medium,
              fontSize = 12.sp,
              color = Color(0xFF263C35)
            )
            Spacer(modifier = Modifier.height(8.dp))

            SuyanchTextField(
              value = workerPinInput,
              onValueChange = { if (it.length <= 4) workerPinInput = it },
              placeholder = "4 xonali PIN (Masalan: ${activeBooking.pinCode})",
              label = "PIN kod"
            )

            if (pinErrorMsg != null) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = pinErrorMsg ?: "",
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = SuyanchStatusDangerText
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            SuyanchButton(
              text = "Selfie tushish va kirish",
              onClick = {
                val res = repository.checkInWorker(activeBooking.id, workerPinInput)
                if (res.success) {
                  showSelfieDialog = true
                  pinErrorMsg = null
                  workerPinInput = ""
                } else {
                  pinErrorMsg = res.message
                }
              },
              variant = ButtonVariant.GOLD,
              leadingIcon = {
                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = SuyanchDeepGreen, modifier = Modifier.size(18.dp))
              }
            )
          } else if (activeBooking.status == VisitStatus.CHECKED_IN) {
            // Already inside and working
            Text(
              text = "Ish jarayoni davom etmoqda",
              fontFamily = FrauncesFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = Color(0xFF1E6B4A)
            )
            Text(
              text = "Kirish vaqti: ${activeBooking.checkInTime} • ${visitPhotos.size} ta surat yuklangan",
              fontFamily = ManropeFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 12.sp,
              color = Color(0xFF2E453D)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Add photo button
            SuyanchButton(
              text = "Ish suratini yuklash (Kamera)",
              onClick = { showAddPhotoDialog = true },
              variant = ButtonVariant.GOLD,
              leadingIcon = {
                Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = Color(0xFF0F2620), modifier = Modifier.size(18.dp))
              }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = "Ishni yakunlash (Check-out):",
              fontFamily = FrauncesFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = SuyanchDeepGreen
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Chiqish PIN kodini kiriting va ishni topshiring:",
              fontFamily = ManropeFontFamily,
              fontWeight = FontWeight.Medium,
              fontSize = 12.sp,
              color = Color(0xFF263C35)
            )

            Spacer(modifier = Modifier.height(8.dp))

            SuyanchTextField(
              value = workerPinInput,
              onValueChange = { if (it.length <= 4) workerPinInput = it },
              placeholder = "PIN kod (${activeBooking.pinCode})",
              label = "Chiqish PIN kodi"
            )

            if (pinErrorMsg != null) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = pinErrorMsg ?: "",
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = SuyanchStatusDangerText
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            SuyanchButton(
              text = "Ishni yakunlash (Chiqish)",
              onClick = {
                val res = repository.checkOutWorker(activeBooking.id, workerPinInput)
                if (res.success) {
                  pinErrorMsg = null
                  workerPinInput = ""
                } else {
                  pinErrorMsg = res.message
                }
              },
              variant = ButtonVariant.PRIMARY,
              leadingIcon = {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
              }
            )
          } else {
            Text(
              text = "Tashrif yakunlangan. Mijoz tasdiqlashi kutilmoqda.",
              fontFamily = ManropeFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = Color(0xFF1E6B4A)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Verification Progress Towards Kalit
    Text(
      text = "«Kalit» tekshiruvlari holati",
      fontFamily = FrauncesFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 16.sp,
      color = SuyanchDeepGreen
    )

    Spacer(modifier = Modifier.height(8.dp))

    SuyanchCard(containerColor = Color.White) {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        WorkerCheckRow(title = "Pasport (ID)", status = "Tasdiqlangan", ok = true)
        WorkerCheckRow(title = "IIV sudlanmaganlik ma'lumotnomasi", status = "Tasdiqlangan", ok = true)
        WorkerCheckRow(title = "Oldingi ish joylaridan tavsiyanoma", status = "Tasdiqlangan", ok = true)
        WorkerCheckRow(title = "Shaxsiy xavfsizlik suhbati", status = "O'tilgan", ok = true)
        WorkerCheckRow(title = "Kasbiy mahorat imtihoni", status = "O'tilgan", ok = true)
        WorkerCheckRow(title = "Rasmiy sug'urta polisi", status = "Faol (100 mln so'm)", ok = true)
        WorkerCheckRow(title = "20 ta shikoyatsiz buyurtma", status = "112 ta bajarilgan", ok = true)
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Earnings Summary Card
    SuyanchCard(containerColor = Color.White) {
      Column {
        Text("Daromadlar hisobi", fontFamily = FrauncesFontFamily, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = SuyanchDeepGreen)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
          Text("Bu oylik toshgan summa:", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF263C35))
          Text("14 850 000 so'm", style = SuyanchMonoStyles.priceLarge, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = SuyanchGold)
        }
      }
    }
  }
}

  // Live Selfie Simulation Dialog
  if (showSelfieDialog) {
    AlertDialog(
      onDismissRequest = { showSelfieDialog = false },
      title = {
        Text("Jonli selfie tasdiqlandi", fontFamily = FrauncesFontFamily, fontWeight = FontWeight.Bold, color = SuyanchDeepGreen)
      },
      text = {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
          Box(
            modifier = Modifier
              .size(90.dp)
              .clip(CircleShape)
              .background(Color(0xFFE5EDE8))
              .border(2.dp, SuyanchGold, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF1E6B4A), modifier = Modifier.size(44.dp))
          }
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Xodim yuzi biometrik tekshiruvdan o'tdi. Uy egasiga 'Usta yetib keldi' xabarnomasi yuborildi.",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = Color(0xFF263C35)
          )
        }
      },
      confirmButton = {
        SuyanchButton(
          text = "Ishni boshlash",
          onClick = { showSelfieDialog = false },
          variant = ButtonVariant.PRIMARY,
          fullWidth = false
        )
      }
    )
  }

  // Add Photo Simulation Dialog
  if (showAddPhotoDialog) {
    AlertDialog(
      onDismissRequest = { showAddPhotoDialog = false },
      title = {
        Text("Suratni yuklash", fontFamily = FrauncesFontFamily, fontWeight = FontWeight.Bold, color = SuyanchDeepGreen)
      },
      text = {
        Column {
          Text(
            text = "Kamera orqali bajarilgan ish holati suratga olinadi:",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = Color(0xFF263C35)
          )
          Spacer(modifier = Modifier.height(10.dp))

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (photoPhase == "before") SuyanchDeepGreen else Color(0xFFEEF1EC))
                .clickable { photoPhase = "before" }
                .padding(8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text("Ishdan oldin", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (photoPhase == "before") Color.White else Color(0xFF263C35))
            }

            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (photoPhase == "after") SuyanchDeepGreen else Color(0xFFEEF1EC))
                .clickable { photoPhase = "after" }
                .padding(8.dp),
              contentAlignment = Alignment.Center
            ) {
              Text("Ishdan keyin", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (photoPhase == "after") Color.White else Color(0xFF263C35))
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          SuyanchTextField(
            value = photoDescription,
            onValueChange = { photoDescription = it },
            label = "Surat izohi",
            placeholder = "Masalan: Yangi germetik montaj yakunlandi"
          )
        }
      },
      confirmButton = {
        SuyanchButton(
          text = "Tasdiqlash va yuklash",
          onClick = {
            repository.addVisitPhoto(photoPhase, photoDescription)
            showAddPhotoDialog = false
          },
          variant = ButtonVariant.PRIMARY,
          fullWidth = false
        )
      },
      dismissButton = {
        SuyanchButton(
          text = "Bekor qilish",
          onClick = { showAddPhotoDialog = false },
          variant = ButtonVariant.OUTLINED,
          fullWidth = false
        )
      }
    )
  }
}

@Composable
fun WorkerCheckRow(title: String, status: String, ok: Boolean) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = if (ok) Icons.Default.Check else Icons.Default.Warning,
        contentDescription = null,
        tint = if (ok) SuyanchStatusOkText else SuyanchStatusDangerText,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(title, fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF142923))
    }
    Text(status, fontFamily = ManropeFontFamily, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = Color(0xFF1E6B4A))
  }
}
