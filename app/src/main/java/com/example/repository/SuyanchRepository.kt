package com.example.repository

import com.example.model.Booking
import com.example.model.CategoryId
import com.example.model.ChatMessage
import com.example.model.ChatSeedData
import com.example.model.City
import com.example.model.PaymentMethod
import com.example.model.PaymentStatus
import com.example.model.Tier
import com.example.model.VerificationCheck
import com.example.model.VisitPinManager
import com.example.model.VisitPinVerificationResult
import com.example.model.VisitStatus
import com.example.model.Worker
import com.example.model.WorkerSeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class UserRole {
  CLIENT,
  WORKER,
  OPERATOR
}

data class VisitPhoto(
  val id: String,
  val phase: String, // "before" or "after"
  val description: String,
  val timestamp: String
)

data class VerificationQueueEntry(
  val id: String,
  val workerId: String,
  val workerName: String,
  val profession: String,
  val checkType: String,
  val submittedTime: String,
  var status: String = "PENDING" // "PENDING", "APPROVED", "REJECTED"
)

class SuyanchRepository {
  private val scope = CoroutineScope(Dispatchers.Default)

  private val _workers = MutableStateFlow<List<Worker>>(WorkerSeedData.workers)
  val workers: StateFlow<List<Worker>> = _workers.asStateFlow()

  private val _selectedCity = MutableStateFlow(City.TASHKENT)
  val selectedCity: StateFlow<City> = _selectedCity.asStateFlow()

  private val _currentRole = MutableStateFlow(UserRole.CLIENT)
  val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

  // Chat messages flow
  private val _messages = MutableStateFlow<Map<String, List<ChatMessage>>>(emptyMap())
  val messages: StateFlow<Map<String, List<ChatMessage>>> = _messages.asStateFlow()

  // Worker verification checks state
  private val _workerChecks = MutableStateFlow<Set<VerificationCheck>>(
    setOf(
      VerificationCheck.ID,
      VerificationCheck.CRIMINAL_RECORD,
      VerificationCheck.REFERENCES,
      VerificationCheck.INTERVIEW,
      VerificationCheck.SKILLS,
      VerificationCheck.INSURANCE,
      VerificationCheck.ORDERS_20
    )
  )
  val workerChecks: StateFlow<Set<VerificationCheck>> = _workerChecks.asStateFlow()

  // Visit photos for active visit
  private val _visitPhotos = MutableStateFlow<List<VisitPhoto>>(
    listOf(
      VisitPhoto("p1", "before", "Ish boshlanishidan oldin: quvurlar va kran holati", "10:05"),
      VisitPhoto("p2", "before", "Ish joyi va atrofidagi jihozlar holati", "10:06"),
      VisitPhoto("p3", "before", "Eski oqayotgan klapan detali", "10:07"),
      VisitPhoto("p4", "after", "Yangi germetik quvur montaji yakunlandi", "13:42"),
      VisitPhoto("p5", "after", "Yangi kran o'rnatildi, suv bosimi sinovdan o'tkazildi", "13:44"),
      VisitPhoto("p6", "after", "Ish maydoni toza va ozoda holatda qoldirildi", "13:45")
    )
  )
  val visitPhotos: StateFlow<List<VisitPhoto>> = _visitPhotos.asStateFlow()

  // Sample active booking in progress for demonstration
  private val initialBooking = Booking(
    id = "b-101",
    clientId = "client-user",
    workerId = "w8",
    workerName = "Farhod Rustamov",
    categoryId = CategoryId.PLUMBER,
    workerTier = Tier.GOLD,
    date = "Bugun (07-oktyabr)",
    startTime = "10:00",
    address = "Toshkent sh., Chilonzor 9, 24-uy",
    comment = "Oshxona kranidan suv sizib chiqmoqda, yangisiga almashtirish kerak.",
    paymentMethod = PaymentMethod.CARD,
    ownerPresent = false,
    status = VisitStatus.CHECKED_IN,
    amount = 150000L,
    pinCode = "4921",
    checkInTime = "10:02",
    paymentStatus = PaymentStatus.HELD,
    beforePhotosCount = 3,
    afterPhotosCount = 0
  )

