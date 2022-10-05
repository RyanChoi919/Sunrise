package com.nodes.sunrise.components.comparators

import com.nodes.sunrise.model.PurchaseItem

class PurchaseItemComparator : BaseComparator<PurchaseItem>() {
    override fun areContentsTheSame(oldItem: PurchaseItem, newItem: PurchaseItem): Boolean {
        return oldItem == newItem
    }
}