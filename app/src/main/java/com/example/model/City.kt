package com.example.model

import com.example.core.i18n.StringKey

enum class City(val id: String, val nameKey: StringKey, val displayNameUz: String, val displayNameRu: String) {
  TASHKENT("tashkent", StringKey.CITY_TASHKENT, "Toshkent", "Ташкент"),
  SAMARKAND("samarkand", StringKey.CITY_SAMARKAND, "Samarqand", "Самарканд"),
  BUKHARA("bukhara", StringKey.CITY_BUKHARA, "Buxoro", "Бухара"),
  ANDIJAN("andijan", StringKey.CITY_ANDIJAN, "Andijon", "Андижан"),
  NAMANGAN("namangan", StringKey.CITY_NAMANGAN, "Namangan", "Наманган"),
  FERGANA("fergana", StringKey.CITY_FERGANA, "Farg'ona", "Фергана"),
  NAVOIY("navoiy", StringKey.CITY_NAVOIY, "Navoiy", "Навои"),
  URGENCH("urgench", StringKey.CITY_URGENCH, "Urganch", "Ургенч");

  companion object {
    val DEFAULT = TASHKENT
  }
}

enum class PaymentMethod(val id: String, val labelUz: String, val labelRu: String) {
  CARD("card", "Bank kartasi (Uzcard / Humo)", "Банковская карта (Uzcard / Humo)"),
  CASH("cash", "Naqd pul", "Наличные")
}
