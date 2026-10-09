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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.model.CategoryId
import com.example.model.City
import com.example.model.ServiceCatalog
import com.example.model.Tier
import com.example.model.Worker
import com.example.repository.SuyanchRepository
import com.example.ui.components.CategoryPillSelector
import com.example.ui.components.StarRatingBadge
import com.example.ui.components.SuyanchChip
import com.example.ui.components.SuyanchTextField
import com.example.ui.components.TierBadge
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.IbmPlexMonoFontFamily
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchLightSurface
import com.example.ui.theme.SuyanchMonoStyles
import com.example.ui.theme.SuyanchStatusOkText
import com.example.ui.theme.SuyanchStatusWarnBg

@Composable
fun SearchScreen(
  repository: SuyanchRepository,
  initialCategory: CategoryId? = null,
  initialOnlyKalit: Boolean = false,
  initialSearchQuery: String = "",
  onOpenWorker: (Worker) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember(initialSearchQuery) { mutableStateOf(initialSearchQuery) }
  var selectedCategory by remember(initialCategory) { mutableStateOf<CategoryId?>(initialCategory) }
  var filterOnlyKalit by remember(initialOnlyKalit) { mutableStateOf(initialOnlyKalit) }
  var selectedCityFilter by remember { mutableStateOf<City?>(null) }

  val workers = remember(searchQuery, selectedCategory, filterOnlyKalit, selectedCityFilter) {
    repository.getFilteredWorkers(
      category = selectedCategory,
      onlyKalit = filterOnlyKalit,
      city = selectedCityFilter,
      searchQuery = searchQuery
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF6F8F5))
  ) {
    // Search Header
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .background(SuyanchDeepGreen)
        .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
      Text(
        text = "Mutaxassislar katalogi",
        fontFamily = FrauncesFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = Color.White
      )
      Spacer(modifier = Modifier.height(3.dp))
      Text(
        text = "Rasmiy tekshiruvdan o'tgan ustalar va xizmatlar",
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 13.sp,
        color = Color(0xFFC7D7D2)
      )

      Spacer(modifier = Modifier.height(12.dp))

      SuyanchTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = appString(StringKey.SEARCH_PLACEHOLDER),
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = null, tint = SuyanchDeepGreen)
        },
        trailingIcon = if (searchQuery.isNotEmpty()) {
          {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(Icons.Default.Close, contentDescription = "Tozalash", tint = Color(0xFF6B7B75))
            }
          }
        } else null,
        singleLine = true,
        testTag = "catalog_search_input"
      )
    }

    // Horizontal Scrolling Category Pill Selector (Plumbing, Cleaning, Electrician, etc.)
    CategoryPillSelector(
      selectedCategory = selectedCategory,
      onSelectCategory = { selectedCategory = it },
      contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
    )

    // Sub-filters: "Faqat Kalit" & Cities
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(horizontal = 16.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      item {
        SuyanchChip(
          label = "Faqat «Kalit»",
          selected = filterOnlyKalit,
          onClick = { filterOnlyKalit = !filterOnlyKalit },
          isGoldHighlighted = true,
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Key,
              contentDescription = null,
              tint = if (filterOnlyKalit) SuyanchDeepGreen else SuyanchGold,
              modifier = Modifier.size(14.dp)
            )
          }
        )
      }

      items(City.values()) { city ->
        SuyanchChip(
          label = city.displayNameUz,
          selected = selectedCityFilter == city,
          onClick = { selectedCityFilter = if (selectedCityFilter == city) null else city }
        )
      }
    }

    // Results Counter
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "${workers.size} ta mutaxassis topildi",
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = Color(0xFF263C35)
      )
      Text(
        text = "Saralash: «Kalit» birinchi",
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        color = Color(0xFF4B6058)
      )
    }

    // Worker List / Empty State
    if (workers.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(24.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = Color(0xFFA5B4AE),
            modifier = Modifier.size(48.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "Hech qanday mutaxassis topilmadi",
            fontFamily = FrauncesFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = SuyanchDeepGreen
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Filtrlarni tozalab ko'ring yoki boshqa so'rov kiriting",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = Color(0xFF4B5E57)
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(workers, key = { it.id }) { worker ->
          WorkerCard(
            worker = worker,
            onClick = { onOpenWorker(worker) }
          )
        }
        item {
          Spacer(modifier = Modifier.height(80.dp))
        }
      }
    }
  }
}

