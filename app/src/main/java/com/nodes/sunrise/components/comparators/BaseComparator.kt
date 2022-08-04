package com.nodes.sunrise.components.comparators

import androidx.recyclerview.widget.DiffUtil

abstract class BaseComparator<T : Any> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem === newItem
    }
}