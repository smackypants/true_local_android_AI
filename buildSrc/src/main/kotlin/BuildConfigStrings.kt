object BuildConfigStrings {
    fun quoted(value: String?): String {
        val escaped = (value ?: "sample_val")
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
        return "\"$escaped\""
    }
}
