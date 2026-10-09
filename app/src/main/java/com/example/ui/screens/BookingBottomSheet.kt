package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.model.PaymentMethod
import com.example.model.ServiceCatalog
import com.example.model.Tier
import com.example.model.Worker
import com.example.repository.SuyanchRepository
import com.example.ui.components.ButtonVariant
import com.example.ui.components.PinCodeDisplay
import com.example.ui.components.SuyanchButton
import com.example.ui.components.SuyanchCard
import com.example.ui.components.SuyanchChip
import com.example.ui.components.SuyanchTextField
import com.example.ui.components.TierBadge
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchLightSurface
import com.example.ui.theme.SuyanchMonoStyles
import com.example.ui.theme.SuyanchStatusDangerBg
import com.example.ui.theme.SuyanchStatusDangerText
import com.example.ui.theme.SuyanchStatusWarnBg

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookingSheet(
  worker: Worker,
  repository: SuyanchRepository,
  onDismiss: () -> Unit,
  onBookingSuccess: (pinCode: String) -> Unit,
  onShowKalitWorkers: () -> Unit,
  modifier: Modifier = Modifier
) {
  val availableDates = listOf("Bugun (07-oktyabr)", "Ertaga (08-oktyabr)", "Indinga (09-oktyabr)")
  val availableTimes = listOf("09:00", "10:00", "11:30", "14:00", "16:30", "18:00")

  var selectedDate by remember { mutableStateOf(availableDates.first()) }
  var selectedTime by remember { mutableStateOf("10:00") }
  var address by remember { mutableStateOf("Toshkent sh., Mirobod t., Nukus ko'chasi 42") }
  var comment by remember { mutableStateOf("") }
  var ownerPresent by remember { mutableStateOf(worker.tier == Tier.GOLD) }
  var paymentMethod by remember { mutableStateOf(PaymentMethod.CARD) }
  var showPinDialog by remember { mutableStateOf(false) }
  var generatedPin by remember { mutableStateOf("4921") }

  val isBlockedOwnerAway = !ownerPresent && worker.tier != Tier.GOLD

  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
      .background(Color.White)
      .padding(20.dp)
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Xizmatga buyurtma berish",
          fontFamily = FrauncesFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 20.sp,
          color = SuyanchDeepGreen
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "${worker.fullName} • ",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color(0xFF263C35)
          )
          Text(
            text = "${ServiceCatalog.formatPrice(worker.price, worker.priceFormat)} so'm",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp,
            color = SuyanchGold
          )
        }
      }
      TierBadge(tier = worker.tier, sealSize = 16.dp)
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Date Picker Chips
    Text(
      text = "Qaysi kunga kerak?",
      fontFamily = ManropeFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 13.sp,
      color = SuyanchDeepGreen
    )
    Spacer(modifier = Modifier.height(6.dp))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      items(availableDates) { d ->
        SuyanchChip(
          label = d,
          selected = selectedDate == d,
          onClick = { selectedDate = d },
          leadingIcon = {
            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = if (selectedDate == d) Color.White else SuyanchDeepGreen, modifier = Modifier.size(13.dp))
          }
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Time Slot Chips
    Text(
      text = "Boshlanish vaqti:",
      fontFamily = ManropeFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 13.sp,
      color = SuyanchDeepGreen
    )
    Spacer(modifier = Modifier.height(6.dp))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      items(availableTimes) { t ->
        SuyanchChip(
          label = t,
          selected = selectedTime == t,
          onClick = { selectedTime = t },
          leadingIcon = {
            Icon(Icons.Default.AccessTime, contentDescription = null, tint = if (selectedTime == t) Color.White else SuyanchDeepGreen, modifier = Modifier.size(13.dp))
          }
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Owner Present Toggle
    Text(
      text = "Tashrif vaqtida uyda bo'lasizmi?",
      fontFamily = ManropeFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 13.sp,
      color = SuyanchDeepGreen
    )

    Spacer(modifier = Modifier.height(6.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(Color(0xFFF1F3EF))
        .padding(4.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Box(
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(10.dp))
          .background(if (ownerPresent) SuyanchDeepGreen else Color.Transparent)
          .clickable { ownerPresent = true }
          .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "Ha, uyda bo'laman",
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = if (ownerPresent) Color.White else Color(0xFF42564F)
        )
      }

      Box(
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(10.dp))
          .background(if (!ownerPresent) (if (worker.tier == Tier.GOLD) SuyanchGold else SuyanchStatusDangerText) else Color.Transparent)
          .clickable { ownerPresent = false }
          .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Key,
            contentDescription = null,
            tint = if (!ownerPresent) (if (worker.tier == Tier.GOLD) Color(0xFF0F2620) else Color.White) else Color(0xFF42564F),
            modifier = Modifier.size(15.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "Yo'q, uyda bo'lmayman",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = if (!ownerPresent) (if (worker.tier == Tier.GOLD) Color(0xFF0F2620) else Color.White) else Color(0xFF42564F)
          )
        }
      }
    }

    // BLOCKING WARNING RULE IF OWNER AWAY AND NOT GOLD
    if (isBlockedOwnerAway) {
      Spacer(modifier = Modifier.height(12.dp))
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .background(SuyanchStatusDangerBg)
          .border(1.5.dp, SuyanchStatusDangerText, RoundedCornerShape(12.dp))
          .padding(12.dp)
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Warning,
              contentDescription = null,
              tint = SuyanchStatusDangerText,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Buyurtma qabul qilinmaydi",
              fontFamily = FrauncesFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = SuyanchStatusDangerText
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Siz yo'q bo'lganingizda faqat «Kalit» (Oltin) maqomidagi ustalar ishlay oladi. Ushbu usta faqat egasi uyda bo'lganda ishlashga ruxsat olgan.",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = Color(0xFF5A2A20)
          )
          Spacer(modifier = Modifier.height(8.dp))
          SuyanchButton(
            text = "«Kalit» ustalarini ko'rsatish",
            onClick = onShowKalitWorkers,
            variant = ButtonVariant.GOLD
          )
        }
      }
    }

    // Warning for cash when owner is away
    if (!ownerPresent && paymentMethod == PaymentMethod.CASH && !isBlockedOwnerAway) {
      Spacer(modifier = Modifier.height(10.dp))
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(10.dp))
          .background(SuyanchStatusWarnBg)
          .padding(10.dp)
      ) {
        Text(
          text = "Eslatma: Uyda bo'lmaganingizda xavfsizlik uchun karta orqali to'lov tavsiya etiladi (pul ish tasdiqlanguncha muzlatiladi).",
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          color = SuyanchDeepGreen
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Address
    SuyanchTextField(
      value = address,
      onValueChange = { address = it },
      label = "Xonadon manzili",
      placeholder = "Shahar, ko'cha, uy raqami"
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Comment
    SuyanchTextField(
      value = comment,
      onValueChange = { comment = it },
      label = "Izoh va topshiriqlar",
      placeholder = "Qanday ish bajarilishi kerakligini yozing..."
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Payment Method
    Text(
      text = "To'lov usuli:",
      fontFamily = ManropeFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 13.sp,
      color = SuyanchDeepGreen
    )
    Spacer(modifier = Modifier.height(6.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Box(
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(10.dp))
          .background(if (paymentMethod == PaymentMethod.CARD) Color(0xFFE5EDE8) else Color(0xFFF6F8F5))
          .border(
            1.5.dp,
            if (paymentMethod == PaymentMethod.CARD) SuyanchDeepGreen else SuyanchLightLine,
            RoundedCornerShape(10.dp)
          )
          .clickable { paymentMethod = PaymentMethod.CARD }
          .padding(10.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.CreditCard, contentDescription = null, tint = SuyanchDeepGreen, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Karta (Hold)", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SuyanchDeepGreen)
        }
      }

      Box(
        modifier = Modifier
          .weight(1f)
          .clip(RoundedCornerShape(10.dp))
          .background(if (paymentMethod == PaymentMethod.CASH) Color(0xFFE5EDE8) else Color(0xFFF6F8F5))
          .border(
            1.5.dp,
            if (paymentMethod == PaymentMethod.CASH) SuyanchDeepGreen else SuyanchLightLine,
            RoundedCornerShape(10.dp)
          )
          .clickable { paymentMethod = PaymentMethod.CASH }
          .padding(10.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Money, contentDescription = null, tint = SuyanchDeepGreen, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Naqd pul", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = SuyanchDeepGreen)
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // Submit Button
    SuyanchButton(
      text = if (isBlockedOwnerAway) "Faqat «Kalit» ustalariga ruxsat" else "Buyurtmani tasdiqlash",
      onClick = {
        val newPin = (1000..9999).random().toString()
        generatedPin = newPin
        val result = repository.createBooking(
          worker = worker,
          date = selectedDate,
          startTime = selectedTime,
          address = address,
          comment = comment,
          paymentMethod = paymentMethod,
          ownerPresent = ownerPresent,
          customPin = newPin
        )
        if (result.isSuccess) {
          showPinDialog = true
        }
      },
      enabled = !isBlockedOwnerAway,
      variant = if (worker.tier == Tier.GOLD) ButtonVariant.GOLD else ButtonVariant.PRIMARY
    )
  }

  // Confirmation PIN Dialog
  if (showPinDialog) {
    AlertDialog(
      onDismissRequest = {
        showPinDialog = false
        onBookingSuccess(generatedPin)
      },
      title = {
        Text(
          text = "Buyurtma qabul qilindi!",
          fontFamily = FrauncesFontFamily,
          fontWeight = FontWeight.Bold,
          color = SuyanchDeepGreen
        )
      },
      text = {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "Xavfsizlik bir martalik PIN kodi:",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color(0xFF263C35)
          )
          Spacer(modifier = Modifier.height(10.dp))
          PinCodeDisplay(pin = generatedPin)
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Usta belgilangan vaqtda yetib kelgach, ushbu kodni aytasiz. U ilovaga kiritib, jonli selfie tushgandan so'ng ish boshlanadi.",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 17.sp,
            color = Color(0xFF334841)
          )
        }
      },
      confirmButton = {
        SuyanchButton(
          text = "Tushunarli",
          onClick = {
            showPinDialog = false
            onBookingSuccess(generatedPin)
          },
          variant = ButtonVariant.PRIMARY,
          fullWidth = false
        )
      }
    )
  }
}
