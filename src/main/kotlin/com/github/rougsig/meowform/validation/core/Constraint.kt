package com.github.rougsig.meowform.validation.core

data class Constraint<T>(
  val errorMessage: String,
  val errorMessageParameters: List<Any>,
  val test: (T) -> Boolean
)
