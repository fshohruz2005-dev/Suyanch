package com.example.model

import com.example.core.i18n.StringKey

enum class VisitStatus(val titleKey: StringKey) {
  BOOKED(StringKey.STATUS_BOOKED),
  CHECKED_IN(StringKey.STATUS_CHECKED_IN),
  CHECKED_OUT(StringKey.STATUS_CHECKED_OUT),
  CONFIRMED(StringKey.STATUS_CONFIRMED),
  DISPUTED(StringKey.STATUS_DISPUTED)
}

enum class PaymentStatus {
  HELD,
  CAPTURED,
  FROZEN,
  REFUNDED
}

enum class TimelineStepState {
  COMPLETED,
  IN_PROGRESS,
  PENDING
}

data class TimelineStepInfo(
  val stepNumber: Int,
  val titleKey: StringKey,
  val subtitle: String,
  val timestamp: String? = null,
  val state: TimelineStepState
)

data class VisitPinVerificationResult(
  val success: Boolean,
  val remainingAttempts: Int,
  val isLocked: Boolean,
  val message: String
)

class VisitPinManager(
  private val correctPin: String = "4921",
  private var failedAttempts: Int = 0
) {
  companion object {
    const val MAX_ATTEMPTS = 5
  }

  fun verifyPin(enteredPin: String): VisitPinVerificationResult {
    if (failedAttempts >= MAX_ATTEMPTS) {
      return VisitPinVerificationResult(
        success = false,
        remainingAttempts = 0,
        isLocked = true,
        message = "Xavfsizlik qulflandi (5 ta noto'g'ri urinish). Operator tekshirmoqda."
      )
    }

    if (enteredPin == correctPin) {
      failedAttempts = 0
      return VisitPinVerificationResult(
        success = true,
        remainingAttempts = MAX_ATTEMPTS,
        isLocked = false,
        message = "PIN kod to'g'ri tasdiqlandi"
      )
    } else {
      failedAttempts++
      val locked = failedAttempts >= MAX_ATTEMPTS
      val remaining = (MAX_ATTEMPTS - failedAttempts).coerceAtLeast(0)
      return VisitPinVerificationResult(
        success = false,
        remainingAttempts = remaining,
        isLocked = locked,
        message = if (locked) "Qulflangan: operatorga xabar yuborildi." else "Noto'g'ri PIN. Qolgan urinishlar: $remaining"
      )
    }
  }

  fun reset() {
    failedAttempts = 0
  }
}
