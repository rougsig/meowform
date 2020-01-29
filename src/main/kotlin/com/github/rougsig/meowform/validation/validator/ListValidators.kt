package com.github.rougsig.meowform.validation.validator

import com.github.rougsig.meowform.validation.core.ValidationResult
import com.github.rougsig.meowform.validation.core.ValidationSpec
import com.github.rougsig.meowform.validation.locale.ErrorMessages

fun <T, M : ErrorMessages> ValidationSpec<List<T>, M>.all(init: ValidationSpec<T, M>.() -> Unit) {
  val validator = ValidationSpec<T, M>(errorMessages).also(init).build()
  addValidator { list ->
    list.foldIndexed(ValidationResult.Valid(list)) { index, acc: ValidationResult<List<T>>, value ->
      acc.combineWith(validator(value)
        .mapError { listOf("$index") + it }
        .map { list })
    }
  }
}

fun <T, M : ErrorMessages> ValidationSpec<List<T?>, M>.allNotNull(init: ValidationSpec<T, M>.() -> Unit) {
  val validator = ValidationSpec<List<T?>, M>(errorMessages)
    .apply {
      all {
        notNull(init)
      }
    }
    .build()
  addValidator(validator)
}