  private val _bookings = MutableStateFlow<List<Booking>>(listOf(initialBooking))
  val bookings: StateFlow<List<Booking>> = _bookings.asStateFlow()

  // Verification queue for operator
  private val _verificationQueue = MutableStateFlow<List<VerificationQueueEntry>>(
    listOf(
      VerificationQueueEntry(
        id = "vq-1",
        workerId = "w11",
        workerName = "Rustam Aliyev",
        profession = "Santexnik",
        checkType = "Rasmiy sug'urta polisi (Kalit uchun)",
        submittedTime = "10 daqiqa oldin"
      ),
      VerificationQueueEntry(
        id = "vq-2",
        workerId = "w26",
        workerName = "Sayyora Yusupova",
        profession = "Oshpaz",
        checkType = "IIV sudlanmaganlik ma'lumotnomasi",
        submittedTime = "35 daqiqa oldin"
      ),
      VerificationQueueEntry(
        id = "vq-3",
        workerId = "w33",
        workerName = "Malika Abdullayeva",
        profession = "Klining",
        checkType = "Kasbiy mahorat va tozalik sertifikati",
        submittedTime = "1 soat oldin"
      )
    )
  )
  val verificationQueue: StateFlow<List<VerificationQueueEntry>> = _verificationQueue.asStateFlow()

  private val _sosAlertMessage = MutableStateFlow<String?>(null)
  val sosAlertMessage: StateFlow<String?> = _sosAlertMessage.asStateFlow()

  val pinManager = VisitPinManager("4921")

  fun setCity(city: City) {
    _selectedCity.value = city
  }

  fun switchRole(role: UserRole) {
    _currentRole.value = role
  }

  fun getWorkerById(id: String): Worker? {
    return _workers.value.find { it.id == id }
  }

  /**
   * Filter and sort workers:
   * Rule: Gold ("Kalit") first, then sorted by rating descending
   */
  fun getFilteredWorkers(
    category: CategoryId? = null,
    onlyKalit: Boolean = false,
    city: City? = null,
    searchQuery: String = ""
  ): List<Worker> {
    return _workers.value
      .filter { worker ->
        !worker.isHidden &&
        (category == null || worker.categoryId == category) &&
        (!onlyKalit || worker.tier == Tier.GOLD) &&
        (city == null || worker.city == city) &&
        (searchQuery.isBlank() ||
            worker.fullName.contains(searchQuery, ignoreCase = true) ||
            worker.bioUz.contains(searchQuery, ignoreCase = true) ||
            worker.skills.any { it.contains(searchQuery, ignoreCase = true) })
      }
      .sortedWith(
        compareByDescending<Worker> { it.tier == Tier.GOLD }
          .thenByDescending { it.ratingAvg }
      )
  }

  /**
   * Server-side rule:
   * A booking with owner_present = false is REJECTED by the server unless worker is GOLD.
   */
  fun createBooking(
    worker: Worker,
    date: String,
    startTime: String,
    address: String,
    comment: String,
    paymentMethod: PaymentMethod,
    ownerPresent: Boolean,
    customPin: String? = null
  ): Result<Booking> {
    if (!ownerPresent && worker.tier != Tier.GOLD) {
      return Result.failure(
        IllegalStateException("Siz uyda bo'lmaganingizda faqat «Kalit» (Oltin) maqomidagi ustalar ishlay oladi.")
      )
    }

    val generatedPin = customPin ?: (1000..9999).random().toString()

    val newBooking = Booking(
      id = "b-${System.currentTimeMillis() % 100000}",
      clientId = "client-user",
      workerId = worker.id,
      workerName = worker.fullName,
      categoryId = worker.categoryId,
      workerTier = worker.tier,
      date = date,
      startTime = startTime,
      address = address.ifBlank { "Toshkent sh., Mirobod t., Nukus ko'chasi 42" },
      comment = comment.ifBlank { "Uy xizmati buyurtmasi" },
      paymentMethod = paymentMethod,
      ownerPresent = ownerPresent,
      status = VisitStatus.BOOKED,
      amount = worker.price,
      pinCode = generatedPin,
      paymentStatus = PaymentStatus.HELD,
      beforePhotosCount = 0,
      afterPhotosCount = 0
    )

    _bookings.update { listOf(newBooking) + it }
    return Result.success(newBooking)
  }

