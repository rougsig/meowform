package com.github.rougsig.meowform.validation.core

import com.github.rougsig.meowform.validation.core.ValidationResult.Invalid
import com.github.rougsig.meowform.validation.core.ValidationResult.Valid

internal class SpecValidator<T>(
  private val constraints: List<Constraint<T>>,
  private val validators: List<Validator<T>>
) : Validator<T> {
  override fun invoke(value: T): ValidationResult<T> {
    val constraintErrors = constraints.mapNotNull { constraint ->
      if (constraint.test(value)) null
      else constraint.errorMessage.format(*(constraint.errorMessageParameters.toTypedArray()))
    }
    if (constraintErrors.isNotEmpty()) {
      return Invalid(mapOf(emptyList<String>() to constraintErrors))
    }

    val validationResult = validators.map { it.invoke(value) }
    if (validationResult.isNotEmpty()) {
      return validationResult.fold(Valid(value)) { acc: ValidationResult<T>, validator ->
        acc.combineWith(validator)
      }
    }

    return Valid(value)
  }
}
