package com.nodes.sunrise.components.comparators

import android.net.Uri

class UriComparator : BaseComparator<Uri>() {
    override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem == newItem
    }
}