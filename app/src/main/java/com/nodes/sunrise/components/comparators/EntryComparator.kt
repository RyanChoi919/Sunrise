package com.nodes.sunrise.components.comparators

import com.nodes.sunrise.db.entity.Entry

class EntryComparator : BaseComparator<Entry>() {

    override fun areContentsTheSame(oldItem: Entry, newItem: Entry): Boolean {
        return oldItem == newItem
    }

}