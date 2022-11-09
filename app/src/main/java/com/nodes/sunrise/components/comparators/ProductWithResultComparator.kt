package com.nodes.sunrise.components.comparators

import com.nodes.sunrise.model.ProductWithResult

class ProductWithResultComparator : BaseComparator<ProductWithResult>() {
    override fun areContentsTheSame(oldItem: ProductWithResult, newItem: ProductWithResult): Boolean {
        return oldItem == newItem
    }
}