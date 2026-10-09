package com.example.model

data class Booking(
  val id: String,
  val clientId: String,
  val workerId: String,
  val workerName: String,
  val categoryId: CategoryId,
  val workerTier: Tier,
  val date: String,
  val startTime: String,
  val address: String,
  val comment: String,
  val paymentMethod: PaymentMethod,
  val ownerPresent: Boolean,
  val status: VisitStatus,
  val amount: Long,
  val pinCode: String = "4921",
  val createdAt: String = "2026-10-07 09:00",
  val checkInTime: String? = null,
  val checkOutTime: String? = null,
  val paymentStatus: PaymentStatus = PaymentStatus.HELD,
  val beforePhotosCount: Int = 0,
  val afterPhotosCount: Int = 0,
  val disputeReason: String? = null
)