  // State transitions:
  fun checkInWorker(bookingId: String, enteredPin: String): VisitPinVerificationResult {
    val booking = _bookings.value.find { it.id == bookingId }
    val targetPin = booking?.pinCode ?: "4921"
    
    // Check against booking's exact PIN or test pin "4921"
    val isCorrect = enteredPin == targetPin || enteredPin == "4921"

    if (isCorrect) {
      pinManager.reset()
      _bookings.update { list ->
        list.map { b ->
          if (b.id == bookingId) {
            b.copy(
              status = VisitStatus.CHECKED_IN,
              checkInTime = "Hozirgina",
              beforePhotosCount = if (b.beforePhotosCount == 0) 3 else b.beforePhotosCount
            )
          } else b
        }
      }
      return VisitPinVerificationResult(
        success = true,
        remainingAttempts = 5,
        isLocked = false,
        message = "PIN kod to'g'ri tasdiqlandi. Kirish ruxsat etildi."
      )
    } else {
      return pinManager.verifyPin(enteredPin)
    }
  }

  fun checkOutWorker(bookingId: String, enteredPin: String): VisitPinVerificationResult {
    val booking = _bookings.value.find { it.id == bookingId }
    val targetPin = booking?.pinCode ?: "4921"

    val isCorrect = enteredPin == targetPin || enteredPin == "4921"

    if (isCorrect) {
      pinManager.reset()
      _bookings.update { list ->
        list.map { b ->
          if (b.id == bookingId) {
            b.copy(
              status = VisitStatus.CHECKED_OUT,
              checkOutTime = "Hozirgina",
              afterPhotosCount = if (b.afterPhotosCount == 0) 3 else b.afterPhotosCount
            )
          } else b
        }
      }
      return VisitPinVerificationResult(
        success = true,
        remainingAttempts = 5,
        isLocked = false,
        message = "Chiqish kodi tasdiqlandi. Tashrif yakunlandi."
      )
    } else {
      return pinManager.verifyPin(enteredPin)
    }
  }

  fun confirmEverythingFine(bookingId: String) {
    _bookings.update { list ->
      list.map { b ->
        if (b.id == bookingId) {
          b.copy(
            status = VisitStatus.CONFIRMED,
            paymentStatus = PaymentStatus.CAPTURED
          )
        } else b
      }
    }
  }

  fun reportProblem(bookingId: String, reason: String) {
    _bookings.update { list ->
      list.map { b ->
        if (b.id == bookingId) {
          // Hide worker and freeze payment
          hideWorker(b.workerId)
          b.copy(
            status = VisitStatus.DISPUTED,
            paymentStatus = PaymentStatus.FROZEN,
            disputeReason = reason
          )
        } else b
      }
    }
  }

  fun addVisitPhoto(phase: String, description: String) {
    val newPhoto = VisitPhoto(
      id = "p-${System.currentTimeMillis()}",
      phase = phase,
      description = description,
      timestamp = "Hozirgina"
    )
    _visitPhotos.update { listOf(newPhoto) + it }
    // Also update booking counts
    _bookings.update { list ->
      list.map { b ->
        if (b.status == VisitStatus.CHECKED_IN) {
          if (phase == "before") b.copy(beforePhotosCount = b.beforePhotosCount + 1)
          else b.copy(afterPhotosCount = b.afterPhotosCount + 1)
        } else b
      }
    }
  }

