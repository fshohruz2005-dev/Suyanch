package com.example.model

import com.example.core.i18n.StringKey

enum class VerificationCheck(val id: String) {
  ID("id"),
  CRIMINAL_RECORD("criminal_record"),
  REFERENCES("references"),
  INTERVIEW("interview"),
  SKILLS("skills"),
  INSURANCE("insurance"),
  ORDERS_20("orders_20")
}

enum class Tier(
  val id: String,
  val nameKey: StringKey,
  val descKey: StringKey,
  val canWorkWhileOwnerAway: Boolean
) {
  BASIC(
    id = "basic",
    nameKey = StringKey.TIER_BASIC,
    descKey = StringKey.TIER_BASIC_DESC,
    canWorkWhileOwnerAway = false
  ),
  SILVER(
    id = "silver",
    nameKey = StringKey.TIER_SILVER,
    descKey = StringKey.TIER_SILVER_DESC,
    canWorkWhileOwnerAway = false
  ),
  GOLD(
    id = "gold",
    nameKey = StringKey.TIER_GOLD,
    descKey = StringKey.TIER_GOLD_DESC,
    canWorkWhileOwnerAway = true
  );

  companion object {
    /**
     * Server-side rule:
     * Basic: id
     * Silver: id, criminal_record, references, interview, skills
     * Gold ("Kalit"): all of silver + insurance + 20 completed orders without complaints
     */
    fun compute(
      activeChecks: Set<VerificationCheck>,
      cleanOrdersCount: Int
    ): Tier {
      val hasId = activeChecks.contains(VerificationCheck.ID)
      if (!hasId) return BASIC

      val hasSilverRequirements = activeChecks.containsAll(
        listOf(
          VerificationCheck.ID,
          VerificationCheck.CRIMINAL_RECORD,
          VerificationCheck.REFERENCES,
          VerificationCheck.INTERVIEW,
          VerificationCheck.SKILLS
        )
      )

      if (!hasSilverRequirements) return BASIC

      val hasGoldInsurance = activeChecks.contains(VerificationCheck.INSURANCE)
      val hasOrders = cleanOrdersCount >= 20 || activeChecks.contains(VerificationCheck.ORDERS_20)

      return if (hasGoldInsurance && hasOrders) {
        GOLD
      } else {
        SILVER
      }
    }
  }
}
