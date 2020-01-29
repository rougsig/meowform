package com.github.rougsig.meowform.form

import java.lang.reflect.Modifier
import kotlin.reflect.KProperty1

class FormValues<T : Any>(initialState: T) {
  private val formValuesClass = initialState::class.java
  private val values = LinkedHashMap<String, Any?>()

  init {
    formValuesClass.declaredFields
      .filter { !Modifier.isStatic(it.modifiers) }
      .map { field ->
        field.isAccessible = true
        values[field.name] = field[initialState]
      }
  }

  operator fun <R> get(property: KProperty1<T, R>): R {
    return values[property.name] as R
  }

  fun <R> setValue(key: String, value: R) {
    values[key] = value
  }

  internal fun deserialize(): T {
    return formValuesClass.constructors.first()
      .newInstance(*(values.values.toTypedArray())) as T
  }
}
