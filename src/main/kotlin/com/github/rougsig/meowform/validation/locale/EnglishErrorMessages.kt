package com.github.rougsig.meowform.validation.locale

open class EnglishErrorMessages : ErrorMessages() {
  companion object {
    val INSTANCE by lazy { EnglishErrorMessages() }
  }

  override val required: String = "required field"
}
