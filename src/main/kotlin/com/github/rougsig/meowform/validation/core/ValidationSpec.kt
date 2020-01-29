package com.github.rougsig.meowform.validation.core

import com.github.rougsig.meowform.validation.locale.EnglishErrorMessages
import com.github.rougsig.meowform.validation.locale.ErrorMessages
import com.github.rougsig.meowform.validation.validator.optionalValidator
import com.github.rougsig.meowform.validation.validator.requiredValidator
import kotlin.reflect.KProperty1

class ValidationSpec<T, M : ErrorMessages>(val errorMessages: M) {
  private val constraints = mutableListOf<Constraint<T>>()
  private val validators = mutableListOf<Validator<T>>()

  operator fun <R> KProperty1<T, R?>.invoke(init: ValidationSpec<R, M>.() -> Unit) {
    val validator = ValidationSpec<R, M>(errorMessages).also(init).build()
    validators.add(optionalValidator(this, errorMessages, validator))
  }

  infix fun <R> KProperty1<T, R?>.notNull(init: ValidationSpec<R, M>.() -> Unit) {
    val validator = ValidationSpec<R, M>(errorMessages).also(init).build()
    validators.add(requiredValidator(this, errorMessages, validator))
  }

  fun addConstraint(errorMessage: String, vararg errorMessageParameters: Any, test: (T) -> Boolean): Constraint<T> {
    return Constraint(errorMessage, errorMessageParameters.toList(), test).also { constraints.add(it) }
  }

  fun addValidator(validator: Validator<T>): Validator<T> {
    return validator.also { validators.add(validator) }
  }

  infix fun Constraint<T>.message(errorMessage: String) {
    constraints.remove(this)
    constraints.add(this.copy(errorMessage = errorMessage))
  }

  fun build(): Validator<T> {
    return SpecValidator(constraints, validators)
  }
}

fun <T : Any, M : ErrorMessages> validationSpec(errorMessages: M, init: ValidationSpec<T, M>.() -> Unit): Validator<T> {
  return ValidationSpec<T, M>(errorMessages).also(init).build()
}

fun <T : Any> validationSpec(init: ValidationSpec<T, ErrorMessages>.() -> Unit): Validator<T> {
  return ValidationSpec<T, ErrorMessages>(EnglishErrorMessages.INSTANCE).also(init).build()
}