@Composable
fun WorkerCard(
  worker: Worker,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(Color.White)
      .border(
        width = if (worker.tier == Tier.GOLD) 1.5.dp else 1.dp,
        color = if (worker.tier == Tier.GOLD) SuyanchGold.copy(alpha = 0.9f) else SuyanchLightLine,
        shape = RoundedCornerShape(16.dp)
      )
      .clickable(onClick = onClick)
      .padding(15.dp)
      .testTag("worker_card_${worker.id}")
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(if (worker.tier == Tier.GOLD) SuyanchDeepGreen else Color(0xFF38524B))
              .border(
                width = if (worker.tier == Tier.GOLD) 2.dp else 1.dp,
                color = if (worker.tier == Tier.GOLD) SuyanchGold else Color(0xFF6B8B82),
                shape = CircleShape
              ),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = worker.fullName.take(2).uppercase(),
              fontFamily = ManropeFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 20.sp,
              color = if (worker.tier == Tier.GOLD) SuyanchGold else Color.White
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = worker.fullName,
              fontFamily = FrauncesFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = SuyanchDeepGreen
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Star Rating System
            StarRatingBadge(
              rating = worker.ratingAvg,
              reviewsCount = worker.reviewsCount,
              showStars = true,
              starSize = 13.dp,
              fontSize = 13.5.sp,
              showConfidenceLabel = true
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
              text = "📍 ${worker.city.displayNameUz} • ${worker.experienceYears} yil tajriba",
              fontFamily = ManropeFontFamily,
              fontWeight = FontWeight.SemiBold,
              fontSize = 12.sp,
              color = Color(0xFF4A5F56)
            )
          }
        }

        TierBadge(tier = worker.tier, sealSize = 18.dp)
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = worker.bioUz,
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        color = Color(0xFF142923),
        maxLines = 2
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Trust line: Can work while owner away or not
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(8.dp))
          .background(if (worker.tier == Tier.GOLD) SuyanchStatusWarnBg else Color(0xFFE5EDE8))
          .border(
            width = if (worker.tier == Tier.GOLD) 1.dp else 0.dp,
            color = if (worker.tier == Tier.GOLD) SuyanchGold.copy(alpha = 0.5f) else Color.Transparent,
            shape = RoundedCornerShape(8.dp)
          )
          .padding(horizontal = 10.dp, vertical = 6.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = if (worker.tier == Tier.GOLD) Icons.Default.Key else Icons.Default.VerifiedUser,
            contentDescription = null,
            tint = if (worker.tier == Tier.GOLD) SuyanchDeepGreen else Color(0xFF1D3D34),
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (worker.tier == Tier.GOLD)
              appString(StringKey.CAN_WORK_ALONE)
            else
              appString(StringKey.CANNOT_WORK_ALONE),
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 12.5.sp,
            color = if (worker.tier == Tier.GOLD) SuyanchDeepGreen else Color(0xFF1D3D34)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Price & experience bottom row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "${worker.experienceYears} yil tajriba • ${worker.completedOrders} buyurtma",
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 13.5.sp,
          color = Color(0xFF142923)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "${ServiceCatalog.formatPrice(worker.price, worker.priceFormat)} so'm",
            style = SuyanchMonoStyles.price,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            color = SuyanchGold
          )
          Text(
            text = " / ${appString(worker.priceFormat.suffixKey)}",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color(0xFF263C35)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // High-visibility action button
      com.example.ui.components.SuyanchButton(
        text = if (worker.tier == Tier.GOLD) "«Kalit» bilan tanlash" else "Profilni ko'rish",
        onClick = onClick,
        variant = if (worker.tier == Tier.GOLD) com.example.ui.components.ButtonVariant.GOLD else com.example.ui.components.ButtonVariant.PRIMARY,
        leadingIcon = if (worker.tier == Tier.GOLD) {
          {
            Icon(
              imageVector = Icons.Default.Key,
              contentDescription = null,
              tint = Color(0xFF0F2620),
              modifier = Modifier.size(18.dp)
            )
          }
        } else null
      )
    }
  }
}
