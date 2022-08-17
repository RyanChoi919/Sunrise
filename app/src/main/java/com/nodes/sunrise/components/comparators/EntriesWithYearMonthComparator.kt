package com.nodes.sunrise.components.comparators

import com.nodes.sunrise.model.EntryAndYearMonth

class EntriesWithYearMonthComparator : BaseComparator<EntryAndYearMonth>() {

    override fun areContentsTheSame(
        oldItem: EntryAndYearMonth,
        newItem: EntryAndYearMonth
    ): Boolean {
        return oldItem == newItem
    }

}