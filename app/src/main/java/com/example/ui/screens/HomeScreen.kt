package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.ui.components.ButtonVariant
import com.example.ui.components.CategoryPillSelector
import com.example.ui.components.StarRatingBadge
import com.example.ui.components.SuyanchButton
import com.example.ui.components.SuyanchCard
import com.example.ui.components.SuyanchChip
import com.example.ui.components.SuyanchSeal
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
import com.example.ui.theme.SuyanchStatusWarnBg

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
  repository: SuyanchRepository,
  onSelectCategory: (CategoryId) -> Unit,
  onOpenWorker: (Worker) -> Unit,
  onOpenSearch: (onlyKalit: Boolean, category: CategoryId?, query: String) -> Unit,
  onOpenSafety: () -> Unit,
  modifier: Modifier = Modifier
) {
  val selectedCity by repository.selectedCity.collectAsState()
  val allWorkers by repository.workers.collectAsState()

  var willBeHome by remember { mutableStateOf(true) }
  var showCityDialog by remember { mutableStateOf(false) }
  var homeSearchQuery by remember { mutableStateOf("") }
  var selectedCategoryFilter by remember { mutableStateOf<CategoryId?>(null) }

  // Filtered categories based on search query
  val filteredCategories = remember(homeSearchQuery, selectedCategoryFilter) {
    CategoryId.values().filter { cat ->
      val matchesCategoryFilter = selectedCategoryFilter == null || cat == selectedCategoryFilter
      val catNameUz = when (cat) {
        CategoryId.CARE -> "Enaga va parvarish"
        CategoryId.PLUMBER -> "Santexnika xizmati"
        CategoryId.ELECTRIC -> "Elektr montaj"
        CategoryId.HANDYMAN -> "Mayda ta'mirlash (Usta)"
        CategoryId.COOK -> "Uy oshpazi"
        CategoryId.CLEANING -> "Klining va tozalash"
      }
      val skillsForCat = ServiceCatalog.skills.filter { it.categoryId == cat }
      val matchesQuery = homeSearchQuery.isBlank() ||
        catNameUz.contains(homeSearchQuery, ignoreCase = true) ||
        cat.id.contains(homeSearchQuery, ignoreCase = true) ||
        skillsForCat.any { skill ->
          skill.id.contains(homeSearchQuery, ignoreCase = true)
        }
      matchesCategoryFilter && matchesQuery
    }
  }

  // Filtered workers on Home when searching
  val filteredWorkers = remember(homeSearchQuery, selectedCategoryFilter, allWorkers) {
    if (homeSearchQuery.isBlank() && selectedCategoryFilter == null) {
      emptyList()
    } else {
      repository.getFilteredWorkers(
        category = selectedCategoryFilter,
        onlyKalit = !willBeHome,
        searchQuery = homeSearchQuery
      )
    }
  }

  // Featured Gold ("Kalit") workers
  val goldWorkers = remember(allWorkers) {
    allWorkers.filter { it.tier == Tier.GOLD }.take(8)
  }

  // Category worker counts
  val categoryCounts = remember(allWorkers) {
    CategoryId.values().associateWith { cat ->
      allWorkers.count { it.categoryId == cat }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(bottom = 80.dp)
  ) {
    // Top Bar Banner
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(SuyanchDeepGreen)
        .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            SuyanchSeal(tier = Tier.GOLD, size = 36.dp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Suyanch",
                fontFamily = FrauncesFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.White
              )
              Text(
                text = appString(StringKey.APP_TAGLINE),
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                color = Color(0xFFC7D7D2)
              )
            }
          }

          // Interactive City Selector Badge
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(14.dp))
              .background(Color(0xFF274D42))
              .clickable { showCityDialog = true }
              .padding(horizontal = 12.dp, vertical = 7.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = SuyanchGold,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = selectedCity.displayNameUz,
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color.White
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Quick Search Card with Interactive Search Bar
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
        ) {
          Column {
            // Interactive Search Bar (filter by service name or category)
            SuyanchTextField(
              value = homeSearchQuery,
              onValueChange = { homeSearchQuery = it },
              placeholder = "Xizmat nomi yoki toifani qidiring...",
              leadingIcon = {
                Icon(
                  imageVector = Icons.Default.Search,
                  contentDescription = null,
                  tint = SuyanchDeepGreen,
                  modifier = Modifier.size(20.dp)
                )
              },
              trailingIcon = if (homeSearchQuery.isNotEmpty()) {
                {
                  IconButton(onClick = { homeSearchQuery = "" }) {
                    Icon(
                      imageVector = Icons.Default.Close,
                      contentDescription = "Tozalash",
                      tint = Color(0xFF6B7B75),
                      modifier = Modifier.size(18.dp)
                    )
                  }
                }
              } else null,
              singleLine = true,
              testTag = "home_search_bar"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Horizontal Scrolling Category Pill Selector (Plumbing, Cleaning, Electrician, etc.)
            CategoryPillSelector(
              selectedCategory = selectedCategoryFilter,
              onSelectCategory = { cat -> selectedCategoryFilter = cat },
              categoryCounts = categoryCounts,
              modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Will you be home? Toggle
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF2F5F1))
                .padding(6.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Uyda bo'lasizmi?",
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = SuyanchDeepGreen,
                modifier = Modifier.padding(start = 6.dp)
              )

              Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (willBeHome) SuyanchDeepGreen else Color(0xFFE4EAE6))
                    .border(1.dp, if (willBeHome) SuyanchDeepGreen else Color(0xFFC7D5CF), RoundedCornerShape(10.dp))
                    .clickable { willBeHome = true }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                  Text(
                    text = "Ha",
                    fontFamily = ManropeFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (willBeHome) Color.White else Color(0xFF334842)
                  )
                }

                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (!willBeHome) Color(0xFFD4A346) else Color(0xFFE4EAE6))
                    .border(1.dp, if (!willBeHome) Color(0xFFB58428) else Color(0xFFC7D5CF), RoundedCornerShape(10.dp))
                    .clickable { willBeHome = false }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.Key,
                      contentDescription = null,
                      tint = if (!willBeHome) Color(0xFF0F2620) else Color(0xFF334842),
                      modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "Yo'q (Kalit)",
                      fontFamily = ManropeFontFamily,
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp,
                      color = if (!willBeHome) Color(0xFF0F2620) else Color(0xFF334842)
                    )
                  }
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Prominent Search CTA Button (goes to catalog with active filters)
            SuyanchButton(
              text = if (homeSearchQuery.isNotBlank()) "Katalogda ko'rish (${filteredWorkers.size} usta)" else if (willBeHome) "Barcha mutaxassislarni ko'rish" else "«Kalit» mutaxassislarini qidirish",
              onClick = { onOpenSearch(!willBeHome, selectedCategoryFilter, homeSearchQuery) },
              variant = if (willBeHome) ButtonVariant.PRIMARY else ButtonVariant.GOLD,
              leadingIcon = {
                Icon(
                  imageVector = if (willBeHome) Icons.Default.Search else Icons.Default.Key,
                  contentDescription = null,
                  tint = if (willBeHome) Color.White else Color(0xFF0F2620),
                  modifier = Modifier.size(20.dp)
                )
              }
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Kalit Explainer Banner
    Box(
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(SuyanchStatusWarnBg)
        .border(1.dp, SuyanchGold.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
        .clickable(onClick = onOpenSafety)
        .padding(16.dp)
    ) {
      Row(verticalAlignment = Alignment.Top) {
        SuyanchSeal(tier = Tier.GOLD, size = 44.dp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = appString(StringKey.KALIT_TITLE),
              fontFamily = FrauncesFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = SuyanchDeepGreen
            )
            Spacer(modifier = Modifier.width(6.dp))
            TierBadge(tier = Tier.GOLD, sealSize = 14.dp)
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = appString(StringKey.KALIT_SUBTITLE),
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = Color(0xFF344840)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Category Tiles Header Bar
    Box(
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(SuyanchDeepGreen)
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = if (homeSearchQuery.isNotBlank() || selectedCategoryFilter != null) "Filtrlangan xizmatlar" else "Xizmat turlari",
            fontFamily = FrauncesFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.White
          )
          if (homeSearchQuery.isNotBlank() || selectedCategoryFilter != null) {
            Text(
              text = "${filteredCategories.size} ta xizmat toifasi topildi",
              fontFamily = ManropeFontFamily,
              fontWeight = FontWeight.Medium,
              fontSize = 12.sp,
              color = Color(0xFFC7D7D2)
            )
          }
        }

        if (homeSearchQuery.isNotBlank() || selectedCategoryFilter != null) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(Color(0xFF274D42))
              .clickable {
                homeSearchQuery = ""
                selectedCategoryFilter = null
              }
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Text(
              text = "Filtrni tozalash",
              fontFamily = ManropeFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              color = SuyanchGold
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    if (filteredCategories.isEmpty()) {
      Box(
        modifier = Modifier
          .padding(horizontal = 16.dp)
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(Color.White)
          .border(1.dp, SuyanchLightLine, RoundedCornerShape(16.dp))
          .padding(24.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = Color(0xFF8BA098),
            modifier = Modifier.size(40.dp)
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "«$homeSearchQuery» bo'yicha xizmat topilmadi",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = SuyanchDeepGreen
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Boshqa kalit so'z yoki toifani qidirib ko'ring",
            fontFamily = ManropeFontFamily,
            fontSize = 12.sp,
            color = Color(0xFF6B7B75)
          )
        }
      }
    } else {
      FlowRow(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        maxItemsInEachRow = 2
      ) {
        filteredCategories.forEach { category ->
          val count = allWorkers.count { it.categoryId == category }
          CategoryTile(
            category = category,
            workerCount = count,
            onClick = { onSelectCategory(category) },
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Featured Gold ("Kalit") or Search Results Workers Header Bar
    val isFiltering = homeSearchQuery.isNotBlank() || selectedCategoryFilter != null
    val displayWorkers = if (isFiltering) filteredWorkers else goldWorkers

    Box(
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(SuyanchDeepGreen)
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      Column {
        Text(
          text = if (isFiltering) "Mos keluvchi mutaxassislar (${displayWorkers.size})" else "«Kalit» maqomidagi mutaxassislar",
          fontFamily = FrauncesFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp,
          color = Color.White
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = if (isFiltering) "Qidiruv parametrlariga mos ustalar" else "Uyingiz kalitini xotirjam topshirishingiz mumkin",
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 12.sp,
          color = Color(0xFFC7D7D2)
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    if (displayWorkers.isEmpty()) {
      Box(
        modifier = Modifier
          .padding(horizontal = 16.dp)
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .background(Color.White)
          .border(1.dp, SuyanchLightLine, RoundedCornerShape(14.dp))
          .padding(16.dp),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "Ushbu parametrlar bo'yicha mutaxassis topilmadi.",
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.Medium,
          fontSize = 13.sp,
          color = Color(0xFF5B7169)
        )
      }
    } else {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(displayWorkers) { worker ->
          FeaturedWorkerCard(worker = worker, onClick = { onOpenWorker(worker) })
        }
      }
    }
  }

  // City Picker Dialog
  if (showCityDialog) {
    AlertDialog(
      onDismissRequest = { showCityDialog = false },
      title = {
        Text("Shaharni tanlang", fontFamily = FrauncesFontFamily, fontWeight = FontWeight.Bold, color = SuyanchDeepGreen)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          City.values().forEach { city ->
            val isSelected = city == selectedCity
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (isSelected) Color(0xFFE5EDE8) else Color(0xFFF6F8F5))
                .border(1.dp, if (isSelected) SuyanchDeepGreen else SuyanchLightLine, RoundedCornerShape(10.dp))
                .clickable {
                  repository.setCity(city)
                  showCityDialog = false
                }
                .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = city.displayNameUz,
                  fontFamily = ManropeFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  color = SuyanchDeepGreen
                )
                if (isSelected) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF1E6B4A), modifier = Modifier.size(18.dp))
                }
              }
            }
          }
        }
      },
      confirmButton = {
        SuyanchButton(
          text = "Yopish",
          onClick = { showCityDialog = false },
          variant = ButtonVariant.OUTLINED,
          fullWidth = false
        )
      }
    )
  }
}

