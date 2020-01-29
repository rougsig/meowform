package com.github.rougsig.meowform.validation.core

import kotlin.reflect.KProperty1

sealed class ValidationResult<T> {
  abstract operator fun get(vararg propertyPath: Any): List<String>?
  abstract fun <R> map(transform: (T) -> R): ValidationResult<R>

  data class Valid<T>(val value: T) : ValidationResult<T>() {
    override fun get(vararg propertyPath: Any): List<String>? {
      return null
    }

    override fun <R> map(transform: (T) -> R): ValidationResult<R> {
      return Valid(transform(value))
    }
  }

  data class Invalid<T>(val errors: Map<List<String>, List<String>>) : ValidationResult<T>() {
    override fun get(vararg propertyPath: Any): List<String>? {
      return errors[propertyPath.map(::toPathSegment)]
    }

    private fun toPathSegment(it: Any): String {
      return when (it) {
        is KProperty1<*, *> -> it.name
        else -> it.toString()
      }
    }

    override fun <R> map(transform: (T) -> R): ValidationResult<R> {
      return Invalid(errors)
    }
  }

  internal fun combineWith(other: ValidationResult<T>): ValidationResult<T> {
    return when (this) {
      is Valid -> return other
      is Invalid -> when (other) {
        is Valid -> this
        is Invalid -> Invalid((this.errors.toList() + other.errors.toList())
          .groupBy(keySelector = { it.first }, valueTransform = { it.second })
          .mapValues { (_, values) -> values.flatten() })
      }
    }
  }

  internal fun mapError(keyTransform: (List<String>) -> List<String>): ValidationResult<T> {
    return if (this is Invalid) {
      Invalid(this.errors.mapKeys { (key, _) -> keyTransform(key) })
    } else {
      this
    }
  }
}
