object BuildConfigStrings {
    private const val DEFAULT_PLACEHOLDER_VALUE = "sample_val"

    fun quoted(value: String?): String {
        val escaped = escapeJavaStringLiteral(value ?: DEFAULT_PLACEHOLDER_VALUE)
        return "\"$escaped\""
    }

    private fun escapeJavaStringLiteral(value: String): String {
        val escaped = StringBuilder(value.length)
        for (char in value) {
            when (char) {
                '\\' -> escaped.append("\\\\")
                '"' -> escaped.append("\\\"")
                '\n' -> escaped.append("\\n")
                '\r' -> escaped.append("\\r")
                '\t' -> escaped.append("\\t")
                '\b' -> escaped.append("\\b")
                '\u000C' -> escaped.append("\\f")
                else -> {
                    if (char < ' ') {
                        escaped.append("\\u")
                        escaped.append(char.code.toString(16).padStart(4, '0'))
                    } else {
                        escaped.append(char)
                    }
                }
            }
        }
        return escaped.toString()
    }
}
