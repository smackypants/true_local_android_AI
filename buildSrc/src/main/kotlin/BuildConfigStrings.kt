object BuildConfigStrings {
    private const val DEFAULT_PLACEHOLDER_VALUE = "sample_val"

    fun quoted(value: String?): String {
        val escaped = (value ?: DEFAULT_PLACEHOLDER_VALUE)
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
        return "\"$escaped\""
    }
}