@Composable
fun CategoryTile(
  category: CategoryId,
  workerCount: Int,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val icon: ImageVector = when (category) {
    CategoryId.CARE -> Icons.Default.MedicalServices
    CategoryId.PLUMBER -> Icons.Default.Plumbing
    CategoryId.ELECTRIC -> Icons.Default.ElectricBolt
    CategoryId.HANDYMAN -> Icons.Default.Handyman
    CategoryId.COOK -> Icons.Default.Restaurant
    CategoryId.CLEANING -> Icons.Default.CleaningServices
  }

  SuyanchCard(
    modifier = modifier,
    onClick = onClick,
    cornerRadius = 16.dp,
    borderColor = SuyanchLightLine,
    containerColor = Color.White,
    elevation = 2.dp,
    testTag = "category_tile_${category.id}"
  ) {
    Column {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(Color(0xFFE5EDE8)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = SuyanchDeepGreen,
          modifier = Modifier.size(24.dp)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = appString(category.titleKey),
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
        color = SuyanchDeepGreen
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "$workerCount nafar usta",
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Color(0xFF263C35)
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "dan ",
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = Color(0xFF1D3D34)
        )
        Text(
          text = "${ServiceCatalog.formatPrice(category.defaultStartingPrice, category.priceFormat)} so'm",
          style = SuyanchMonoStyles.price,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 16.sp,
          color = SuyanchGold
        )
      }
    }
  }
}

