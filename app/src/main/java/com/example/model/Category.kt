package com.example.model

import com.example.core.i18n.StringKey

enum class CategoryId(
  val id: String,
  val titleKey: StringKey,
  val defaultStartingPrice: Long,
  val priceFormat: PriceFormat
) {
  CARE("care", StringKey.CAT_CARE, 25000L, PriceFormat.HOURLY),
  PLUMBER("plumber", StringKey.CAT_PLUMBER, 120000L, PriceFormat.VISIT),
  ELECTRIC("electric", StringKey.CAT_ELECTRIC, 100000L, PriceFormat.VISIT),
  HANDYMAN("handyman", StringKey.CAT_HANDYMAN, 80000L, PriceFormat.HOURLY),
  COOK("cook", StringKey.CAT_COOK, 200000L, PriceFormat.DAY),
  CLEANING("cleaning", StringKey.CAT_CLEANING, 350000L, PriceFormat.VISIT)
}

enum class PriceFormat(val suffixKey: StringKey) {
  HOURLY(StringKey.PRICE_HOURLY),
  VISIT(StringKey.PRICE_VISIT),
  DAY(StringKey.PRICE_DAY),
  MONTH(StringKey.PRICE_MONTH)
}

data class SkillItem(
  val id: String,
  val categoryId: CategoryId,
  val labelKey: StringKey
)

object ServiceCatalog {
  val skills: List<SkillItem> = listOf(
    // Caregiver
    SkillItem("bedridden", CategoryId.CARE, StringKey.SKILL_BEDRIDDEN),
    SkillItem("dementia", CategoryId.CARE, StringKey.SKILL_DEMENTIA),
    SkillItem("rehab", CategoryId.CARE, StringKey.SKILL_REHAB),
    SkillItem("medical", CategoryId.CARE, StringKey.SKILL_MEDICAL),
    // Plumber
    SkillItem("leak", CategoryId.PLUMBER, StringKey.SKILL_LEAK),
    SkillItem("pipes", CategoryId.PLUMBER, StringKey.SKILL_PIPES),
    SkillItem("boiler", CategoryId.PLUMBER, StringKey.SKILL_BOILER),
    SkillItem("sanitary", CategoryId.PLUMBER, StringKey.SKILL_SANITARY),
    // Electrician
    SkillItem("wiring", CategoryId.ELECTRIC, StringKey.SKILL_WIRING),
    SkillItem("sockets", CategoryId.ELECTRIC, StringKey.SKILL_SOCKETS),
    SkillItem("appliances", CategoryId.ELECTRIC, StringKey.SKILL_APPLIANCES),
    // Handyman
    SkillItem("furniture", CategoryId.HANDYMAN, StringKey.SKILL_FURNITURE),
    SkillItem("doors", CategoryId.HANDYMAN, StringKey.SKILL_DOORS),
    SkillItem("wall", CategoryId.HANDYMAN, StringKey.SKILL_WALL),
    SkillItem("paint", CategoryId.HANDYMAN, StringKey.SKILL_PAINT),
    // Cook
    SkillItem("national", CategoryId.COOK, StringKey.SKILL_NATIONAL),
    SkillItem("plov", CategoryId.COOK, StringKey.SKILL_PLOV),
    SkillItem("diet", CategoryId.COOK, StringKey.SKILL_DIET),
    SkillItem("baking", CategoryId.COOK, StringKey.SKILL_BAKING),
    // Cleaning
    SkillItem("regular", CategoryId.CLEANING, StringKey.SKILL_REGULAR),
    SkillItem("deep", CategoryId.CLEANING, StringKey.SKILL_DEEP),
    SkillItem("after_repair", CategoryId.CLEANING, StringKey.SKILL_AFTER_REPAIR),
    SkillItem("carpets", CategoryId.CLEANING, StringKey.SKILL_CARPETS)
  )

  fun formatPrice(amount: Long, format: PriceFormat): String {
    val formattedNumber = String.format("%,d", amount).replace(',', ' ')
    return formattedNumber
  }
}
