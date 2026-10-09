package com.example

import com.example.model.PriceFormat
import com.example.model.ServiceCatalog
import com.example.model.Tier
import com.example.model.VerificationCheck
import com.example.model.VisitPinManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SuyanchFoundationUnitTest {

  @Test
  fun testTierComputationRules() {
    // 1. Empty or missing ID -> Basic
    assertEquals(Tier.BASIC, Tier.compute(emptySet(), 0))

    // 2. Only ID check -> Basic
    assertEquals(Tier.BASIC, Tier.compute(setOf(VerificationCheck.ID), 5))
    assertFalse(Tier.BASIC.canWorkWhileOwnerAway)

    // 3. Silver requirements (ID, criminal record, references, interview, skills)
    val silverChecks = setOf(
      VerificationCheck.ID,
      VerificationCheck.CRIMINAL_RECORD,
      VerificationCheck.REFERENCES,
      VerificationCheck.INTERVIEW,
      VerificationCheck.SKILLS
    )
    assertEquals(Tier.SILVER, Tier.compute(silverChecks, 10))
    assertFalse(Tier.SILVER.canWorkWhileOwnerAway)

    // 4. Missing one silver check drops back to Basic
    val incompleteSilver = setOf(
      VerificationCheck.ID,
      VerificationCheck.REFERENCES,
      VerificationCheck.INTERVIEW,
      VerificationCheck.SKILLS
    )
    assertEquals(Tier.BASIC, Tier.compute(incompleteSilver, 25))

    // 5. Silver + Insurance but < 20 orders remains Silver
    val silverWithInsurance = silverChecks + VerificationCheck.INSURANCE
    assertEquals(Tier.SILVER, Tier.compute(silverWithInsurance, 19))

    // 6. Gold ("Kalit"): all of Silver + insurance + 20 clean orders
    assertEquals(Tier.GOLD, Tier.compute(silverWithInsurance, 20))
    assertTrue(Tier.GOLD.canWorkWhileOwnerAway)
  }

  @Test
  fun testPinVerificationAndRateLimit() {
    val pinManager = VisitPinManager("4921")

    // Correct PIN succeeds
    val successResult = pinManager.verifyPin("4921")
    assertTrue(successResult.success)
    assertFalse(successResult.isLocked)

    // 4 wrong attempts: decrements remaining attempts
    for (i in 1..4) {
      val wrongResult = pinManager.verifyPin("0000")
      assertFalse(wrongResult.success)
      assertFalse(wrongResult.isLocked)
      assertEquals(5 - i, wrongResult.remainingAttempts)
    }

    // 5th wrong attempt: locks the visit
    val fifthResult = pinManager.verifyPin("0000")
    assertFalse(fifthResult.success)
    assertTrue(fifthResult.isLocked)
    assertEquals(0, fifthResult.remainingAttempts)

    // Subsequent attempts remain locked
    val lockedResult = pinManager.verifyPin("4921")
    assertFalse(lockedResult.success)
    assertTrue(lockedResult.isLocked)
  }

  @Test
  fun testPriceFormattingUzbekistanFormat() {
    val formatted120k = ServiceCatalog.formatPrice(120000L, PriceFormat.VISIT)
    assertEquals("120 000", formatted120k)

    val formatted25k = ServiceCatalog.formatPrice(25000L, PriceFormat.HOURLY)
    assertEquals("25 000", formatted25k)

    val formatted1m = ServiceCatalog.formatPrice(1500000L, PriceFormat.MONTH)
    assertEquals("1 500 000", formatted1m)
  }
}
