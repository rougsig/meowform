package com.github.rougsig.meowform.validation.validator

import com.github.rougsig.meowform.validation.core.ValidationSpec
import com.github.rougsig.meowform.validation.core.Validator
import com.github.rougsig.meowform.validation.locale.ErrorMessages

fun <T, M : ErrorMessages> ValidationSpec<T?, M>.notNull(init: ValidationSpec<T, M>.() -> Unit): Validator<T?> {
  val validator = ValidationSpec<T, M>(errorMessages).also(init).build()
  return addValidator(requiredValidator(errorMessages, validator))
}
