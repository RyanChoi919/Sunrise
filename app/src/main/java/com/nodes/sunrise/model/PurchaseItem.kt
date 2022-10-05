package com.nodes.sunrise.model

data class PurchaseItem(
    val name: String,
    val description: String,
    val price: String,
    val priceOriginal: String? = null
)