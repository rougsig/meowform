package com.github.rougsig.meowform.form

import kotlin.reflect.KProperty1

class TouchedValues<T : Any> {
  private val values = LinkedHashMap<String, Boolean>()

  operator fun get(property: KProperty1<T, *>): Boolean {
    return values[property.name] ?: false
  }

  fun markTouched(key: String) {
    values[key] = true
  }
}