  fun resolveDispute(bookingId: String, refundToClient: Boolean) {
    _bookings.update { list ->
      list.map { b ->
        if (b.id == bookingId) {
          b.copy(
            status = if (refundToClient) VisitStatus.CONFIRMED else VisitStatus.CONFIRMED,
            paymentStatus = if (refundToClient) PaymentStatus.REFUNDED else PaymentStatus.CAPTURED
          )
        } else b
      }
    }
  }

  fun approveVerificationQueueEntry(entryId: String, approved: Boolean) {
    _verificationQueue.update { list ->
      list.map { item ->
        if (item.id == entryId) {
          item.copy(status = if (approved) "APPROVED" else "REJECTED")
        } else item
      }
    }
  }

  private fun hideWorker(workerId: String) {
    _workers.update { list ->
      list.map { w ->
        if (w.id == workerId) w.copy(isHidden = true) else w
      }
    }
  }

  fun triggerSos(visitId: String, senderName: String) {
    _sosAlertMessage.value = "DIQQAT! SOS signali qabul qilindi (#$visitId - $senderName). Operator zudlik bilan bog'lanmoqda!"
  }

  fun dismissSos() {
    _sosAlertMessage.value = null
  }

  fun getMessagesForWorker(workerId: String, workerName: String): List<ChatMessage> {
    val current = _messages.value[workerId]
    if (current == null) {
      val initial = ChatSeedData.getInitialMessages(workerId, workerName)
      _messages.update { it + (workerId to initial) }
      return initial
    }
    return current
  }

  fun sendChatMessage(workerId: String, workerName: String, text: String) {
    if (text.isBlank()) return
    val newMsg = ChatMessage(
      id = "m-${System.currentTimeMillis()}",
      workerId = workerId,
      senderName = "Mijoz",
      text = text,
      timestamp = "Hozirgina",
      isFromClient = true
    )
    _messages.update { map ->
      val list = map[workerId] ?: ChatSeedData.getInitialMessages(workerId, workerName)
      map + (workerId to (list + newMsg))
    }

    // Realistic worker auto-reply after 700ms delay
    scope.launch {
      delay(750)
      val replyText = when {
        text.contains("vaqt", ignoreCase = true) || text.contains("qachon", ignoreCase = true) || text.contains("kel", ignoreCase = true) ->
          "Assalomu alaykum! Albatta, aytilgan vaqtda manzilga yetib boraman. Xavotir olmang!"
        text.contains("asbob", ignoreCase = true) || text.contains("qurol", ignoreCase = true) || text.contains("detal", ignoreCase = true) ->
          "Barcha zarur asbob-uskunalar va ehtiyot qismlar o'zim bilan bo'ladi. Ishni tez va sifatli bajaramiz."
        text.contains("narx", ignoreCase = true) || text.contains("pul", ignoreCase = true) || text.contains("summa", ignoreCase = true) ->
          "Narx Suyanch platformasida ko'rsatilganidek qat'iy va shaffof, qo'shimcha to'lovlar olinmaydi."
        text.contains("manzil", ignoreCase = true) || text.contains("uy", ignoreCase = true) || text.contains("ko'cha", ignoreCase = true) ->
          "Manzilni tasdiqladim, navigator orqali to'g'ri yetib boraman. Rahmat!"
        else ->
          "Xabaringizni oldim! Sizga xizmat ko'rsatishdan mamnunman. Tez orada ko'rishguncha!"
      }

      val replyMsg = ChatMessage(
        id = "m-${System.currentTimeMillis() + 1}",
        workerId = workerId,
        senderName = workerName,
        text = replyText,
        timestamp = "Hozirgina",
        isFromClient = false
      )

      _messages.update { map ->
        val list = map[workerId] ?: emptyList()
        map + (workerId to (list + replyMsg))
      }
    }
  }

  fun toggleVerificationCheck(check: VerificationCheck) {
    _workerChecks.update { current ->
      if (current.contains(check)) current - check else current + check
    }
  }
}
