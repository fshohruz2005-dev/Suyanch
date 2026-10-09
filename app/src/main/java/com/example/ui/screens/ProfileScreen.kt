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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Work
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
import com.example.core.i18n.AppLanguage
import com.example.core.i18n.LocaleManager
import com.example.model.City
import com.example.repository.SuyanchRepository
import com.example.repository.UserRole
import com.example.ui.components.ButtonVariant
import com.example.ui.components.SuyanchButton
import com.example.ui.components.SuyanchCard
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchLightSurface

@Composable
fun ProfileScreen(
  repository: SuyanchRepository,
  modifier: Modifier = Modifier
) {
  val currentRole by repository.currentRole.collectAsState()
  val selectedCity by repository.selectedCity.collectAsState()
  val currentLanguage by LocaleManager.currentLanguage.collectAsState()

  var showCityPicker by remember { mutableStateOf(false) }

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
          text = "Mening profilim",
          fontFamily = FrauncesFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 22.sp,
          color = Color.White
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
          text = "Foydalanuvchi sozlamalari va shaxsiy ma'lumotlar",
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
        .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
      // User Avatar Card
    SuyanchCard {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(62.dp)
            .clip(CircleShape)
            .background(SuyanchDeepGreen)
            .border(2.dp, SuyanchGold, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Person, contentDescription = null, tint = SuyanchGold, modifier = Modifier.size(32.dp))
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
          Text(
            text = "Asadbek Istamov",
            fontFamily = FrauncesFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = SuyanchDeepGreen
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "+998 90 123 45 67",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.5.sp,
            color = Color(0xFF142923)
          )
          Spacer(modifier = Modifier.height(3.dp))
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(Color(0xFFE5EDE8))
              .padding(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Text(
              text = "Joriy rejim: ${when(currentRole) { UserRole.CLIENT -> "Mijoz"; UserRole.WORKER -> "Usta"; UserRole.OPERATOR -> "Operator" }}",
              fontFamily = ManropeFontFamily,
              fontWeight = FontWeight.ExtraBold,
              fontSize = 12.sp,
              color = Color(0xFF1E6B4A)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Role Switcher Card (Client <-> Worker <-> Operator)
    SuyanchCard {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = SuyanchDeepGreen, modifier = Modifier.size(22.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Foydalanuvchi rejimi",
            fontFamily = FrauncesFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = SuyanchDeepGreen
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Bitta akkaunt orqali buyurtmachi yoki usta sifatida ishlashingiz mumkin.",
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 12.5.sp,
          color = Color(0xFF263C35)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          RoleTab(
            label = "Mijoz",
            selected = currentRole == UserRole.CLIENT,
            onClick = { repository.switchRole(UserRole.CLIENT) },
            modifier = Modifier.weight(1f)
          )
          RoleTab(
            label = "Usta",
            selected = currentRole == UserRole.WORKER,
            onClick = { repository.switchRole(UserRole.WORKER) },
            modifier = Modifier.weight(1f)
          )
          RoleTab(
            label = "Operator",
            selected = currentRole == UserRole.OPERATOR,
            onClick = { repository.switchRole(UserRole.OPERATOR) },
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Preferences & Settings
    SuyanchCard {
      Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Language Switcher
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { LocaleManager.toggle() },
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Language, contentDescription = null, tint = SuyanchDeepGreen, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text("Ilova tili", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 14.5.sp, color = SuyanchDeepGreen)
          }
          Text(
            text = "${currentLanguage.displayName} (O'zgartirish)",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color(0xFF1E6B4A)
          )
        }

        HorizontalDivider(color = SuyanchLightLine)

        // City Selector
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { showCityPicker = !showCityPicker },
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = SuyanchDeepGreen, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text("Shahar / Hudud", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 14.5.sp, color = SuyanchDeepGreen)
          }
          Text(
            text = selectedCity.displayNameUz,
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = SuyanchDeepGreen
          )
        }

        if (showCityPicker) {
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            City.values().forEach { city ->
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(if (selectedCity == city) Color(0xFFE5EDE8) else Color(0xFFF6F8F5))
                  .clickable {
                    repository.setCity(city)
                    showCityPicker = false
                  }
                  .padding(10.dp)
              ) {
                Text(
                  text = city.displayNameUz,
                  fontFamily = ManropeFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = SuyanchDeepGreen
                )
              }
            }
          }
        }

        HorizontalDivider(color = SuyanchLightLine)

        // Saved Payment Cards
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CreditCard, contentDescription = null, tint = SuyanchDeepGreen, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text("Ulangan karta", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 14.5.sp, color = SuyanchDeepGreen)
          }
          Text("Uzcard •••• 8492", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF263C35))
        }

        HorizontalDivider(color = SuyanchLightLine)

        // Support Contacts
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Phone, contentDescription = null, tint = SuyanchDeepGreen, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text("Qo'llab-quvvatlash (24/7)", fontFamily = ManropeFontFamily, fontWeight = FontWeight.Bold, fontSize = 14.5.sp, color = SuyanchDeepGreen)
          }
          Text("1088 / +998 71 200 00 00", fontFamily = ManropeFontFamily, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = Color(0xFF1E6B4A))
        }
      }
    }
  }
}
}

@Composable
fun RoleTab(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .background(if (selected) SuyanchDeepGreen else Color(0xFFE5EDE8))
      .border(1.dp, if (selected) SuyanchDeepGreen else SuyanchLightLine, RoundedCornerShape(10.dp))
      .clickable(onClick = onClick)
      .padding(vertical = 10.dp),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = label,
      fontFamily = ManropeFontFamily,
      fontWeight = FontWeight.ExtraBold,
      fontSize = 13.sp,
      color = if (selected) Color.White else SuyanchDeepGreen
    )
  }
}
