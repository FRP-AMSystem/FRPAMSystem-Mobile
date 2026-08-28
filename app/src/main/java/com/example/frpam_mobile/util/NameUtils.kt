package com.example.frpam_mobile.util

object NameUtils {

    /** Lấy chữ cái đầu của từ cuối cùng trong họ tên (VD: "Le Van Admin" → "A"). */
    fun getNameInitial(fullName: String): String {
        val trimmed = fullName.trim()
        if (trimmed.isEmpty()) return "?"

        val lastWord = trimmed.split(Regex("\\s+")).lastOrNull().orEmpty()
        return lastWord.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    }
}
