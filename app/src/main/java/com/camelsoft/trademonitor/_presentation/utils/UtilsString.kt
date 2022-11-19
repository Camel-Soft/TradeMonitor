package com.camelsoft.trademonitor._presentation.utils

fun String.rm001d() = this.replace(oldValue = "\u001d", newValue = "", ignoreCase = false)

fun String.trim001d(): String {
    if (this.isEmpty()) return this
    var result = this
    if (result.substring(0,1) == "\u001d") result = result.substring(1)
    if (result.substring(result.count()-1) == "\u001d") result = result.substring(0, result.count()-1)
    return result
}

fun String.validateEmail(): Boolean {
    val EMAIL_REGEX = Regex(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    return if (this.isBlank() || !this.matches(EMAIL_REGEX)) false else true
}

fun String.prepareUrl(): String {
    if (!this.contains("http")) return "https://$this"
    if (!this.contains("https")) return this.replaceFirst(oldValue = "http", newValue = "https", ignoreCase = true)
    return this
}
