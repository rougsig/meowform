package com.github.rougsig.meowform.form

import com.github.rougsig.meowform.validation.core.ValidationResult
import com.github.rougsig.meowform.validation.core.Validator
import kotlin.reflect.KProperty1

class MeowForm<T : Any>(
  initialValue: T,
  private val validator: Validator<T>,
  private val onSubmit: (T) -> Unit
) {
  val touched: TouchedValues<T> = TouchedValues()
  val values: FormValues<T> = FormValues(initialValue)

  var errors: ValidationResult.Invalid<T>? = null
    private set

  fun <R> setValue(property: KProperty1<T, R>, value: R) {
    touched.markTouched(property.name)
    values.setValue(property.name, value)
  }

  fun submit(): ValidationResult<T> {
    val value = values.deserialize()
    val result = validator(value)
    when (result) {
      is ValidationResult.Valid -> onSubmit(value)
      is ValidationResult.Invalid -> errors = result
    }
    return result
  }
}
