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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.example.model.ChatMessage
import com.example.model.Tier
import com.example.model.Worker
import com.example.repository.SuyanchRepository
import com.example.ui.components.TierBadge
import com.example.ui.theme.FrauncesFontFamily
import com.example.ui.theme.IbmPlexMonoFontFamily
import com.example.ui.theme.ManropeFontFamily
import com.example.ui.theme.SuyanchDeepGreen
import com.example.ui.theme.SuyanchGold
import com.example.ui.theme.SuyanchLightLine
import com.example.ui.theme.SuyanchLightSurface

@Composable
fun ChatScreen(
  worker: Worker,
  repository: SuyanchRepository,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val messagesMap by repository.messages.collectAsState()
  val messages = remember(messagesMap, worker.id) {
    repository.getMessagesForWorker(worker.id, worker.fullName)
  }

  var inputText by remember { mutableStateOf("") }

  val suggestions = listOf(
    "Qachon kela olasiz?",
    "Asbob-uskunangiz bormi?",
    "Manzilni tasdiqlamoqchiman",
    "Narxi bo'yicha aniqlik kiritsak"
  )

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      Surface(
        color = SuyanchDeepGreen,
        shadowElevation = 4.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Ortga", tint = Color.White)
          }

          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(if (worker.tier == Tier.GOLD) Color(0xFF2C4F44) else Color(0xFF4A6860)),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = worker.fullName.take(2).uppercase(),
              fontFamily = ManropeFontFamily,
              fontWeight = FontWeight.Bold,
              color = if (worker.tier == Tier.GOLD) SuyanchGold else Color.White,
              fontSize = 15.sp
            )
          }

          Spacer(modifier = Modifier.width(10.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = worker.fullName,
              fontFamily = FrauncesFontFamily,
              fontWeight = FontWeight.Bold,
              fontSize = 16.sp,
              color = Color.White
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(7.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF4ADE80))
              )
              Spacer(modifier = Modifier.width(5.dp))
              Text(
                text = "Hozir tarmoqda",
                fontFamily = ManropeFontFamily,
                fontSize = 11.sp,
                color = Color(0xFFD4E3DE)
              )
            }
          }

          TierBadge(tier = worker.tier, sealSize = 14.dp)
        }
      }
    },
    bottomBar = {
      Surface(
        color = SuyanchLightSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, SuyanchLightLine)
      ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
          // Quick suggestion chips
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 8.dp)
          ) {
            items(suggestions) { s ->
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(Color(0xFFEAF1EC))
                  .border(1.dp, SuyanchLightLine, RoundedCornerShape(12.dp))
                  .clickable {
                    repository.sendChatMessage(worker.id, worker.fullName, s)
                  }
                  .padding(horizontal = 12.dp, vertical = 6.dp)
              ) {
                Text(
                  text = s,
                  fontFamily = ManropeFontFamily,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.5.sp,
                  color = SuyanchDeepGreen
                )
              }
            }
          }

          // Message input bar
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = inputText,
              onValueChange = { inputText = it },
              modifier = Modifier.weight(1f),
              placeholder = { Text("Xabar yozing...", fontFamily = ManropeFontFamily, fontSize = 14.sp) },
              shape = RoundedCornerShape(20.dp),
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SuyanchDeepGreen,
                unfocusedBorderColor = SuyanchLightLine,
                focusedContainerColor = Color(0xFFF7FAF8),
                unfocusedContainerColor = Color(0xFFF7FAF8)
              ),
              maxLines = 3
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
              onClick = {
                if (inputText.isNotBlank()) {
                  repository.sendChatMessage(worker.id, worker.fullName, inputText)
                  inputText = ""
                }
              },
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(SuyanchDeepGreen)
            ) {
              Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Yuborish", tint = Color.White, modifier = Modifier.size(20.dp))
            }
          }
        }
      }
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(Color(0xFFF3F6F2)),
      contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(messages, key = { it.id }) { msg ->
        ChatMessageBubble(message = msg)
      }
    }
  }
}

@Composable
fun ChatMessageBubble(message: ChatMessage) {
  val isMe = message.isFromClient

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
  ) {
    Box(
      modifier = Modifier
        .widthIn(max = 280.dp)
        .clip(
          RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = if (isMe) 16.dp else 4.dp,
            bottomEnd = if (isMe) 4.dp else 16.dp
          )
        )
        .background(if (isMe) SuyanchDeepGreen else Color.White)
        .border(1.dp, if (isMe) SuyanchDeepGreen else SuyanchLightLine, RoundedCornerShape(16.dp))
        .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
      Column {
        if (!isMe) {
          Text(
            text = message.senderName,
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = SuyanchGold
          )
          Spacer(modifier = Modifier.height(2.dp))
        }
        Text(
          text = message.text,
          fontFamily = ManropeFontFamily,
          fontWeight = FontWeight.SemiBold,
          fontSize = 14.sp,
          lineHeight = 20.sp,
          color = if (isMe) Color.White else Color(0xFF142923)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = message.timestamp,
          fontFamily = IbmPlexMonoFontFamily,
          fontWeight = FontWeight.Medium,
          fontSize = 11.sp,
          color = if (isMe) Color(0xFFC5D9D2) else Color(0xFF4A5C55),
          modifier = Modifier.align(Alignment.End)
        )
      }
    }
  }
}
