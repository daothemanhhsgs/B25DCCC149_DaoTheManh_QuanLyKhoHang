package com.example.quanlykhohang1
data class Product(
    val id: Int = 0,
    val name: String,
    val price: Long,
    val quantity: Int,
    val category: String,
    val active: Int = 1, // 1: đang kinh doanh, 0: ngừng kinh doanh
    val description: String = ""
)