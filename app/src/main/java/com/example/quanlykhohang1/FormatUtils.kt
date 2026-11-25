package com.example.quanlykhohang1
import java.text.NumberFormat
import java.util.Locale

object FormatUtils {
    fun formatVnd(amount: Long): String {
        val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
        return "${formatter.format(amount)} đ"
    }
}