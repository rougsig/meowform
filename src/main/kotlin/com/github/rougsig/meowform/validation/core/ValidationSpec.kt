package com.github.rougsig.meowform.validation.core

import kotlin.reflect.KProperty1

class ValidationSpec<T> {
  private val constraints = mutableListOf<Constraint<T>>()
  private val validators = mutableListOf<Validator<T>>()

  operator fun <R : Any> KProperty1<T, R>.invoke(init: ValidationSpec<R>.() -> Unit) {
    val validator = ValidationSpec<R>().also(init).build()
    val namePrefix = listOf(this.name)
    validators.add { value ->
      validator(get(value))
        .mapError { namePrefix + it }
        .map { value }
    }
  }

  fun constraint(errorMessage: String, vararg errorMessageParameters: Any, test: (T) -> Boolean): Constraint<T> {
    return Constraint(errorMessage, errorMessageParameters.toList(), test).also { constraints.add(it) }
  }

  infix fun Constraint<T>.message(errorMessage: String) {
    constraints.remove(this)
    constraints.add(this.copy(errorMessage = errorMessage))
  }

  fun build(): Validator<T> {
    return SpecValidator(constraints, validators)
  }
}

fun <T : Any> validationSpec(init: ValidationSpec<T>.() -> Unit): Validator<T> {
  return ValidationSpec<T>().also(init).build()
}
