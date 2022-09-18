package com.frybits.preferences.toolbox.rx2

// Internal optional to handle null keys
internal data class Optional<T>(val value: T?)

internal fun <T> T?.asOptional() = Optional(this)
