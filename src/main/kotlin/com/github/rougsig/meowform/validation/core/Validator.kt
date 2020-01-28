package com.github.rougsig.meowform.validation.core

typealias Validator<T> = (value: T) -> ValidationResult<T>
