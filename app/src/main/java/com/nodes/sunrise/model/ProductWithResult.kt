package com.nodes.sunrise.model

import com.android.billingclient.api.ProductDetails

data class ProductWithResult(
    val productDetails: ProductDetails,
    var isPurchased: Boolean = false
)