@Composable
fun FeaturedWorkerCard(
  worker: Worker,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .width(225.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(Color.White)
      .border(1.dp, SuyanchGold.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .padding(14.dp)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(SuyanchDeepGreen),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = worker.fullName.take(2).uppercase(),
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            color = SuyanchGold,
            fontSize = 16.sp
          )
        }

        TierBadge(tier = Tier.GOLD, sealSize = 16.dp)
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = worker.fullName,
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = SuyanchDeepGreen,
        maxLines = 1
      )

      Spacer(modifier = Modifier.height(3.dp))

      // Star Rating System
      StarRatingBadge(
        rating = worker.ratingAvg,
        reviewsCount = worker.reviewsCount,
        showStars = true,
        starSize = 13.dp,
        fontSize = 13.sp,
        showReviewCount = true,
        showConfidenceLabel = false
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = "${ServiceCatalog.formatPrice(worker.price, worker.priceFormat)} so'm",
          style = SuyanchMonoStyles.price,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 16.sp,
          color = SuyanchGold
        )
        Text(
          text = " / ${appString(worker.priceFormat.suffixKey)}",
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = Color(0xFF142923)
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      SuyanchButton(
        text = "Tanlash",
        onClick = onClick,
        variant = ButtonVariant.GOLD,
        modifier = Modifier.fillMaxWidth()
      )
    }
  }
}
