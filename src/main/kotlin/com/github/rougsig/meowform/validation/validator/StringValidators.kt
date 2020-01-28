package com.github.rougsig.meowform.validation.validator

import com.github.rougsig.meowform.validation.core.Constraint
import com.github.rougsig.meowform.validation.core.ValidationSpec

fun ValidationSpec<String>.requireNotBlank(errorMessage: String = "required not blank"): Constraint<String> {
  return constraint(
    errorMessage,
    test = String::isNotBlank
  )
}
