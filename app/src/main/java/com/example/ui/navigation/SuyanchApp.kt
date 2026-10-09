package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CategoryId
import com.example.model.Worker
import com.example.repository.SuyanchRepository
import com.example.repository.UserRole
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.BookingSheet
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyVisitsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SafetyScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.WorkerDashboardScreen
import com.example.ui.screens.WorkerProfileScreen
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchLightSurface

enum class ClientTab(val title: String, val icon: ImageVector) {
  HOME("Asosiy", Icons.Default.Home),
  SEARCH("Qidiruv", Icons.Default.Search),
  VISITS("Tashriflar", Icons.Default.EventNote),
  SAFETY("Xavfsizlik", Icons.Default.Shield),
  PROFILE("Profil", Icons.Default.Person)
}

enum class WorkerTab(val title: String, val icon: ImageVector) {
  TODAY("Bugun", Icons.Default.Work),
  PROFILE("Profil", Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuyanchApp(
  repository: SuyanchRepository,
  modifier: Modifier = Modifier
) {
  val currentRole by repository.currentRole.collectAsState()

  // Navigation states
  var currentClientTab by remember { mutableStateOf(ClientTab.HOME) }
  var currentWorkerTab by remember { mutableStateOf(WorkerTab.TODAY) }

  // Sub-screens
  var selectedWorkerForProfile by remember { mutableStateOf<Worker?>(null) }
  var selectedWorkerForChat by remember { mutableStateOf<Worker?>(null) }
  var selectedCategoryFilter by remember { mutableStateOf<CategoryId?>(null) }
  var onlyKalitFilter by remember { mutableStateOf(false) }
  var currentSearchQuery by remember { mutableStateOf("") }
  var bookingWorker by remember { mutableStateOf<Worker?>(null) }

  // Robust Back handling
  BackHandler(enabled = selectedWorkerForChat != null || selectedWorkerForProfile != null || (currentRole == UserRole.CLIENT && currentClientTab != ClientTab.HOME)) {
    if (selectedWorkerForChat != null) {
      selectedWorkerForChat = null
    } else if (selectedWorkerForProfile != null) {
      selectedWorkerForProfile = null
    } else if (currentRole == UserRole.CLIENT && currentClientTab != ClientTab.HOME) {
      currentClientTab = ClientTab.HOME
    }
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    bottomBar = {
      if (selectedWorkerForProfile == null && selectedWorkerForChat == null) {
        when (currentRole) {
          UserRole.CLIENT -> {
            NavigationBar(
              containerColor = SuyanchLightSurface,
              modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
              ClientTab.values().forEach { tab ->
                val selected = currentClientTab == tab
                NavigationBarItem(
                  selected = selected,
                  onClick = { currentClientTab = tab },
                  icon = {
                    Icon(
                      imageVector = tab.icon,
                      contentDescription = tab.title,
                      modifier = Modifier.size(22.dp)
                    )
                  },
                  label = {
                    Text(
                      text = tab.title,
                      fontFamily = ManropeFontFamily,
                      fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                      fontSize = 11.sp
                    )
                  },
                  colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SuyanchDeepGreen,
                    selectedTextColor = SuyanchDeepGreen,
                    indicatorColor = Color(0xFFE5EDE8),
                    unselectedIconColor = Color(0xFF6B7B75),
                    unselectedTextColor = Color(0xFF6B7B75)
                  )
                )
              }
            }
          }

          UserRole.WORKER -> {
            NavigationBar(
              containerColor = SuyanchLightSurface,
              modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
              WorkerTab.values().forEach { tab ->
                val selected = currentWorkerTab == tab
                NavigationBarItem(
                  selected = selected,
                  onClick = { currentWorkerTab = tab },
                  icon = {
                    Icon(
                      imageVector = tab.icon,
                      contentDescription = tab.title,
                      modifier = Modifier.size(22.dp)
                    )
                  },
                  label = {
                    Text(
                      text = tab.title,
                      fontFamily = ManropeFontFamily,
                      fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                      fontSize = 11.sp
                    )
                  },
                  colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SuyanchDeepGreen,
                    selectedTextColor = SuyanchDeepGreen,
                    indicatorColor = Color(0xFFE5EDE8),
                    unselectedIconColor = Color(0xFF6B7B75),
                    unselectedTextColor = Color(0xFF6B7B75)
                  )
                )
              }
            }
          }

          UserRole.OPERATOR -> {
            // Operator dashboard has dedicated top switcher
          }
        }
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      if (selectedWorkerForChat != null) {
        ChatScreen(
          worker = selectedWorkerForChat!!,
          repository = repository,
          onBack = { selectedWorkerForChat = null }
        )
      } else if (selectedWorkerForProfile != null) {
        WorkerProfileScreen(
          worker = selectedWorkerForProfile!!,
          onBack = { selectedWorkerForProfile = null },
          onBookNow = { bookingWorker = selectedWorkerForProfile },
          onOpenChat = { selectedWorkerForChat = selectedWorkerForProfile }
        )
      } else {
        when (currentRole) {
          UserRole.CLIENT -> {
            when (currentClientTab) {
              ClientTab.HOME -> {
                HomeScreen(
                  repository = repository,
                  onSelectCategory = { cat ->
                    selectedCategoryFilter = cat
                    onlyKalitFilter = false
                    currentSearchQuery = ""
                    currentClientTab = ClientTab.SEARCH
                  },
                  onOpenWorker = { worker ->
                    selectedWorkerForProfile = worker
                  },
                  onOpenSearch = { onlyKalit, category, query ->
                    selectedCategoryFilter = category
                    onlyKalitFilter = onlyKalit
                    currentSearchQuery = query
                    currentClientTab = ClientTab.SEARCH
                  },
                  onOpenSafety = {
                    currentClientTab = ClientTab.SAFETY
                  }
                )
              }

              ClientTab.SEARCH -> {
                SearchScreen(
                  repository = repository,
                  initialCategory = selectedCategoryFilter,
                  initialOnlyKalit = onlyKalitFilter,
                  initialSearchQuery = currentSearchQuery,
                  onOpenWorker = { worker ->
                    selectedWorkerForProfile = worker
                  }
                )
              }

              ClientTab.VISITS -> {
                MyVisitsScreen(
                  repository = repository,
                  onOpenChatWithWorker = { worker ->
                    selectedWorkerForChat = worker
                  }
                )
              }

              ClientTab.SAFETY -> {
                SafetyScreen()
              }

              ClientTab.PROFILE -> {
                ProfileScreen(repository = repository)
              }
            }
          }

          UserRole.WORKER -> {
            when (currentWorkerTab) {
              WorkerTab.TODAY -> WorkerDashboardScreen(
                repository = repository,
                onOpenClientChat = { worker ->
                  selectedWorkerForChat = worker
                }
              )
              WorkerTab.PROFILE -> ProfileScreen(repository = repository)
            }
          }

          UserRole.OPERATOR -> {
            AdminDashboardScreen(repository = repository)
          }
        }
      }

      // Booking Sheet Modal
      if (bookingWorker != null) {
        ModalBottomSheet(
          onDismissRequest = { bookingWorker = null },
          sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
          containerColor = SuyanchLightSurface
        ) {
          BookingSheet(
            worker = bookingWorker!!,
            repository = repository,
            onDismiss = { bookingWorker = null },
            onBookingSuccess = { pin ->
              bookingWorker = null
              selectedWorkerForProfile = null
              currentClientTab = ClientTab.VISITS
            },
            onShowKalitWorkers = {
              bookingWorker = null
              selectedWorkerForProfile = null
              onlyKalitFilter = true
              currentClientTab = ClientTab.SEARCH
            }
          )
        }
      }
    }
  }
}
