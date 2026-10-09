package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.i18n.StringKey
import com.example.core.i18n.appString
import com.example.model.ServiceCatalog
import com.example.model.Tier
import com.example.model.VerificationCheck
import com.example.model.Worker
import com.example.ui.components.ButtonVariant
import com.example.ui.components.StarRatingBar
import com.example.ui.components.StarRatingBadge
import com.example.ui.components.SuyanchButton
import com.example.ui.components.SuyanchCard
import com.example.ui.components.SuyanchSeal
import com.example.ui.components.TierBadge
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchLightSurface
import com.example.ui.theme.SuyanchMonoStyles
import com.example.ui.theme.SuyanchStatusOkText
import com.example.ui.theme.SuyanchStatusWarnBg

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkerProfileScreen(
  worker: Worker,
  onBack: () -> Unit,
  onBookNow: () -> Unit,
  onOpenChat: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var showRussianBio by remember { mutableStateOf(false) }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      Surface(
        color = SuyanchDeepGreen,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = onBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Ortga",
              tint = Color.White
            )
          }
          Text(
            text = "Mutaxassis profili",
            fontFamily = FrauncesFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
          )
        }
      }
    },
    bottomBar = {
      // Sticky booking and chat buttons
      Surface(
        color = SuyanchLightSurface,
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, SuyanchLightLine)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Xizmat narxi:",
              fontFamily = ManropeFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = Color(0xFF263C35)
            )
            Text(
              text = "${ServiceCatalog.formatPrice(worker.price, worker.priceFormat)} so'm",
              style = SuyanchMonoStyles.priceLarge,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 22.sp,
              color = SuyanchGold
            )
          }

          IconButton(
            onClick = onOpenChat,
            modifier = Modifier
              .size(50.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(Color(0xFFE5EDE8))
              .border(1.dp, SuyanchLightLine, RoundedCornerShape(14.dp))
          ) {
            Icon(
              imageVector = Icons.Default.Chat,
              contentDescription = "Muloqot (Chat)",
              tint = SuyanchDeepGreen,
              modifier = Modifier.size(24.dp)
            )
          }

          SuyanchButton(
            text = "Buyurtma",
            onClick = onBookNow,
            variant = if (worker.tier == Tier.GOLD) ButtonVariant.GOLD else ButtonVariant.PRIMARY,
            modifier = Modifier.weight(1.3f),
            fullWidth = false
          )
        }
      }
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .verticalScroll(rememberScrollState())
        .padding(16.dp)
    ) {
      // Worker Header Card
      SuyanchCard {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
          Box(
            modifier = Modifier
              .size(82.dp)
              .clip(CircleShape)
              .background(if (worker.tier == Tier.GOLD) SuyanchDeepGreen else Color(0xFF4A6860))
              .border(2.5.dp, if (worker.tier == Tier.GOLD) SuyanchGold else Color(0xFF8BA29B), CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = worker.fullName.take(2).uppercase(),
              fontFamily = ManropeFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 28.sp,
              color = if (worker.tier == Tier.GOLD) SuyanchGold else Color.White
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = worker.fullName,
            fontFamily = FrauncesFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = SuyanchDeepGreen
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = appString(worker.categoryId.titleKey),
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color(0xFF263C35)
          )

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            TierBadge(tier = worker.tier, showFullLabel = true, sealSize = 22.dp)
          }

          Spacer(modifier = Modifier.height(8.dp))

          // 5-Star Visual Rating Bar & Summary
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            StarRatingBar(rating = worker.ratingAvg, starSize = 18.dp, starSpacing = 3.dp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "${worker.ratingAvg}",
              fontFamily = ManropeFontFamily,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 16.sp,
              color = SuyanchDeepGreen
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "(${worker.reviewsCount} ta izoh)",
              fontFamily = ManropeFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 13.sp,
              color = Color(0xFF4A5F56)
            )
          }

          Spacer(modifier = Modifier.height(14.dp))
          HorizontalDivider(color = SuyanchLightLine)
          Spacer(modifier = Modifier.height(14.dp))

          // 3-metric statistics
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
          ) {
            MetricItem(
              title = "Reyting",
              value = "${worker.ratingAvg} ★",
              subtitle = "${worker.reviewsCount} ta baho"
            )
            MetricItem(
              title = "Tajriba",
              value = "${worker.experienceYears} yil",
              subtitle = "${2026 - worker.birthYear} yoshda"
            )
            MetricItem(
              title = "Buyurtmalar",
              value = "${worker.completedOrders}+",
              subtitle = "Shikoyatsiz"
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Trust / Owner Away Card
      val isGold = worker.tier == Tier.GOLD
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(if (isGold) SuyanchStatusWarnBg else Color(0xFFECEFEA))
          .border(
            width = 1.dp,
            color = if (isGold) SuyanchGold else SuyanchLightLine,
            shape = RoundedCornerShape(16.dp)
          )
          .padding(16.dp)
      ) {
        Row(verticalAlignment = Alignment.Top) {
          SuyanchSeal(tier = worker.tier, size = 42.dp)
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = if (isGold) "Uyda egasisiz ishlash ruxsati bor" else "Faqat egasi uyda bo'lganda",
              fontFamily = FrauncesFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = SuyanchDeepGreen
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = if (isGold)
                "Ushbu mutaxassis sug'urtalangan va 20+ buyurtmadan so'ng «Kalit» maqomini olgan. Siz yo'g'ingizda ham bir martalik PIN kod bilan ishlay oladi."
              else
                "Ushbu mutaxassis shaxsiy guvohnoma tekshiruvidan o'tgan, lekin xavfsizlik qoidalariga ko'ra egasi yo'qligida ishlash huquqiga ega emas.",
              fontFamily = ManropeFontFamily,
              fontSize = 12.sp,
              lineHeight = 17.sp,
              color = Color(0xFF455851)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Passed Verifications Checklist
      SuyanchCard {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = null,
              tint = SuyanchDeepGreen,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "O'tilgan tekshiruvlar ro'yxati",
              fontFamily = FrauncesFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = SuyanchDeepGreen
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          VerificationItem(title = "Shaxsiy guvohnoma (Pasport) tekshirilgan", passed = true)
          VerificationItem(title = "IIV sudlanmaganlik ma'lumotnomasi", passed = worker.tier != Tier.BASIC)
          VerificationItem(title = "Avvalgi ish joylaridan tavsiyanomalar", passed = worker.tier != Tier.BASIC)
          VerificationItem(title = "Shaxsiy xavfsizlik suhbati", passed = worker.tier != Tier.BASIC)
          VerificationItem(title = "Kasbiy mahorat imtihoni", passed = worker.tier != Tier.BASIC)
          VerificationItem(title = "Rasmiy sug'urta polisi", passed = worker.tier == Tier.GOLD)
          VerificationItem(title = "Shikoyatsiz kamida 20 ta buyurtma", passed = worker.tier == Tier.GOLD)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Biography Card
      SuyanchCard {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Mutaxassis haqida",
              fontFamily = FrauncesFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = SuyanchDeepGreen
            )

            // Uz / Ru bio toggle
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color(0xFFECEFEA),
              modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
              Row(modifier = Modifier.padding(2.dp)) {
                BioLangTab(label = "O'zbek", selected = !showRussianBio, onClick = { showRussianBio = false })
                BioLangTab(label = "Русский", selected = showRussianBio, onClick = { showRussianBio = true })
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = if (showRussianBio) worker.bioRu else worker.bioUz,
            fontFamily = ManropeFontFamily,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = Color(0xFF334640)
          )

          Spacer(modifier = Modifier.height(14.dp))
          Text(
            text = "Ko'nikmalar:",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = SuyanchDeepGreen
          )
          Spacer(modifier = Modifier.height(6.dp))

          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            worker.skills.forEach { skillId ->
              val skillItem = ServiceCatalog.skills.find { it.id == skillId }
              val label = skillItem?.let { appString(it.labelKey) } ?: skillId
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(Color(0xFFE9EFE9))
                  .padding(horizontal = 10.dp, vertical = 5.dp)
              ) {
                Text(
                  text = label,
                  fontFamily = ManropeFontFamily,
                  fontSize = 12.sp,
                  color = SuyanchDeepGreen
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}

@Composable
fun MetricItem(title: String, value: String, subtitle: String) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = value,
      fontFamily = ManropeFontFamily,
      fontWeight = FontWeight.ExtraBold,
      fontSize = 19.sp,
      color = SuyanchDeepGreen
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = title,
      fontFamily = ManropeFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 13.sp,
      color = Color(0xFF142923)
    )
    Text(
      text = subtitle,
      fontFamily = ManropeFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 11.5.sp,
      color = Color(0xFF263C35)
    )
  }
}

@Composable
fun VerificationItem(title: String, passed: Boolean) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 5.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = if (passed) Icons.Default.CheckCircle else Icons.Default.Warning,
      contentDescription = null,
      tint = if (passed) SuyanchStatusOkText else Color(0xFFB5533D),
      modifier = Modifier.size(18.dp)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = title,
      fontFamily = ManropeFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 13.5.sp,
      color = if (passed) Color(0xFF142923) else Color(0xFF7A3629)
    )
  }
}

@Composable
fun BioLangTab(label: String, selected: Boolean, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(10.dp))
      .background(if (selected) SuyanchDeepGreen else Color.Transparent)
      .padding(horizontal = 10.dp, vertical = 5.dp)
  ) {
    Text(
      text = label,
      fontFamily = ManropeFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 12.sp,
      color = if (selected) Color.White else SuyanchDeepGreen
    )
  }
}
