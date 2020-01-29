package com.github.rougsig.meowform.validation.validator

import com.github.rougsig.meowform.validation.core.ValidationResult
import com.github.rougsig.meowform.validation.core.Validator
import com.github.rougsig.meowform.validation.locale.ErrorMessages
import kotlin.reflect.KProperty1

internal fun <T, R, M : ErrorMessages> optionalValidator(
  property: KProperty1<T, R?>,
  errorMessages: M,
  propertyValidator: Validator<R>
): Validator<T> {
  val namePrefix = listOf(property.name)
  return { value ->
    val propertyValue: R? = property.get(value)
    optionalValidator(errorMessages, propertyValidator)(propertyValue)
      .mapError { namePrefix + it }
      .map { value }
  }
}

internal fun <T, M : ErrorMessages> optionalValidator(
  errorMessages: M,
  validator: Validator<T>
): Validator<T?> {
  return { value ->
    if (value == null) {
      ValidationResult.Valid(value)
    } else {
      validator(value).map { value }
    }
  }
}

internal fun <T, R, M : ErrorMessages> requiredValidator(
  property: KProperty1<T, R?>,
  errorMessages: M,
  propertyValidator: Validator<R>
): Validator<T> {
  val namePrefix = listOf(property.name)
  return { value ->
    val propertyValue: R? = property.get(value)
    requiredValidator(errorMessages, propertyValidator)(propertyValue)
      .mapError { namePrefix + it }
      .map { value }
  }
}

internal fun <T, M : ErrorMessages> requiredValidator(
  errorMessages: M,
  validator: Validator<T>
): Validator<T?> {
  return { value ->
    if (value == null) {
      ValidationResult.Invalid(mapOf(emptyList<String>() to listOf(errorMessages.required)))
    } else {
      validator(value).map { value }
    }
  }
}
