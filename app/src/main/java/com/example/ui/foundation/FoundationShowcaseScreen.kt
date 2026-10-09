package com.example.ui.foundation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.i18n.AppLanguage
import com.example.core.i18n.LocaleManager
import com.example.core.i18n.StringKey
import com.example.core.i18n.appString
import com.example.model.CategoryId
import com.example.model.PriceFormat
import com.example.model.ServiceCatalog
import com.example.model.Tier
import com.example.model.TimelineStepInfo
import com.example.model.TimelineStepState
import com.example.model.VisitPinManager
import com.example.model.VisitStatus
import com.example.ui.components.ButtonVariant
import com.example.ui.components.PinCodeDisplay
import com.example.ui.components.StatusPill
import com.example.ui.components.StatusTone
import com.example.ui.components.SuyanchButton
import com.example.ui.components.SuyanchCard
import com.example.ui.components.SuyanchChip
import com.example.ui.components.SuyanchSeal
import com.example.ui.components.SuyanchTextField
import com.example.ui.components.TierBadge
import com.example.ui.components.TimelineView
import com.example.ui.components.VisitStatusPill
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.IbmPlexMonoFontFamily
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchGreen
import com.example.ui.theme.SuyanchLightBackground
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchLightSurface
import com.example.ui.theme.SuyanchMonoStyles
import com.example.ui.theme.SuyanchStatusDangerBg
import com.example.ui.theme.SuyanchStatusDangerText
import com.example.ui.theme.SuyanchStatusOkBg
import com.example.ui.theme.SuyanchStatusOkText
import com.example.ui.theme.SuyanchStatusWarnBg
import com.example.ui.theme.SuyanchStatusWarnText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FoundationShowcaseScreen(
  isDarkTheme: Boolean,
  onToggleTheme: () -> Unit,
  modifier: Modifier = Modifier
) {
  val currentLanguage by LocaleManager.currentLanguage.collectAsState()
  var selectedCategory by remember { mutableStateOf<CategoryId?>(CategoryId.CARE) }
  var filterOnlyKalit by remember { mutableStateOf(false) }
  var isButtonLoading by remember { mutableStateOf(false) }
  var enteredPin by remember { mutableStateOf("") }
  var pinResultText by remember { mutableStateOf<String?>(null) }
  val pinManager = remember { VisitPinManager("4921") }

  // Interactive timeline step counter (1 to 5)
  var currentTimelineStep by remember { mutableIntStateOf(2) }

  val timelineSteps = remember(currentTimelineStep, currentLanguage) {
    listOf(
      TimelineStepInfo(
        stepNumber = 1,
        titleKey = StringKey.TIMELINE_CODE_ISSUED,
        subtitle = "PIN kod mijozga taqdim etildi (#4921)",
        timestamp = "09:30",
        state = if (currentTimelineStep >= 1) TimelineStepState.COMPLETED else TimelineStepState.PENDING
      ),
      TimelineStepInfo(
        stepNumber = 2,
        titleKey = StringKey.TIMELINE_ARRIVED,
        subtitle = "PIN kiritildi va jonli selfie tekshirildi",
        timestamp = if (currentTimelineStep >= 2) "10:02" else null,
        state = when {
          currentTimelineStep > 2 -> TimelineStepState.COMPLETED
          currentTimelineStep == 2 -> TimelineStepState.IN_PROGRESS
          else -> TimelineStepState.PENDING
        }
      ),
      TimelineStepInfo(
        stepNumber = 3,
        titleKey = StringKey.TIMELINE_BEFORE_PHOTOS,
        subtitle = "Kamida 3 ta xona surati yuklandi",
        timestamp = if (currentTimelineStep >= 3) "10:08" else null,
        state = when {
          currentTimelineStep > 3 -> TimelineStepState.COMPLETED
          currentTimelineStep == 3 -> TimelineStepState.IN_PROGRESS
          else -> TimelineStepState.PENDING
        }
      ),
      TimelineStepInfo(
        stepNumber = 4,
        titleKey = StringKey.TIMELINE_LEFT,
        subtitle = "PIN bilan chiqish + yakuniy natija suratlari",
        timestamp = if (currentTimelineStep >= 4) "13:45" else null,
        state = when {
          currentTimelineStep > 4 -> TimelineStepState.COMPLETED
          currentTimelineStep == 4 -> TimelineStepState.IN_PROGRESS
          else -> TimelineStepState.PENDING
        }
      ),
      TimelineStepInfo(
        stepNumber = 5,
        titleKey = StringKey.TIMELINE_CONFIRMED,
        subtitle = "Uy egasi tasdiqladi, to'lov yechildi",
        timestamp = if (currentTimelineStep >= 5) "13:50" else null,
        state = if (currentTimelineStep >= 5) TimelineStepState.COMPLETED else TimelineStepState.PENDING
      )
    )
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = MaterialTheme.colorScheme.background
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      // Top Navigation / Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          SuyanchSeal(tier = Tier.GOLD, size = 38.dp)
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = appString(StringKey.APP_NAME),
              style = MaterialTheme.typography.headlineMedium,
              color = MaterialTheme.colorScheme.primary
            )
            Text(
              text = "Phase 1 Foundation",
              fontFamily = IbmPlexMonoFontFamily,
              fontSize = 11.sp,
              color = Color(0xFF7A8D86)
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          // Language switcher toggle
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(1.dp, SuyanchLightLine),
            modifier = Modifier.clip(RoundedCornerShape(20.dp))
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = currentLanguage.displayName,
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = SuyanchDeepGreen,
                modifier = Modifier.padding(horizontal = 6.dp)
              )
              IconButton(
                onClick = { LocaleManager.toggle() },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Language,
                  contentDescription = "Tilni almashtirish",
                  tint = SuyanchDeepGreen,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.width(8.dp))

          // Dark/Light Theme toggle
          IconButton(
            onClick = onToggleTheme,
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.surface)
              .border(1.dp, SuyanchLightLine, CircleShape)
          ) {
            Icon(
              imageVector = if (isDarkTheme) Icons.Default.Brightness7 else Icons.Default.Brightness4,
              contentDescription = "Mavzuni almashtirish",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Tagline Card
      SuyanchCard(
        containerColor = SuyanchDeepGreen,
        borderColor = SuyanchDeepGreen
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = null,
              tint = SuyanchGold,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = appString(StringKey.KALIT_TITLE),
              fontFamily = FrauncesFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = Color.White
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = appString(StringKey.KALIT_SUBTITLE),
            fontFamily = ManropeFontFamily,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            color = Color(0xFFD3E0DC)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Section 1: Color Tokens Swatches
      SectionHeader(title = "1. Theme Tokens (Colors & Palette)")
      SuyanchCard {
        Column {
          Text(
            text = "Asosiy ranglar tizimi (Material Design 3 tokens)",
            fontFamily = ManropeFontFamily,
            fontSize = 12.sp,
            color = Color(0xFF6B7B75)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            ColorSwatch(name = "Deep Green", hex = "#1D3D34", color = SuyanchDeepGreen)
            ColorSwatch(name = "Green", hex = "#2F5D50", color = SuyanchGreen)
            ColorSwatch(name = "Gold Kalit", hex = "#C79A45", color = SuyanchGold)
            ColorSwatch(name = "Surface", hex = "#FFFFFF", color = SuyanchLightSurface)
          }
          Spacer(modifier = Modifier.height(12.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            ColorSwatch(name = "Status OK", hex = "#1E6B4A", color = SuyanchStatusOkText)
            ColorSwatch(name = "Status Warn", hex = "#8A6B1F", color = SuyanchStatusWarnText)
            ColorSwatch(name = "Status Danger", hex = "#B5533D", color = SuyanchStatusDangerText)
            ColorSwatch(name = "Line Border", hex = "#DCE1D8", color = SuyanchLightLine)
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Section 2: Typography & Bundled Fonts
      SectionHeader(title = "2. Typography (Fraunces, Manrope, IBM Plex)")
      SuyanchCard {
        Column {
          Text(
            text = "Fraunces — Sarlavhalar va brend matnlari",
            fontFamily = FrauncesFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Manrope — Matnlar, tavsiflar va interfeys elementlari (o'qilishi oson va qulay).",
            fontFamily = ManropeFontFamily,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(8.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "IBM Plex Mono (Narxlar):",
              fontFamily = ManropeFontFamily,
              fontSize = 13.sp,
              color = Color(0xFF6B7B75)
            )
            Text(
              text = "120 000 so'm / soat",
              style = SuyanchMonoStyles.priceLarge,
              color = SuyanchDeepGreen
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Section 3: Brand Seal & Tier Badges
      SectionHeader(title = "3. Reusable Seals & Tier Badges")
      SuyanchCard {
        Column {
          Text(
            text = "Suyanch muhrlari: 2 ta konsentrik aylana (ichkisi nuqtali) + Kalit / Belgisi",
            fontFamily = ManropeFontFamily,
            fontSize = 12.sp,
            color = Color(0xFF6B7B75)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              SuyanchSeal(tier = Tier.GOLD, size = 52.dp)
              Spacer(modifier = Modifier.height(6.dp))
              Text("Kalit (Gold)", fontSize = 11.sp, fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              SuyanchSeal(tier = Tier.SILVER, size = 52.dp)
              Spacer(modifier = Modifier.height(6.dp))
              Text("Kumush", fontSize = 11.sp, fontFamily = ManropeFontFamily, fontWeight = FontWeight.Medium)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              SuyanchSeal(tier = Tier.BASIC, size = 52.dp)
              Spacer(modifier = Modifier.height(6.dp))
              Text("Asosiy", fontSize = 11.sp, fontFamily = ManropeFontFamily, fontWeight = FontWeight.Medium)
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          HorizontalDivider(color = SuyanchLightLine)
          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            TierBadge(tier = Tier.GOLD, showFullLabel = true)
            TierBadge(tier = Tier.SILVER, showFullLabel = true)
            TierBadge(tier = Tier.BASIC, showFullLabel = true)
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Owner present rules explanation
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(SuyanchStatusWarnBg.copy(alpha = 0.5f))
              .border(1.dp, SuyanchGold.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
              .padding(10.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Key,
                contentDescription = null,
                tint = SuyanchDeepGreen,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = appString(StringKey.CAN_WORK_ALONE),
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = SuyanchDeepGreen
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Section 4: Status Pills & Badges
      SectionHeader(title = "4. StatusPill & Visit States")
      SuyanchCard {
        Column {
          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            StatusPill(text = "Tasdiqlangan (OK)", tone = StatusTone.OK)
            StatusPill(text = "Jarayonda (Warning)", tone = StatusTone.WARNING)
            StatusPill(text = "Nizo / Rad (Danger)", tone = StatusTone.DANGER)
            StatusPill(text = "Kutilmoqda (Neutral)", tone = StatusTone.NEUTRAL)
          }

          Spacer(modifier = Modifier.height(14.dp))
          Text(
            text = "VisitStatus holatlari:",
            fontFamily = ManropeFontFamily,
            fontSize = 12.sp,
            color = Color(0xFF6B7B75)
          )
          Spacer(modifier = Modifier.height(8.dp))

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            VisitStatusPill(status = VisitStatus.BOOKED)
            VisitStatusPill(status = VisitStatus.CHECKED_IN)
            VisitStatusPill(status = VisitStatus.CHECKED_OUT)
            VisitStatusPill(status = VisitStatus.CONFIRMED)
            VisitStatusPill(status = VisitStatus.DISPUTED)
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Section 5: Buttons & Variations
      SectionHeader(title = "5. Reusable SuyanchButton Variants")
      SuyanchCard {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          SuyanchButton(
            text = "Asosiy tugma (Primary Deep Green)",
            onClick = { /* Demo */ },
            variant = ButtonVariant.PRIMARY
          )

          SuyanchButton(
            text = "«Kalit» maxsus harakat (Gold Accent)",
            onClick = { /* Demo */ },
            variant = ButtonVariant.GOLD,
            leadingIcon = {
              Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = SuyanchDeepGreen, modifier = Modifier.size(18.dp))
            }
          )

          SuyanchButton(
            text = "Konturli tugma (Outlined)",
            onClick = { /* Demo */ },
            variant = ButtonVariant.OUTLINED
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            SuyanchButton(
              text = "Xavf (Danger)",
              onClick = { /* Demo */ },
              variant = ButtonVariant.DANGER,
              modifier = Modifier.weight(1f)
            )

            SuyanchButton(
              text = "SOS",
              onClick = { /* Demo */ },
              variant = ButtonVariant.SOS,
              modifier = Modifier.weight(1f)
            )
          }

          SuyanchButton(
            text = if (isButtonLoading) "Yuklanmoqda..." else "Yuklanish holatini sinash",
            onClick = { isButtonLoading = !isButtonLoading },
            variant = ButtonVariant.PRIMARY,
            isLoading = isButtonLoading
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Section 6: Category & Filter Chips
      SectionHeader(title = "6. Category & Filter Chips")
      SuyanchCard {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            SuyanchChip(
              label = appString(StringKey.FILTER_ONLY_KALIT),
              selected = filterOnlyKalit,
              onClick = { filterOnlyKalit = !filterOnlyKalit },
              isGoldHighlighted = true,
              leadingIcon = {
                Icon(
                  imageVector = Icons.Default.Key,
                  contentDescription = null,
                  tint = if (filterOnlyKalit) SuyanchDeepGreen else SuyanchGold,
                  modifier = Modifier.size(16.dp)
                )
              }
            )

            Text(
              text = "6 ta toifa mavjud",
              fontFamily = ManropeFontFamily,
              fontSize = 12.sp,
              color = Color(0xFF6B7B75)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            CategoryId.values().forEach { cat ->
              SuyanchChip(
                label = appString(cat.titleKey),
                selected = selectedCategory == cat,
                onClick = { selectedCategory = cat },
                badgeText = ServiceCatalog.formatPrice(cat.defaultStartingPrice, cat.priceFormat) + " so'm"
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Section 7: Live Visit Timeline & PIN State Machine
      SectionHeader(title = "7. Visit Flow State Machine & PIN Verification")
      SuyanchCard {
        Column {
          Text(
            text = "Tashrif bosqichlari (Tugmalar orqali bosqichni o'zgartiring):",
            fontFamily = ManropeFontFamily,
            fontSize = 12.sp,
            color = Color(0xFF6B7B75)
          )
          Spacer(modifier = Modifier.height(12.dp))

          TimelineView(steps = timelineSteps)

          Spacer(modifier = Modifier.height(14.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            SuyanchButton(
              text = "Ortga",
              onClick = { if (currentTimelineStep > 1) currentTimelineStep-- },
              variant = ButtonVariant.OUTLINED,
              enabled = currentTimelineStep > 1,
              modifier = Modifier.weight(1f)
            )

            SuyanchButton(
              text = if (currentTimelineStep < 5) "Oldinga (${currentTimelineStep}/5)" else "Yakunlandi",
              onClick = { if (currentTimelineStep < 5) currentTimelineStep++ },
              variant = ButtonVariant.PRIMARY,
              enabled = currentTimelineStep < 5,
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))
          HorizontalDivider(color = SuyanchLightLine)
          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "Xavfsizlik PIN kodi (Test: 4921):",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.height(6.dp))

          PinCodeDisplay(pin = if (enteredPin.isEmpty()) "4921" else enteredPin)

          Spacer(modifier = Modifier.height(10.dp))

          SuyanchTextField(
            value = enteredPin,
            onValueChange = { if (it.length <= 4) enteredPin = it },
            placeholder = "4 xonali PIN kiriting (masalan: 4921)",
            label = "PIN kodni tekshirish",
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          SuyanchButton(
            text = "PIN kodni tasdiqlash",
            onClick = {
              val res = pinManager.verifyPin(enteredPin)
              pinResultText = res.message
            },
            variant = ButtonVariant.GOLD,
            enabled = enteredPin.length == 4
          )

          if (pinResultText != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = pinResultText ?: "",
              fontFamily = ManropeFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = if (pinResultText?.contains("to'g'ri") == true) SuyanchStatusOkText else SuyanchStatusDangerText
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(32.dp))
    }
  }
}

@Composable
fun SectionHeader(title: String) {
  Text(
    text = title,
    fontFamily = FrauncesFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
    color = MaterialTheme.colorScheme.primary,
    modifier = Modifier.padding(bottom = 8.dp, start = 2.dp)
  )
}

@Composable
fun ColorSwatch(name: String, hex: String, color: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Box(
      modifier = Modifier
        .size(46.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(color)
        .border(1.dp, SuyanchLightLine, RoundedCornerShape(10.dp))
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = name,
      fontFamily = ManropeFontFamily,
      fontWeight = FontWeight.SemiBold,
      fontSize = 10.sp,
      color = Color(0xFF4B5D57)
    )
    Text(
      text = hex,
      fontFamily = IbmPlexMonoFontFamily,
      fontSize = 9.sp,
      color = Color(0xFF8B9E96)
    )
  }
}
