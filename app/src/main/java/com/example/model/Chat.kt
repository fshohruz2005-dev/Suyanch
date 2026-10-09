package com.example.model

data class ChatMessage(
  val id: String,
  val workerId: String,
  val senderName: String,
  val text: String,
  val timestamp: String,
  val isFromClient: Boolean
)

object ChatSeedData {
  fun getInitialMessages(workerId: String, workerName: String): List<ChatMessage> {
    return listOf(
      ChatMessage(
        id = "m1",
        workerId = workerId,
        senderName = workerName,
        text = "Assalomu alaykum! Xizmat bo'yicha qanday savollaringiz bor? Sizga yordam berishdan mamnunman.",
        timestamp = "09:15",
        isFromClient = false
      ),
      ChatMessage(
        id = "m2",
        workerId = workerId,
        senderName = "Mijoz",
        text = "Assalomu alaykum! Bugun soat 10:00 da kelishga imkoniyatingiz bormi?",
        timestamp = "09:18",
        isFromClient = true
      ),
      ChatMessage(
        id = "m3",
        workerId = workerId,
        senderName = workerName,
        text = "Ha, albatta! Barcha asbob-uskunalarim tayyor, aytilgan vaqtda manzilga yetib boraman.",
        timestamp = "09:20",
        isFromClient = false
      )
    )
  }
}
