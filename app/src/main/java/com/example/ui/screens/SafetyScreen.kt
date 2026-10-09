package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Tier
import com.example.ui.components.SuyanchCard
import com.example.ui.components.SuyanchSeal
import com.example.ui.components.TierBadge
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchStatusWarnBg

@Composable
fun SafetyScreen(modifier: Modifier = Modifier) {
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
      Column {
        Text(
          text = "Xavfsizlik va «Kalit» kafolati",
          fontFamily = FrauncesFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 22.sp,
          color = Color.White
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
          text = "«Suyanch» platformasining asosiy va'dasi — xonadoningiz xavfsizligi va mutlaq ishonchdir.",
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          lineHeight = 18.sp,
          color = Color(0xFFC7D7D2)
        )
      }
    }

    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
      // Kalit Banner
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(SuyanchStatusWarnBg)
        .border(1.5.dp, SuyanchGold, RoundedCornerShape(16.dp))
        .padding(16.dp)
    ) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          SuyanchSeal(tier = Tier.GOLD, size = 46.dp)
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = "«Kalit» maqomi qanday beriladi?",
              fontFamily = FrauncesFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp,
              color = SuyanchDeepGreen
            )
            Text(
              text = "Uyda egasisiz ishlash huquqi",
              fontFamily = ManropeFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = Color(0xFF263C35)
            )
          }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "«Kalit» maqomini birorta ham xodim o'z xohishi bilan sotib ololmaydi. U faqat barcha tekshiruvlardan o'tib, rasmiy sug'urta polisi rasmiylashtirilgan va «Suyanch»da shikoyatsiz kamida 20 ta buyurtmani a'lo bahoga bajargandan so'ng server tomonidan avtomatik beriladi.",
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 13.sp,
          lineHeight = 18.sp,
          color = Color(0xFF142923)
        )
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // 3 Tiers Explained
    Text(
      text = "Ustalarning 3 ta tekshiruv darajasi",
      fontFamily = FrauncesFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 18.sp,
      color = SuyanchDeepGreen
    )

    Spacer(modifier = Modifier.height(10.dp))

    TierCard(
      tier = Tier.GOLD,
      title = "«Kalit» (Oltin maqom)",
      checks = "Barcha tekshiruvlar + Sug'urta + 20 ta shikoyatsiz buyurtma",
      canStayAlone = true,
      desc = "Uy egasi bo'lmaganida xonadonga kirish huquqiga ega yagona maqom. To'liq sug'urtalangan."
    )

    Spacer(modifier = Modifier.height(10.dp))

    TierCard(
      tier = Tier.SILVER,
      title = "Kumush maqom",
      checks = "Pasport, IIV sudlanmaganlik ma'lumotnomasi, tavsiyanomalar, suhbat va imtihon",
      canStayAlone = false,
      desc = "Barcha malaka va xavfsizlik suhbatlaridan o'tgan, lekin faqat egasi uyda bo'lganda ishlay oladi."
    )

    Spacer(modifier = Modifier.height(10.dp))

    TierCard(
      tier = Tier.BASIC,
      title = "Asosiy (Bazaviy) maqom",
      checks = "Shaxsiy pasport guvohnomasi tekshirilgan",
      canStayAlone = false,
      desc = "Platformadagi yangi mutaxassis. Xonadonda faqat egasining bevosita nazorati ostida ishlaydi."
    )

    Spacer(modifier = Modifier.height(20.dp))

    // 5-step visit control
    Text(
      text = "Xavfsiz tashrif mexanizmi",
      fontFamily = FrauncesFontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 18.sp,
      color = SuyanchDeepGreen
    )

    Spacer(modifier = Modifier.height(10.dp))

    SuyanchCard {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SafetyRuleItem(
          icon = Icons.Default.Lock,
          title = "1. Bir martalik 4 xonali PIN kod",
          desc = "Har bir tashrif uchun serverda random PIN yaratiladi va faqat uy egasiga ko'rsatiladi."
        )
        SafetyRuleItem(
          icon = Icons.Default.CameraAlt,
          title = "2. Jonli selfie va kadrlar",
          desc = "Usta kelgach PIN kiritadi va faqat jonli kamera orqali selfie oladi (galereyadan rasm taqiqlangan)."
        )
        SafetyRuleItem(
          icon = Icons.Default.Shield,
          title = "3. Ishdan oldingi va keyingi 3 ta surat",
          desc = "Usta ishni boshlashdan oldin kamida 3 ta, tugatgach yana 3 ta surat yuklashi majburiy."
        )
        SafetyRuleItem(
          icon = Icons.Default.Payment,
          title = "4. To'lovni muzlatib turish (Hold)",
          desc = "Mijoz 'Hammasi a'lo' deb tasdiqlamaguncha pul usta hisobiga o'tmaydi. 48 soat ichida nizo ochish mumkin."
        )
        SafetyRuleItem(
          icon = Icons.Default.Security,
          title = "5. Qora ro'yxat va operator nazorati",
          desc = "Har qanday qonunbuzarlikda xodim butunlay bloklanadi va pasporti xashlangan qora ro'yxatga kiritiladi."
        )
      }
    }
  }
}
}

@Composable
fun TierCard(
  tier: Tier,
  title: String,
  checks: String,
  canStayAlone: Boolean,
  desc: String
) {
  SuyanchCard(
    borderColor = if (tier == Tier.GOLD) SuyanchGold.copy(alpha = 0.8f) else SuyanchLightLine
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          SuyanchSeal(tier = tier, size = 34.dp)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = title,
            fontFamily = FrauncesFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = SuyanchDeepGreen
          )
        }
        TierBadge(tier = tier, sealSize = 16.dp)
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = checks,
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.5.sp,
        color = Color(0xFF142923)
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = desc,
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.5.sp,
        lineHeight = 18.sp,
        color = Color(0xFF263C35)
      )

      Spacer(modifier = Modifier.height(10.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(if (canStayAlone) Color(0xFFE5EDE8) else Color(0xFFF9EDE9))
          .border(
            width = 1.dp,
            color = if (canStayAlone) Color(0xFFB0D4C5) else Color(0xFFF0CBC5),
            shape = RoundedCornerShape(8.dp)
          )
          .padding(horizontal = 10.dp, vertical = 6.dp)
      ) {
        Text(
          text = if (canStayAlone) "✓ Uyda yolg'iz qolishga ruxsat bor" else "✕ Faqat egasi uyda bo'lganda",
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 12.5.sp,
          color = if (canStayAlone) Color(0xFF1E6B4A) else Color(0xFF8B2B1B)
        )
      }
    }
  }
}

@Composable
fun SafetyRuleItem(icon: ImageVector, title: String, desc: String) {
  Row(verticalAlignment = Alignment.Top) {
    Box(
      modifier = Modifier
        .size(40.dp)
        .clip(CircleShape)
        .background(Color(0xFFE5EDE8)),
      contentAlignment = Alignment.Center
    ) {
      Icon(icon, contentDescription = null, tint = SuyanchDeepGreen, modifier = Modifier.size(20.dp))
    }
    Spacer(modifier = Modifier.width(12.dp))
    Column {
      Text(title, fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 14.5.sp, color = SuyanchDeepGreen)
      Spacer(modifier = Modifier.height(2.dp))
      Text(desc, fontFamily = ManropeFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 12.5.sp, lineHeight = 18.sp, color = Color(0xFF1D332B))
    }
  }
}
